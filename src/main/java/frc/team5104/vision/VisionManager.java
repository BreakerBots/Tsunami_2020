package frc.team5104.vision;

import frc.team5104.util.DriveSignal;
import frc.team5104.util.DriveSignal.DriveUnit;
import frc.team5104.util.MovingAverage;
import frc.team5104.util.Tuner.tunerInput;
import frc.team5104.util.Tuner.tunerOutput;
import frc.team5104.util.console;
import frc.team5104.util.console.c;
import frc.team5104.util.console.t;
import frc.team5104.util.motion.PDFController;
import frc.team5104.vision.Limelight.LEDMode;
import frc.team5104.vision.Limelight.CamMode;

public class VisionManager {

	@tunerInput
	private static double VISION_TURN_P = .5;
	@tunerInput
	private static double VISION_TURN_D = 60;
	@tunerInput
	private static double VISION_FWD_P = 1;
	@tunerInput
	private static double VISION_FWD_D = 200;
	@tunerInput
	private static double VISION_TARGET_X = 2.2;
	@tunerInput
	private static double VISION_TARGET_Y = 2;
	private static PDFController turnController = new PDFController(VISION_TURN_P, VISION_TURN_D, 0, -6, 6);
	private static PDFController fwdController = new PDFController(VISION_FWD_P, VISION_FWD_D, 0, -6, 6);
	private static MovingAverage filterX = new MovingAverage(10, 0);
	private static MovingAverage filterY = new MovingAverage(5, 0);
	private static enum VisionState { VISION, FINISHED }
	private static VisionState visionState = VisionState.FINISHED;
	private static final int VISION_LOST_TARGET_EXIT_COUNT = 20;
	private static int lostTargetCount;
	@tunerOutput
	private static double lastX;
	@tunerOutput
	private static double lastY;
	
	//Update Loop
	public static DriveSignal getNextDriveSignal() {
		if (!Limelight.hasTarget()) {
			lostTargetCount++;
			if (lostTargetCount > VISION_LOST_TARGET_EXIT_COUNT) {
				console.log(c.VISION, t.ERROR, "Lost Vision Target");
				visionState = VisionState.FINISHED;
			}
		}
		else if (visionState == VisionState.VISION) {
			
			turnController.setPDF(VISION_TURN_P, VISION_TURN_D, 0);
			fwdController.setPDF(VISION_FWD_P, VISION_FWD_D, 0);
			
			filterX.update(Limelight.getTargetX());
			filterY.update(Limelight.getTargetY());
			
			turnController.set(VISION_TARGET_X);
			double turn = turnController.get(filterX.getDoubleOutput());
			fwdController.set(VISION_TARGET_Y);
			double fwd = -fwdController.get(filterY.getDoubleOutput());
			
			lastX = filterX.getDoubleOutput();
			lastY = filterY.getDoubleOutput();
			
			return new DriveSignal(fwd - turn, fwd + turn, true, DriveUnit.VOLTAGE);
		}
		return new DriveSignal();
	}

	//Getters
	public static boolean isFinished() { return visionState == VisionState.FINISHED; }
	public static boolean isInVision() { return !isFinished(); }

	//Start and End
	public static void end() {
		Limelight.setLEDMode(LEDMode.OFF);
		Limelight.setcamMode(CamMode.DRIVE);
	}
	public static void start() {
		Limelight.setLEDMode(LEDMode.ON);
		Limelight.setcamMode(CamMode.VISION);
		visionState = VisionState.VISION;
		lostTargetCount = 0;
		lastX = 0;
		lastY = 0;
		turnController.reset();
		fwdController.reset();
	}
}