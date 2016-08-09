package core;

/**
 * Joins user input with core components
 * @author Trevor
 *
 */
public class Teleop {
	private RobotCore robotCore;
	private Drive drive;
	/**
	 * Creates standard teleop object
	 * 
	 * @param robotCorxe
	 * @param drive
	 */
	public Teleop (RobotCore robotCore, Drive drive)
 {
		this.robotCore = robotCore;
		this.drive = drive;
	}
		
	/**
	 * Periodic functionality including drive
	 */
	public void run() {
		robotCore.joy.update();
		joyDrive();
	}
	
	private void joyDrive() {
		double[] rTheta = robotCore.joy.getRTheta();
		drive.move(rTheta[0], rTheta[1]);		
	
		
	}
}
