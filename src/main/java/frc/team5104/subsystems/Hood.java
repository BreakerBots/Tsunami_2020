package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.Superstructure.Target;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.Limelight;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Tuner;
import frc.team5104.util.managers.Subsystem;

public class Hood extends Subsystem {
	private static TalonSRX talon;
	private static MovingAverage visionFilterY;
	private static double ticksPerRev = 4096.0 * (360.0 / 18.0);
	private static double targetAngle = 10;

	//Loop
	public void update() {
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			if (backLimitHit()) {
				stop();
			}
			else setPercentOutput(-Constants.HOOD_CALIBRATE_SPEED);
		}
		
		//Automatic
		else if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			if (Superstructure.getTarget() == Target.LOW) {
				setAngle(40);
			}
			
			else {
				//Vision
				if (Superstructure.getMode() == Mode.AIMING && Limelight.hasTarget()) {
					//Vision
					if (Limelight.hasTarget()) {
						visionFilterY.update(Limelight.getTargetY());
						setAngle(getTargetVisionAngle());
					}
					else stop();
				}
				
				//Stop
				else if (Superstructure.getMode() == Mode.SHOOTING)
					stop();
				
				//Pull Back
				else setAngle(0);
			}
		}
			
		//Disabled
		else stop();
		
		//Zero
		if (backLimitHit()) {
			resetEncoder();
			talon.configForwardSoftLimitEnable(true);
		}
		
		visionFilterY.update(Limelight.getTargetY());
		
		Tuner.setTunerOutput("Hood Position", getAngle());
		Tuner.setTunerOutput("Hood Output", talon.getMotorOutputPercent());
		Tuner.setTunerOutput("Hood Y", Limelight.getTargetY());
		
//		kA = Tuner.getTunerInputDouble("kA", kA);
//		kB = Tuner.getTunerInputDouble("kB", kB);
//		kC = Tuner.getTunerInputDouble("kC", kC);
		targetAngle = Tuner.getTunerInputDouble("targetAngle", targetAngle);
		
		Constants.HOOD_KP = Tuner.getTunerInputDouble("Hood KP", Constants.HOOD_KP);
		Constants.HOOD_KD = Tuner.getTunerInputDouble("Hood KD", Constants.HOOD_KD);
		talon.config_kP(0, Constants.HOOD_KP);
		talon.config_kD(0, Constants.HOOD_KD);
	}

	//Internal Functions
	private void setAngle(double degrees) {
		talon.set(ControlMode.Position, BreakerMath.clamp(degrees, 0, 40) / 360.0 * ticksPerRev);
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
		if (talon == null)
			return 0;
		return talon.getSelectedSensorPosition() / ticksPerRev * 360.0;
	}
	public static boolean backLimitHit() {
		if (talon == null)
			return true;
		return !talon.getSensorCollection().isRevLimitSwitchClosed();
	}
	public static boolean onTarget() {
		if (talon == null)
			return true;
		return Math.abs(getAngle() - getTargetVisionAngle()) < Constants.HOOD_TOL;
	}
	public static double getDistance() {
		return 81.5 /* Limelight to Powerport Height */ / 
				Math.tan(
					(Constants.LIMELIGHT_ANGLE + Limelight.getTargetY()) * (Math.PI / 180)
				);
	}
	public static double getTargetVisionAngle() {
//		return targetAngle;
		double x = visionFilterY.getDoubleOutput();
		return -0.00643 * x * x * x - 0.122 * x * x - 0.934 * x + 7.715;
	}

	//Config
	public void init() {
		talon = new TalonSRX(Ports.HOOD_TALON);
		talon.configFactoryDefault();
		talon.configClosedloopRamp(Constants.HOOD_RAMP_RATE);
		talon.configOpenloopRamp(Constants.HOOD_RAMP_RATE);
		talon.config_kP(0, Constants.HOOD_KP);
		talon.config_kD(0, Constants.HOOD_KD);
		talon.setInverted(false);
		talon.setSensorPhase(true);
		
		talon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
		talon.configForwardSoftLimitThreshold((int) (ticksPerRev * (38.0 / 360.0)));
		talon.configForwardSoftLimitEnable(false);
		
		visionFilterY = new MovingAverage(5, 0);
	}

	//Reset
	public void reset() {
		stop();
		resetEncoder();
	}
}