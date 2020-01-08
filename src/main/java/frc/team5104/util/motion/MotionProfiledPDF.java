package frc.team5104.util.motion;

import edu.wpi.first.wpilibj.Timer;

public class MotionProfiledPDF {
	private PDFController controller;
	private MotionProfile.State target = new MotionProfile.State(), setpoint = new MotionProfile.State();
	private double maxVelocity, maxAcceleration, lastTime;

	//Constructor
	public MotionProfiledPDF(double kP, double kD, double kF, double maxVelocity, double maxAcceleration) {
		controller = new PDFController(kP, kD, kF);
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
		lastTime = Timer.getFPGATimestamp();
	}

	//Tuning Parameters
	public void setPDFVA(double kP, double kD, double kF, double maxVelocity, double maxAcceleration) {
		controller.setPDF(kP, kD, kF);
		this.maxVelocity = maxVelocity;
		this.maxAcceleration = maxAcceleration;
	}

	//Target
	public void set(double target) {
		this.target = new MotionProfile.State(target, 0);
	}
	public boolean onTarget() {
		return controller.onTarget();
	}
	public void setTolerance(double tolerance) {
		controller.setTolerance(tolerance);
	}

	//Get
	public double get(double current) {
		double deltaTime = (Timer.getFPGATimestamp() - lastTime) * 1000;
		this.lastTime = Timer.getFPGATimestamp();
		return get(current, deltaTime);
	}
	public double get(double current, double deltaTime) {
		MotionProfile profile = new MotionProfile(maxVelocity, maxAcceleration, target, setpoint);
		setpoint = profile.calculate(deltaTime);
		controller.set(setpoint.position);
		return controller.get(current, deltaTime);
	}

	//Reset
	public void reset() {
		controller.reset();
	}
}
