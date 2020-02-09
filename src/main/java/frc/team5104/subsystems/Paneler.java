package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.PanelState;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.ColorSensor;
import frc.team5104.util.ColorSensor.PanelColor;
import frc.team5104.util.managers.Subsystem;

public class Paneler extends Subsystem {
	private static ColorSensor sensor;
	private static TalonSRX talon;
	private static DoubleSolenoid piston;
	private static boolean complete;
	
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
					if (talon.getSelectedSensorPosition() / 4096.0 * 1.5 / 16.0 >= 4) {
						stop();
						setPiston(false);
						complete = true;
					} 
					else setPercentOutput(Constants.PANELER_ROT_SPEED);
				}
		
				//position
				else {
					if (readFMS().length() > 0 && PanelColor.fromChar(readFMS().charAt(0)) == readColor()) {
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
	private PanelColor readColor() {
		return sensor.getNearestColor();
	}
	private void resetEncoder() {
		talon.setSelectedSensorPosition(0);
	}
	private String readFMS() {
		String FMS = DriverStation.getInstance().getGameSpecificMessage();
		return FMS;
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