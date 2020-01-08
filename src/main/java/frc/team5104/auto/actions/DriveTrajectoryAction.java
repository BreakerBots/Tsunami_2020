/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team5104.auto.util.AutoPathAction;
import frc.team5104.auto.util.TrajectoryCacher;
import frc.team5104.auto.util.Odometry;
import frc.team5104.auto.util.TrajectoryFollower;
import frc.team5104.auto.util.FieldPosition;
import frc.team5104.subsystems.Drive;
import frc.team5104.util.console;
import frc.team5104.util.console.c;

/**
 * Follow a trajectory using the Breaker Trajectory Follower (Ramses Follower)
 */
public class DriveTrajectoryAction extends AutoPathAction {
	@SuppressWarnings("unused")
	private TrajectoryFollower follower;
	private FieldPosition[] waypoints;
	@SuppressWarnings("unused")
	private double lastTime;

    public DriveTrajectoryAction(FieldPosition[] points) {
    	this.waypoints = points;
    }

    public void init() {
    	console.sets.create("RunTrajectoryTime");
    	console.log(c.AUTO, "Running Trajectory");
    	
    	//Reset Odometry and Get Path (Reset it twice to make sure it all good)
    	follower = new TrajectoryFollower(TrajectoryCacher.getTrajectory(waypoints));
		Odometry.reset();
		
		//Wait 100ms for Device Catchup
		try { Thread.sleep(100); }  catch (Exception e) { console.error(e); e.printStackTrace(); }
		lastTime = Timer.getFPGATimestamp();
    }

    public boolean update() {
//    	DriveSignal nextSignal = follower.getNextDriveSignal(Odometry.getPosition(), (Timer.getFPGATimestamp() - lastTime) * 1000);
//    	Drive.set(nextSignal);
//    	lastTime = Timer.getFPGATimestamp();
//		return follower.isFinished();
    	Drive.stop();
    	return true;
    }

    public void end() {
    	Drive.stop();
    	console.log(c.AUTO, "Trajectory Finished in " + console.sets.getTime("RunTrajectoryTime") + "s at position " + Odometry.getPosition().toString());
    }
}
