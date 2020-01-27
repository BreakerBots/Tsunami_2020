/*BreakerBots Robotics Team 2020*/
package frc.team5104;

import frc.team5104.util.BezierCurve;
import frc.team5104.util.Deadband;
import frc.team5104.util.XboxController;
import frc.team5104.util.XboxController.Axis;
import frc.team5104.util.XboxController.Button;
import frc.team5104.util.XboxController.DoubleButton;

/** All the controls for the robot */
public class Controls {
	public static XboxController driver = XboxController.create(0);
	public static XboxController operator = XboxController.create(1);
	
	//Main
	public static final Button IDLE = new DoubleButton(
		driver.getButton(Button.LIST),
		operator.getButton(Button.LIST));
	public static final DoubleButton COMPRESSOR_TOGGLE = new DoubleButton(
			driver.getButton(Button.MENU),
			operator.getButton(Button.MENU));
	
	//Drive
	public static final Axis DRIVE_TURN = driver.getAxis(Axis.LEFT_JOYSTICK_X, new Deadband(0.08), new BezierCurve(0.15, 0.7, 0.8, 0.225));
	public static final Axis DRIVE_FORWARD = driver.getAxis(Axis.RIGHT_TRIGGER, new Deadband(0.01));
	public static final Axis DRIVE_REVERSE = driver.getAxis(Axis.LEFT_TRIGGER, new Deadband(0.01));
	public static final Button DRIVE_SHIFT = driver.getButton(Button.LEFT_JOYSTICK_PRESS);

	//Intake
	public static final DoubleButton INTAKE = new DoubleButton(
		driver.getButton(Button.X),
		operator.getButton(Button.X));
	
	//Hopper
	public static final DoubleButton HOPPER_UNJAM = new DoubleButton(
		driver.getHoldButton(Button.LEFT_BUMPER),
		operator.getHoldButton(Button.LEFT_BUMPER));
	
	//Shooter
	public static final DoubleButton SHOOT = new DoubleButton(
			driver.getButton(Button.B),
			operator.getButton(Button.B));
	public static final DoubleButton CHARGE_FLYWHEEL = new DoubleButton(
			driver.getButton(Button.RIGHT_BUMPER),
			operator.getButton(Button.RIGHT_BUMPER));
	public static final DoubleButton SHOOT_LOW = new DoubleButton(
			driver.getButton(Button.DIRECTION_PAD_LEFT),
			operator.getButton(Button.DIRECTION_PAD_LEFT));
	
	public static final Button SHOOT_HIGH = driver.getButton(Button.DIRECTION_PAD_RIGHT);
	public static final Button SHOOT_HIGH_OP = operator.getButton(Button.DIRECTION_PAD_RIGHT);
	
	//Panel
	public static final Button PANEL_DEPLOY = driver.getButton(Button.Y);
	public static final Button PANEL_SPIN = driver.getButton(Button.A);
	public static final Button PANEL_ROTATION = driver.getButton(Button.DIRECTION_PAD_UP);
	public static final Button PANEL_POSITION = driver.getButton(Button.DIRECTION_PAD_DOWN);
	public static final Button PANEL_DEPLOY_OP = operator.getButton(Button.Y);
	public static final Button PANEL_SPIN_OP = operator.getButton(Button.A);
	public static final Button PANEL_ROTATION_OP = operator.getButton(Button.DIRECTION_PAD_UP);
	public static final Button PANEL_POSITION_OP = operator.getButton(Button.DIRECTION_PAD_DOWN);
	
	//Climb
	public static final Button CLIMBER_DEPLOY = driver.getButton(Button.RIGHT_JOYSTICK_PRESS);
	public static final Axis CLIMBER_WINCH = driver.getAxis(Axis.RIGHT_JOYSTICK_Y);
	public static final Button CLIMBER_DEPLOY_OP = operator.getButton(Button.RIGHT_JOYSTICK_PRESS);
	public static final Axis CLIMBER_WINCH_OP = operator.getAxis(Axis.RIGHT_JOYSTICK_Y);
}
