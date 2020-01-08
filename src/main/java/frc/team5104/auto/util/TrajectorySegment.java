/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.util;

import java.io.Serializable;

/**
 * A segment of trajectory.
 * Contains position, velocity, acceleration, jerk, heading, delta time, x, and y. 
 */
public class TrajectorySegment implements Serializable {
	private static final long serialVersionUID = 1L;
	public double position, velocity, acceleration, jerk, theta, deltaTime, x, y;

	//Constructors
	public TrajectorySegment() { }
	public TrajectorySegment(double position, double velocity, double acceleration, double jerk, double theta, double deltaTime, double x, double y) {
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.jerk = jerk;
		this.theta = theta;
		this.deltaTime = deltaTime;
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return  "pos: " + position  + ", " + 
				"vel: " + velocity  + ", " + 
				"acc: " + acceleration  + ", " + 
				"jerk: "+ jerk + ", " + 
				"theta: " + theta;
	}
	
	public double heading() {
		return Math.toDegrees(theta);
	}
}