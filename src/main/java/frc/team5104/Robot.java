/*BreakerBots Robotics Team 2019*/
package frc.team5104;

import frc.team5104.Superstructure.SystemState;
import frc.team5104.auto.AutoManager;
import frc.team5104.auto.paths.ExamplePath;
import frc.team5104.auto.util.Odometry;
import frc.team5104.subsystems.Drive;
import frc.team5104.teleop.CompressorController;
import frc.team5104.teleop.DriveController;
import frc.team5104.teleop.SuperstructureController;
import frc.team5104.util.Tuner;
import frc.team5104.util.XboxController;
import frc.team5104.util.console;
import frc.team5104.util.managers.SubsystemManager;
import frc.team5104.util.managers.TeleopControllerManager;
import frc.team5104.util.setup.RobotController;
import frc.team5104.util.setup.RobotState;
import frc.team5104.vision.Limelight;
import frc.team5104.vision.VisionManager;
import frc.team5104.util.Webapp;

public class Robot extends RobotController.BreakerRobot {
	public Robot() {
		//Managers
		SubsystemManager.useSubsystems(
			new Drive()
		);
		TeleopControllerManager.useTeleopControllers(
			new DriveController(),
			new SuperstructureController(),
			new CompressorController()
		);
		
		//Other Initialization
		Webapp.run();
		Odometry.run();
		Limelight.init();
		CompressorController.stop();
		AutoManager.setTargetPath(new ExamplePath());
		Tuner.use(VisionManager.class);
	}
	
	//Teleop (includes sandstorm)
	public void teleopStart() {
		console.logFile.start();
		if (RobotState.isSandstorm()) { Odometry.reset(); AutoManager.run(); }
		else { TeleopControllerManager.enabled(); }
		TeleopControllerManager.enabled();
		Superstructure.reset();
		SubsystemManager.enabled();
	}
	public void teleopStop() {
		if (RobotState.isSandstorm()) { AutoManager.stop(); }
		else { TeleopControllerManager.disabled(); }
		Superstructure.reset();
		SubsystemManager.disabled();
		console.logFile.end();
	}
	public void teleopLoop() {
		if (RobotState.isSandstorm()) { CompressorController.stop(); }
		else { TeleopControllerManager.update(); }
		Superstructure.update();
		SubsystemManager.update();
	}
	
	//Test
	public void testLoop() {
		Superstructure.setSystemState(SystemState.DISABLED);
		Drive.stop();
		SubsystemManager.update();
		CompressorController.start(); 
	}
	
	//Main
	public void mainLoop() { 
		XboxController.update(); 
	}
}
