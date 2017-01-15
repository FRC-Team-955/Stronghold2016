package core;

import config.*;
import util.Dashboard;
import vision.VisionCore;

/**
 * Joins user input with core components
 * @author Trevor
 *
 */
public class Teleop {
	private RobotCore robotCore;
	private Drive drive;
	private Dashboard dashboard;
	private VisionCore vision;
	private VisionDriving visionDriving;
	
	/**
	 * Creates standard teleop object
	 * 
	 * @param robotCorxe
	 * @param drive
	 */
	public Teleop (RobotCore robotCore, Drive drive, Dashboard dashboard, VisionCore vision, VisionDriving visionDriving)
	{
		this.robotCore = robotCore;
		this.drive = drive;
		this.dashboard = dashboard;
		this.vision = vision;
		this.visionDriving = visionDriving;
		
//		this.climber = climber;
	}
		
	/**
	 * Periodic functionality including drive
	 */
	public void run() {
		robotCore.joy.update();
        dashboard.update();
		joyDrive();
	}
	
	/**
	 * Runs drive code 
	 */
	private void joyDrive() {
		visionDriving.driveToGear();
		double[] rTheta = robotCore.joy.getRTheta();
		drive.move(rTheta[0], rTheta[1]);
		dashboard.putDouble("leftEnc", robotCore.driveEncLeft.getDistance());

		if(robotCore.joy.getDpadUp()) {
			drive.setReverseMode(true);
		}
		
		if(robotCore.joy.getDpadDown()) {
			drive.setReverseMode(false);
		}
		
		if(robotCore.joy.getButton(1)) {
			visionDriving.startDrivingGear();
		}
		
		drive.driveDistance(1, 20);
		//drive.turnStep(1, 180);
		
//		System.out.println(rTheta[0] + "\t" + (rTheta[1] * 180/Math.PI));
	}
}
