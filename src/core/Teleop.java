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
		dashboard.putDouble("leftEnc", robotCore.driveEncLeft.getDistance());

		//drive.turnStep(1, 180);
		
//		System.out.println(rTheta[0] + "\t" + (rTheta[1] * 180/Math.PI));
	}
}
