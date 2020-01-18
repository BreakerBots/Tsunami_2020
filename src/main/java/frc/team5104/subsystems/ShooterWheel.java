package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.util.managers.Subsystem;

public class ShooterWheel extends Subsystem {
	private static TalonFX falon1, falcon2;
	
	//Loop
	public void update() {
		
	}

	//Internal Functions
	private void setSpeed(double rpms) {
		
	}
	private void setPercentOutput(double percent) {
		
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