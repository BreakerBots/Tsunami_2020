/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto;

import edu.wpi.first.wpilibj.Notifier;
import frc.team5104.Constants;
import frc.team5104.auto.util.AutoPath;
import frc.team5104.util.CrashLogger;
import frc.team5104.util.CrashLogger.Crash;
import frc.team5104.util.setup.RobotState;

/** Basically just handles a separate thread for autonomous. The path is updated in "AutoPathScheduler" */
public class AutoManager {
	private static Notifier _thread = null;
	private static AutoPath targetPath = null;
	private static AutoPathScheduler pathScheduler;
	
	private static void init() {
		//choose path
		pathScheduler = new AutoPathScheduler(targetPath);
		
		//loop
		_thread = new Notifier(() -> {try {
			if (RobotState.isSandstorm()) {
				//update path
				pathScheduler.update();
			}
			else {
				//stop
				stop();
			}
		} catch (Exception e) { CrashLogger.logCrash(new Crash("auto", e)); }});
	}
	
	public static void setTargetPath(AutoPath path) { targetPath = path; }
	
	public static void run() {
		init();
		_thread.startPeriodic(1.0 / Constants.AUTO_LOOP_SPEED);
	}
	
	public static void stop() {
		if (_thread != null) 
			_thread.stop();
		_thread = null;
		pathScheduler = null;
	}
}
