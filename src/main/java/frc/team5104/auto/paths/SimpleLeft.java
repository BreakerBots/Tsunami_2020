/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.ZeroOdometry;

// Rough Path Complete
//Robot Position - Farthest position on left (Ball Collection) side
// Leave initiation line
// Drive to PowerPort to shoot


public class SimpleLeft extends AutoPath {
	public SimpleLeft() {
		add(new ZeroOdometry(new Position(10, 1, 0)));
		//Robot Position - Farthest position on left (Ball Collection) side
		add(new DriveTrajectoryAction(true, false,
				/*new Position(0, 0, 0),
				new Position(1, 1, 90),
				new Position(1, 18.33, 90)*/
				new Position(10, 1, 0),
				new Position(11, 1, 90),
				new Position(11, 20.2, 90)
			));
		//Shooting Code
		add(new DriveStopAction());
	}
}
