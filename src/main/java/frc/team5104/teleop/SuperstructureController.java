package frc.team5104.teleop;

import frc.team5104.Controls;
import frc.team5104.Superstructure;
import frc.team5104.Superstructure.Mode;
import frc.team5104.Superstructure.PanelState;
import frc.team5104.subsystems.Paneler;
import frc.team5104.util.console;
import frc.team5104.util.managers.TeleopController;

public class SuperstructureController extends TeleopController {
	protected String getName() {
		return "Superstructure-Controller";
	}

	protected void update() {
		if (Controls.IDLE.get()) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}

		// Panel
		if (Controls.PANEL_DEPLOY.get() && Superstructure.getMode() != Mode.PANEL_DEPLOYING) {
			Superstructure.setMode(Mode.PANEL_DEPLOYING);
			console.log("Deploying Paneller");
		} else if (Controls.PANEL_DEPLOY.get() && Superstructure.getMode() == Mode.PANEL_DEPLOYING) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}
		if (Controls.PANEL_SPIN.get() && Superstructure.getMode() == Mode.PANEL_DEPLOYING) {
			Superstructure.setMode(Mode.PANELING);
			console.log(Superstructure.getPanelState());
		} else if (Controls.PANEL_SPIN.get() && Superstructure.getMode() == Mode.PANELING) {
			Superstructure.setMode(Mode.PANEL_DEPLOYING);
			console.log("Deploying Paneller");
		}
		if (Controls.PANEL_TOGGLE.get()) {
			if (Superstructure.getPanelState() == PanelState.ROTATION)
				Superstructure.setPanelState(PanelState.POSITION);
			else
				Superstructure.setPanelState(PanelState.ROTATION);
			console.log(Superstructure.getPanelState());
		}
		if (Superstructure.getMode() == Mode.PANELING && Paneler.isFinished()) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}

		// Intake
		if (Controls.BALL_INTAKE.get() && Superstructure.getMode() != Mode.INTAKE) {
			Superstructure.setMode(Mode.INTAKE);
			console.log("Intaking");
		} else if (Controls.BALL_INTAKE.get() && Superstructure.getMode() == Mode.INTAKE) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}

		// Shooter
		if (Controls.BALL_SHOOT.get() && Superstructure.getMode() != Mode.SHOOTING) {
			Superstructure.setMode(Mode.SHOOTING);
			console.log("Shooting, pew pew");
		} else if (Controls.BALL_SHOOT.get() && Superstructure.getMode() == Mode.SHOOTING) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}
		if (Controls.UNJAM.isDown() && Superstructure.getMode() != Mode.UNJAM) {
			Superstructure.setMode(Mode.UNJAM);
			console.log("Unjam!");
		} else if (Controls.UNJAM.getAlt()) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}

		// Climb
		if (Controls.CLIMBER_DEPLOY.get() && Superstructure.getMode() == Mode.IDLE) {
			Superstructure.setMode(Mode.CLIMBING);
			console.log("Time to fly!");
		}
		if (Controls.CLIMBER_FOLD.get() && Superstructure.getMode() == Mode.CLIMBING) {
			Superstructure.setMode(Mode.IDLE);
			console.log("Idle");
		}
	}
}
