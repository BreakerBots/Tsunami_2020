package frc.team5104.util;

import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.Constraints;

public class CharacterizedController {
	private ProfiledPIDController pid;
	private SimpleMotorFeedforward ff;
	
	/**
	 * Creates a CharacterizedController
	 * @param kP Proportional value for PID
	 * @param kI Integral value for PID
	 * @param kD Derivative value for PID
	 * @param kS The static gain for feedforward (from characterization)
	 * @param kV The velocity gain for feedforward (from characterization)
	 * @param kA The acceleration gain for feedforward (from characterization)
	 */
	public CharacterizedController(double kP, double kI, double kD, 
			double kS, double kV, double kA) {
		this(kP, kI, kD, Double.MAX_VALUE, Double.MAX_VALUE, kS, kV, kA);
	}
	
	/**
	 * Creates a CharacterizedController
	 * @param kP Proportional value for PID
	 * @param kI Integral value for PID
	 * @param kD Derivative value for PID
	 * @param maxVel Max velocity for profiling
	 * @param maxAccel Max acceleration for profiling
	 * @param kS The static gain for feedforward (from characterization)
	 * @param kV The velocity gain for feedforward (from characterization)
	 * @param kA The acceleration gain for feedforward (from characterization)
	 */
	public CharacterizedController(double kP, double kI, double kD, double maxVel, 
			double maxAccel, double kS, double kV, double kA) {
		pid = new ProfiledPIDController(kP, kI, kD, new Constraints(maxVel, maxAccel));
		ff = new SimpleMotorFeedforward(kS, kV, kA);
	}
	
	/**
	 * Calculates the output depending on the current position and the target position
	 * @return The output of the motors in volts
	 */
	public double get(double position, double target) {
		return getFF() + getPID(position, target);
	}
	
	public double getFF() {
		return ff.calculate(pid.getSetpoint().position, pid.getSetpoint().velocity);
	}
	
	public double getPID(double position, double target) {
		return pid.calculate(position, target);
	} 
	
	public void setPID(double kP, double kI, double kD) {
		pid.setPID(kP, kI, kD);
	}
	
	public void setProfiling(double maxVel, double maxAccel) {
		pid.setConstraints(new Constraints(maxVel, maxAccel));
	}
}
