package util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.ChooserType;
import vision.VisionCore;
import config.DashboardConfig;
import config.VisionConfig;

/**
 * Controlls the smartDashboard
 * @author Trevor
 *
 */
public class Dashboard {

	VisionCore vision;
	private SendableChooser autoChooser = new SendableChooser();
	private SendableChooser autoGoalChooser = new SendableChooser();
	
	/**
	 * Creates sections of dashboard
	 */
	public Dashboard(VisionCore vision) {
		this.vision = vision;
		
		autoChooser.addDefault("Do Nothing", new ChooserType(DashboardConfig.idDoNothing));
		autoChooser.addObject("To Auto Zone", new ChooserType(DashboardConfig.idDriveForward));
		SmartDashboard.putData("Auto Mode", autoChooser);
		
		autoGoalChooser.addDefault("Don't Shoot", new ChooserType(DashboardConfig.idNoShoot));
		autoGoalChooser.addObject("Left Goal", new ChooserType(DashboardConfig.idLeftGoal));
		autoGoalChooser.addObject("Middle Goal", new ChooserType(DashboardConfig.idMiddleGoal));
		autoGoalChooser.addObject("Right Goal", new ChooserType(DashboardConfig.idRightGoal));
		SmartDashboard.putData("Auto Goal Chooser", autoGoalChooser);
		
		SmartDashboard.putDouble("shooterSpeed", 0);
	}
	
	/**
	 * Gives the id of the selected autoType
	 * @return id of auto type
	 */
	public int getAutoType() {
		return ((ChooserType) autoChooser.getSelected()).getId();
	}
	
	public void update() {
		SmartDashboard.putBoolean("Connected to Jetson", vision.socket.getSocketStatus());
		for(int i = 0; i < VisionConfig.numberOfGoals; i++){
			SmartDashboard.putString("Goal " + i, vision.vs.goals[i].toString());
		}
		SmartDashboard.putString("xml:", vision.socket.getXML());
	}
	
	/**
	 * Gives the corresponding file name of the autoType selected
	 * @return file name
	 */
	public String getFileName() {
		int id = ((ChooserType) autoChooser.getSelected()).getId();
		
		switch (id) {
			case 0:
				return DashboardConfig.doNothingFileName;
	
			case 1:
				return DashboardConfig.driveForwardFileName;
				
			default:
				return DashboardConfig.doNothingFileName;
		}
	}
	
	/**
	 * Gives which goal to shoot at
	 * @return goal number from 0 to 4
	 */
	public int getGoalNumber() {
		return ((ChooserType) autoGoalChooser.getSelected()).getId();
	}
	
	public double getSpeed() {
		return SmartDashboard.getDouble("shooterSpeed");
	}
	
	public void putDouble(String key, double num) {
		SmartDashboard.putDouble(key, num);
	}
}