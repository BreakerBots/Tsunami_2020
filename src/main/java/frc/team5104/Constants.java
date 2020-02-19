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
	public static final double DRIVE_TICKS_PER_REV = 4096.0;
	public static final double DRIVE_WHEEL_BASE_WIDTH = 25.25 / 12.0; //ft
	public static final double DRIVE_KP = 1;
	public static final double DRIVE_KD = 0;
	public static final double DRIVE_KS = 0.0551;
	public static final double DRIVE_KV = 1.69;
	public static final double DRIVE_KA = 0.131;
	public static final double AUTO_MAX_VELOCITY = 8; //ft/s
	public static final double AUTO_MAX_ACCEL = 6;
	public static final double AUTO_CORRECTION_FACTOR = 1; //>0
	public static final double AUTO_DAMPENING_FACTOR  = 0.5; //0-1
	public static final boolean AUTO_PLOT_ODOMETRY = true;
	
	//Flywheel
	public static final boolean FLYWHEEL_OPEN_LOOP = false;
	public static final double FLYWHEEL_RAMP_RATE_UP = 0.7;
	public static final double FLYWHEEL_RAMP_RATE_DOWN = 3;
	public static final double FLYWHEEL_RPM_TOL = 200;
	public static final double FLYWHEEL_KP = 0.2;
	public static final double FLYWHEEL_KF = 0.05;
	
	//Panelers
	public static final double ROTATION_MOTOR_SPEED = 0.75;
	public static final double POSITION_MOTOR_SPEED = 0.3;

	//Hood
	public static final double HOOD_TOL = 3;
	public static final double HOOD_CALIBRATE_SPEED = 0.1;
	public static final double HOOD_TICKS_PER_REV = 4096.0 * (360.0 / 18.0);
	public static double HOOD_KP = 0.0979;
	public static double HOOD_KD = 0.0296;
	public static final double HOOD_KS = 0.49;
	public static final double HOOD_KV = 0.0605;
	public static final double HOOD_KA = 0.00218;
	public static final double HOOD_MAX_VEL = 200;
	public static final double HOOD_MAX_ACC = 2000;
	
	//Hopper
	public static final double HOPPER_START_INTAKE_SPEED = 7;
	public static final double HOPPER_START_INDEX_SPEED = 0.5;
	public static final double HOPPER_MIDDLE_BALL_SIZE = 17000;
	public static final double HOPPER_MIDDLE_INDEX_SPEED = 12;
	public static final double HOPPER_FEED_SPEED = 12;
	public static final double HOPPER_FEED_INDEX_SPEED = 2;
	
	//Intake
	public static final double INTAKE_SPEED = 1.0;
	public static final double INTAKE_FIRE_SPEED = 0;//0.25;
	public static final double INTAKE_REJECT_SPEED = 0;//.25
	
	//Paneler
	public static final double PANELER_ROT_SPEED = 1;
	public static final double PANELER_POS_SPEED = 0.3;
	public static final double PANELER_TICKS_PER_REV = 4096.0 * (/*belt*/30.0 / 24.0) * (/*control panel*/16.0 / 1.5);
	
	//Turret
	public static final double TURRET_CALIBRATE_SPEED = 0.08;
	public static final double TURRET_VOLT_LIMIT = 6;
	public static final double TURRET_TICKS_PER_REV = 2048.0 / (8.0 / 60.0 /*gear*/) / (22.0 / 150.0 /*sprocket*/);
	public static double TURRET_KP = 0.2;
	public static double TURRET_KD = 0.0;
	public static final double TURRET_KS = 0.223;
	public static final double TURRET_KV = 0.015;
	public static final double TURRET_KA = 0.000384;
	public static final double TURRET_MAX_VEL = 200;
	public static final double TURRET_MAX_ACC = 2000;
	public static final double TURRET_VISION_TOL = 2;
}