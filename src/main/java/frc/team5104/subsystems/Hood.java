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
import frc.team5104.util.CharacterizedController;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.Limelight;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Tuner;
import frc.team5104.util.managers.Subsystem;

public class Hood extends Subsystem {
	private static TalonSRX motor;
	private static MovingAverage visionFilterY;
	private static CharacterizedController controller;
	private static double targetAngle = 0;
	private static double visionTargetAngle = 10; //TODO: take out

	//Loop
	public void update() {
		//Debugging
		Tuner.setTunerOutput("Hood Angle", getAngle());
		Tuner.setTunerOutput("Hood Output", motor.getMotorOutputPercent());
		visionTargetAngle = Tuner.getTunerInputDouble("Hood Target Vision Angle", visionTargetAngle);
		
		//Calibrating
		if (Superstructure.getSystemState() == SystemState.CALIBRATING) {
			if (backLimitHit()) {
				stop();
			}
			else setPercentOutput(-Constants.HOOD_CALIBRATE_SPEED);
		}
		
		//Automatic
		else if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			//Low
			if (Superstructure.getTarget() == Target.LOW)
				setTargetAngle(40);
			
			else {
				//Vision
				if (Superstructure.getMode() == Mode.AIMING) {
					if (Limelight.hasTarget()) {
						visionFilterY.update(Limelight.getTargetY());
						setTargetAngle(getTargetVisionAngle());
					}
				}
				
				//Pull Back
				else if (Superstructure.getMode() != Mode.SHOOTING)
					setTargetAngle(0);
			}
			
			setVoltage(controller.get(getAngle(), targetAngle));
		}
			
		//Disabled
		else stop();
		
		//Zero
		if (backLimitHit()) {
			resetEncoder();
			motor.configForwardSoftLimitEnable(true);
		}
	}

	//Internal Functions
	private void setTargetAngle(double degrees) {
		targetAngle = BreakerMath.clamp(degrees, 0, 40);
	}
	private void setVoltage(double volts) {
		setPercentOutput(volts / motor.getBusVoltage());
	}
	private void setPercentOutput(double percent) {
		motor.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		motor.set(ControlMode.Disabled, 0);
	}
	private void resetEncoder() {
		motor.setSelectedSensorPosition(0);
	}

	//External Functions
	public static double getAngle() {
		if (motor == null) return 0;
		return motor.getSelectedSensorPosition() / Constants.HOOD_TICKS_PER_REV * 360.0;
	}
	public static boolean backLimitHit() {
		if (motor == null) return true;
		return !motor.getSensorCollection().isRevLimitSwitchClosed();
	}
	public static boolean onTarget() {
		if (motor == null) return true;
		return Math.abs(getAngle() - getTargetVisionAngle()) < Constants.HOOD_TOL;
	}
	public static double getTargetVisionAngle() {
		return visionTargetAngle;
		//double x = visionFilterY.getDoubleOutput();
		//return -0.00643 * x * x * x - 0.122 * x * x - 0.934 * x + 7.715;
	}

	//Config
	public void init() {
		motor = new TalonSRX(Ports.HOOD_MOTOR);
		motor.configFactoryDefault();
		motor.setInverted(false);
		motor.setSensorPhase(true);
		
		motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
		motor.configForwardSoftLimitThreshold((int) (Constants.HOOD_TICKS_PER_REV * (38.0 / 360.0)));
		motor.configForwardSoftLimitEnable(false);
		
		controller = new CharacterizedController(
				Constants.HOOD_KP,
				0,
				Constants.HOOD_KD,
				Constants.HOOD_MAX_VEL,
				Constants.HOOD_MAX_ACC,
				Constants.HOOD_KS,
				Constants.HOOD_KV,
				Constants.HOOD_KA
			);
		visionFilterY = new MovingAverage(3, 0);
	}

	//Reset
	public void reset() {
		stop();
		resetEncoder();
	}
}