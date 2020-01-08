/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.util;

/** A waypoint to generate a trajectory from. */
public class FieldPosition {
	public double x, y, theta;
	
	//Constructors
	/**
	 * Creates a new waypoint from specific parameters.
	 * @param x The x (sideways) position (in feet) of the robot, relative to the start.
	 * @param y The y (forward) position (in feet) of the robot, relative to the start.
	 * @param heading The angle of the robot (in degrees)
	 */
	public FieldPosition(double x, double y, double heading) {
		this.x = x;
		this.y = y;
		this.theta = Math.toRadians(heading);
	}
	
	public String toString() {
		return "x: " + x + ", " +
			   "y: " + y + ", " +
			   "heading: " + Math.toDegrees(theta);
	}
}