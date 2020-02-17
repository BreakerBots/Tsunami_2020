/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.util.managers;

import frc.team5104.util.console;
import frc.team5104.util.console.c;

/** 
 * A snickers rapper of all the requirements of a subsystem. 
 */
public abstract class Subsystem {
	protected boolean isAttached = false;
	protected boolean emergencyStopped = false;
	
	/** Called periodically from the robot loop */
	public abstract void update();
	
	/** Called when robots boots up; initialize devices here */
	public abstract void init();
	
	/** Called whenever the robot becomes enabled or disabled. Stop motors here. */
	public abstract void disabled();
	
	/** Call to stop all motors */
	public void emergencyStop() {
		console.error(c.MAIN, "Emergency stopped the " + this.getClass().getSimpleName());
		emergencyStopped = true;
	}
}
