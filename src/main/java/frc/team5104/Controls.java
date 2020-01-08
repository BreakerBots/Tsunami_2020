/*BreakerBots Robotics Team 2019*/
package frc.team5104;

import frc.team5104.util.BezierCurve;
import frc.team5104.util.Deadband;
import frc.team5104.util.XboxController;
import frc.team5104.util.XboxController.Axis;
import frc.team5104.util.XboxController.Button;

/** All the controls for the robot */
public class Controls {
	public static XboxController driver = XboxController.create(0);
	public static XboxController operator = XboxController.create(1);
	
	//Drive
	public static final Axis DRIVE_TURN = driver.getAxis(Axis.LEFT_JOYSTICK_X, new Deadband(0.08), new BezierCurve(0.15, 0.7, 0.8, 0.225));
	public static final Axis DRIVE_FORWARD = driver.getAxis(Axis.RIGHT_TRIGGER, new Deadband(0.01));
	public static final Axis DRIVE_REVERSE = driver.getAxis(Axis.LEFT_TRIGGER, new Deadband(0.01));
	public static final Button TOGGLE_VISION = driver.getButton(Button.A);
	
	//Compressor
	public static final Button COMPRESSOR_TOGGLE = driver.getButton(Button.MENU);
}
