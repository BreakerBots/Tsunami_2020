package frc.team5104.teleop;

import frc.team5104.Controls;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.FlywheelState;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.PanelState;
import frc.team5104.Superstructure.Target;
import frc.team5104.util.console;
import frc.team5104.util.console.c;
import frc.team5104.util.managers.TeleopController;

public class SuperstructureController extends TeleopController {
	protected void update() {
		//Idle
		if (Controls.IDLE.get()) {
			Superstructure.setMode(Mode.IDLE);
			console.log(c.SUPERSTRUCTURE, "idling");
		}

		//Panel
		if (Controls.PANEL_DEPLOY.get()) {
			if (Superstructure.getMode() == Mode.PANEL_DEPLOYING || 
				Superstructure.getMode() == Mode.PANELING) {
				Superstructure.setMode(Mode.IDLE);
				console.log(c.SUPERSTRUCTURE, "manually exiting paneling... idling");
			}
			else {
				Superstructure.setMode(Mode.PANEL_DEPLOYING);
				console.log(c.SUPERSTRUCTURE, "deploying paneler");
			}
		}
		if (Controls.PANEL_SPIN.get()) {
			if (Superstructure.getMode() == Mode.PANEL_DEPLOYING) {
				Superstructure.setMode(Mode.PANELING);
				console.log(c.SUPERSTRUCTURE, "panel starting " + Superstructure.getPanelState().toString());
			}
			else if (Superstructure.getMode() == Mode.PANELING) {
				Superstructure.setMode(Mode.PANEL_DEPLOYING);
				console.log(c.SUPERSTRUCTURE, "deploying paneler");
			}
		}
		if (Controls.PANEL_POSITION.get()) {
			Superstructure.setPanelState(PanelState.POSITION);
			console.log(c.SUPERSTRUCTURE, "setting panel mode to position");
		}
		if (Controls.PANEL_ROTATION.get()) {
			Superstructure.setPanelState(PanelState.ROTATION);
			console.log(c.SUPERSTRUCTURE, "setting panel mode to rotation");
		}
		

		//Intake
		if (Controls.INTAKE.get()) {
			if (Superstructure.getMode() == Mode.INTAKE) {
				Superstructure.setMode(Mode.IDLE);
				console.log(c.SUPERSTRUCTURE, "exiting intake... idling");
			}
			else {
				Superstructure.setMode(Mode.INTAKE);
				console.log(c.SUPERSTRUCTURE, "intaking");
			}
		}

		//Shooter
		if (Controls.SHOOT.get()) {
			if (Superstructure.getMode() == Mode.SHOOTING) {
				Superstructure.setMode(Mode.IDLE);
				console.log(c.SUPERSTRUCTURE, "exiting shooting... idling");
			}
			else {
				Superstructure.setMode(Mode.SHOOTING);
				console.log(c.SUPERSTRUCTURE, "shooting");
			}
		}
		if (Controls.SHOOT_LOW.get()) {
			Superstructure.setTarget(Target.LOW);
			console.log(c.SUPERSTRUCTURE, "setting shooter target to low");
		}
		if (Controls.SHOOT_HIGH.get()) {
			Superstructure.setTarget(Target.HIGH);
			console.log(c.SUPERSTRUCTURE, "setting shooter target to high");
		}
		if (Controls.CHARGE_FLYWHEEL.get()) {
			Superstructure.setFlywheelState(FlywheelState.SPINNING);
			console.log(c.SUPERSTRUCTURE, "charging flywheel");
		}
		
		//Hopper
		if (Controls.HOPPER_UNJAM.get()) {
			if (Superstructure.getMode() != Mode.UNJAM) {
				Superstructure.setMode(Mode.UNJAM);
				console.log(c.SUPERSTRUCTURE, "unjamming");
			}
		} 
		else if (Superstructure.getMode() == Mode.UNJAM) {
			Superstructure.setMode(Mode.IDLE);
			console.log(c.SUPERSTRUCTURE, "exiting unjamming... idling");
		}

		//Climb
		if (Controls.CLIMBER_DEPLOY.get() && Superstructure.getMode() == Mode.IDLE) {
			Superstructure.setMode(Mode.CLIMBING);
			console.log(c.SUPERSTRUCTURE, "Time to fly!");
		}
		//Climber.climberManual = Controls.CLIMBER_WINCH.get();
	}
}
