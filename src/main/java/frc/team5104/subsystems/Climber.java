package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.util.managers.Subsystem;

//TODO: add port numbers for pistons
public class Climber extends Subsystem {

	private static TalonSRX talon;
	private static DoubleSolenoid leftPiston;
	private static DoubleSolenoid rightPiston;
	private static double winchingSpeed;
	private static double input;
	public static double climberManual = 0.0;
	
	//Loop
	public void update() {
		if (Superstructure.getMode() == Mode.CLIMBING) { 
			winchingSpeed = winchingSpeed < 0? Math.abs(input): input;
			setPiston(true);
			setPercentOutput(winchingSpeed);
		}
		else {
			setPiston(false);
		}	
	}

	//Internal Functions
	private void setPiston(boolean position) {
		leftPiston.set(position? Value.kForward: Value.kReverse);
		rightPiston.set(position? Value.kForward: Value.kReverse);
	}

	private void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		talon.set(ControlMode.Disabled, 0.0);
	}

	//Config
	public void init() {
		leftPiston = new DoubleSolenoid(1,2);
		rightPiston = new DoubleSolenoid(3,4);
		talon = new TalonSRX(Ports.CLIMBER_TALON_1);
	}

	//Reset
	public void reset() {
		stop();
	}
}