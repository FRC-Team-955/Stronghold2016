package core;

import config.*;
import sensors.MyJoystick;
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
	private MyJoystick joy;
	private boolean isFirst = true;
	
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
		
		joy = new MyJoystick(0);
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
		//if(joy.getButton(1)) {
		//	visionDriving.driveToGear();	
		//}
		if(isFirst) {
			visionDriving.driveToGear();
			isFirst = false;
		}
		
		drive.update();
		visionDriving.update();

		//drive.turnStep(1, 180);
		
//		System.out.println(rTheta[0] + "\t" + (rTheta[1] * 180/Math.PI));
	}
}
