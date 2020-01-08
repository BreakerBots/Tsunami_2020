/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.util;

import edu.wpi.first.wpilibj.AnalogInput;

/** Use a analog port on the roboRio as a digital port. (For dumb sensors that are being weird) */
public class AnalogToDigital {
	private AnalogInput analogInput;
	private double triggerValue;
	private boolean inverted;
	
	//Constructors
	public AnalogToDigital(int port) { this(port, false); }
	public AnalogToDigital(int port, boolean inverted) { this(port, 2.5, inverted); }
	public AnalogToDigital(int port, double triggerValue, boolean inverted) { 
		analogInput = new AnalogInput(port);
		this.triggerValue = triggerValue;
		this.inverted = inverted;
	}
	
	//Getters
	public boolean get() {
		if (inverted)
			return analogInput.getVoltage() < triggerValue;
		return analogInput.getVoltage() > triggerValue;
	}
}