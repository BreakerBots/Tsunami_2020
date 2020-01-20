package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.util.managers.Subsystem;

public class ShooterWheel extends Subsystem {
	private static TalonFX falcon1, falcon2;
	
	//Loop
	public void update() {
		
	}

	//Internal Functions
	private void setSpeed(double rpms) {
	}
	private void setPercentOutput(double percent) {
		falcon1.set(ControlMode.PercentOutput, percent);
		falcon2.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		
	}
	
	//External Functions
	public static boolean isSped() {
		return false;
	}
	
	//Config
	public void init() {
		
	}

	//Reset
	public void reset() {
		stop();
	}
}