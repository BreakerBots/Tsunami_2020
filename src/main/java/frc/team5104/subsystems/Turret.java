package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.console;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX falcon;
	private static double fieldOrientedOffset = 0;
	
	//Loop
	public void update() {
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			if (rightLimitHit()) {
				stop();
			}
			else setPercentOutput(Constants.TURRET_CALIBRATE_SPEED);
		}
		
		//Automatic
		else if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			//Vision Mode
			if (Superstructure.getMode() == Mode.SHOOTING) {
				if(!onTarget())
					setAngle(getTargetVisionAngle());
				else stop();
			}
			
			//Field Oriented Mode
			else {
				//TODO!!! - wrap around
				setAngle(fieldOrientedOffset + Drive.getGyro());
			}
		}
		
		//Disabled
		else stop();
		
		//Zero Encoder
		if (rightLimitHit())
			resetEncoder();
	}

	//Internal Functions
	private void setAngle(double angle) {
		falcon.set(ControlMode.MotionMagic, BreakerMath.clamp(angle, 0, 240));
	}
	private static void setPercentOutput(double percent) {
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
		return falcon.getSelectedSensorPosition() / 4096.0 * (8.0 / 60.0 /*gear*/) * (22.0 / 150.0 /*sprocket*/);
	}
	public static boolean leftLimitHit() {
		return falcon.isRevLimitSwitchClosed() == 1;
	}
	public static boolean rightLimitHit() {
		return falcon.isFwdLimitSwitchClosed() == 1;
	}
	public static boolean onTarget() {

		return Math.abs(getAngle() - getTargetVisionAngle()) < Constants.TURRET_TOL;
	}
	public static double getTargetVisionAngle() {
		//TODO!!!
		return 0;
	}
	public static void setFieldOrientedTarget(double angle) {
		fieldOrientedOffset = angle;
	}

	//Config
	public void init() {
		falcon = new TalonFX(Ports.TURRET_FALCON);
		falcon.configFactoryDefault();
		falcon.config_kP(0, Constants.TURRET_KP);
		falcon.config_kD(0, Constants.TURRET_KD);
		falcon.configMotionAcceleration((int) Constants.TURRET_ACC);
		falcon.configMotionCruiseVelocity((int) Constants.TURRET_VEL);
	}

	//Reset
	public void reset() {
		stop();
		resetEncoder();
	}
}