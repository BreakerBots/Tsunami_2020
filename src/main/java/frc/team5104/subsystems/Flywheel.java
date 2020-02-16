package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.FlywheelState;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.managers.Subsystem;

public class Flywheel extends Subsystem {
	private static TalonFX motor1, motor2;
	private static MovingAverage avgRPMS;
	//private static CharacterizedController controller;
	private static final double targetRPMS = 5000;
	
	//Loop
	public void update() {
		if ((Superstructure.getSystemState() == SystemState.AUTOMATIC || 
			Superstructure.getSystemState() == SystemState.MANUAL) &&
			Superstructure.getFlywheelState() == FlywheelState.SPINNING) {
			setRampRate(Constants.FLYWHEEL_RAMP_RATE_UP);
			if (Constants.FLYWHEEL_OPEN_LOOP)
				setPercentOutput(targetRPMS / 6100);
			else setSpeed(targetRPMS);
		}
		else {
			setRampRate(Constants.FLYWHEEL_RAMP_RATE_DOWN);
			stop();
		}
		
//		Tuner.setTunerOutput("Flywheel RPMS", getRPMS());
		
		avgRPMS.update(getRPMS());
	}

	//Internal Functions
	private void setSpeed(double rpms) {
		//rev/min -> ticks/100ms
		motor1.set(ControlMode.Velocity, rpms * 2048.0 / 60.0 / 10.0);
	}
	private void setPercentOutput(double percent) {
		motor1.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		motor1.set(ControlMode.Disabled, 0);
	}
	private void setRampRate(double rate) {
		motor1.configOpenloopRamp(rate);
		motor2.configOpenloopRamp(rate);
		motor1.configClosedloopRamp(rate);
		motor2.configClosedloopRamp(rate);
	}
	
	//External Functions
	public static double getRPMS() {
		if (motor1 == null)
			return 0;
		return motor1.getSelectedSensorVelocity() / 2048.0 * 60.0 * 10.0;
	}
	public static double getAvgRPMS() {
		if (motor1 == null)
			return 0;
		return avgRPMS.getDoubleOutput();
	}
	public static boolean isSpedUp() {
		if (motor1 == null)
			return true;
		return BreakerMath.roughlyEquals(
				getRPMS(), targetRPMS, Constants.FLYWHEEL_RPM_TOL);
	}
	public static boolean isAvgSpedUp() {
		if (motor1 == null)
			return true;
		return BreakerMath.roughlyEquals(
				getAvgRPMS(), targetRPMS, Constants.FLYWHEEL_RPM_TOL);
	}
	
	//Config
	public void init() {
		motor1 = new TalonFX(Ports.FLYWHEEL_MOTOR_1);
		motor1.configFactoryDefault();
		motor1.config_kP(0, Constants.FLYWHEEL_KP);
		motor1.config_kF(0, Constants.FLYWHEEL_KF);
		motor1.setInverted(false);
		
		motor2 = new TalonFX(Ports.FLYWHEEL_MOTOR_2);
		motor2.configFactoryDefault();
		motor2.config_kP(0, Constants.FLYWHEEL_KP);
		motor2.config_kF(0, Constants.FLYWHEEL_KF);
		motor2.follow(motor1);
		motor2.setInverted(true);
		
		setRampRate(Constants.FLYWHEEL_RAMP_RATE_UP);
		
		avgRPMS = new MovingAverage(50, 0);
	}

	//Reset
	public void reset() {
		stop();
	}
}