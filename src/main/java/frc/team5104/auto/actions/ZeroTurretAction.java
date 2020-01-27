/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto.actions;

import frc.team5104.auto.AutoPathAction;

public class ZeroTurretAction extends AutoPathAction {
	
	@SuppressWarnings("unused")
	private double angleToZeroTurretAt;
	
    public ZeroTurretAction(double angleToZeroTurretAt) {
    	this.angleToZeroTurretAt = angleToZeroTurretAt;
    }

    public void init() {
    	//Turret.zeroTurret(angleToZeroTurretAt);
    }

    public boolean update() {
    	return true;
    }

    public void end() {
    	
    }
}
