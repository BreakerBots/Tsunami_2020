package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.ArmController;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.Limelight;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Tuner;
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX falcon;
	@SuppressWarnings("unused")
	private static double fieldOrientedOffset = 0;
	private static ArmController controller;
	private static MovingAverage visionFilter;
	private static double targetAngle = 0;
	
	
	//Loop
	public void update() {
		//Debugging
		Tuner.setTunerOutput("Turret Angle", getAngle());
		
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
					setTargetAngle(getAngle() + visionFilter.getDoubleOutput());
				}
			}
			
			//Field Oriented Mode
			else if (Superstructure.getMode() != Mode.SHOOTING) {
				setTargetAngle(180);
				//TODO: setTargetAngle(fieldOrientedOffset + (-Drive.getGyro() % 360));
			}
			
			setVoltage(controller.get(getAngle(), targetAngle));
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
		setPercentOutput(voltage / falcon.getBusVoltage());
	}
	private void setPercentOutput(double percent) {
		falcon.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		falcon.set(ControlMode.Disabled, 0);
	}
	@SuppressWarnings("unused")
	private void resetEncoder() {
		falcon.setSelectedSensorPosition(0);
	}

	//External Functions
	public static double getAngle() {
		if (falcon == null) return 0;
		return falcon.getSelectedSensorPosition() / Constants.TURRET_TICKS_PER_REV * 360.0;
	}
	public static boolean leftLimitHit() {
		if (falcon == null) return true;
		return false;
		//return falcon.isRevLimitSwitchClosed() == 1;
	}
	public static boolean onTarget() {
		if (falcon == null) return true;
		return Math.abs(visionFilter.getDoubleOutput()) < Constants.TURRET_VISION_TOL;
	}
	public static void setFieldOrientedTarget(double angle) {
		fieldOrientedOffset = angle;
	}

	//Config
	public void init() {
		falcon = new TalonFX(Ports.TURRET_FALCON);
		falcon.configFactoryDefault();
		falcon.setInverted(true);
		
		falcon.configForwardSoftLimitThreshold((int) (Constants.TURRET_TICKS_PER_REV * (220.0 / 360.0)));
		falcon.configReverseSoftLimitThreshold((int) (Constants.TURRET_TICKS_PER_REV * (20.0 / 360.0)));
		falcon.configForwardSoftLimitEnable(true);
		falcon.configReverseSoftLimitEnable(true);
		
//		resetEncoder();

		controller = new ArmController(
				Constants.TURRET_KP,
				0,
				Constants.TURRET_KD,
				Constants.TURRET_MAX_VEL,
				Constants.TURRET_MAX_ACC,
				Constants.TURRET_KS,
				Constants.TURRET_KC,
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