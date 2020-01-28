/*BreakerBots Robotics Team 2019*/
package frc.team5104;

public class Constants {
	//Main
	public static final boolean OVERWRITE_NON_MATCH_LOGS = true;
	public static final boolean OVERWRITE_MATCH_LOGS = false;
	public static final int MAIN_LOOP_SPEED = 50;
	public static final String ROBOT_NAME = "Example-Robot";
	
	//Drive
	public static final double DRIVE_WHEEL_DIAMETER = 6.0/12.0; //ft
	public static final double DRIVE_TICKS_PER_REVOLUTION = 4096.0 * 3.0 * (54.0/30.0);
	public static final double DRIVE_WHEEL_BASE_WIDTH = 25.5 / 12.0; //ft
	public static final double DRIVE_KP = 1.6;
	public static final double DRIVE_KD = 0;
	public static final double DRIVE_KS = 1.04;
	public static final double DRIVE_KV = 0.627;
	public static final double DRIVE_KA = 0.378;
	public static final double AUTO_MAX_VELOCITY = 6; //ft/s
	public static final double AUTO_MAX_ACCEL = 6;
	public static final double AUTO_CORRECTION_FACTOR = 15; //>0
	public static final double AUTO_DAMPENING_FACTOR  = 0.5; //0-1
	public static final boolean AUTO_PLOT_ODOMETRY = true;
	
	//Turret
	public static final double TURRET_MIN_TARGET_ERR = 0.2;
	public static final double TURRET_KINPUT = 0.5;
	
	//Flywheel
	public static final boolean FLYWHEEL_OPEN_LOOP = false;
	public static final double FLYWHEEL_KP = 0;
	public static final double FLYWHEEL_KD = 0;
	public static final double FLYWHEEL_KF = 0;
	public static final double FLYWHEEL_ACC = 0;
	public static final double FLYWHEEL_VEL = 0;
	
	//Hood
	public static final double HOOD_ANGLE = 37.0;
	public static final double HOOD_MIN_ANGLE_ERR = 0.1;
	public static final double HOOD_MAX_ANGLE = 40;
	public static final double HOOD_KINPUT = 0.3;
	
	//Intake
	public static final double INTAKE_TALON_SPEED = 0.6;
	
	//Panelers
	public static final double ROTATION_MOTOR_SPEED = 0.75;
	public static final double POSITION_MOTOR_SPEED = 0.3;

}