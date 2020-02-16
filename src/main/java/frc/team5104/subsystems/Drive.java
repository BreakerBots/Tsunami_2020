/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.util.DriveSignal;
import frc.team5104.util.DriveEncoder;
import frc.team5104.util.managers.Subsystem;

public class Drive extends Subsystem {
	private static TalonSRX talonL1, talonL2, talonR;
	private static VictorSPX victorR;
	private static DriveEncoder leftEncoder, rightEncoder;
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
						DriveEncoder.feetPerSecondToTalonVel(currentDriveSignal.leftSpeed), 
						DriveEncoder.feetPerSecondToTalonVel(currentDriveSignal.rightSpeed), 
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
		talonR.set(controlMode, rightSpeed, DemandType.ArbitraryFeedForward, rightFeedForward);
	}
	void stopMotors() {
		talonL1.set(ControlMode.Disabled, 0);
		talonR.set(ControlMode.Disabled, 0);
	}
	
	//External Functions
	public static void set(DriveSignal signal) { currentDriveSignal = signal; }
	public static void stop() { currentDriveSignal = new DriveSignal(); }
	public static double getLeftGearboxVoltage() { return talonL1.getBusVoltage(); }
	public static double getRightGearboxVoltage() { return talonR.getBusVoltage(); }
	public static double getLeftGearboxOutputVoltage() { return talonL1.getMotorOutputVoltage(); }
	public static double getRightGearboxOutputVoltage() { return talonR.getMotorOutputVoltage(); }
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
	public static DriveEncoder getLeftEncoder() {
		return leftEncoder;
	}
	public static DriveEncoder getRightEncoder() {
		return rightEncoder;
	}
	
	//Config
	public void init() {
		talonL1 = new TalonSRX(Ports.DRIVE_TALON_L1);
		talonL2 = new TalonSRX(Ports.DRIVE_TALON_L2);
		talonR = new TalonSRX(Ports.DRIVE_TALON_R1);
		victorR = new VictorSPX(Ports.DRIVE_VICTOR_R2);
		gyro = new PigeonIMU(talonL2);
		leftEncoder = new DriveEncoder(talonL1);
		rightEncoder = new DriveEncoder(talonR);
		
		talonL1.configFactoryDefault();
		talonL2.configFactoryDefault();
		talonL1.config_kP(0, Constants.DRIVE_KP, 0);
		talonL1.config_kD(0, Constants.DRIVE_KD, 0);
		talonL2.set(ControlMode.Follower, talonL1.getDeviceID());
		talonL1.setInverted(true);
		talonL2.setInverted(true);
		
		talonR.configFactoryDefault();
		victorR.configFactoryDefault();
		talonR.config_kP(0, Constants.DRIVE_KP, 0);
		talonR.config_kD(0, Constants.DRIVE_KD, 0);
		victorR.set(ControlMode.Follower, talonR.getDeviceID());
		
		stopMotors();
		resetGyro();
		resetEncoders();
	}
	
	//Reset
	public void reset() {
		currentDriveSignal = new DriveSignal();
	}
}