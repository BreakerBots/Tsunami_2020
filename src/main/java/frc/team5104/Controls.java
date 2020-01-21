/*BreakerBots Robotics Team 2020*/
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
	
	public static final Button IDLE = driver.getButton(Button.LIST);
	public static final Button IDLE_OP = operator.getButton(Button.LIST);
	
	//Drive
	public static final Axis DRIVE_TURN = driver.getAxis(Axis.LEFT_JOYSTICK_X, new Deadband(0.08), new BezierCurve(0.15, 0.7, 0.8, 0.225));
	public static final Axis DRIVE_FORWARD = driver.getAxis(Axis.RIGHT_TRIGGER, new Deadband(0.01));
	public static final Axis DRIVE_REVERSE = driver.getAxis(Axis.LEFT_TRIGGER, new Deadband(0.01));
	public static final Button DRIVE_SHIFT = driver.getButton(Button.LEFT_JOYSTICK_PRESS);

	//Balls
	public static final Button BALL_INTAKE = driver.getButton(Button.X);
	public static final Button BALL_SHOOT = driver.getButton(Button.B);
	public static final Button UNJAM = driver.getButton(Button.LEFT_BUMPER);
	public static final Button BALL_INTAKE_OP = operator.getButton(Button.X);
	public static final Button BALL_SHOOT_OP = operator.getButton(Button.B);
	public static final Button UNJAM_OP = operator.getButton(Button.LEFT_BUMPER);
	
	//Panel
	public static final Button PANEL_DEPLOY = driver.getButton(Button.Y);
	public static final Button PANEL_SPIN = driver.getButton(Button.A);
	public static final Button PANEL_TOGGLE = driver.getButton(Button.RIGHT_BUMPER);
	public static final Button PANEL_DEPLOY_OP = operator.getButton(Button.Y);
	public static final Button PANEL_SPIN_OP = operator.getButton(Button.A);
	
	//Climb
	public static final Button CLIMBER_DEPLOY = driver.getButton(Button.DIRECTION_PAD_UP);
	public static final Button CLIMBER_FOLD = driver.getButton(Button.DIRECTION_PAD_DOWN);
	public static final Axis CLIMBER_WINCH = driver.getAxis(Axis.RIGHT_JOYSTICK_Y);
	public static final Button CLIMBER_DEPLOY_OP = operator.getButton(Button.DIRECTION_PAD_UP);
	public static final Button CLIMBER_FOLD_OP = operator.getButton(Button.DIRECTION_PAD_DOWN);
	public static final Axis CLIMBER_WINCH_OP = operator.getAxis(Axis.RIGHT_JOYSTICK_Y);
	
	//Compressor
	public static final Button COMPRESSOR_TOGGLE = driver.getButton(Button.MENU);
}
