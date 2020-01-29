/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class Center2BallPickup extends AutoPath {
	//Robot Position - Middle of Initiation Line
	public Center2BallPickup() {
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(9.58, -10.83, 0),
				new Position(4.75, 5.25, 90)
			));
		//Shooting Code
		add(new DriveStopAction());
	}
}