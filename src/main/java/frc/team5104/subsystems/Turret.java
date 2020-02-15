package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.Limelight;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.PDFController;
import frc.team5104.util.Tuner;
import frc.team5104.util.console;
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX falcon;
	private static double fieldOrientedOffset = 0;
	private static PDFController visionController;
	private static MovingAverage visionFilterX;
	private static double ticksPerRev = 
			2048.0 / (8.0 / 60.0 /*gear*/) / (22.0 / 150.0 /*sprocket*/);
	
	//Loop
	public void update() {
		Constants.TURRET_VISION_KP = Tuner.getTunerInputDouble("Vision P", Constants.TURRET_VISION_KP);
		Constants.TURRET_VISION_KD = Tuner.getTunerInputDouble("Vision D", Constants.TURRET_VISION_KD);
		Constants.TURRET_VISION_TOL = Tuner.getTunerInputDouble("Vision Tol", Constants.TURRET_VISION_TOL);
		Tuner.setTunerOutput("Turret Position", getAngle());
		Tuner.setTunerOutput("Vision Target", visionFilterX.getDoubleOutput());
		
		visionController.setPDF(Constants.TURRET_VISION_KP, Constants.TURRET_VISION_KD, 0);
		visionController.setTolerance(Constants.TURRET_VISION_TOL);
		
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			//if (leftLimitHit()) {
				stop();
			//}
			//else setPercentOutput(Constants.TURRET_CALIBRATE_SPEED);
		}
		
		//Automatic
		else if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			//stop();
//			setPercentOutput(Climber.climberManual / 5.0);
//			console.log("out: " + Climber.climberManual / 5.0, "angle: " + getAngle());
			//Vision Mode
			if (Superstructure.getMode() == Mode.SHOOTING && Limelight.hasTarget()) {
//				if(!onTarget()) {
					visionFilterX.update(Limelight.getTargetX());
					setVoltage(visionController.get(visionFilterX.getDoubleOutput()));
//				}
//				else stop();
			}
			
			//Field Oriented Mode
			else {
				stop();
				//setAngle(fieldOrientedOffset + (-Drive.getGyro() % 360));
			}
		}
		
		//Disabled
		else stop();
		
		//Zero Encoder
//		if (leftLimitHit()) {
//			resetEncoder((int) (ticksPerRev * (240.0 / 360.0)));
//			//falcon.configForwardSoftLimitEnable(true);
//			//falcon.configReverseSoftLimitEnable(true);
//		}
	}

	//Internal Functions
	private void setAngle(double angle) {
		falcon.set(ControlMode.Position, 
				BreakerMath.clamp(angle, 0, 240) 
				/ 360.0 * ticksPerRev
		);
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
	private void resetEncoder() {
		falcon.setSelectedSensorPosition(0);
	}

	//External Functions
	public static double getAngle() {
		if (falcon == null)
			return 0;
		return falcon.getSelectedSensorPosition() 
				/ ticksPerRev * 360.0;
	}
	public static boolean leftLimitHit() {
		return false;
//		if (falcon == null)
//			return true;
//		return falcon.isRevLimitSwitchClosed() == 1;
	}
	public static boolean onTarget() {
		if (falcon == null)
			return true;
		return visionController.onTarget();
	}
	public static void setFieldOrientedTarget(double angle) {
		fieldOrientedOffset = angle;
	}

	//Config
	public void init() {
		falcon = new TalonFX(Ports.TURRET_FALCON);
		falcon.configFactoryDefault();
		falcon.config_kP(0, Constants.TURRET_NORMAL_KP);
		falcon.config_kD(0, Constants.TURRET_NORMAL_KD);
		falcon.configOpenloopRamp(Constants.TURRET_RAMP_RATE);
		falcon.configClosedloopRamp(Constants.TURRET_RAMP_RATE);
		falcon.setInverted(true);
		
		falcon.configForwardSoftLimitThreshold((int) (ticksPerRev * (220.0 / 360.0)));
		falcon.configReverseSoftLimitThreshold((int) (ticksPerRev * (20.0 / 360.0)));
		falcon.configForwardSoftLimitEnable(true);
		falcon.configReverseSoftLimitEnable(true);
		
//		resetEncoder();

		
		visionController = new PDFController(
			Constants.TURRET_VISION_KP, Constants.TURRET_VISION_KD, 0, 
			-4, 4, Constants.TURRET_VISION_TOL
		);
		visionFilterX = new MovingAverage(5, 0);
	}

	//Reset
	public void reset() {
		stop();
	}
}