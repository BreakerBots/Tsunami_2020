/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.util;

import frc.team5104.Constants;
import frc.team5104.subsystems.Drive;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.CrashLogger;
import frc.team5104.util.CrashLogger.Crash;
import frc.team5104.util.console.c;
import frc.team5104.util.Units;
import frc.team5104.util.console;
import edu.wpi.first.wpilibj.Notifier;

/**
 * <h1>Odometry (Robot Position Estimator/Kinematics)</h1>
 * Calculates the Robots x, y position according to encoder values.
 */
public class Odometry {
	private static Notifier _thread = null;
		
	private volatile static double lastPos, currentPos, dPos, theta;
	public volatile static RobotPosition position = new RobotPosition(0, 0, 0);
	
	private static void init() {
		lastPos = currentPos = (Drive.getLeftEncoderPositionRaw() + Drive.getRightEncoderPositionRaw()) / 2.0;
		_thread = new Notifier(() -> {
			try {
				currentPos = (Drive.getLeftEncoderPositionRaw() + Drive.getRightEncoderPositionRaw()) / 2.0;
				dPos = Units.ticksToFeet(currentPos - lastPos);
				lastPos = currentPos;
				theta = Units.degreesToRadians(BreakerMath.boundDegrees180(Drive.getGyro()));
	            position.addX(Math.cos(theta) * dPos);
	            position.addY(Math.sin(theta) * dPos);
	            position.setTheta(theta);
			} catch (Exception e) { CrashLogger.logCrash(new Crash("odometry", e)); }
        });
	}
	
	public static void run() {
		if (_thread == null)
			init();
		
		_thread.startPeriodic(1.0 / Constants.AUTO_LOOP_SPEED);
	}
	
	public static void stop() {
		if (_thread != null)
			_thread.stop();
	}
	
	public static RobotPosition getPosition() {
		return position;
	}
	
	public static void reset() {
		console.log("Resetting Odometry");
		stop();
		Drive.resetEncoders();
		Drive.resetGyro();
		lastPos = 0; 
		currentPos = 0; 
		dPos = 0; 
		theta = 0;
		position = new RobotPosition(0, 0, 0);
		lastPos = 0;
		init();
		run();
		console.log(c.AUTO, "Finished Resetting Odometry at " + getPosition().toString());
	}
}
