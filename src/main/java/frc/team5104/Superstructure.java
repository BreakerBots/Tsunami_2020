/*BreakerBots Robotics Team 2019*/
package frc.team5104;

import frc.team5104.subsystems.Paneler;
import frc.team5104.util.console;

/** 
 * The Superstructure is a massive state machine that handles the Intake, Wrist, and Elevator
 * The Superstructure only controls the states... its up the subsystems to figure out what to do
 * based on the state of the Superstructure.
 */
public class Superstructure {
	//States and Variables
	public static enum SystemState { DISABLED, CALIBRATING, MANUAL, AUTOMATIC }
	public static enum Mode { 
		IDLE, INTAKE, SHOOTING, CLIMBING, PANELING, PANEL_DEPLOYING }
	public static enum PanelState { ROTATION, POSITION };
	public static enum FlywheelState { STOPPED, SPINNING };
	
	private static SystemState systemState = SystemState.DISABLED;
	private static Mode mode = Mode.IDLE;
	private static PanelState panelState = PanelState.ROTATION;
	private static FlywheelState shooterWheelState = FlywheelState.STOPPED;
	
	//External Functions
	public static SystemState getSystemState() { return systemState; }
	public static void setSystemState(SystemState systemState) {  Superstructure.systemState = systemState; }
	public static Mode getMode() { return mode; }
	public static void setMode(Mode mode) { Superstructure.mode = mode; }
	public static void setPanelState(PanelState panelState) { Superstructure.panelState = panelState; }
	public static PanelState getPanelState() { return panelState; }
	public static void setFlywheelState(FlywheelState shooterWheelState) { Superstructure.shooterWheelState = shooterWheelState; }
	public static FlywheelState getFlywheelState() { return shooterWheelState; }
	
	//Loop
	protected static void update() {
		
		//Panel
//		console.log(getMode());
		if (Controls.PANEL_DEPLOY.get() && getMode() != Mode.PANEL_DEPLOYING) {
			setMode(Mode.PANEL_DEPLOYING);
			console.log("Deploying Paneller");
		}
		else if (Controls.PANEL_DEPLOY.get() && getMode() == Mode.PANEL_DEPLOYING) {
			setMode(Mode.IDLE);
			console.log("Idle");
		}
		if (Controls.PANEL_SPIN.get() && getMode() == Mode.PANEL_DEPLOYING) {
			setMode(Mode.PANELING);
			console.log(getPanelState());
		}
		else if (Controls.PANEL_SPIN.get() && getMode() == Mode.PANELING) {
			setMode(Mode.PANEL_DEPLOYING);
			console.log("Deploying Paneller");
		}
		if (Controls.PANEL_TOGGLE.get()) {
			if (getPanelState() == PanelState.ROTATION)
				setPanelState(PanelState.POSITION);
			else
				setPanelState(PanelState.ROTATION);	
			console.log(getPanelState());
		}
		if (getMode() == Mode.PANELING && Paneler.isFinished()) {
			setMode(Mode.IDLE);
			console.log("Idle");
		}
		
		//Make sure flywheel is spinning while in shoot mode
		if (getMode() == Mode.SHOOTING) {
			setFlywheelState(FlywheelState.SPINNING);
		}
	}
	
	//Reset
	protected static void reset() {
		setSystemState(SystemState.CALIBRATING); 
		setMode(Mode.IDLE);
	}
}