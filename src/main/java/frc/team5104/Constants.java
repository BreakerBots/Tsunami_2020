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
	public static final double DRIVE_WHEEL_DIAMETER = 0.5; //ft
	public static final double DRIVE_TICKS_PER_REV = 2048.0 * (50.0/8.0) * (46.0/24.0);
	public static final double DRIVE_WHEEL_BASE_WIDTH = 2.0589795990198065; //ft
	public static final double DRIVE_KP = 1;//0.153;
	public static final double DRIVE_KD = 0;
	public static final double DRIVE_KS = 0.151;
	public static final double DRIVE_KV = 0.832*4;//0.832;
	public static final double DRIVE_KA = 0.135;
	public static final double AUTO_MAX_VELOCITY = 6; //ft/s
	public static final double AUTO_MAX_ACCEL = 4;
	public static final double AUTO_CORRECTION_FACTOR = 1; //>0
	public static final double AUTO_DAMPENING_FACTOR  = 0.5; //0-1
	public static final boolean AUTO_PLOT_ODOMETRY = true;
	
	//Flywheel
	public static final boolean FLYWHEEL_OPEN_LOOP = false;
	public static final double FLYWHEEL_RAMP_RATE_UP = 0.7;
	public static final double FLYWHEEL_RAMP_RATE_DOWN = 3;
	public static final double FLYWHEEL_RPM_TOL = 200;
	public static double FLYWHEEL_KP = 0.000747;
//	public static final double FLYWHEEL_MAX_VEL = 0;
//	public static final double FLYWHEEL_MAX_ACC = 0;
	public static final double FLYWHEEL_KS = 0.238;
	public static final double FLYWHEEL_KV = 0.11;
	public static final double FLYWHEEL_KA = 0.00406;
//	public static final double FLYWHEEL_KF = 0.05;
	
	//Panelers
	public static final double ROTATION_MOTOR_SPEED = 1.0;
	public static final double POSITION_MOTOR_SPEED = 0.3;

	//Hood
	public static final double HOOD_TOL = 3;
	public static final double HOOD_CALIBRATE_SPEED = 0.15;
	public static final double HOOD_TICKS_PER_REV = 4096.0 * (360.0 / 18.0);
	public static double HOOD_KP = 0.0979;
	public static final double HOOD_KD = 0.0;
	public static final double HOOD_KS = 0.49;
	public static final double HOOD_KV = 0.0605;
	public static final double HOOD_KA = 0.00218;
	public static final double HOOD_MAX_VEL = 200;
	public static final double HOOD_MAX_ACC = 2000;
	
	//Hopper
	public static final double HOPPER_START_INTAKE_SPEED = 7;
	public static final double HOPPER_START_INDEX_SPEED = 0.5;
	public static final double HOPPER_INDEX_BALL_SIZE = 1.25;
	public static final double HOPPER_INDEX_TOL = 0.1;
	public static final double HOPPER_INDEX_TICKS_PER_REV = 2048 * (70.0/12.0);
	public static final double HOPPER_INDEX_KP = 10;
	public static final double HOPPER_INDEX_KD = 0.08;
	public static final double HOPPER_INDEX_KS = 0.42;
	public static final double HOPPER_INDEX_KV = 0.685;
	public static final double HOPPER_INDEX_KA = 0.0197;
	public static final double HOPPER_INDEX_VEL = 50;
	public static final double HOPPER_INDEX_ACC = 800;
	public static final double HOPPER_FEED_SPEED = 10;
	public static final double HOPPER_FEEDER_INDEX_SPEED = 4;
	
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
	public static final double TURRET_KP = 0.2;
	public static final double TURRET_KD = 0.0;
	public static final double TURRET_KS = 0.223;
	public static final double TURRET_KV = 0.015;
	public static final double TURRET_KA = 0.000384;
	public static final double TURRET_MAX_VEL = 200;
	public static final double TURRET_MAX_ACC = 2000;
	public static final double TURRET_VISION_TOL = 2;
}