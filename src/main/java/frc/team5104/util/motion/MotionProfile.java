package frc.team5104.util.motion;

/**
 * Implementation of a simple trapezoidal motion profile
 * *referenced from WPILIB TrapezoidProfile.java
 */
public class MotionProfile {
	private int direction;
	private State initial, goal;
	private double maxVelocity, maxAcceleration, endAccel, endFullSpeed, endDeccel;

	//Constructors
	public MotionProfile(double maxVelocity, double maxAcceleration, State goalState) { this(maxVelocity, maxAcceleration, goalState, new State(0, 0)); }
	public MotionProfile(double maxVelocity, double maxAcceleration, State goalState, State initialState) {
		direction = shouldFlipAcceleration(initialState, goalState) ? -1 : 1;
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		this.initial = initialState;
		this.goal = goalState;
		initial.direct(direction);
		goal.direct(direction);
		
		if (initial.velocity > maxVelocity)
			initial.velocity = maxVelocity;

		// Deal with a possibly truncated motion profile (with nonzero initial or
		// final velocity) by calculating the parameters as if the profile began and
		// ended at zero velocity
		double cutoffBegin = initial.velocity / maxAcceleration;
		double cutoffDistBegin = cutoffBegin * cutoffBegin * maxAcceleration / 2.0;
		double cutoffEnd = goal.velocity / maxAcceleration;
		double cutoffDistEnd = cutoffEnd * cutoffEnd * maxAcceleration / 2.0;

		// Now we can calculate the parameters as if it was a full trapezoid instead
		// of a truncated one
		double fullTrapezoidDist = cutoffDistBegin + (goal.position - initial.position) + cutoffDistEnd;
		double accelerationTime = maxVelocity / maxAcceleration;
		double fullSpeedDist = fullTrapezoidDist - accelerationTime * accelerationTime * maxAcceleration;

		// Handle the case where the profile never reaches full speed
		if (fullSpeedDist < 0) {
			accelerationTime = Math.sqrt(fullTrapezoidDist / maxAcceleration);
			fullSpeedDist = 0;
		}

		endAccel = accelerationTime - cutoffBegin;
		endFullSpeed = endAccel + fullSpeedDist / maxVelocity;
		endDeccel = endFullSpeed + accelerationTime - cutoffEnd;
	}

	/**
	 * Calculate the correct position and velocity for the profile at a time t
	 * where the beginning of the profile was at time t = 0.
	 *
	 * @param t The time since the beginning of the profile.
	 */
	public State calculate(double t) {
		State result = initial;
		if (t < endAccel) {
			result.velocity += t * maxAcceleration;
			result.position += (initial.velocity + t * maxAcceleration / 2.0) * t;
		} 
		else if (t < endFullSpeed) {
			result.velocity = maxVelocity;
			result.position += (initial.velocity + endAccel * maxAcceleration / 2.0) * endAccel + maxVelocity * (t - endAccel);
		} 
		else if (t <= endDeccel) {
			result.velocity = goal.velocity + (endDeccel - t) * maxAcceleration;
			double timeLeft = endDeccel - t;
			result.position = goal.position - (goal.velocity + timeLeft * maxAcceleration / 2.0) * timeLeft;
		} 
		else result = goal;
		result.direct(direction);
		return result;
	}

	/**
	 * Returns the time left until a target distance in the profile is reached.
	 * @param target The target distance.
	 */
	public double timeLeftUntil(double target) {
		double position = initial.position * direction;
		double velocity = initial.velocity * direction;
		double curEndAccel = endAccel * direction;
		double curEndFullSpeed = endFullSpeed * direction - curEndAccel;
		if (target < position) {
			curEndAccel = -curEndAccel;
			curEndFullSpeed = -curEndFullSpeed;
			velocity = -velocity;
		}
		curEndAccel = Math.max(curEndAccel, 0);
		curEndFullSpeed = Math.max(curEndFullSpeed, 0);
		double curEndDeccel = endDeccel - curEndAccel - curEndFullSpeed;
		curEndDeccel = Math.max(curEndDeccel, 0);
		final double acceleration = maxAcceleration;
		final double decceleration = -maxAcceleration;
		double distToTarget = Math.abs(target - position);
		if (distToTarget < 1e-6) return 0;
		double accelDist = velocity * curEndAccel + 0.5 * acceleration * curEndAccel * curEndAccel;
		double deccelVelocity;
		if (curEndAccel > 0)
			deccelVelocity = Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist));
		else deccelVelocity = velocity;
		double deccelDist = deccelVelocity * curEndDeccel + 0.5 * decceleration * curEndDeccel * curEndDeccel;
		deccelDist = Math.max(deccelDist, 0);
		double fullSpeedDist = maxVelocity * curEndFullSpeed;
		if (accelDist > distToTarget) {
			accelDist = distToTarget;
			fullSpeedDist = 0;
			deccelDist = 0;
		} 
		else if (accelDist + fullSpeedDist > distToTarget) {
			fullSpeedDist = distToTarget - accelDist;
			deccelDist = 0;
		}
		else deccelDist = distToTarget - fullSpeedDist - accelDist;
		double accelTime = (-velocity + Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist))) / acceleration;
		double deccelTime = (-deccelVelocity + Math.sqrt(Math.abs(deccelVelocity * deccelVelocity + 2 * decceleration * deccelDist))) / decceleration;
		double fullSpeedTime = fullSpeedDist / maxVelocity;
		return accelTime + fullSpeedTime + deccelTime;
	}

	/** Returns the total time the profile takes to reach the goal. */
	public double totalTime() {
		return endDeccel;
	}
	/**
	 * Returns true if the profile has reached the goal.
	 * <p>The profile has reached the goal if the time since the profile started
	 * has exceeded the profile's total time.
	 * @param t The time since the beginning of the profile.
	 */
	public boolean isFinished(double t) {
		return t >= totalTime();
	}
	private static boolean shouldFlipAcceleration(State initial, State goal) {
		return initial.position > goal.position;
	}
	
	//State
	public static class State {
		public double position;
		public double velocity;
		public State() { this(0, 0); }
		public State(double position, double velocity) {
			this.position = position;
			this.velocity = velocity;
		}
		public boolean equals(Object other) {
			if (other instanceof State) {
				State rhs = (State) other;
				return this.position == rhs.position && this.velocity == rhs.velocity;
			} else {
				return false;
			}
		}
		public void direct(double direction) {
			this.position *= direction;
			this.velocity *= direction;
		}
	}
}
