package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.Sensor;
import frc.team5104.util.Sensor.PortType;
import frc.team5104.util.managers.Subsystem;

public class Hopper extends Subsystem {
	private static TalonSRX talonStart, talonFeeder;
	private static TalonFX falconMid;
	private static Sensor entrySensor, endSensor;
	private static boolean isIndexing, wasIndexing;
	
	//Loop
	public void update() {
		//Force Stopped
		if (Superstructure.getMode() == Mode.CLIMBING ||
			Superstructure.getMode() == Mode.PANEL_DEPLOYING ||
			Superstructure.getMode() == Mode.PANELING ||
			Superstructure.getSystemState() == SystemState.CALIBRATING ||
			Superstructure.getSystemState() == SystemState.DISABLED) {
			stopAll();
		}
		
		//Unjam
		else if (Superstructure.getMode() == Mode.UNJAM) {
			setMiddlePercentOutput(-Constants.HOPPER_UNJAM_SPEED);
			setFeeder(-Constants.HOPPER_UNJAM_SPEED);
			setStart(-Constants.HOPPER_UNJAM_SPEED);
		}
		
		//Shooting
		else if (Superstructure.getMode() == Mode.SHOOTING) {
			setMiddlePercentOutput(Constants.HOPPER_FEED_SPEED);
			setFeeder(Constants.HOPPER_FEED_SPEED);
			setStart(Constants.HOPPER_FEED_SPEED);
		}
		
		//Indexing
		else {
			//Indexing
			wasIndexing = isIndexing;
			isIndexing = !isFull() && isEntrySensorTripped();
			
			//Mid and Feeder
			if (isIndexing) {
				setFeeder(-Constants.HOPPER_FEEDER_ROLLBALL_SPEED);
				if (!wasIndexing)
					resetMiddleEncoder();
				setMiddleTarget(1000);
			}
			else {
				setFeeder(0);
				setMiddlePercentOutput(0);
			}
			
			//Entry
			if (Superstructure.getMode() == Mode.INTAKE)
				setStart(Constants.HOPPER_START_INTAKE_SPEED);
			else if (isIndexing)
				setStart(0);
			else setStart(0);
		}
	}

	//Internal Functions
	private void setMiddlePercentOutput(double percent) {
		falconMid.set(ControlMode.PercentOutput, percent);
	}
	private void setMiddleTarget(double encoderTarget) {
		falconMid.set(ControlMode.MotionMagic, encoderTarget);
	}
	private void setStart(double percent) {
		talonStart.set(ControlMode.PercentOutput, percent);
	}
	private void setFeeder(double percent) {
		talonFeeder.set(ControlMode.PercentOutput, percent);
	}
	private void stopAll() {
		talonStart.set(ControlMode.Disabled, 0);
		talonFeeder.set(ControlMode.Disabled, 0);
		falconMid.set(ControlMode.Disabled, 0);
	}
	private void resetMiddleEncoder() {
		falconMid.setSelectedSensorPosition(0);
	}
	private static boolean isEntrySensorTripped() {
		return entrySensor.get();
	}
	private static boolean isEndSensorTripped() {
		return endSensor.get();
	}
	
	//External Functions
	public static boolean isEmpty() {
		return !isEndSensorTripped() && !isEntrySensorTripped();
	}
	public static boolean isFull() {
		return isEndSensorTripped() && isEntrySensorTripped();
	}
	public static boolean isIndexing() {
		return isIndexing;
	}
	
	//Config
	public void init() {
		talonStart = new TalonSRX(Ports.HOPPER_TALON_START);
		talonStart.configFactoryDefault();
		
		talonFeeder = new TalonSRX(Ports.HOPPER_TALON_FEEDER);
		talonFeeder.configFactoryDefault();
		
		falconMid = new TalonFX(Ports.HOPPER_FALCON_MID);
		falconMid.configFactoryDefault();
		falconMid.config_kP(0, Constants.HOPPER_KP);
		falconMid.configMotionAcceleration((int) Constants.HOPPER_ACC);
		falconMid.configMotionCruiseVelocity((int) Constants.HOPPER_VEL);
		
		entrySensor = new Sensor(PortType.ANALOG, Ports.HOPPER_SENSOR_START);
		endSensor = new Sensor(PortType.ANALOG, Ports.HOPPER_SENSOR_END);
	}

	//Reset
	public void reset() {
		stopAll();
	}
}