/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class Left2BallPickup extends AutoPath {
	public Left2BallPickup() {
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(9.58, 0, 0)
			));
		add(new DriveTrajectoryAction(true, true,
				new Position(5, 0, 0),
				new Position(4.17, 0.83, 0),
				new Position(4.17, 11.17, 0)
			));
		//Shooting Code
		add(new DriveStopAction());
	}
}