/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.util;

import frc.team5104.util.BreakerMath;
import frc.team5104.util.console;

/**
 * <h1>Trajectory Generator</h1>
 * Baking fresh trajectories since 20-never...
 */
public class TrajectoryGenerator {
	//Speed Curve Strategies
	public static class SpeedProfile {
		private final String value_;
		private SpeedProfile(String value) { value_ = value; }
		public String toString() { return value_; }
	}
	public static final SpeedProfile StepProfile = new SpeedProfile("StepStrategy");
	public static final SpeedProfile TrapezoidalProfile = new SpeedProfile("TrapezoidalStrategy");
	public static final SpeedProfile SCurveProfile = new SpeedProfile("SCurvesStrategy");
	public static final SpeedProfile AutomaticProfile = new SpeedProfile("AutomaticStrategy");

	//Integration Methods
	private static class IntegrationMethod { 
		private final String value_;
		private IntegrationMethod(String value) { value_ = value; }
		public String toString() { return value_; }
	}
	private static final IntegrationMethod RectangularIntegration = new IntegrationMethod("RectangularIntegration");
	private static final IntegrationMethod TrapezoidalIntegration = new IntegrationMethod("TrapezoidalIntegration");

	/**
	 * Generates a trajectory that hits all the waypoints with the specified configuration
	 * @param waypoints The waypoints for the trajectory to hit
	 * @param maxVelocity The max velocity (ft/s) in the trajectory
	 * @param maxAcceleration The max acceleration (ft/s/s) in the trajectory
	 * @param maxJerk The max jerk (ft/s/s/s) in the trajectory
	 * @param deltaTime The time between each loop. (1/loopHz)
	 */
	public static Trajectory generate(FieldPosition[] waypoints, double maxVelocity, double maxAcceleration, double maxJerk, double deltaTime) {
		if (waypoints.length < 2) {
			console.error("Not Enough Waypoints!");
			return null;
		}

		//Creates splines between each pair of waypoints
		TrajectorySpline[] splines = new TrajectorySpline[waypoints.length - 1];
		double[] splineLengths = new double[splines.length];
		double totalDistance = 0;
		for (int i = 0; i < splines.length; ++i) {
			splines[i] = new TrajectorySpline();
			if (!TrajectorySpline.reticulateSplines(waypoints[i], waypoints[i + 1], splines[i], TrajectorySpline.CubicHermite)) {
				console.error("Invalid Path!");
				return null;
			}
			splineLengths[i] = splines[i].calculateLength();
			totalDistance += splineLengths[i];
		}

		//Combines all the splines and smoothes them out
		Trajectory trajectory = generateFullTrajectory(maxVelocity, maxAcceleration, maxJerk, deltaTime, TrajectoryGenerator.SCurveProfile, 0.0, waypoints[0].theta, totalDistance, 0.0, waypoints[0].theta);

		//Assings headings to segments
		int currentSpline = 0;
		double currentSplineStartPosition = 0;
		int currentSplineLength = 0;
		for (int i = 0; i < trajectory.length(); ++i) {
			double cur_pos = trajectory.get(i).position;

			boolean found_spline = false;
			while (!found_spline) {
				double cur_pos_relative = cur_pos - currentSplineStartPosition;
				if (cur_pos_relative <= splineLengths[currentSpline]) {
					double percentage = splines[currentSpline].getPercentageForDistance(
									cur_pos_relative);
					trajectory.get(i).theta = splines[currentSpline].angleAt(percentage);
					double[] coords = splines[currentSpline].getXandY(percentage);
					trajectory.get(i).x = coords[0];
					trajectory.get(i).y = coords[1];
					found_spline = true;
				} else if (currentSpline < splines.length - 1) {
					currentSplineLength += splineLengths[currentSpline];
					currentSplineStartPosition = currentSplineLength;
					++currentSpline;
				} else {
					trajectory.get(i).theta = splines[splines.length - 1].angleAt(1.0);
					double[] coords = splines[splines.length - 1].getXandY(1.0);
					trajectory.get(i).x = coords[0];
					trajectory.get(i).y = coords[1];
					found_spline = true;
				}
			}
		}

		return trajectory;
	}
	
	private static Trajectory generateFullTrajectory(double maxVelocity, double maxAcceleration, double maxJerk, double deltaTime, 
													 SpeedProfile strategy, double startVelocity, double startHeading, double goalPosition, 
													 double goalVelocity, double goalHeading) {
		// Choose an automatic strategy.
		if (strategy == AutomaticProfile) {
			if (startVelocity == goalVelocity && startVelocity == maxVelocity)
				strategy = StepProfile;
			else if (startVelocity == goalVelocity && startVelocity == 0)
				strategy = SCurveProfile;
			else
				strategy = TrapezoidalProfile;
		}

		Trajectory traj;
		if (strategy == StepProfile) {
			double impulse = (goalPosition / maxVelocity) / deltaTime;

			// Round down, meaning we may undershoot by less than max_vel*dt.
			// This is due to discretization and avoids a strange final
			// velocity.
			int time = (int) (Math.floor(impulse));
			traj = secondOrderFilter(1, 1, deltaTime, maxVelocity, maxVelocity, impulse, time, TrapezoidalIntegration);

		} else if (strategy == TrapezoidalProfile) {
			// How fast can we go given maximum acceleration and deceleration?
			double start_discount = .5 * startVelocity * startVelocity / maxAcceleration;
			double end_discount = .5 * goalVelocity * goalVelocity / maxAcceleration;

			double adjusted_max_vel = BreakerMath.min(maxVelocity,
							Math.sqrt(maxAcceleration * goalPosition - start_discount
											- end_discount));
			double t_rampup = (adjusted_max_vel - startVelocity) / maxAcceleration;
			double x_rampup = startVelocity * t_rampup + .5 * maxAcceleration
							* t_rampup * t_rampup;
			double t_rampdown = (adjusted_max_vel - goalVelocity) / maxAcceleration;
			double x_rampdown = adjusted_max_vel * t_rampdown - .5
							* maxAcceleration * t_rampdown * t_rampdown;
			double x_cruise = goalPosition - x_rampdown - x_rampup;

			// The +.5 is to round to nearest
			int time = (int) ((t_rampup + t_rampdown + x_cruise
							/ adjusted_max_vel) / deltaTime + .5);

			// Compute the length of the linear filters and impulse.
			int f1_length = (int) Math.ceil((adjusted_max_vel
							/ maxAcceleration) / deltaTime);
			double impulse = (goalPosition / adjusted_max_vel) / deltaTime
							- startVelocity / maxAcceleration / deltaTime
							+ start_discount + end_discount;
			traj = secondOrderFilter(f1_length, 1, deltaTime, startVelocity,
							adjusted_max_vel, impulse, time, TrapezoidalIntegration);

		} else if (strategy == SCurveProfile) {
			// How fast can we go given maximum acceleration and deceleration?
			double adjusted_max_vel = Math.min(maxVelocity,
							(-maxAcceleration * maxAcceleration + Math.sqrt(maxAcceleration
											* maxAcceleration * maxAcceleration * maxAcceleration
											+ 4 * maxJerk * maxJerk * maxAcceleration
											* goalPosition)) / (2 * maxJerk));

			// Compute the length of the linear filters and impulse.
			int f1_length = (int) Math.ceil((adjusted_max_vel
							/ maxAcceleration) / deltaTime);
			int f2_length = (int) Math.ceil((maxAcceleration
							/ maxJerk) / deltaTime);
			double impulse = (goalPosition / adjusted_max_vel) / deltaTime;
			int time = (int) (Math.ceil(f1_length + f2_length + impulse));
			traj = secondOrderFilter(f1_length, f2_length, deltaTime, 0,
							adjusted_max_vel, impulse, time, TrapezoidalIntegration);

		} else {
			return null;
		}

		// Now assign headings by interpolating along the path.
		// Don't do any wrapping because we don't know units.
		double total_heading_change = goalHeading - startHeading;
		for (int i = 0; i < traj.length(); ++i) {
			traj.segments_[i].theta = startHeading + total_heading_change
							* (traj.segments_[i].position)
							/ traj.segments_[traj.length() - 1].position;
		}

		return traj;
	}

	private static Trajectory secondOrderFilter(int f1_length, int f2_length, double dt, double start_vel, double max_vel, double total_impulse, int length, IntegrationMethod integration) {
		if (length <= 0 || length > Integer.MAX_VALUE-6)
			return null;
		Trajectory traj = new Trajectory(length);

		TrajectorySegment last = new TrajectorySegment();
		last.position = 0;
		last.velocity = start_vel;
		last.acceleration = 0;
		last.jerk = 0;
		last.deltaTime = dt;

		// f2 is the average of the last f2_length samples from f1, so while we
		// can recursively compute f2's sum, we need to keep a buffer for f1.
		double[] f1 = new double[length];
		f1[0] = (start_vel / max_vel) * f1_length;
		double f2;
		for (int i = 0; i < length; ++i) {
			// Apply input
			double input = Math.min(total_impulse, 1);
			if (input < 1) {
				// The impulse is over, so decelerate
				input -= 1;
				total_impulse = 0;
			} else {
				total_impulse -= input;
			}

			// Filter through F1
			double f1_last;
			if (i > 0) {
				f1_last = f1[i - 1];
			} else {
				f1_last = f1[0];
			}
			f1[i] = Math.max(0.0, Math.min(f1_length, f1_last + input));

			f2 = 0;
			// Filter through F2
			for (int j = 0; j < f2_length; ++j) {
				if (i - j < 0)
					break;

				f2 += f1[i - j];
			}
			f2 = f2 / f1_length;

			// Velocity is the normalized sum of f2 * the max velocity
			traj.segments_[i].velocity = f2 / f2_length * max_vel;

			if (integration == RectangularIntegration) {
				traj.segments_[i].position = traj.segments_[i].velocity * dt + last.position;
			} 
			else if (integration == TrapezoidalIntegration) {
				traj.segments_[i].position = (last.velocity
								+ traj.segments_[i].velocity) / 2.0 * dt + last.position;
			}
			traj.segments_[i].x = traj.segments_[i].position;
			traj.segments_[i].y = 0;

			// Acceleration and jerk are the differences in velocity and
			// acceleration, respectively.
			traj.segments_[i].acceleration = (traj.segments_[i].velocity - last.velocity) / dt;
			traj.segments_[i].jerk = (traj.segments_[i].acceleration - last.acceleration) / dt;
			traj.segments_[i].deltaTime = dt;

			last = traj.segments_[i];
		}

		return traj;
	}
}
