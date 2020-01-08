package frc.team5104.auto.util;

/** A simple class for sending/saving robot positions. */
public class RobotPosition {
	public double x, y; //in feet
	private double t; //in radians
	public RobotPosition(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.t = theta;
	}
	
	public void set(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.t = theta;
	}
	
    public double getTheta() {
        return t;
    }
	
    public void setTheta(double value) {
    	this.t = value;
    }
    
    public void addX(double by) {
    	this.x += by;
    }
    
    public void addY(double by) {
    	this.y += by;
    }
    
	public String toString() {
		return  "x: " + String.format("%.2f", x) + ", " +
				"y: " + String.format("%.2f", y);
	}
}