package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.CharacterizedController;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.Limelight;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Tuner;
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX motor;
	private static double fieldOrientedOffset = 120;
	private static CharacterizedController controller;
	private static MovingAverage visionFilter;
	private static double targetAngle = getAngle();
	
	//Loop
	public void update() {
		//Debugging
		Tuner.setTunerOutput("Turret FF", controller.getLastFFOutput());
		Tuner.setTunerOutput("Turret PID", controller.getLastPIDOutput());
		Tuner.setTunerOutput("Turret Angle", getAngle());
		Tuner.setTunerOutput("Turret Target Angle", targetAngle);
		Constants.TURRET_KP = Tuner.getTunerInputDouble("Turret KP", Constants.TURRET_KP);
		Constants.TURRET_KD = Tuner.getTunerInputDouble("Turret KD", Constants.TURRET_KD);
		controller.setPID(Constants.TURRET_KP, 0, Constants.TURRET_KD);
		
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			if (leftLimitHit()) {
				stop();
			}
			else {
				if (Superstructure.getTimeInSystemState() < 15000)
					setPercentOutput(Constants.TURRET_CALIBRATE_SPEED);
				else if (Superstructure.getTimeInSystemState() < 30000)
					setPercentOutput(-Constants.TURRET_CALIBRATE_SPEED);
				else emergencyStop();
			}
		}
		
		//Automatic
		else if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			//Vision
			if (Superstructure.getMode() == Mode.AIMING) {
				if (Limelight.hasTarget()) {
					visionFilter.update(Limelight.getTargetX());
					setTargetAngle(getAngle() - visionFilter.getDoubleOutput());
				}
			}

			//Field Oriented Mode
			else if (Superstructure.getMode() != Mode.SHOOTING) {
				//TODO ANGLE MATH FIX DIS PLZ
				setTargetAngle(fieldOrientedOffset + (-Drive.getGyro() % 360));
			}
			
			setVoltage(controller.calculate(getAngle(), targetAngle));
		}
		
		//Disabled
		else stop();
		
		//Zero Encoder
		if (leftLimitHit()) {
			resetEncoder(250);
			enableSoftLimits(true);
		}
	}

	//Internal Functions
	private void setTargetAngle(double angle) {
		targetAngle = BreakerMath.clamp(angle, 0, 240);
	}
	private void setVoltage(double volts) {
		volts = BreakerMath.clamp(volts, -6, 6); //TODO DELETE ME!!!!
		setPercentOutput(volts / motor.getBusVoltage());
	}
	private void setPercentOutput(double percent) {
		motor.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		motor.set(ControlMode.Disabled, 0);
	}
	private void resetEncoder(double angle) {
		motor.setSelectedSensorPosition((int) (angle / 360.0 * Constants.TURRET_TICKS_PER_REV));
	}
	private void enableSoftLimits(boolean enabled) {
		motor.configForwardSoftLimitEnable(enabled);
		motor.configReverseSoftLimitEnable(enabled);
	}

	//External Functions
	public static double getAngle() {
		if (motor == null) return 0;
		return motor.getSelectedSensorPosition() / Constants.TURRET_TICKS_PER_REV * 360.0;
	}
	public static boolean leftLimitHit() {
		if (motor == null) return true;
		return motor.isRevLimitSwitchClosed() == 1;
	}
	public static boolean onTarget() {
		if (motor == null) return true;
		return Math.abs(visionFilter.getDoubleOutput()) < Constants.TURRET_VISION_TOL;
	}
	public static void setFieldOrientedTarget(double angle) {
		fieldOrientedOffset = angle;
	}

	//Config
	public void init() {
		motor = new TalonFX(Ports.TURRET_MOTOR);
		motor.configFactoryDefault();
		motor.setInverted(true);
		
		motor.configForwardSoftLimitThreshold((int) (Constants.TURRET_TICKS_PER_REV * (230.0 / 360.0)));
		motor.configReverseSoftLimitThreshold((int) (Constants.TURRET_TICKS_PER_REV * (0.0 / 360.0)));
		enableSoftLimits(false);
		motor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
		
		controller = new CharacterizedController(
				Constants.TURRET_KP,
				0,
				Constants.TURRET_KD,
				Constants.TURRET_MAX_VEL,
				Constants.TURRET_MAX_ACC,
				Constants.TURRET_KS,
				Constants.TURRET_KV,
				Constants.TURRET_KA
			);
		visionFilter = new MovingAverage(3, 0);
	}

	//Reset
	public void disabled() {
		stop();
		enableSoftLimits(false);
	}
}