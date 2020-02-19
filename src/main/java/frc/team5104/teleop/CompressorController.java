/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.teleop;

import edu.wpi.first.wpilibj.Compressor;
import frc.team5104.Controls;
import frc.team5104.util.console;
import frc.team5104.util.managers.TeleopController;

public class CompressorController extends TeleopController {
	private static Compressor compressor = new Compressor();
	
	protected void update() {
		if (Controls.COMPRESSOR_TOGGLE.get()) {
			if (compressor.enabled())
				stop();
			else start();
		}
	}
	
	public static void stop() {
		if (compressor.enabled()) {
			console.log("Stop compressing");
			compressor.stop();
		}
	}
	
	public static void start() {
		if (!compressor.enabled()) {
			console.log("Start compressing");
			compressor.start();
		}
	}
}
