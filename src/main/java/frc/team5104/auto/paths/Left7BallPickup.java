/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;

//GOOD! Rough path complete
//steal 2, take 5
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.ZeroOdometry;

public class Left7BallPickup extends AutoPath {
	public Left7BallPickup() {
		// drive forward to pick up two balls in other trench
		/*
		 * 
		 * add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(5.83, 0, 0)
			));
		

		// drive to shoot
		add(new DriveTrajectoryAction(true,true,
				new Position(5.83, 0, 0),
				new Position(1.67, 16.67, -90)
			));
		
		
		//  Line up for 5 ball pick up in our trench
		add(new DriveTrajectoryAction(true,true,
				new Position(1.67, 16.67, -90),
				new Position(-3, 23.67, 0)
			));
		
		
		//Go pick up 5 more balls from our own trench
		add(new DriveTrajectoryAction(true,false,
				new Position(-3, 23.67, 0),
				//new Position(4.17, 21.67, 180),
				//new Position(16.67, 21.67, 180),
				//new Position(4.17, 21.67, 180),
				new Position(16, 23.67, 0)
			));
		
		
		//drive back to shoot
		add(new DriveTrajectoryAction(true,true,
				new Position(14, 23.67, 0),
				//new Position(4.17, 21.67, 180),
				//new Position(16.67, 21.67, 180),
				//new Position(4.17, 21.67, 180),
				new Position(1.67, 16.67, 90)
			));
		*/
		
		add(new ZeroOdometry(new Position(10, 1, 0)));
	
		// pick up two balls from enemy trench
		add(new DriveTrajectoryAction(true, false,
				new Position(10, 1, 0),
				new Position(19.83, 1, 0)
			));
		
		// drive to PowerPort to shoot
		add(new DriveTrajectoryAction(true, true,
				new Position(19.83, 1, 0),
				new Position(10, 19.25, -90)
			));
		
	//  Line up for 5 ball pick up in our trench
			add(new DriveTrajectoryAction(true,true,
					new Position(10, 19.25, -90),
					new Position(5.33, 26.25, 0)
				));
			
		// pick up 5 balls in our trench
		add(new DriveTrajectoryAction(true, false,
				new Position(5.33, 26.25, 0),
				new Position(34-7, 24.25, 0)//subtracting seven b/c we ran out of room :(
			));
		
		// reverse to PowerPort
				add(new DriveTrajectoryAction(true, true,
						new Position(34-7, 24.25, 0),//subtracting seven b/c we ran out of room D:
						new Position(10, 19.25, 90)
					));
		
		//Shooting Code
		
		add(new DriveStopAction());
	}
}
