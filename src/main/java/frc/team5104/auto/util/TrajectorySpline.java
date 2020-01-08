/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.util;

import frc.team5104.util.BreakerMath;

/** A spline... */
public class TrajectorySpline {
	//Spline Types
	public static class Type {
		private final String value_;
		private Type(String value) { this.value_ = value; }
		public String toString() { return value_; }
	}
	//Cubic Spline where positions and first derivatives (angle) constraints will be met but second derivatives may be discontinuous.
	public static final Type CubicHermite = new Type("CubicHermite");
	//Quintic Spline where positions and first derivatives (angle) constraints will be met, and all second derivatives at knots = 0.
	public static final Type QuinticHermite = new Type("QuinticHermite");

	Type type_;
	double a_;	// ax^5
	double b_;	// + bx^4
	double c_;	// + cx^3
	double d_;	// + dx^2
	double e_;	// + ex

	//The offset from the world frame to the spline frame.
	//Add these to the output of the spline to obtain world coordinates.
	double y_offset_;
	double x_offset_;
	double knot_distance_;
	double theta_offset_;
	double arc_length_;

	TrajectorySpline() {
		arc_length_ = -1;
	}

	public static boolean reticulateSplines(FieldPosition start, FieldPosition goal, TrajectorySpline result, Type type) {
		return reticulateSplines(start.y, start.x, start.theta, goal.y, goal.x, goal.theta, result, type);
	}
	public static boolean reticulateSplines(double x0, double y0, double theta0, double x1, double y1, double theta1, TrajectorySpline result, Type type) {
		result.type_ = type;

		//Move point 0 to origin
		result.x_offset_ = x0;
		result.y_offset_ = y0;
		double x1_hat = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
		if (x1_hat == 0)
			return false;
		result.knot_distance_ = x1_hat;
		result.theta_offset_ = Math.atan2(y1 - y0, x1 - x0);
		double theta0_hat = BreakerMath.radianDiff(result.theta_offset_, theta0);
		double theta1_hat = BreakerMath.radianDiff(result.theta_offset_, theta1);
		
		//90d Removal :l
		//if (Math.abs(BreakerMath.radianDiff(theta0_hat, theta1_hat)) >= Math.PI / 2 || almostEqual(Math.abs(theta0_hat), Math.PI / 2) || almostEqual(Math.abs(theta1_hat), Math.PI / 2))
		//	return false;
		
		// Turn angles into derivatives (slopes)
		double yp0_hat = Math.tan(theta0_hat);
		double yp1_hat = Math.tan(theta1_hat);

		if (type == CubicHermite) {
			result.a_ = 0;
			result.b_ = 0;
			result.c_ = (yp1_hat + yp0_hat) / (x1_hat * x1_hat);
			result.d_ = -(2 * yp0_hat + yp1_hat) / x1_hat;
			result.e_ = yp0_hat;
		} 
		else if (type == QuinticHermite) {
			result.a_ = -(3 * (yp0_hat + yp1_hat)) / (x1_hat * x1_hat * x1_hat * x1_hat);
			result.b_ = (8 * yp0_hat + 7 * yp1_hat) / (x1_hat * x1_hat * x1_hat);
			result.c_ = -(6 * yp0_hat + 4 * yp1_hat) / (x1_hat * x1_hat);
			result.d_ = 0;
			result.e_ = yp0_hat;
		}

		return true;
	}

	public double calculateLength() {
		if (arc_length_ >= 0)
			return arc_length_;

		final int kNumSamples = 100000;
		double arc_length = 0;
		double t, dydt;
		double integrand, last_integrand
						= Math.sqrt(1 + derivativeAt(0) * derivativeAt(0)) / kNumSamples;
		for (int i = 1; i <= kNumSamples; ++i) {
			t = ((double) i) / kNumSamples;
			dydt = derivativeAt(t);
			integrand = Math.sqrt(1 + dydt * dydt) / kNumSamples;
			arc_length += (integrand + last_integrand) / 2;
			last_integrand = integrand;
		}
		arc_length_ = knot_distance_ * arc_length;
		return arc_length_;
	}

	public double getPercentageForDistance(double distance) {
		final int kNumSamples = 100000;
		double arc_length = 0;
		double t = 0;
		double last_arc_length = 0;
		double dydt;
		double integrand, last_integrand
						= Math.sqrt(1 + derivativeAt(0) * derivativeAt(0)) / kNumSamples;
		distance /= knot_distance_;
		for (int i = 1; i <= kNumSamples; ++i) {
			t = ((double) i) / kNumSamples;
			dydt = derivativeAt(t);
			integrand = Math.sqrt(1 + dydt * dydt) / kNumSamples;
			arc_length += (integrand + last_integrand) / 2;
			if (arc_length > distance) {
				break;
			}
			last_integrand = integrand;
			last_arc_length = arc_length;
		}

		// Interpolate between samples.
		double interpolated = t;
		if (arc_length != last_arc_length) {
			interpolated += ((distance - last_arc_length)
							/ (arc_length - last_arc_length) - 1) / (double) kNumSamples;
		}
		return interpolated;
	}

	public double[] getXandY(double percentage) {
		double[] result = new double[2];

		percentage = Math.max(Math.min(percentage, 1), 0);
		double x_hat = percentage * knot_distance_;
		double y_hat = (a_ * x_hat + b_) * x_hat * x_hat * x_hat * x_hat
						+ c_ * x_hat * x_hat * x_hat + d_ * x_hat * x_hat + e_ * x_hat;

		double cos_theta = Math.cos(theta_offset_);
		double sin_theta = Math.sin(theta_offset_);

		result[0] = x_hat * cos_theta - y_hat * sin_theta + x_offset_;
		result[1] = x_hat * sin_theta + y_hat * cos_theta + y_offset_;
		return result;
	}

	public double valueAt(double percentage) {
		percentage = Math.max(Math.min(percentage, 1), 0);
		double x_hat = percentage * knot_distance_;
		double y_hat = (a_ * x_hat + b_) * x_hat * x_hat * x_hat * x_hat
						+ c_ * x_hat * x_hat * x_hat + d_ * x_hat * x_hat + e_ * x_hat;

		double cos_theta = Math.cos(theta_offset_);
		double sin_theta = Math.sin(theta_offset_);

		double value = x_hat * sin_theta + y_hat * cos_theta + y_offset_;
		return value;
	}

	private double derivativeAt(double percentage) {
		percentage = Math.max(Math.min(percentage, 1), 0);

		double x_hat = percentage * knot_distance_;
		double yp_hat = (5 * a_ * x_hat + 4 * b_) * x_hat * x_hat * x_hat + 3 * c_ * x_hat * x_hat
						+ 2 * d_ * x_hat + e_;

		return yp_hat;
	}

	private double secondDerivativeAt(double percentage) {
		percentage = Math.max(Math.min(percentage, 1), 0);

		double x_hat = percentage * knot_distance_;
		double ypp_hat = (20 * a_ * x_hat + 12 * b_) * x_hat * x_hat + 6 * c_ * x_hat + 2 * d_;

		return ypp_hat;
	}

	public double angleAt(double percentage) {
		double angle = BreakerMath.boundRadians2PI(
						Math.atan(derivativeAt(percentage)) + theta_offset_);
		return angle;
	}

	public double angleChangeAt(double percentage) {
		return BreakerMath.boundRadiansPI(
						Math.atan(secondDerivativeAt(percentage)));
	}
}
