package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.Superstructure.Target;
import frc.team5104.util.Limelight;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.managers.Subsystem;

public class Hood extends Subsystem {
	private static TalonSRX talon;
	private static MovingAverage visionFilterY;

	//Loop
	public void update() {
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			if (backLimitHit()) {
				stop();
			}
			else setPercentOutput(Constants.HOOD_CALIBRATE_SPEED);
		}
		
		//Automatic
		else if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			if (Superstructure.getTarget() == Target.LOW) {
				setAngle(Constants.HOOD_MAX_ANGLE);
			}

			else {
				if (Superstructure.getMode() == Mode.SHOOTING && Limelight.hasTarget()) {
					//Vision
					if (!onTarget()) {
						visionFilterY.update(Limelight.getTargetY());
						setAngle(getTargetVisionAngle(visionFilterY.getDoubleOutput()));
					}
					else stop();
				}
				else setAngle(0);
			}
		}
			
		//Disabled
		else stop();
	}

	//Internal Functions
	private void setAngle(double degrees) {
		talon.set(ControlMode.MotionMagic, degrees);
	}
	private void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		talon.set(ControlMode.Disabled, 0);
	}
	private void resetEncoder() {
		talon.setSelectedSensorPosition(0);
	}

	//External Functions
	public static double getAngle() {
		return talon.getSelectedSensorPosition() / 4096.0 * (18.0/360.0) * 360.0;
	}
	public static boolean backLimitHit() {
		return talon.isRevLimitSwitchClosed() == 1;
	}
	public static boolean onTarget() {
		return Math.abs(getAngle() - getTargetVisionAngle(visionFilterY.getDoubleOutput())) < Constants.HOOD_TOL;
	}
	public static double getDistance(double limelightY) {
		return 80.5 / (Math.tan((Constants.LIMELIGHT_ANGLE + limelightY) * (Math.PI / 180)));
	}
	public static double getTargetVisionAngle(double limelightY) {
		//TODO!!!
		return 0;
	}

	//Config
	public void init() {
		talon = new TalonSRX(Ports.HOOD_TALON);
		talon.configFactoryDefault();
		talon.configClosedloopRamp(Constants.HOOD_RAMP_RATE);
		talon.configOpenloopRamp(Constants.HOOD_RAMP_RATE);
		talon.config_kP(0, Constants.HOOD_KP);
		talon.config_kD(0, Constants.HOOD_KD);
		talon.configMotionAcceleration((int) Constants.HOOD_ACC);
		talon.configMotionCruiseVelocity((int) Constants.HOOD_VEL);
		
		visionFilterY = new MovingAverage(5, 0);
	}

	//Reset
	public void reset() {
		stop();
		resetEncoder();
	}
}