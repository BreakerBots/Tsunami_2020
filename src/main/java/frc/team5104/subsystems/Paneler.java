package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5104.util.managers.Subsystem;

public class Paneler extends Subsystem {
	private static TalonSRX talon;
	private static DoubleSolenoid piston;
	
	//Loop
	public void update() {
		
	}

	//Internal Functions
	private void setPiston(boolean up) {
		piston.set(up ? Value.kForward : Value.kReverse);
	}
	private void setMotionMagic(double degrees) {
		talon.set(ControlMode.MotionMagic, degrees);
	}
	private void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
	
	//External Functions
	public static boolean atTargetPosition() {
		return false;
	}
	public static void readColor() {
		
	}
	
	//Config
	public void init() {
		
	}

	//Reset
	public void reset() {
		
	}
}