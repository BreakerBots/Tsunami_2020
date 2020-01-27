/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.actions;

import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.auto.AutoPathAction;

public class ShootAction extends AutoPathAction {
    public ShootAction() {
    	
    }

    public void init() {
    	Superstructure.setMode(Mode.SHOOTING);
    }

    public boolean update() {
    	return true;
    }

    public void end() {
    	
    }
}
