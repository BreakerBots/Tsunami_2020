package frc.team5104.auto.actions;

import frc.team5104.auto.AutoPathAction;
import frc.team5104.auto.Odometry;
import frc.team5104.auto.Position;

public class ZeroOdometry extends AutoPathAction {

	private Position pos;
	
	public ZeroOdometry(Position pos) {
		this.pos = pos;
	}
	
	public void init() {
		Odometry.reset(pos);
	}

	public boolean update() {
		return true;
	}

	public void end() {
		
	}
}
