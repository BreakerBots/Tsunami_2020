/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.util;

import edu.wpi.first.cameraserver.CameraServer;

/** A wrapper class to handle camera streaming */
public class Cameras {
	public static void start() {
		CameraServer.getInstance().startAutomaticCapture();
	}
}
