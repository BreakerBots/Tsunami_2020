package frc.team5104.util;

/**
 * A wrapper class for solenoids (single and double)
 */
public class Solenoid {
	public static enum SolenoidType { DOUBLE, SINGLE }
	public static enum SolenoidState { OFF, FORWARD, REVERSE }
	
	private SolenoidType type;
	private SolenoidBase child;
	
	//Constructors
	/** Makes a single solenoid on the default PCM
	 * @param port The port that the solenoid is plugged into on the PCM
	 */
	public Solenoid(int port) {
		this(port, PCM.DEFAULT);
	}
	/** Makes a single solenoid on the specific PCM
	 * @param port The port that the solenoid is plugged into on the PCM
	 * @param pcm The pcm that the solenoid is plugged into
	 */
	public Solenoid(int port, PCM pcm) {
		this.type = SolenoidType.SINGLE;
		this.child = new SingleSolenoid(pcm.getCANID(), port);
	}
	
	/** Makes a double solenoid on the default PCM
	 * @param forwardPort The port that the solenoid's forward wire is plugged into on the PCM
	 * @param reversePort The port that the solenoid's reverse wire is plugged into on the PCM
	 */
	public Solenoid(int forwardPort, int reversePort) {
		this(forwardPort, reversePort, PCM.DEFAULT);
	}
	/** Makes a double solenoid on the specific PCM
	 * @param forwardPort The port that the solenoid's forward wire is plugged into on the PCM
	 * @param reversePort The port that the solenoid's reverse wire is plugged into on the PCM
	 * @param pcmId The pcm that the solenoid is plugged into
	 */
	public Solenoid(int forwardPort, int reversePort, PCM pcm) {
		this.type = SolenoidType.DOUBLE;
		this.child = new DoubleSolenoid(pcm.getCANID(), forwardPort, reversePort);
	}
	
	//Setters
	/** Set the solenoid to either forward or reverse */
	public void set(boolean toForward) {
		if (toForward)
			child.set(SolenoidState.FORWARD);
		else child.set(SolenoidState.REVERSE);
	}
	/** Sets the state of this solenoid */
	public void setState(SolenoidState state) {
		child.set(state);
	}
	
	//Getters
	/** @return If the solenoid state is forward */
	public boolean get() {
		return child.get() == SolenoidState.FORWARD;
	}
	/** @return The state of this solenoid */
	public SolenoidState getState() {
		return child.get();
	}
	/** @return The type of this solenoid */
	public SolenoidType getType() {
		return type;
	}
	
	//Child Classes
	private static abstract class SolenoidBase {
		abstract SolenoidState get();
		abstract void set(SolenoidState state);
	}
	private static class SingleSolenoid extends SolenoidBase {
		private edu.wpi.first.wpilibj.Solenoid sol;
		SingleSolenoid(int port, int pcmCANID) {
			this.sol = new edu.wpi.first.wpilibj.Solenoid(pcmCANID, port);
		}
		SolenoidState get() {
			if (sol.get())
				return SolenoidState.FORWARD;
			return SolenoidState.REVERSE;
		}
		void set(SolenoidState state) {
			sol.set(state == SolenoidState.FORWARD);
		}
	}
	private static class DoubleSolenoid extends SolenoidBase {
		private edu.wpi.first.wpilibj.DoubleSolenoid sol;
		DoubleSolenoid(int forwardPort, int reversePort, int pcmCANID) {
			this.sol = new edu.wpi.first.wpilibj.DoubleSolenoid(pcmCANID, forwardPort, reversePort);
		}
		SolenoidState get() {
			if (sol.get() == edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward)
				return SolenoidState.FORWARD;
			if (sol.get() == edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse)
				return SolenoidState.REVERSE;
			return SolenoidState.OFF;
		}
		void set(SolenoidState state) {
			if (state == SolenoidState.FORWARD)
				sol.set(edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward);
			else if (state == SolenoidState.REVERSE)
				sol.set(edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse);
			else sol.set(edu.wpi.first.wpilibj.DoubleSolenoid.Value.kOff);
		}
	}
}