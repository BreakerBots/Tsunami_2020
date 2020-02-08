/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

// Rough Path Complete
//Robot Position - Center of Initiation Line
// Leave initiatio line and move to Power Port to shoot 3 balls


public class SimpleCenter extends AutoPath {
	
	public SimpleCenter() {
		add(new DriveTrajectoryAction(true, false,
				/*new Position(0, 0, 0),
				new Position(1, 1, 90),
				new Position(1, 6.67, 90) */
				// trajectory based off starting position as (0,0)
				
				new Position(10, 13.5, 0),
				new Position(11, 14.5, 90),
				new Position(11, 20.17, 90) 
				
				
			));
		//Shooting Code
		add(new DriveStopAction());
	}
}