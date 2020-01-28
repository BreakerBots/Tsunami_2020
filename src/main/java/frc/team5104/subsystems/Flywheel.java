package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.FlywheelState;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.BreakerMath;
import frc.team5104.util.managers.Subsystem;

public class Flywheel extends Subsystem {
	private static TalonFX falcon1, falcon2;
	private static double targetRPMS = 0;
	
	
	
	//Loop
	public void update() {
		if ((Superstructure.getSystemState() == SystemState.AUTOMATIC || 
			Superstructure.getSystemState() == SystemState.MANUAL) &&
			Superstructure.getFlywheelState() == FlywheelState.SPINNING) {
			if (Constants.FLYWHEEL_OPEN_LOOP)
				setPercentOutput(targetRPMS / 6380);
			else setSpeed(targetRPMS);
		}
		else stop();
	}

	//Internal Functions
	private void setSpeed(double rpms) {
		//rev/min -> ticks/100ms
		falcon1.set(ControlMode.Velocity, rpms * 4096.0 / 60.0 / 10.0);
	}
	private void setPercentOutput(double percent) {
		falcon1.set(ControlMode.PercentOutput, percent);
	}
	private void stop() {
		falcon1.set(ControlMode.Disabled, 0);
	}
	
	//External Functions
	public static double getRPMS() {
		return falcon1.getSelectedSensorPosition() / 4096.0 * 60.0 * 10.0;
	}
	public static boolean isSpedUp() {
		return BreakerMath.roughlyEquals(
				getRPMS(), targetRPMS, Constants.FLYWHEEL_RPM_TOL);
	}
	
	//Config
	public void init() {
		falcon1 = new TalonFX(Ports.FLYWHEEL_FALCON_1);
		falcon1.configFactoryDefault();
		falcon1.config_kP(0, Constants.FLYWHEEL_KP);
		falcon1.config_kD(0, Constants.FLYWHEEL_KD);
		falcon1.config_kF(0, Constants.FLYWHEEL_KF);
		falcon1.configClosedloopRamp(Constants.FLYWHEEL_RAMP_RATE);
		
		falcon2 = new TalonFX(Ports.FLYWHEEL_FALCON_1);
		falcon2.configFactoryDefault();
		falcon2.setInverted(true);
		falcon2.follow(falcon1);
	}

	//Reset
	public void reset() {
		stop();
	}
}