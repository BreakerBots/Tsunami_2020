package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.Superstructure.Target;
import frc.team5104.util.managers.Subsystem;
import frc.team5104.vision.Limelight;

public class Hood extends Subsystem {
	private static TalonSRX talon;
	private static double targetAngle = 0;
	
	// Loop
	public void update() {
		if (Superstructure.getSystemState() == SystemState.AUTOMATIC) {
			if (Superstructure.getTarget() == Target.LOW) {
				targetAngle = Constants.HOOD_MAX_ANGLE;
			} else {
				targetAngle = getTargetAngle();
			}
			if (Superstructure.getMode() == Mode.SHOOTING && !onTarget()) {
				setAngle(targetAngle);
			} else {
				setPercentOutput(0);
			}
		}
	}

	// Internal Functions
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

	private double getDistance() {
		return 80.5 / (Math.tan((Constants.HOOD_ANGLE + Limelight.getTargetY()) * (Math.PI / 180)));
	}

	private double getTargetAngle() {
		// TODO also clamp in between 0 and max angle
		return 0;
	}

	// External Functions
	public boolean onTarget() {
		return true;
//		 return Math.abs(getTargetAngle() - getAngle()) < _minAngleErr;
	}

	public double getAngle() {
		// fix
		return talon.getSelectedSensorPosition();
	}

	public boolean reverseLimitHit() {
		return true;
	}

	public void setSpeed(double input) {
		setPercentOutput(input * Constants.HOOD_KINPUT);
	}
	// Config
	public void init() {
		talon = new TalonSRX(Ports.HOOD_TALON);
	}

	// Reset
	public void reset() {
		stop();
		talon.setSelectedSensorPosition(0);
	}
}
