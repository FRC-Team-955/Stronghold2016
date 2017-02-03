package util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.ChooserType;
import vision.VisionCore;
import config.DashboardConfig;
import config.VisionConfig;
import core.RobotCore;

/**
 * Controlls the smartDashboard
 * @author Trevor
 *
 */
public class Dashboard {

	VisionCore vision;
	RobotCore robotCore;
	
	/**
	 * Creates sections of dashboard
	 */
	public Dashboard(VisionCore vision, RobotCore robotCore) {
		this.vision = vision;
		this.robotCore = robotCore;
		
		SmartDashboard.putNumber("angleOffset", VisionConfig.angleOffset);
	}
	
	
	public void update() {
		SmartDashboard.putNumber("navX Angle", robotCore.navX.getAngle());
		SmartDashboard.putString("xml", vision.socket.getXML());
		SmartDashboard.putNumber("pitch", robotCore.navX.getRoll());
	}
	
	public void putDouble(String key, double num) {
		SmartDashboard.putNumber(key, num);
	}
	
	public void putBoolean(String key, Boolean bool) {
		SmartDashboard.putBoolean(key, bool);
	}
}