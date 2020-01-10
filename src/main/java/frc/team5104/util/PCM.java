package frc.team5104.util;

import edu.wpi.first.wpilibj.SensorUtil;

/**
 * Stores the CAN ID of a PCM (Pneumatics Control Module).
 * Used for the Solenoid class.
 */
public class PCM {
	public static final PCM DEFAULT = new PCM(SensorUtil.getDefaultSolenoidModule());
	
	private byte canID;
	
	/**
	 * Creates a PCM object with the specific CAN ID (values from 0-63)
	 * @param canID
	 */
	public PCM(int canID) {
		this.canID = (byte) canID;
	}
	
	/**
	 * @return The CAN ID of this PCM
	 */
	public byte getCANID() {
		return canID;
	}
}