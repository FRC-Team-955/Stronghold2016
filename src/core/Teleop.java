package core;

import config.*;
import edu.wpi.first.wpilibj.Timer;
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
	private Timer timer = new Timer();
	private boolean update = false;
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
		
	public void init() {
		timer.reset();
		timer.start();
		update = false;
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
		if(isFirst && timer.get() > 3) {
			//visionDriving.driveToGear();
			drive.setVelocityPoints();
			isFirst = false;
		}
		
		if(timer.get() > 6) {
			update = true;
		}
		
		if(update) {
			drive.update();
		}
		
		dashboard.putDouble("timer", timer.get());
		
		if(timer.get() > 10) {
			timer.reset();
			timer.stop();
			drive.startMotionProfile();
		}
		//visionDriving.update();

		//drive.turnStep(1, 180);
		
//		System.out.println(rTheta[0] + "\t" + (rTheta[1] * 180/Math.PI));
	}
}
