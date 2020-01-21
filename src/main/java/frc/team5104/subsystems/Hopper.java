package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.team5104.util.managers.Subsystem;

public class Hopper extends Subsystem {
	private static TalonSRX talonStart;
	private static TalonFX falconMid;
	private static TalonSRX talonFeeder;
	
	//Loop
	public void update() {
		
	}

	//Internal Functions
	private void setPercentOutput() {
		
	}
	private void stop() {
		
	}
	
	//External Functions
	public static int getBallCount() {
		return -1;
	}
	
	public static boolean isFull() {
		return true;
	}
	//Config
	public void init() {
		
	}

	//Reset
	public void reset() {
		stop();
	}
}