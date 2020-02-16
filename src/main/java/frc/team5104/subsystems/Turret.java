package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
//		Tuner.setTunerOutput("Turret Angle", getAngle());
//		Constants.TURRET_KP = Tuner.getTunerInputDouble("Turret KP", Constants.TURRET_KP);
//		Constants.TURRET_KD = Tuner.getTunerInputDouble("Turret KD", Constants.TURRET_KD);
//		controller.setPID(Constants.TURRET_KP,
//				0,
//				Constants.TURRET_KD);
		
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			//TODO: Slowly drive
			stop();
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
				setTargetAngle(fieldOrientedOffset + (-Drive.getGyro() % 360));
			}
			
			setVoltage(BreakerMath.clamp(controller.calculate(getAngle(), targetAngle), -4, 4));
		}
		
		//Disabled
		else stop();
		
		//TODO: Zero Encoder
	}

	//Internal Functions
	private void setTargetAngle(double angle) {
		targetAngle = BreakerMath.clamp(angle, 0, 240);
	}
	private void setVoltage(double voltage) {
		setPercentOutput(voltage / motor.getBusVoltage());
	}
	private void setPercentOutput(double percent) {
		motor.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		motor.set(ControlMode.Disabled, 0);
	}
	@SuppressWarnings("unused")
	private void resetEncoder() {
		motor.setSelectedSensorPosition(0);
	}

	//External Functions
	public static double getAngle() {
		if (motor == null) return 0;
		return motor.getSelectedSensorPosition() / Constants.TURRET_TICKS_PER_REV * 360.0;
	}
	public static boolean leftLimitHit() {
		if (motor == null) return true;
		return false;
		//return falcon.isRevLimitSwitchClosed() == 1;
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
		
		motor.configForwardSoftLimitThreshold((int) (Constants.TURRET_TICKS_PER_REV * (220.0 / 360.0)));
		motor.configReverseSoftLimitThreshold((int) (Constants.TURRET_TICKS_PER_REV * (20.0 / 360.0)));
		motor.configForwardSoftLimitEnable(true);
		motor.configReverseSoftLimitEnable(true);
		
		//resetEncoder();

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
	public void reset() {
		stop();
	}
}