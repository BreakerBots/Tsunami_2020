package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.managers.Subsystem;

public class Intake extends Subsystem {
	private static TalonSRX talon;
	private static DoubleSolenoid rightPiston;
	private static DoubleSolenoid leftPiston;
	
	//Loop
	public void update() {
		if (Superstructure.getSystemState() == SystemState.AUTOMATIC ||
			Superstructure.getSystemState() == SystemState.MANUAL) {
			if (Superstructure.getMode() == Mode.INTAKE) {
				setPiston(true);
				setPercentOutput(Constants.INTAKE_SPEED);
			}
			else {
				setPiston(false);
				stop();
			}
		}
		else {
			setPiston(false);
			stop();
		}
	}

	//Internal Functions
	public void setPiston(boolean position) {
		leftPiston.set(position ? Value.kForward : Value.kReverse);
		rightPiston.set(position ? Value.kForward : Value.kReverse);
	}
	public void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
	public void stop() {
		setPiston(false);
		talon.set(ControlMode.Disabled, 0.0);
	}

	//Config
	public void init() {
		leftPiston = new DoubleSolenoid(Ports.INTAKE_DEPLOYER_FORWARD, Ports.INTAKE_DEPLOYER_REVERSE);
		rightPiston = new DoubleSolenoid(Ports.INTAKE_DEPLOYER_FORWARD, Ports.INTAKE_DEPLOYER_REVERSE);
		
		talon = new TalonSRX(Ports.INTAKE_TALON);
		talon.configFactoryDefault();
	}

	//Reset
	public void reset() {
		stop();
	}
}