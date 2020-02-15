package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.managers.Subsystem;

public class Climber extends Subsystem {
	private static TalonSRX talon1, talon2;
	private static DoubleSolenoid piston;
	
	public static double climberManual = 0.0;
	
	//Loop
	public void update() {
		if ((Superstructure.getSystemState() == SystemState.AUTOMATIC ||
			Superstructure.getSystemState() == SystemState.MANUAL) &&
			Superstructure.getMode() == Mode.CLIMBING) { 
				setPiston(true);
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
		talon1.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		talon1.set(ControlMode.Disabled, 0.0);
	}

	//Config
	public void init() {
		piston = new DoubleSolenoid(Ports.CLIMBER_DEPLOYER_FORWARD, Ports.CLIMBER_DEPLOYER_REVERSE);
		
		talon1 = new TalonSRX(Ports.CLIMBER_MOTOR_1);
		talon1.configFactoryDefault();
		
		talon2 = new TalonSRX(Ports.CLIMBER_MOTOR_2);
		talon2.configFactoryDefault();
		talon2.follow(talon1);
	}

	//Reset
	public void reset() {
		stop();
	}
}