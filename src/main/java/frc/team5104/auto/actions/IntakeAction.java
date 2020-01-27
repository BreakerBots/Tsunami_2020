/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.actions;

import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.auto.AutoPathAction;

public class IntakeAction extends AutoPathAction {
    public IntakeAction() {
    	
    }

    public void init() {
    	Superstructure.setMode(Mode.INTAKE);
    }

    public boolean update() {
    	return true;
    }

    public void end() {
    	
    }
}
