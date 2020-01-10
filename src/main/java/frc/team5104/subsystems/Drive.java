/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import frc.team5104.Constants;
import frc.team5104.util.DriveSignal;
import frc.team5104.util.Encoder;
import frc.team5104.util.managers.Subsystem;

public class Drive extends Subsystem {
	private static TalonSRX talonL1, talonL2, talonR1, talonR2;
	private static Encoder leftEncoder, rightEncoder;
	private static PigeonIMU gyro;
	
	//Update
	private static DriveSignal currentDriveSignal = new DriveSignal();
	public void update() {
		switch (currentDriveSignal.unit) {
			case PERCENT_OUTPUT: {
				setMotors(
						currentDriveSignal.leftSpeed, 
						currentDriveSignal.rightSpeed, 
						ControlMode.PercentOutput,
						currentDriveSignal.leftFeedForward,
						currentDriveSignal.rightFeedForward
					);
				break;
			}
			case FEET_PER_SECOND: {
				setMotors(
						Encoder.feetPerSecondToTalonVel(currentDriveSignal.leftSpeed), 
						Encoder.feetPerSecondToTalonVel(currentDriveSignal.rightSpeed), 
						ControlMode.Velocity,
						currentDriveSignal.leftFeedForward,
						currentDriveSignal.rightFeedForward
					);
				break;
			}
			case VOLTAGE: {
				setMotors(
						currentDriveSignal.leftSpeed / getLeftGearboxVoltage(),
						currentDriveSignal.rightSpeed / getRightGearboxVoltage(),
						ControlMode.PercentOutput,
						currentDriveSignal.leftFeedForward,
						currentDriveSignal.rightFeedForward
					);
				break;
			}
			case STOP:
				stopMotors();
				break;
		}
		currentDriveSignal = new DriveSignal();
	}
	
	//Internal Functions
	void setMotors(double leftSpeed, double rightSpeed, ControlMode controlMode, double leftFeedForward, double rightFeedForward) {
		talonL1.set(controlMode, leftSpeed, DemandType.ArbitraryFeedForward, leftFeedForward);
		talonR1.set(controlMode, rightSpeed, DemandType.ArbitraryFeedForward, rightFeedForward);
	}
	void stopMotors() {
		talonL1.set(ControlMode.Disabled, 0);
		talonR1.set(ControlMode.Disabled, 0);
	}
	
	//External Functions
	public static void set(DriveSignal signal) { currentDriveSignal = signal; }
	public static void stop() { currentDriveSignal = new DriveSignal(); }
	public static double getLeftGearboxVoltage() { return talonL1.getBusVoltage(); }
	public static double getRightGearboxVoltage() { return talonR1.getBusVoltage(); }
	public static double getLeftGearboxOutputVoltage() { return talonL1.getMotorOutputVoltage(); }
	public static double getRightGearboxOutputVoltage() { return talonR1.getMotorOutputVoltage(); }
	public static void resetGyro() { 
		gyro.setFusedHeading(0);
	}
	public static double getGyro() {
		return gyro.getFusedHeading();
	}
	public static void resetEncoders() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	public static Encoder getLeftEncoder() {
		return leftEncoder;
	}
	public static Encoder getRightEncoder() {
		return rightEncoder;
	}
	
	//Config
	public void init() {
		talonL1 = new TalonSRX(11);
		talonL2 = new TalonSRX(12);
		talonR1 = new TalonSRX(13);
		talonR2 = new TalonSRX(14);
		gyro = new PigeonIMU(talonR2);
		leftEncoder = new Encoder(talonL1);
		rightEncoder = new Encoder(talonR1);
		
		talonL1.configFactoryDefault();
		talonL2.configFactoryDefault();
		talonL1.config_kP(0, Constants.DRIVE_KP, 0);
		talonL1.config_kD(0, Constants.DRIVE_KD, 0);
		talonL2.set(ControlMode.Follower, talonL1.getDeviceID());
		talonL1.setInverted(true);
		talonL2.setInverted(true);
		
		talonR1.configFactoryDefault();
		talonR2.configFactoryDefault();
		talonR1.config_kP(0, Constants.DRIVE_KP, 0);
		talonR1.config_kD(0, Constants.DRIVE_KD, 0);
		talonR2.set(ControlMode.Follower, talonR1.getDeviceID());
		
		stopMotors();
		resetGyro();
		resetEncoders();
	}
	
	//Reset
	public void reset() {
		currentDriveSignal = new DriveSignal();
	}
}