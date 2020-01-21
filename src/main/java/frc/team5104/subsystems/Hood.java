package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.team5104.Ports;
import frc.team5104.util.managers.Subsystem;

public class Hood extends Subsystem {
	private static TalonSRX talon;
	
	//Loop
	public void update() {
		
	}
	
	//Internal Functions
	private void setAngle(double degrees) {
		talon.set(ControlMode.MotionMagic, degrees);
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
	public boolean onTarget() {
		return false;
	}
	public double getAngle() {
		// fix
		return talon.getSelectedSensorPosition();
	}
	public boolean reverseLimitHit() {
		return true;
	}
	
	//Config
	public void init() {
		talon = new TalonSRX(Ports.HOOD_TALON);
	}

	//Reset
	public void reset() {
		stop();
	}
}
