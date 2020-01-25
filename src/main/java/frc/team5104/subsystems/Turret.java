package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX falcon;
	
	//Loop
	public void update() {
		//Manual 
		if (Superstructure.getSystemState() == SystemState.MANUAL) {
			
		}
		else {
			if (Superstructure.getMode() == Mode.SHOOTING) {
				//Shoot
			}
			else {
				setPercentOutput(0);
			}
		}
	}

	//Internal Functions
	private void setAngle(double angle) {
		falcon.set(ControlMode.MotionMagic, angle);
	}
	private void setPercentOutput(double percent) {
		falcon.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		falcon.set(ControlMode.Disabled, 0);
	}
	private void resetEncoder() {
		falcon.setSelectedSensorPosition(0);
	}
	
	//External Functions
	public static double getAngle() {
		return -1;
	}
	public static boolean leftLimitHit() {
		return true;
	}
	public static boolean rightLimitHit() {
		return true;
	}
	
	//Config
	public void init() {
		falcon = new TalonFX(Ports.TURRET_FALCON);
	}

	//Reset
	public void reset() {
		stop();
	}
}