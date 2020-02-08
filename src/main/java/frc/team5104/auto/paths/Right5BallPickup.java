/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
// Line up on the Right.  Shoot from there
// Collect 5 balls from our trench
// Go to Power Port to shoot
// Rough Path complete! it's good (enough)


public class Right5BallPickup extends AutoPath {
	public Right5BallPickup() {
		
		//Shooting Code
		
		//Pick up 5 balls in our trench
		add(new DriveTrajectoryAction(true,false,
				new Position(0, 0, 0),
				new Position(18, 0, 0)
				//new Position(20.5, 1.25, 0),
				//new Position(20.92, 0, 90),
				//new Position(20.5, -1.25, 180),
				//new Position(-0.42, -1.25, 180)
			));
		
		// Come back to Power Port to shoot again
		add(new DriveTrajectoryAction(true,true,
				new Position(18, 0, 0),
				new Position(0, -5, 90)
				
			));
		//Shooting Code
		add(new DriveStopAction());
	}
}
