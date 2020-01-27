/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;

public class Left7BallPickup extends AutoPath {
	public Left7BallPickup() {
		System.out.println("Starting Left 7 Ball Pickup");
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(0.5, 0, 0),
				new Position(5.83, 0, 0)
			));
		//Reversing
		System.out.println("Now reversing!!");
		add(new DriveTrajectoryAction(true,true,  
				new Position(1.67, 2.92, 90),
				new Position(1.67, 16.67, 90)
			));
		
		
		//Shooting Code
		System.out.println("Now shooting...");
		add(new DriveTrajectoryAction(true,true,
				new Position(4.17, 21.67, 180),
				new Position(16.67, 21.67, 180),
				new Position(4.17, 21.67, 180),
				new Position(1.67, 16.67, 90)
			));
		//Shooting Code
		System.out.println("Shooting again!");
		add(new DriveStopAction());
	}
}
