/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;

//Rough path complete
// Pick up two balls from our trench
// Go to shoot


import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.ZeroOdometry;

public class Right2BallPickup extends AutoPath {
	

	public Right2BallPickup() {
		/*add(new DriveTrajectoryAction(true, false,
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
		*/
		
		add(new ZeroOdometry(new Position(10, 24.25, 0)));
		
		add(new DriveTrajectoryAction(true,false,
				//new Position(0, 0, 0),
				new Position(10, 24.25, 0),
				new Position(22, 24.25, 0)
			));
		
		add(new DriveTrajectoryAction(true,true,
				new Position(22, 24.25, 0),
				new Position(10, 19.25, 90)
			));
		
		/* Richmond add(new DriveTrajectoryAction(true,true,
				new Position(22, 24.25, 0),
				new Position(0, -5, 90)
			)); */
		
		
		
		
		
		
	}
}
