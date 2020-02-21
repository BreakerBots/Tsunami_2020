package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
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
	private static TalonSRX motor;
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
				stop();
				resetEncoder();
			}
	
			//paneling
			if (Superstructure.getMode() == Mode.PANELING) {
				//rotation
				if (Superstructure.getPanelState() == PanelState.ROTATION) {
					if (getPanelRotations() >= 4) {
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
	
	//Debugging
	public void debug() {
		
	}

	// Internal Functions
	private void setPiston(boolean up) {
		piston.set(up ? Value.kForward : Value.kReverse);
	}
	private void setPercentOutput(double percent) {
		motor.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		motor.set(ControlMode.Disabled, 0);
	}
	private PanelColor readColor() {
		return sensor.getNearestColor();
	}
	private void resetEncoder() {
		motor.setSelectedSensorPosition(0);
	}
	private String readFMS() {
		String FMS = DriverStation.getInstance().getGameSpecificMessage();
		return FMS;
	}

	//External Functions
	public static boolean isFinished() {
		return complete;
	}
	public static double getPanelRotations() {
		return motor.getSelectedSensorPosition() / Constants.PANELER_TICKS_PER_REV;
	}

	//Config
	public void init() {
		sensor = new ColorSensor(I2C.Port.kOnboard);
		piston = new DoubleSolenoid(Ports.PANELER_DEPLOYER_FORWARD, Ports.PANELER_DEPLOYER_REVERSE);
		motor = new TalonSRX(Ports.PANELER_MOTOR);
		motor.configOpenloopRamp(0.25);
		motor.configFactoryDefault();
		resetEncoder();
	}

	//Reset
	public void disabled() {
		resetEncoder();
		stop();
		setPiston(false);
	}
}