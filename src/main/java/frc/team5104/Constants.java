/*BreakerBots Robotics Team 2019*/
package frc.team5104;

public class Constants {
	//Main
	public static final boolean OVERWRITE_NON_MATCH_LOGS = true;
	public static final boolean OVERWRITE_MATCH_LOGS = false;
	public static final int MAIN_LOOP_SPEED = 50;
	public static final boolean COMPB_BOT = false;
	public static final String ROBOT_NAME = "Tidal-Wave";
	
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
	public static void main(String[] args) {
	}
}