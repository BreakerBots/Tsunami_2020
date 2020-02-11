/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.ZeroOdometry;

//GOOD! Rough path complete
//Robot Position - Middle of Initiation Line
// Steal two balls from other trench, then come back to shoot

public class Center2BallPickup extends AutoPath {
	public Center2BallPickup() {
		/*add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(9.58, -10.83, 0)
			));
		add(new DriveTrajectoryAction(true, true,
				new Position(9.58, -10.83, 0),
				new Position(4.75, 5.25, 90)  */
		
		
		// zero starting position
		add(new ZeroOdometry(new Position(10, 13.5, 0)));
		
		
		// drive to steal two balls
		add(new DriveTrajectoryAction(true, false,
				new Position(10, 13.5, 0),
				new Position(19.58, 2.5, 0)
				));
		
		// drive to shoot
		add(new DriveTrajectoryAction(true, true,
				new Position(19.58, 2.5, 0),
				new Position(10, 20.2, -90)
				));
				
				
		//Shooting Code
		add(new DriveStopAction());
	}
}
