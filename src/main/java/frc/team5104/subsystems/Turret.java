package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.managers.Subsystem;
import frc.team5104.vision.Limelight;

public class Turret extends Subsystem {
	static double _minTargetErr = 0.2;
	private static TalonFX falcon;
	//Loop
	public void update() {
		if(Superstructure.getSystemState() == SystemState.MANUAL) {
			// TODO: write code n stuff
		}
		else {
			if(Superstructure.getMode() == Mode.SHOOTING && !onTarget()) {
				setAngle(getAngle() + Limelight.getTargetX());
			} else {
				setPercentOutput(0);
			}
		}
	}
// Good Work everyone =P
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
		// fix
		return falcon.getSelectedSensorPosition();
	}
	public static boolean leftLimitHit() {
		return true;
	}
	public static boolean rightLimitHit() {
		return true;
	}
	public static boolean onTarget() {
		return Math.abs(Limelight.getTargetX()) < _minTargetErr;
	}

	//Config
	public void init() {
		falcon = new TalonFX(Ports.TURRET_FALCON);
	}

	//Reset
	public void reset() {
		stop();
		falcon.setSelectedSensorPosition(0);
	}
}