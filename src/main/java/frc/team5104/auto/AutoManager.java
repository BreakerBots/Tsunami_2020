/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto;

import frc.team5104.Constants;
import frc.team5104.teleop.CompressorController;
import frc.team5104.util.Plotter;
import frc.team5104.util.Plotter.Color;
import frc.team5104.util.setup.RobotState;
import frc.team5104.util.setup.RobotState.RobotMode;

/** Basically just handles a separate thread for autonomous. The path is updated in "AutoPathScheduler" */
public class AutoManager {
	private static AutoPath targetPath = null;
	private static AutoPathScheduler pathScheduler;
	
	//Enabled/Disbabled
	public static void enabled() {
		//reset odometry
		Odometry.reset();
		
		//choose path
		pathScheduler = new AutoPathScheduler(targetPath);
	}
	public static void disabled() {
		
	}
	
	//Init
	public static void setTargetPath(AutoPath path) { 
		targetPath = path;
	}
	
	//Update
	public static void update() {
		if (RobotState.getMode() == RobotMode.AUTONOMOUS) {
			//stop compressor
			CompressorController.stop();
			
			//update odometry
			Odometry.update();
			if (Constants.AUTO_PLOT_ODOMETRY) {
				Plotter.plot(
						Odometry.getPose2dMeters().getTranslation().getX(), 
						Odometry.getPose2dMeters().getTranslation().getY(),
						Color.ORANGE
					);
			}
			
			//update path
			if (pathScheduler != null)
				pathScheduler.update();
		}
	}
}