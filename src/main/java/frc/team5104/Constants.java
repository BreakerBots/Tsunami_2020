/*BreakerBots Robotics Team 2019*/
package frc.team5104;

public class Constants {
	//Main
	public static final boolean OVERWRITE_NON_MATCH_LOGS = true;
	public static final boolean OVERWRITE_MATCH_LOGS = false;
	public static final int MAIN_LOOP_SPEED = 50;
	public static final boolean COMP_BOT = false;
	public static final String ROBOT_NAME = COMP_BOT ? "Tsunami" : "Tidal-Wave";
	public static final double LIMELIGHT_ANGLE = 50.0;
	public static final boolean LIMELIGHT_DEFAULT_OFF = false;
	
	//Drive
	public static final double DRIVE_WHEEL_DIAMETER = 6.0/12.0; //ft
	public static final double DRIVE_TICKS_PER_REVOLUTION = 4096.0;
	public static final double DRIVE_WHEEL_BASE_WIDTH = 25.25 / 12.0; //ft
	public static final double DRIVE_KP = 0.5;
	public static final double DRIVE_KD = 0;
	public static final double DRIVE_KS = 0.806;
	public static final double DRIVE_KV = 0.624;
	public static final double DRIVE_KA = 0.163;
	public static final double AUTO_MAX_VELOCITY = 4; //ft/s
	public static final double AUTO_MAX_ACCEL = 2;
	public static final double AUTO_CORRECTION_FACTOR = 100; //>0
	public static final double AUTO_DAMPENING_FACTOR  = 0.7; //0-1
	public static final boolean AUTO_PLOT_ODOMETRY = true;
	
	//Flywheel
	public static final boolean FLYWHEEL_OPEN_LOOP = false;
	public static final double FLYWHEEL_RAMP_RATE = 0.5;
	public static final double FLYWHEEL_RPM_TOL = 2000;
	public static final double FLYWHEEL_KP = 0.2;
	public static final double FLYWHEEL_KF = 0.05;
	
	//Panelers
	public static final double ROTATION_MOTOR_SPEED = 0.75;
	public static final double POSITION_MOTOR_SPEED = 0.3;

	//Hood
	public static final double HOOD_MIN_ANGLE = 50.0;
	public static final double HOOD_MAX_ANGLE = 90.0;
	public static final double HOOD_RAMP_RATE = 0.25;
	public static final double HOOD_TOL = 0.2;
	public static final double HOOD_CALIBRATE_SPEED = 0.1;
	public static final double HOOD_KP = 0.15;
	public static final double HOOD_KD = 0;
	
	//Hopper
	public static final double HOPPER_KP = 10;
	public static final double HOPPER_ACC = 0;
	public static final double HOPPER_VEL = 0;
	public static final double HOPPER_MID_BALL_SIZE = 17000;//17000;
	public static final double HOPPER_UNJAM_SPEED = 0.25;
	public static final double HOPPER_FEED_SPEED = 1;
	public static final double HOPPER_START_INTAKE_SPEED = 0.8;
	
	//Intake
	public static final double INTAKE_SPEED = 1.0;
	public static final double INTAKE_FIRE_SPEED = 0;//0.25;
	public static final double INTAKE_REJECT_SPEED = 0;//.25
	
	//Paneler
	public static final double PANELER_ROT_SPEED = 1;
	public static final double PANELER_POS_SPEED = 0.3;
	
	//Turret
	public static final double TURRET_CALIBRATE_SPEED = 0.1;
	public static final double TURRET_RAMP_RATE = 0.25;
	public static final double TURRET_NORMAL_KP = 0;
	public static final double TURRET_NORMAL_KD = 0;
	public static double TURRET_VISION_KP = 0.25;
	public static double TURRET_VISION_KD = 0.2;
	public static double TURRET_VISION_TOL = 0;
}