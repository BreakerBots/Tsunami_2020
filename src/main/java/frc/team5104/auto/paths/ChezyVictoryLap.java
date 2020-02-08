/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;


// VERY Rough, needs lots of tuning when we have balls 
// Shoot 3, Pickup 5 from rendezvous point


public class ChezyVictoryLap extends AutoPath {
	public ChezyVictoryLap() {
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(8, 0, -45)
			));
		add(new DriveTrajectoryAction(true, true,
				new Position(8, 0, -45),
				new Position(0, -1, 0)
			));
		add(new DriveTrajectoryAction(true, false,
				new Position(0, -1, 0),
				new Position(6, -4, +45)
			));
		add(new DriveTrajectoryAction(true, true,
				new Position(6, -4, +45),
				new Position(0, 0, 0)
			));
		//Shooting Code
		/*add(new DriveTrajectoryAction(true, false,
				new Position(8, 0, 0),
				new Position(6, -4.17, 0)
			));
		//Reverse
		add(new DriveTrajectoryAction(true,true,
				new Position(6, -4.17, 0),
				new Position(3.33, -2.5, 90),
				new Position(3, -5.58, 180),
				new Position(6, -9.33, 270),
				new Position(4.17, 4.67, 0),
				new Position(-2.33, -7.5, 0)
			));
		//Shooting Code*/
		add(new DriveStopAction());
	}
}
