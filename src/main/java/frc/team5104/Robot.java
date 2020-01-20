/*BreakerBots Robotics Team 2019*/
package frc.team5104;

import frc.team5104.Superstructure.SystemState;
import frc.team5104.auto.AutoManager;
import frc.team5104.auto.Odometry;
import frc.team5104.auto.paths.ExamplePath;
import frc.team5104.subsystems.Drive;
import frc.team5104.subsystems.Paneler;
import frc.team5104.teleop.CompressorController;
import frc.team5104.teleop.DriveController;
import frc.team5104.teleop.SuperstructureController;
import frc.team5104.util.XboxController;
import frc.team5104.util.console;
import frc.team5104.util.managers.SubsystemManager;
import frc.team5104.util.managers.TeleopControllerManager;
import frc.team5104.util.setup.RobotController;
import frc.team5104.vision.Limelight;
import frc.team5104.util.Plotter;
import frc.team5104.util.Webapp;

public class Robot extends RobotController.BreakerRobot {
	public Robot() {
		console.logFile.start();
		
		//Managers
		SubsystemManager.useSubsystems(
			new Paneler()
//			new Drive()
		);
		TeleopControllerManager.useTeleopControllers(
//			new DriveController(),
			new SuperstructureController(),
			new CompressorController()
		);
		
		//Other Initialization
//		Webapp.run();
		Plotter.reset();
//		Odometry.init();
		Limelight.init();
		CompressorController.stop();
		AutoManager.setTargetPath(new ExamplePath());
	}
	
	//Teleop 
	public void teleopStart() {
		TeleopControllerManager.enabled();
	}
	public void teleopStop() {
		TeleopControllerManager.disabled();
	}
	public void teleopLoop() {
		TeleopControllerManager.update();
	}
	
	//Autonomous
	public void autoStart() {
		AutoManager.enabled();
	}
	public void autoStop() {
		AutoManager.disabled();
	}
	public void autoLoop() {
		AutoManager.update();
	}
	
	//Test
	public void testLoop() {
		Superstructure.setSystemState(SystemState.DISABLED);
		Drive.stop();
		CompressorController.start(); 
	}
	
	//Main
	public void mainStart() {
		Superstructure.reset();
		SubsystemManager.reset();
	}
	public void mainStop() {
		Superstructure.reset();
		SubsystemManager.reset();
		
		console.logFile.end();
	}
	public void mainLoop() { 
		Superstructure.update();
		SubsystemManager.update();
		
		XboxController.update();
	}
}
