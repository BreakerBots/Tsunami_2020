package frc.team5104.util;

import edu.wpi.first.wpilibj.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.Constraints;

public class ArmController {
	private ProfiledPIDController pid;
	private ArmFeedforward ff;
	
	/**
	 * Creates a Arm Controller
	 * @param kP Proportional value for PID
	 * @param kI Integral value for PID
	 * @param kD Derivative value for PID
	 * @param maxVel Max velocity for profiling
	 * @param maxAccel Max acceleration for profiling
	 * @param kS The static gain for feedforward (from characterization)
	 * @param kC The gravity gain for feedforward (from characterization)
	 * @param kV The velocity gain for feedforward (from characterization)
	 * @param kA The acceleration gain for feedforward (from characterization)
	 */
	public ArmController(double kP, double kI, double kD, double maxVel, 
			double maxAccel, double kS, double kC, double kV, double kA) {
		pid = new ProfiledPIDController(kP, kI, kD, new Constraints(maxVel, maxAccel));
		ff = new ArmFeedforward(kS, kC, kV, kA);
	}
	
	/**
	 * Calculates the output depending on the current position and the target position
	 * @return The output of the motors in volts
	 */
	public double get(double position, double target) {
		double a = pid.calculate(position, target);
		double b = ff.calculate(pid.getSetpoint().position, pid.getSetpoint().velocity);
		return a + b;
	}
	
	public void setPID(double kP, double kI, double kD) {
		pid.setPID(kP, kI, kD);
	}
	
	public void setProfiling(double maxVel, double maxAccel) {
		pid.setConstraints(new Constraints(maxVel, maxAccel));
	}
}
