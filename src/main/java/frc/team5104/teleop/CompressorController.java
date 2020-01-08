/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.teleop;

import edu.wpi.first.wpilibj.Compressor;
import frc.team5104.Controls;
import frc.team5104.util.managers.TeleopController;

public class CompressorController extends TeleopController {
	protected String getName() { return "Compressor Controller"; }

	private static Compressor compressor = new Compressor();
	
	protected void update() {
		if (Controls.COMPRESSOR_TOGGLE.get()) {
			if (compressor.enabled())
				stop();
			else start();
		}
	}
	
	protected void enabled() {
		stop();
	}
	
	public static void stop() {
		compressor.stop();
	}
	
	public static void start() {
		compressor.start();
	}
}
