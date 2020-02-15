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
	private static TalonFX falcon1, falcon2;
	private static MovingAverage avgRPMS;
	private static final double targetRPMS = 5000;
	
	//Loop
	public void update() {
		if ((Superstructure.getSystemState() == SystemState.AUTOMATIC || 
			Superstructure.getSystemState() == SystemState.MANUAL) &&
			Superstructure.getFlywheelState() == FlywheelState.SPINNING) {
			if (Constants.FLYWHEEL_OPEN_LOOP)
				setPercentOutput(targetRPMS / 6100);
			else setSpeed(targetRPMS);
		}
		else stop();
		
		avgRPMS.update(getRPMS());
		
//		try {
//			Constants.FLYWHEEL_KP = Double.parseDouble(Tuner.getTunerInput("KP", Constants.FLYWHEEL_KP));
//			Constants.FLYWHEEL_KF = Double.parseDouble(Tuner.getTunerInput("KF", Constants.FLYWHEEL_KF));
//			targetRPMS = Double.parseDouble(Tuner.getTunerInput("target", targetRPMS));
//		} catch (Exception e) {}
//		Tuner.setTunerOutput("Out Percent", falcon1.getMotorOutputPercent());
//		Tuner.setTunerOutput("Speed", getRPMS());
		
//		falcon1.config_kP(0, Constants.FLYWHEEL_KP);
//		falcon1.config_kF(0, Constants.FLYWHEEL_KF);
	}

	//Internal Functions
	private void setSpeed(double rpms) {
		//rev/min -> ticks/100ms
		falcon1.set(ControlMode.Velocity, rpms * 2048.0 / 60.0 / 10.0);
	}
	private void setPercentOutput(double percent) {
		falcon1.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		falcon1.set(ControlMode.Disabled, 0);
	}
	
	//External Functions
	public static double getRPMS() {
		if (falcon1 == null)
			return 0;
		return falcon1.getSelectedSensorVelocity() / 2048.0 * 60.0 * 10.0;
	}
	public static double getAvgRPMS() {
		if (falcon1 == null)
			return 0;
		return avgRPMS.getDoubleOutput();
	}
	public static boolean isSpedUp() {
		if (falcon1 == null)
			return true;
		return BreakerMath.roughlyEquals(
				getRPMS(), targetRPMS, Constants.FLYWHEEL_RPM_TOL);
	}
	public static boolean isAvgSpedUp() {
		if (falcon1 == null)
			return true;
		return BreakerMath.roughlyEquals(
				getAvgRPMS(), targetRPMS, Constants.FLYWHEEL_RPM_TOL);
	}
	
	//Config
	public void init() {
		falcon1 = new TalonFX(Ports.FLYWHEEL_FALCON_1);
		falcon1.configFactoryDefault();
		falcon1.config_kP(0, Constants.FLYWHEEL_KP);
		falcon1.config_kF(0, Constants.FLYWHEEL_KF);
		falcon1.configClosedloopRamp(Constants.FLYWHEEL_RAMP_RATE);
		falcon1.setInverted(false);
		
		falcon2 = new TalonFX(Ports.FLYWHEEL_FALCON_2);
		falcon2.configFactoryDefault();
		falcon2.config_kP(0, Constants.FLYWHEEL_KP);
		falcon2.config_kF(0, Constants.FLYWHEEL_KF);
		falcon2.configClosedloopRamp(Constants.FLYWHEEL_RAMP_RATE);
		falcon2.follow(falcon1);
		falcon2.setInverted(true);
		
		avgRPMS = new MovingAverage(50, 0);
	}

	//Reset
	public void reset() {
		stop();
	}
}