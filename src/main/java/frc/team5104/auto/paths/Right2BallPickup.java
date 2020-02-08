/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;

//Rough path complete
// Pick up two balls from our trench
// Go to shoot


import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class Right2BallPickup extends AutoPath {
	

	public Right2BallPickup() {
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(11.67, 0, 0)
			));
		
		
		//Reversing
		add(new DriveTrajectoryAction(true,true,
				//new Position(0, 0, 0),
				new Position(11.67, 0, 0),
				new Position(0, -5, 90)
			));
		//Shooting Code
		add(new DriveStopAction());
		//Code checked 1/25/2020 - Seems to work
	}
}
