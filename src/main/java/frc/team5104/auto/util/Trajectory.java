/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.util;

import java.io.Serializable;

/**
 * A generated or empty trajectory. 
 * Simply contains an array of segments.
 */
public class Trajectory implements Serializable {
	private static final long serialVersionUID = 1L;
	TrajectorySegment[] segments_;
	
	//Constructors
	/** Creates a trajectory from an array of segments. */
	public Trajectory(TrajectorySegment[] segments) {
		segments_ = segments;
	}
	/** Creates a empty trajectory of a certain length. */
	public Trajectory(int length) {
		if (length < Integer.MAX_VALUE-6) {
			segments_ = new TrajectorySegment[length];
			for (int i = 0; i < length; ++i) {
				segments_[i] = new TrajectorySegment();
			}
		}
		else {
			//Trajectory too big
			segments_ = null;
		}
	}
	
	//Getters and Setters
	public int length() { return segments_.length; }
	public TrajectorySegment get(int index) {
		if (index < length())
			return segments_[index];
		else
			return new TrajectorySegment();
	}
	public void setSegment(int index, TrajectorySegment segment) {
		if (index < length())
			segments_[index] = segment;
	}

	//String Functions
	public String toString() {
		String str = "X, Y, Heading\n";
		for (int i = 0; i < length(); i++) {
			TrajectorySegment segment = get(i);
			str += segment.x + ", ";
			str += segment.y + ", ";
			str += segment.theta + ", ";
			str += "\n";
		}
		return str;
	}
	
	public String toJSON() {
		String str = "[";
		for (int i = 0; i < length(); i++) {
			TrajectorySegment segment = get(i);
			str += "{"
				+  "\"x\": " + segment.x + ", "
				+  "\"y\": " + segment.y + ", "
				+  "\"heading\": " + segment.heading() + ", "
				+  "\"velocity\": " + segment.velocity
				+  "}" + ((i == length()-1) ? "" : ",");
		}
		str += "]";
		return str;
	}
}
