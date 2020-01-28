package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.PanelState;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.ColorSensor;
import frc.team5104.util.ColorSensor.CoolColor;
import frc.team5104.util.managers.Subsystem;

public class Paneler extends Subsystem {
	private static ColorSensor sensor;
	private static TalonSRX talon;
	private static DoubleSolenoid piston;
	private static boolean complete = false;
	
	//Loop
	public void update() {
		if (Superstructure.getSystemState() == SystemState.MANUAL ||
			Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			//deploying
			if (Superstructure.getMode() == Mode.PANEL_DEPLOYING) {
				complete = false;
				setPiston(true);
				setPercentOutput(0);
				talon.setSelectedSensorPosition(0);
			}
	
			//paneling
			if (Superstructure.getMode() == Mode.PANELING) {
				//rotation
				if (Superstructure.getPanelState() == PanelState.ROTATION) {
					if (rotationControl()) {
						stop();
						setPiston(false);
						complete = true;
					} 
					else setPercentOutput(Constants.PANELER_ROT_SPEED);
				}
		
				//position
				else {
					if (atTargetPosition()) {
						stop();
						setPiston(false);
						complete = true;
					}
					else setPercentOutput(Constants.PANELER_POS_SPEED);
				}
			}
			//idle
			if (Superstructure.getMode() == Mode.IDLE) {
				stop();
				setPiston(false);
			}
		}
		else {
			stop();
			setPiston(false);
		}
	}

	// Internal Functions
	private void setPiston(boolean up) {
		piston.set(up ? Value.kForward : Value.kReverse);
	}
	private void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		talon.set(ControlMode.Disabled, 0);
	}
	private CoolColor readColor() {
		return sensor.getColor();
	}
	private boolean atTargetPosition() {
		String FMS = DriverStation.getInstance().getGameSpecificMessage();
		if (FMS.length() > 0) {
			if (FMS.charAt(0) == 'R' && readColor() == CoolColor.BLUE)
				return true;
			else if (FMS.charAt(0) == 'Y' && readColor() == CoolColor.GREEN)
				return true;
			else if (FMS.charAt(0) == 'B' && readColor() == CoolColor.RED)
				return true;
			else if (FMS.charAt(0) == 'G' && readColor() == CoolColor.YELLOW)
				return true;
		}
		return false;
	}
	private boolean rotationControl() {
		double wheelRotations = talon.getSelectedSensorPosition() / (4096);
		double cpRotations = (wheelRotations * 1.5) / 16;
		return cpRotations >= 4;
	}
	private void resetEncoder() {
		talon.setSelectedSensorPosition(0);
	}

	//External Functions
	public static boolean isFinished() {
		return complete;
	}

	//Config
	public void init() {
		sensor = new ColorSensor();
		piston = new DoubleSolenoid(Ports.PANELER_DEPLOYER_FORWARD, Ports.PANELER_DEPLOYER_REVERSE);
		talon = new TalonSRX(21);
		talon.configFactoryDefault();
		resetEncoder();
	}

	//Reset
	public void reset() {
		resetEncoder();
		stop();
		setPiston(false);
	}
}