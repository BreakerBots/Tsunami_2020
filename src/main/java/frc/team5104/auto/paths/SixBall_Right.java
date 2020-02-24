/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.paths;

import frc.team5104.auto.AutoPath;
import frc.team5104.auto.Position;
import frc.team5104.auto.actions.DriveStopAction;
import frc.team5104.auto.actions.DriveTrajectoryAction;
import frc.team5104.auto.actions.IntakeAction;
import frc.team5104.auto.actions.ShootAction;

/**
 * @startingPosition Right of the Field 
 * Shoots 3 balls
 * Intakes 3 balls from our Trench
 * Returns to shoot 3 balls
 */
public class SixBall_Right extends AutoPath {
	public SixBall_Right() {
		add(new ShootAction());
		add(new IntakeAction());
		add(new DriveTrajectoryAction(true, false,
				new Position(0, 0, 0),
				new Position(14, 0, 0)
			));
		
		add(new DriveTrajectoryAction(true, true,
				new Position (14, 0, 0),
				new Position (0, -3.75, 45)
				));
		add(new DriveStopAction());
		add(new ShootAction());
	}
}