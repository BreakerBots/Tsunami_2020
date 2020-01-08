package frc.team5104.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team5104.util.CrashLogger;
import frc.team5104.util.CrashLogger.Crash;
import frc.team5104.util.console;
import frc.team5104.util.console.c;

public class Limelight {
	private static NetworkTable table;
	public static NetworkTableEntry getEntry(String key) { 
		if (table != null)
			return table.getEntry(key); 
		else return null;
	}
	public static void setEntry(String key, double entry) { 
		if (table != null)
			getEntry(key).setDouble(entry); 
	}
	public static double getDouble(String key, double defaultValue) {
		if (table != null)
			return getEntry(key).getDouble(defaultValue);
		else return defaultValue;
	}

	public static double getTargetX() { return getDouble("tx", 5104); }
	public static double getTargetY() { return getDouble("ty", 5104); }
	public static boolean hasTarget() { return getDouble("tv", 0) == 1; }
	public static boolean isConnected() { return getDouble("tl", 0) != 0.0; }
	
	public static enum LEDMode { OFF(1), ON(3), BLINK(2); int value; private LEDMode(int value) { this.value = value; } }
	public static void setLEDMode(LEDMode ledMode) { 
		if (isConnected())
			setEntry("ledMode", ledMode.value);  
		else console.warn(c.VISION, "limelight is not connected");
	}

	public static enum CamMode { VISION(0), DRIVE(1); int value; private CamMode(int value) { this.value = value; } }
	public static void setcamMode(CamMode cMode) { 
		if (isConnected())
			setEntry("camMode", cMode.value);  
		else console.warn(c.VISION, "limelight is not connected");
	}
	
	public static void init() {
		Thread initLimelightThread = new Thread() {
			public void run() {
				while (!Thread.interrupted()) { try { 
					table = NetworkTableInstance.getDefault().getTable("limelight");
					if (isConnected()) {
						setLEDMode(LEDMode.OFF);
						setEntry("camMode", 1);
						setEntry("pipeline", 0);
						setEntry("stream", 0);
						setEntry("snapshot", 0);
						console.log(c.VISION, "connected to limelight");
						break;
					}
					else Thread.sleep(200);
				}
				catch (Exception e) { CrashLogger.logCrash(new Crash("Init Limelight Thread", e)); } }
			}
		};
		initLimelightThread.start();
	}
}
