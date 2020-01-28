package frc.team5104.util.setup;

import edu.wpi.first.wpilibj.Timer;

public class RobotState {
	
	//State Machine
	public static enum RobotMode {
		DISABLED, TELEOP, AUTONOMOUS, TEST
	}; 
	protected RobotMode currentMode = RobotMode.DISABLED;
	protected RobotMode lastMode = RobotMode.DISABLED;
	protected Timer timer = new Timer();
	protected double deltaTime = 0;
	
	//Access
	protected static RobotState instance;
	protected static RobotState getInstance() { 
		if (instance == null) 
			instance = new RobotState();
		return instance; 
	}
	
	//External Functions
	public static boolean isEnabled() { return getInstance().currentMode != RobotMode.DISABLED; }
	public static RobotMode getMode() { return getInstance().currentMode; }
	public static double getDeltaTime() { return getInstance().deltaTime; }
	public static double getTimeSinceEnabled() { return getInstance().timer.get(); }
}