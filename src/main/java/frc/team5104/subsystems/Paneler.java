package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.team5104.util.ColorSensor;
import frc.team5104.util.ColorSensor.CoolColor;
import frc.team5104.util.console;
import frc.team5104.util.managers.Subsystem;

public class Paneler extends Subsystem {
	private static ColorSensor sensor;
	private static TalonSRX talon;
	private static Joystick joystick;
	private static DoubleSolenoid piston;
	private static CoolColor color;
	
	//Loop
	public void update() {
		//console.log(readColor());
		//console.log(atTargetPosition());
		//double in = joystick.getRawAxis(1);
		//setPercentOutput(in * 0.3);
		/*if(atTargetPosition() == true) {
			setPercentOutput(0);
		} else {
			setPercentOutput(0.3);*/
		if(rotationControl() == true) {
			setPercentOutput(0);
		} else {
			setPercentOutput(1);
		}
			
	}

	//Internal Functions
	private void setPiston(boolean up) {
		piston.set(up ? Value.kForward : Value.kReverse);
	}
	private void setMotionMagic(double degrees) {
		talon.set(ControlMode.MotionMagic, degrees);
	}
	private void setPercentOutput(double percent) {
		talon.set(ControlMode.PercentOutput, percent);
	}
	
	//External Functions
	public static boolean atTargetPosition() {
	String FMS = DriverStation.getInstance().getGameSpecificMessage();
		if(FMS.charAt(0) == 'R' && sensor.getColor() == CoolColor.BLUE) {
			//return true;
		} else if(FMS.charAt(0) == 'Y' && sensor.getColor() == CoolColor.GREEN) {
			return true;
		} else if(FMS.charAt(0) == 'B' && sensor.getColor() == CoolColor.RED) {
			return true;
		} else if(FMS.charAt(0) == 'G' && sensor.getColor() == CoolColor.YELLOW) {
			return true;
		} 
			return false;		
	}	
	public static CoolColor readColor() {
		return sensor.getColor();
	}
	public static boolean rotationControl() {
		double wheelRotations = talon.getSelectedSensorPosition() / (49 * 4096);
		console.log(wheelRotations);
		double cpRotations = (wheelRotations * 1.5) / 16;
		if (cpRotations >= 3 && cpRotations <= 5) {
			return true;
		} else {
			return false;
		}
	}
	
	//Config
	public void init() {
		sensor = new ColorSensor();
		talon = new TalonSRX(21);
		joystick = new Joystick(0);
		talon.setSelectedSensorPosition(0);
	}

	//Reset
	public void reset() {
		
	}
}