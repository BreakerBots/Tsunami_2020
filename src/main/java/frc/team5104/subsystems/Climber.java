package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.managers.Subsystem;

public class Climber extends Subsystem {
	private static TalonFX motor;
	private static DoubleSolenoid piston;
	
	public static double climberManual = 0.0;
	
	//Loop
	public void update() {
		if (Superstructure.getSystemState() == SystemState.AUTOMATIC ||
			Superstructure.getSystemState() == SystemState.MANUAL) {
			setPiston(Superstructure.getMode() == Mode.CLIMBING);
			setPercentOutput(climberManual);
		}
		else {
			setPiston(false);
			stop();
		}	
	}

	//Internal Functions
	private void setPiston(boolean up) {
		piston.set(up ? Value.kForward: Value.kReverse);
	}
	private void setPercentOutput(double percent) {
		motor.set(ControlMode.PercentOutput, BreakerMath.clamp(percent, 0, 1));
	}
	private void stop() {
		motor.set(ControlMode.Disabled, 0.0);
	}

	//Config
	public void init() {
		piston = new DoubleSolenoid(Ports.CLIMBER_DEPLOYER_FORWARD, Ports.CLIMBER_DEPLOYER_REVERSE);
		
		motor = new TalonFX(Ports.CLIMBER_MOTOR);
		motor.configFactoryDefault();
	}

	//Reset
	public void disabled() {
		stop();
	}
}