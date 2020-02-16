package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.team5104.Constants;
import frc.team5104.Ports;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.SystemState;
import frc.team5104.util.LatchedBoolean;
import frc.team5104.util.LatchedBoolean.LatchedBooleanMode;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Sensor;
import frc.team5104.util.Sensor.PortType;
import frc.team5104.util.managers.Subsystem;

public class Hopper extends Subsystem {
	private static VictorSPX startMotor, feederMotor;
	private static TalonFX middleMotor;
	private static Sensor entrySensor, endSensor;
	private static LatchedBoolean entrySensorLatch;
	private static MovingAverage isFullAverage, hasFed;
	private static boolean isIndexing;
	private static double targetMidPosition = 0;
	
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
		
		//Shooting
		else if (Superstructure.getMode() == Mode.SHOOTING) {
			setMiddle(Constants.HOPPER_FEED_SPEED);
			setFeeder(Constants.HOPPER_FEED_SPEED);
			setStart(Constants.HOPPER_FEED_SPEED);
			hasFed.update(true);
		}
		
		//Indexing
		else {
			hasFed.update(false);
			
			//Indexing
			isIndexing = !isFull() && (isEntrySensorTripped() || getMidPosition() < targetMidPosition);
			if (entrySensorLatch.get(isEntrySensorTripped())) {
				targetMidPosition = Constants.HOPPER_MIDDLE_BALL_SIZE;
				resetMidEncoder();
			}
			
			//Mid and Feeder
			if (isIndexing) {
				setMiddle(Constants.HOPPER_MIDDLE_INDEX_SPEED);
				setFeeder(Constants.HOPPER_FEED_INDEX_SPEED);
			}
			else {
				setFeeder(0);
				setMiddle(0);
			}
			
			//Entry
			if (Superstructure.getMode() == Mode.INTAKE)
				setStart(Constants.HOPPER_START_INTAKE_SPEED);
			else if (isIndexing)
				setStart(Constants.HOPPER_START_INDEX_SPEED);
			else setStart(0);
		}
		
		isFullAverage.update(isFull());
		//Constants.HOPPER_MID_BALL_SIZE = Double.parseDouble(Tuner.getTunerInput("Hopper Mid Ball Size", Constants.HOPPER_MID_BALL_SIZE));
	}

	//Internal Functions
	private void setMiddle(double volts) {
		middleMotor.set(ControlMode.PercentOutput, volts / middleMotor.getBusVoltage());
	}
	private void setStart(double volts) {
		startMotor.set(ControlMode.PercentOutput, volts / startMotor.getBusVoltage());
	}
	private void setFeeder(double volts) {
		feederMotor.set(ControlMode.PercentOutput, volts / feederMotor.getBusVoltage());
	}
	private void stopAll() {
		startMotor.set(ControlMode.Disabled, 0);
		feederMotor.set(ControlMode.Disabled, 0);
		middleMotor.set(ControlMode.Disabled, 0);
	}
	private void resetMidEncoder() {
		middleMotor.setSelectedSensorPosition(0);
	}
	private static boolean isEntrySensorTripped() {
		return entrySensor.get();
	}
	private static boolean isEndSensorTripped() {
		return endSensor.get();
	}
	
	//External Functions
	public static boolean isEmpty() {
		if (middleMotor == null)
			return false;
		return !isEndSensorTripped() && !isEntrySensorTripped();
	}
	public static boolean isFull() {
		if (middleMotor == null)
			return false;
		return isEndSensorTripped();
	}
	public static boolean isFullAverage() {
		if (middleMotor == null)
			return false;
		return isFullAverage.getBooleanOutput();
	}
	public static boolean isIndexing() {
		if (middleMotor == null)
			return false;
		return isIndexing;
	}
	public static boolean hasFedAverage() {
		if (middleMotor == null)
			return false;
		return hasFed.getBooleanOutput();
	}
	public static double getMidPosition() {
		if (middleMotor == null)
			return 0;
		return middleMotor.getSelectedSensorPosition();
	}
	
	//Config
	public void init() {
		startMotor = new VictorSPX(Ports.HOPPER_START_MOTOR);
		startMotor.configFactoryDefault();
		startMotor.setInverted(true);

		feederMotor = new VictorSPX(Ports.HOPPER_FEEDER_MOTOR);
		feederMotor.configFactoryDefault();
		feederMotor.setInverted(true);
		
		middleMotor = new TalonFX(Ports.HOPPER_MIDDLE_MOTOR);
		middleMotor.configFactoryDefault();
		middleMotor.setInverted(true);

		entrySensor = new Sensor(PortType.ANALOG, Ports.HOPPER_SENSOR_START, true);
		endSensor = new Sensor(PortType.ANALOG, Ports.HOPPER_SENSOR_END, true);
		
		entrySensorLatch = new LatchedBoolean(LatchedBooleanMode.RISING);
		isFullAverage = new MovingAverage(100, 0);
		hasFed = new MovingAverage(100, 0);
	}

	//Reset
	public void reset() {
		stopAll();
	}
}