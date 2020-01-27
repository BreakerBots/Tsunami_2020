/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class Right3BallPickup extends AutoPath {
	
	
	public Right3BallPickup() {
		// We changed the path!!  Robot lines up in front of power port,
		// shoots 3 balls, gets 3 balls from our trench
		// returns to shoot
		
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(2, 0, 0)
			));
		//Shooting Code
		
		add(new DriveTrajectoryAction(true, false,
				new Position(2, 0, 0),
				new Position(3, 6.5, 0),
				new Position(16.67, 6.5, 0)
				//new Position(-0.42, 6.75, 180)
			));
		//Shooting Code
		
		add(new DriveTrajectoryAction(true, true,
				new Position (16.67, 6.5, 0),
				new Position (0, 0, 0)
				));
		add(new DriveStopAction());
	}
}
