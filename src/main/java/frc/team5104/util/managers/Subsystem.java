/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.util.managers;

/** 
 * A snickers rapper of all the requirements of a subsystem. 
 */
public abstract class Subsystem {
	/** Called periodically from the robot loop */
	public abstract void update();
	
	/** Called when robots boots up; initialize devices here */
	public abstract void init();
	
	/** Called whenever the robot becomes enabled */
	public abstract void enabled();
	/** Called whenever the robot becomes disabled; stop all devices here */
	public abstract void disabled();
}
