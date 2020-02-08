/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;

// DO NOT ATTEMPT!
// NOT CODED!


import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class ThiefAuto extends AutoPath {
	public ThiefAuto() {
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(0.5, 0, 0)
			));
		//Shooting Code
		add(new DriveTrajectoryAction(true,false,
				new Position(7.08, 12.08, 0),
				new Position(16.25, 12.08, 0)
			));
		//Reversing
		add(new DriveTrajectoryAction(true,true,
				new Position(7.08, 12.08, 0),
				new Position(0, 0, 0)
			));
		//Shooting Code
		add(new DriveStopAction());
	}
}
