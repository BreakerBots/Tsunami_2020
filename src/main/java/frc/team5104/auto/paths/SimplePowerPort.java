/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;

//Rough Path Complete

import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class SimplePowerPort extends AutoPath {
	public SimplePowerPort() {
		//Robot Position - In front of the Power Port
		add(new DriveTrajectoryAction(true, false,
			/*	new Position(0, 0, 0),
				new Position(2, 0, 0)  */
				new Position(10, 20.17, 0),
				new Position(11, 20.17, 0)
			));
		//Shooting Code
		add(new DriveStopAction());
		//Changed x from 0.5 to 2 to get the robot to move forward
		// about 1 foot.  With the 0.5 value, the robot tried to move,
		// but didn't actually move forward
	}

	
	}
