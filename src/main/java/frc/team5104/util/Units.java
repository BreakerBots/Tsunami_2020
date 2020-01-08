/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.util;

import frc.team5104.Constants;

/** Some basic unit conversions */
public class Units {
	//Talons
	public static double encoderTicksToDegrees(int ticks) {
		return ticks / 4096.0 * 360.0;
	}
	
	// Feet and Inches
	public static double feetToInches(double feet) {
		return feet * 12.0;
	}
	public static double inchesToFeet(double inches) {
		return inches / 12.0;
	}
	
	
	// Meters and Feet
	public static double metersToFeet(double meters) {
		return meters * 3.2808;
	}
	public static double feetToMeters(double feet) {
		return feet / 3.2808;
	}
	
	
	// Radians and Degrees
	public static double degreesToRadians(double deg) {
		return Math.toRadians(deg);
	}
	public static double radiansToDegress(double radians) {
		return Math.toDegrees(radians);
	}
	
	//Drive
	public static double ticksToWheelRevolutions(double ticks) {
		return ticks / Constants.DRIVE_TICKS_PER_REVOLUTION;
	}
	public static double wheelRevolutionsToTicks(double revs) {
		return revs * Constants.DRIVE_TICKS_PER_REVOLUTION;
	}
	public static double feetToWheelRevolutions(double feet) {
		return feet / (Constants.DRIVE_WHEEL_DIAMETER * Math.PI);
	}
	public static double wheelRevolutionsToFeet(double revs) {
		return revs * (Constants.DRIVE_WHEEL_DIAMETER * Math.PI);
	}
	public static double ticksToFeet(double ticks) {
		double r = ticksToWheelRevolutions(ticks);
			   r = wheelRevolutionsToFeet(r);
		return r;
	}
	public static double feetToTicks(double feet) {
		double r = feetToWheelRevolutions(feet);
			   r = wheelRevolutionsToTicks(r);
		return r;
	}
	public static double talonVelToFeetPerSecond(double talonVel) {
		return ticksToFeet(talonVel) * 10.0;
	}
	public static double feetPerSecondToTalonVel(double feetPerSecond) {
		return feetToTicks(feetPerSecond) / 10.0;
	}
}
