/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.ShootAction;

/**
 * @startingPosition Front of Power Port
 * Shoots 3 balls
 * Drives forward 7 feet
 */
public class ThreeBall_Forward extends AutoPath {
	public ThreeBall_Forward() {
		add(new ShootAction());
		add(new DriveTrajectoryAction(true, true,
				new Position(0, 0, 0),
				new Position(-7, 0, 0)
			));
		add(new DriveStopAction());
	}
}
