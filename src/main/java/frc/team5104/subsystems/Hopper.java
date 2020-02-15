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
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Sensor;
import frc.team5104.util.Sensor.PortType;
import frc.team5104.util.managers.Subsystem;

public class Hopper extends Subsystem {
	private static VictorSPX intakeToHopper, hopperToShooter;
	private static TalonFX falconMid;
	private static Sensor entrySensor, endSensor;
	private static boolean isIndexing;
	private static LatchedBoolean entrySensorLatch;
	private static MovingAverage isFullAverage, hasFed;
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
		
		//Unjam
		else if (Superstructure.getMode() == Mode.UNJAM) {
			setMiddle(-Constants.HOPPER_UNJAM_SPEED);
			setFeeder(-Constants.HOPPER_UNJAM_SPEED);
			setStart(-Constants.HOPPER_UNJAM_SPEED);
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
			isIndexing = !isFull() && (isEntrySensorTripped() || getMidPosition() < Constants.HOPPER_MID_BALL_SIZE);
			if (entrySensorLatch.get(isEntrySensorTripped()) && isEntrySensorTripped()) {
				targetMidPosition = Constants.HOPPER_MID_BALL_SIZE;
				resetMidEncoder();
			}
			
			//Mid and Feeder
			if (isIndexing) {
				if (getMidPosition() < targetMidPosition) {
					setMiddle(1);
					setFeeder(0.1);
				}
			}
			else {
				setFeeder(0);
				setMiddle(0);
			}
			
			//Entry
			if (Superstructure.getMode() == Mode.INTAKE)
				setStart(Constants.HOPPER_START_INTAKE_SPEED);
			else if (isIndexing)
				setStart(0);
			else setStart(0);
		}
		
		isFullAverage.update(isFull());
		//Constants.HOPPER_MID_BALL_SIZE = Double.parseDouble(Tuner.getTunerInput("Hopper Mid Ball Size", Constants.HOPPER_MID_BALL_SIZE));
	}

	//Internal Functions
	private void setMiddle(double percent) {
		falconMid.set(ControlMode.PercentOutput, percent);
	}
	private void setStart(double percent) {
		intakeToHopper.set(ControlMode.PercentOutput, percent);
	}
	private void setFeeder(double percent) {
		hopperToShooter.set(ControlMode.PercentOutput, percent);
	}
	private void stopAll() {
		intakeToHopper.set(ControlMode.Disabled, 0);
		hopperToShooter.set(ControlMode.Disabled, 0);
		falconMid.set(ControlMode.Disabled, 0);
	}
	private void resetMidEncoder() {
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
		if (falconMid == null)
			return false;
		return !isEndSensorTripped() && !isEntrySensorTripped();
	}
	public static boolean isFull() {
		if (falconMid == null)
			return false;
		return isEndSensorTripped();
	}
	public static boolean isFullAverage() {
		if (falconMid == null)
			return false;
		return isFullAverage.getBooleanOutput();
	}
	public static boolean isIndexing() {
		if (falconMid == null)
			return false;
		return isIndexing;
	}
	public static boolean hasFedAverage() {
		if (falconMid == null)
			return false;
		return hasFed.getBooleanOutput();
	}
	public static double getMidPosition() {
		if (falconMid == null)
			return 0;
		return falconMid.getSelectedSensorPosition();
	}
	
	//Config
	public void init() {
		intakeToHopper = new VictorSPX(Ports.INTAKE_TO_HOPPER_VICTOR);
		intakeToHopper.configFactoryDefault();
		intakeToHopper.setInverted(true);

		hopperToShooter = new VictorSPX(Ports.HOPPER_TO_SHOOTER_VICTOR);
		hopperToShooter.configFactoryDefault();
		hopperToShooter.setInverted(true);
		
		falconMid = new TalonFX(Ports.HOPPER_FALCON_MID);
		falconMid.configFactoryDefault();
		falconMid.config_kP(0, Constants.HOPPER_KP);
		falconMid.configMotionAcceleration((int) Constants.HOPPER_ACC);
		falconMid.configMotionCruiseVelocity((int) Constants.HOPPER_VEL);
		falconMid.setInverted(true);
		//falconMid.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 20, 20, 0.05));

		entrySensor = new Sensor(PortType.ANALOG, Ports.HOPPER_SENSOR_START, true);
		endSensor = new Sensor(PortType.ANALOG, Ports.HOPPER_SENSOR_END, true);
		
		entrySensorLatch = new LatchedBoolean();
		isFullAverage = new MovingAverage(100, 0);
		hasFed = new MovingAverage(100, 0);
	}

	//Reset
	public void reset() {
		stopAll();
	}
}