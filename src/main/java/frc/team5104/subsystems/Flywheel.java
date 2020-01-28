package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.FlywheelState;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.managers.Subsystem;

public class Flywheel extends Subsystem {
	private static TalonFX falcon1, falcon2;
	private static double targetRPMS = 0;
	
	//Loop
	public void update() {
		if ((Superstructure.getSystemState() == SystemState.AUTOMATIC || 
			Superstructure.getSystemState() == SystemState.MANUAL) &&
			Superstructure.getFlywheelState() == FlywheelState.SPINNING) {
			if (Constants.FLYWHEEL_OPEN_LOOP)
				setPercentOutput(targetRPMS / 6380);
			else setSpeed(targetRPMS);
		}
		else stop();
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
	public static boolean isSpedUp() {
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