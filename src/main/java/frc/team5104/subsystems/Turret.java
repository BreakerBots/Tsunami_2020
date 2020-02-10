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
import frc.team5104.util.managers.Subsystem;

public class Turret extends Subsystem {
	private static TalonFX falcon;
	private static double fieldOrientedOffset = 0;
	private static PDFController visionController;
	private static MovingAverage visionFilterX;
	
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
			if (Superstructure.getMode() == Mode.SHOOTING && Limelight.hasTarget()) {
				if(!onTarget()) {
					visionFilterX.update(Limelight.getTargetX());
					setVoltage(visionController.get(visionFilterX.getDoubleOutput()));
				}
				else stop();
			}
			
			//Field Oriented Mode
			else {
				setAngle(fieldOrientedOffset + (-Drive.getGyro() % 360));
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
		falcon.set(ControlMode.Position, BreakerMath.clamp(angle, 0, 240));
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
		return falcon.getSelectedSensorPosition() 
				/ 4096.0 * (8.0 / 60.0 /*gear*/) * (22.0 / 150.0 /*sprocket*/) * 360.0;
	}
	public static boolean leftLimitHit() {
		return falcon.isRevLimitSwitchClosed() == 1;
	}
	public static boolean rightLimitHit() {
		return falcon.isFwdLimitSwitchClosed() == 1;
	}
	public static boolean onTarget() {
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
		
		visionController = new PDFController(
			Constants.TURRET_VISION_KP, Constants.TURRET_VISION_KD, 0, 
			-4, 4, Constants.TURRET_VISION_TOL
		);
		visionFilterX = new MovingAverage(5, 0);
	}

	//Reset
	public void reset() {
		stop();
		resetEncoder();
	}
}