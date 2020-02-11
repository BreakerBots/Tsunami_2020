/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.kauailabs.navx.frc.AHRS;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.util.DriveSignal;
import frc.team5104.util.Encoder;
import frc.team5104.util.managers.Subsystem;

public class Drive extends Subsystem {
	private static TalonFX falconL1, falconL2, falconR1, falconR2;
	private static Encoder leftEncoder, rightEncoder;
	private static AHRS gyro;
	
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
		falconL1.set(controlMode, leftSpeed, DemandType.ArbitraryFeedForward, leftFeedForward);
		falconR1.set(controlMode, rightSpeed, DemandType.ArbitraryFeedForward, rightFeedForward);
	}
	void stopMotors() {
		falconL1.set(ControlMode.Disabled, 0);
		falconR1.set(ControlMode.Disabled, 0);
	}
	
	//External Functions
	public static void set(DriveSignal signal) { currentDriveSignal = signal; }
	public static void stop() { currentDriveSignal = new DriveSignal(); }
	public static double getLeftGearboxVoltage() { return falconL1.getBusVoltage(); }
	public static double getRightGearboxVoltage() { return falconR1.getBusVoltage(); }
	public static double getLeftGearboxOutputVoltage() { return falconL1.getMotorOutputVoltage(); }
	public static double getRightGearboxOutputVoltage() { return falconR1.getMotorOutputVoltage(); }
	public static void resetGyro() { 
		//gyro.setFusedHeading(0);
		gyro.reset();
	}
	public static double getGyro() {
		return -gyro.getAngle();//gyro.getFusedHeading();
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
		falconL1 = new TalonFX(Ports.DRIVE_TALON_L1);
		falconL2 = new TalonFX(Ports.DRIVE_TALON_L2);
		falconR1 = new TalonFX(Ports.DRIVE_TALON_R1);
		falconR2 = new TalonFX(Ports.DRIVE_TALON_R2);
		leftEncoder = new Encoder(falconL1);
		rightEncoder = new Encoder(falconR1);
		gyro = new AHRS();
		//gyro = new PigeonIMU(talonGyro);
		
		falconL1.configFactoryDefault();
		falconL2.configFactoryDefault();
		falconL1.config_kP(0, Constants.DRIVE_KP, 0);
		falconL1.config_kD(0, Constants.DRIVE_KD, 0);
		falconL2.set(ControlMode.Follower, falconL1.getDeviceID());
		
		falconR1.configFactoryDefault();
		falconR2.configFactoryDefault();
		falconR1.config_kP(0, Constants.DRIVE_KP, 0);
		falconR1.config_kD(0, Constants.DRIVE_KD, 0);
		falconR2.set(ControlMode.Follower, falconR1.getDeviceID());
		falconR1.setInverted(true);
		falconR2.setInverted(true);
		
		stopMotors();
		resetGyro();
		resetEncoders();
	}
	
	//Reset
	public void reset() {
		currentDriveSignal = new DriveSignal();
	}
}