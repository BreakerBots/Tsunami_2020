package frc.team5104.util;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import frc.team5104.util._ColorConstants.Green;
import frc.team5104.util._ColorConstants.Red;
import frc.team5104.util._ColorConstants.Yellow;
import frc.team5104.util._ColorConstants.Blue;

/**
 * Senses and returns color
 */
public class ColorSensor {
	public static enum CoolColor { RED, GREEN, BLUE, YELLOW, UNDEFINED }
	private final I2C.Port i2cPort = I2C.Port.kOnboard;
	private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
	
	//Constructors
	public ColorSensor(/*input variables*/) {
	}
	
	//Getters
	public CoolColor getColor() {
		Color detectedColor = m_colorSensor.getColor();
		System.out.println(detectedColor.red + " " + detectedColor.green + " "
				+ detectedColor.blue);
		int n = 0;
		// Red
		if (detectedColor.red > Red.rMin - 0.02 && detectedColor.red < Red.rMax + 0.02) {
			n++;
		}
		if (detectedColor.green > Red.gMin - 0.02 && detectedColor.green < Red.gMax + 0.02) {
			n++;
		}
		if (detectedColor.blue > Red.bMin - 0.02 && detectedColor.blue < Red.bMax + 0.02) {
			n++;
		}
		if (n == 3) {
			return CoolColor.RED;
		}
		// Green
		n = 0;
		if (detectedColor.red > Green.rMin - 0.02 && detectedColor.red < Green.rMax + 0.02) {
			n++;
		}
		if (detectedColor.green > Green.gMin - 0.02 && detectedColor.green < Green.gMax + 0.02) {
			n++;
		}
		if (detectedColor.blue > Green.bMin - 0.02 && detectedColor.blue < Green.bMax + 0.02) {
			n++;
		}
		if (n == 3) {
			return CoolColor.GREEN;
		}
		// Blue
		n = 0;
		if (detectedColor.red > Blue.rMin - 0.02 && detectedColor.red < Blue.rMax + 0.02) {
			n++;
		}
		if (detectedColor.green > Blue.gMin - 0.02 && detectedColor.green < Blue.gMax + 0.02) {
			n++;
		}
		if (detectedColor.blue > Blue.bMin - 0.02 && detectedColor.blue < Blue.bMax + 0.02) {
			n++;
		}
		if (n == 3) {
			return CoolColor.BLUE;
		}
		// Yellow
		n = 0;
		if (detectedColor.red > Yellow.rMin - 0.02 && detectedColor.red < Yellow.rMax + 0.02) {
			n++;
		}
		if (detectedColor.green > Yellow.gMin - 0.02 && detectedColor.green < Yellow.gMax + 0.02) {
			n++;
		}
		if (detectedColor.blue > Yellow.bMin - 0.02 && detectedColor.blue < Yellow.bMax + 0.02) {
			n++;
		}
		if (n == 3) {
			return CoolColor.YELLOW;
		}
		return CoolColor.UNDEFINED;
	}
}
