package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.PanelState;
import frc.team5104.util.ColorSensor;
import frc.team5104.util.ColorSensor.CoolColor;
import frc.team5104.util.console;
import frc.team5104.util.managers.Subsystem;

public class Paneler extends Subsystem {
	private static ColorSensor sensor;
	private static TalonSRX talon;
	private static Joystick joystick;
	private static DoubleSolenoid piston;
	private static CoolColor color;

	private static boolean complete = false;
	private static final double ROTATION_MOTOR_SPEED = 0.75;
	private static final double POSITION_MOTOR_SPEED = 0.3;

	// Loop
	public void update() {
		// Deploy spinner
		if (Superstructure.getMode() == Mode.PANEL_DEPLOYING) {
			complete = false;
			setPiston(true);
			setPercentOutput(0);
			talon.setSelectedSensorPosition(0);
		}
		
		// Rotation Control
		if (Superstructure.getMode() == Mode.PANELING && Superstructure.getPanelState() == PanelState.ROTATION) {

			if (rotationControl() == true) {
				setPercentOutput(0);
				complete = true;
				setPiston(false);
			}

			else {
				setPercentOutput(ROTATION_MOTOR_SPEED);
			}
		}
		
		// Position Control
				if (Superstructure.getMode() == Mode.PANELING && Superstructure.getPanelState() == PanelState.POSITION) {
					if (atTargetPosition() == true) {
						setPercentOutput(0);
						complete = true;
						setPiston(false);
					}

					else {
						setPercentOutput(POSITION_MOTOR_SPEED);
					}

				}

		// Idle
		if (Superstructure.getMode() == Mode.IDLE) {
			setPercentOutput(0);
			setPiston(false);
		}

	}

	// Internal Functions
	private void setPiston(boolean up) {
		piston.set(up ? Value.kForward : Value.kReverse);
	}

	// ---------------------------------------------

	private void setMotionMagic(double degrees) {
		talon.set(ControlMode.MotionMagic, degrees);
	}

	// ------------------------------------------------------

	private void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}

	// ------------------------------------------------------

	public static CoolColor readColor() {
		return sensor.getColor();
	}

	// ------------------------------------------------------

	public static boolean atTargetPosition() {
		String FMS = DriverStation.getInstance().getGameSpecificMessage();
		if (FMS.length() > 0) {
			if (FMS.charAt(0) == 'R' && sensor.getColor() == CoolColor.BLUE) {
				return true;
			} else if (FMS.charAt(0) == 'Y' && sensor.getColor() == CoolColor.GREEN) {
				return true;
			} else if (FMS.charAt(0) == 'B' && sensor.getColor() == CoolColor.RED) {
				return true;
			} else if (FMS.charAt(0) == 'G' && sensor.getColor() == CoolColor.YELLOW) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------

	public static boolean rotationControl() {
		double wheelRotations = talon.getSelectedSensorPosition() / (4096);
		double cpRotations = (wheelRotations * 1.5) / 16;
		//console.log(wheelRotations + "   " + cpRotations);
		// if (cpRotations >= 3 && cpRotations <= 5) {
		if (cpRotations >= 4) {
			return true;
		}

		else {
			return false;
		}
	}

	// External Functions

	public static boolean isFinished() {
		if (complete) {
			return true;
		}

		else {
			return false;
		}
	}

	// Config
	public void init() {
		sensor = new ColorSensor();
		talon = new TalonSRX(21);
		joystick = new Joystick(0);
		talon.setSelectedSensorPosition(0);
		piston = new DoubleSolenoid(Ports.PANELER_DEPLOYER_FORWARD, Ports.PANELER_DEPLOYER_REVERSE);
	}

	// Reset
	public void reset() {

	}
}