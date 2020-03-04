package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.managers.Subsystem;

public class Climber extends Subsystem {
	private static TalonFX motor;
	private static DoubleSolenoid deployerPiston;
	private static Solenoid brakePiston;
	
	public static double climberManual = 0.0;
	
	//Loop
	public void update() {
//		motor.set(TalonFXControlMode.MusicTone, Math.random() * 400);
		if ((Superstructure.getSystemState() == SystemState.AUTOMATIC ||
			Superstructure.getSystemState() == SystemState.MANUAL) && 
			Superstructure.isClimbing()) {
			setBrake(false);
			setDeployerPiston(Superstructure.getMode() == Mode.CLIMBER_DEPLOYING);
			setPercentOutput(climberManual);
		}
		else {
			setBrake(true);
			setDeployerPiston(false);
			stop();
		}	
	}

	//Internal Functions
	private void setBrake(boolean braked) {
		brakePiston.set(braked);
	}
	private void setDeployerPiston(boolean up) {
		deployerPiston.set(up ? Value.kForward: Value.kReverse);
	}
	private void setPercentOutput(double percent) {
		motor.set(ControlMode.PercentOutput, BreakerMath.clamp(percent, 0, 1));
	}
	private void stop() {
		motor.set(ControlMode.Disabled, 0.0);
	}

	//Config
	public void init() {
		deployerPiston = new DoubleSolenoid(Ports.CLIMBER_DEPLOYER_FORWARD, Ports.CLIMBER_DEPLOYER_REVERSE);
		brakePiston = new Solenoid(Ports.CLIMBER_BRAKE);
		motor = new TalonFX(Ports.CLIMBER_MOTOR);
		motor.configFactoryDefault();
	}

	//Reset
	public void disabled() {
		stop();
	}
}