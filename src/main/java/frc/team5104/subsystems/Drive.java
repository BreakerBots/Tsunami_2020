/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.util.DriveSignal;
import frc.team5104.util.Encoder;
import frc.team5104.util.managers.Subsystem;

public class Drive extends Subsystem {
	private static TalonSRX talonL, talonR, talonGyro;
	private static VictorSPX victorL, victorR;
	private static Encoder leftEncoder, rightEncoder;
	private static DoubleSolenoid shifter;
	private static PigeonIMU gyro;
	
	//Update
	private static DriveSignal currentDriveSignal = new DriveSignal();
	public void update() {
		setShifter(currentDriveSignal.isHighGear);
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
		talonL.set(controlMode, leftSpeed, DemandType.ArbitraryFeedForward, leftFeedForward);
		talonR.set(controlMode, rightSpeed, DemandType.ArbitraryFeedForward, rightFeedForward);
	}
	void stopMotors() {
		talonL.set(ControlMode.Disabled, 0);
		talonR.set(ControlMode.Disabled, 0);
	}
	
	//External Functions
	public static void set(DriveSignal signal) { currentDriveSignal = signal; }
	public static void stop() { currentDriveSignal = new DriveSignal(); }
	public static double getLeftGearboxVoltage() { return talonL.getBusVoltage(); }
	public static double getRightGearboxVoltage() { return talonR.getBusVoltage(); }
	public static double getLeftGearboxOutputVoltage() { return talonL.getMotorOutputVoltage(); }
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
	public static Encoder getLeftEncoder() {
		return leftEncoder;
	}
	public static Encoder getRightEncoder() {
		return rightEncoder;
	}
	public static boolean getShifter() {
		return shifter.get() == DoubleSolenoid.Value.kForward;
	}
	public static void setShifter(boolean high) {
		shifter.set(high ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
	}
	
	//Config
	public void init() {
		talonL = new TalonSRX(Ports.DRIVE_TALON_L1);
		victorL = new VictorSPX(Ports.DRIVE_TALON_L2);
		talonR = new TalonSRX(Ports.DRIVE_TALON_R1);
		victorR = new VictorSPX(Ports.DRIVE_TALON_R2);
		talonGyro = new TalonSRX(15);
		gyro = new PigeonIMU(talonGyro);
		leftEncoder = new Encoder(talonL);
		rightEncoder = new Encoder(talonR);
		shifter = new DoubleSolenoid(0, 1);
		
		talonGyro.configFactoryDefault();
		
		talonL.configFactoryDefault();
		victorL.configFactoryDefault();
		talonL.config_kP(0, Constants.DRIVE_KP, 0);
		talonL.config_kD(0, Constants.DRIVE_KD, 0);
		victorL.set(ControlMode.Follower, talonL.getDeviceID());
		talonL.setInverted(true);
		victorL.setInverted(true);
		
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