/*BreakerBots Robotics Team 2019*/
package frc.team5104;

/** 
 * The Superstructure is a massive state machine that handles the Intake, Wrist, and Elevator
 * The Superstructure only controls the states... its up the subsystems to figure out what to do
 * based on the state of the Superstructure.
 */
public class Superstructure {
	//States and Variables
	public static enum SystemState { DISABLED, CALIBRATING, MANUAL, AUTOMATIC }
	public static enum Mode { IDLE /*slap ur modes here*/ }
	/* slap da reset of ur states here */
	private static SystemState systemState = SystemState.DISABLED;
	private static Mode mode = Mode.IDLE;
	/* slap da reset of ur current states here */
	
	//External Functions
	public static SystemState getSystemState() { return systemState; }
	public static void setSystemState(SystemState systemState) {  Superstructure.systemState = systemState; }
	public static Mode getMode() { return mode; }
	public static void setMode(Mode mode) { Superstructure.mode = mode; }
	/* slap da reset of ur getters/setters here */

	//Update
	protected static void update() {
		
	}
	protected static void reset() {
		setSystemState(SystemState.CALIBRATING); 
		setMode(Mode.IDLE);
	}
}