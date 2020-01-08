package frc.team5104.util.motion;

import edu.wpi.first.wpilibj.Timer;
import frc.team5104.util.BreakerMath;

public class PDFController {
	private double 
		kP, kD, kF, 
		prevError, prevPos, target,
		minOutput, maxOutput,
		tolerance,
		lastTime;

	//Constructors
	public PDFController(double kP, double kD, double kF) { this(kP, kD, kF, Double.MIN_VALUE, Double.MAX_VALUE); }
	public PDFController(double kP, double kD, double kF, double minOutput, double maxOutput) { this(kP, kD, kF, minOutput, maxOutput, 0); }
	public PDFController(double kP, double kD, double kF, double minOutput, double maxOutput, double tolerance) {
		setPDF(kP, kD, kF);
		setOutputRange(minOutput, maxOutput);
		setTolerance(tolerance);
		this.lastTime = Timer.getFPGATimestamp();
	}

	//Tuning Parameters
	public void setPDF(double kP, double kD, double kF) {
		this.kP = kP;
		this.kD = kD;
		this.kF = kF;
	}
	public void setF(double kF) {
		this.kF = kF;
	}
	
	//Output Range
	public void setOutputRange(double minOutput, double maxOutput) {
		this.minOutput = minOutput;
		this.maxOutput = maxOutput;
	}
	
	//Target
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	public void set(double target) {
		this.target = target;
	}
	public boolean onTarget() {
		return BreakerMath.roughlyEquals(prevPos, target, tolerance);
	}

	//Get
	public double get(double current) {
		double deltaTime = (Timer.getFPGATimestamp() - lastTime) * 1000;
		this.lastTime = Timer.getFPGATimestamp();
		return get(current, deltaTime);
	}
	public double get(double current, double deltaTime) {
		double error = target - current;
		double deriv = error - prevError;
		double output = (kP * error) + (kD * deriv / deltaTime) + kF;
		prevError = error;
		prevPos = current;
		return BreakerMath.clamp(output, minOutput, maxOutput);
	}

	//Reset
	public void reset() {
		prevError = 0;
	}
}