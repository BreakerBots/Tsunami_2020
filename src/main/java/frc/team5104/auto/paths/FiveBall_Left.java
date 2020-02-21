/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.IntakeAction;
import frc.team5104.auto.actions.ShootAction;

public class FiveBall_Left extends AutoPath {
	public FiveBall_Left() {
		add(new IntakeAction());
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(7.5/*9.58*/, 0, -60)
			));
		add(new DriveTrajectoryAction(true, true,
				new Position(5, 0, 0),
		//		new Position(4.17, 0.83, 0),
				new Position(4.17, 16, -90)
			));
		add(new DriveStopAction());
		add(new ShootAction());
	}
}