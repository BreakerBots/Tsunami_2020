package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.PositionController;
import frc.team5104.util.Tuner;
import frc.team5104.util.console;
import frc.team5104.util.console.c;
import frc.team5104.util.LatencyCompensator;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.Filer;
import frc.team5104.util.Limelight;
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX motor;
	private static double fieldOrientedOffset = 120;
	private static PositionController controller;
	private static LatencyCompensator compensator;
	//private static MovingAverage visionFilter;
	private static double targetAngle = getAngle();
	
	//Loop
	public void update() {
		//Automatic
		if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			//Calibrating
			if (isCalibrating()) {
				if (getTimeInCalibration() < 15000)
					setPercentOutput(Constants.TURRET_CALIBRATE_SPEED);
				else emergencyStop();
			}
			
			//Vision
			else if (Superstructure.getMode() == Mode.AIMING || Superstructure.getMode() == Mode.SHOOTING) {
				if (Limelight.hasTarget()) {
					setAngle(
						compensator.getValueInHistory(Limelight.getLatency()) - Limelight.getTargetX()
					);
				}
				else setAngle(targetAngle);
			}

			//Field Oriented Mode
			else setAngle(BreakerMath.boundDegrees360(Drive.getGyro() + fieldOrientedOffset));
		}
		
		//Disabled
		else stop();
	}
	
	//Fast Loop
	public void fastUpdate() {
		//Exit Calibration
		if (isCalibrating() && leftLimitHit()) {
			console.log(c.TURRET, "finished calibration!");
			stopCalibrating();
		}
		
		//Zero Encoder
		if (leftLimitHit()) {
			resetEncoder(250);
			enableSoftLimits(true);
		}
	}
	
	//Debugging
	public void debug() {
		Tuner.setTunerOutput("Turret FF", controller.getLastFFOutput());
		Tuner.setTunerOutput("Turret PID", controller.getLastPIDOutput());
		Tuner.setTunerOutput("Turret Angle", getAngle());
		Tuner.setTunerOutput("Turret Target Angle", targetAngle);
		Constants.TURRET_KP = Tuner.getTunerInputDouble("Turret KP", Constants.TURRET_KP);
		Constants.TURRET_KD = Tuner.getTunerInputDouble("Turret KD", Constants.TURRET_KD);
		controller.setPID(Constants.TURRET_KP, 0, Constants.TURRET_KD);
	}

	//Internal Functions
	private void setAngle(double angle) {
		targetAngle = BreakerMath.clamp(angle, 0, 240);
		setVoltage(controller.calculate(getAngle(), targetAngle));
	}
	private void setVoltage(double volts) {
		volts = BreakerMath.clamp(volts, -Constants.TURRET_VOLT_LIMIT, Constants.TURRET_VOLT_LIMIT);
		setPercentOutput(volts / motor.getBusVoltage());
	}
	private void setPercentOutput(double percent) {
		motor.setNeutralMode(NeutralMode.Brake);
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
		return Math.abs(getAngle() - targetAngle) < Constants.TURRET_VISION_TOL;
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
		
		controller = new PositionController(
				Constants.TURRET_KP,
				0,
				Constants.TURRET_KD,
				Constants.TURRET_MAX_VEL,
				Constants.TURRET_MAX_ACC,
				Constants.TURRET_KS,
				Constants.TURRET_KV,
				Constants.TURRET_KA
			);
		compensator = new LatencyCompensator(() -> getAngle());
		
		//Always calibrate at comp. Only calibrate once per roborio boot while not.
		if (Constants.AT_COMPETITION || !Filer.fileExists("/tmp/turret_calibrated.txt")) {
			Filer.createFile("/tmp/turret_calibrated.txt");
			startCalibrating();
			console.log(c.TURRET, "ready to calibrate!");
		}
	}

	//Reset
	public void disabled() {
		stop();
		motor.setNeutralMode(NeutralMode.Coast);
		enableSoftLimits(false);
		compensator.reset();
	}
}