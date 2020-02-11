/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.ZeroOdometry;

// Line up left, steal 2 balls, then go shoot
//GOOD! Rough path complete



public class Left2BallPickup extends AutoPath {
	public Left2BallPickup() {
		/*add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(9.58, 0, 0)
			));
		add(new DriveTrajectoryAction(true, true,
				new Position(9.58, 0, 0),
				//new Position(4.17, 0.83, 0),
				new Position(0, 17.57, -90)
			));  */
		
		// zero starting position
		add(new ZeroOdometry(new Position(10, 1, 0)));
		
		// drive to steal two balls
		add (new DriveTrajectoryAction(true, false,
				new Position(10, 1, 0),
				new Position(19.58, 1, 0)
			));
		
		add(new DriveTrajectoryAction(true, true,
				new Position(19.58, 1, 0),
				new Position(10, 19.81, -90)
		));
		//Shooting Code
		add(new DriveStopAction());
	}
}