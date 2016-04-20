package util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.ChooserType;
import vision.VisionCore;
import config.DashboardConfig;
import config.ShooterConfig;
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
	private SendableChooser autoChooserOne = new SendableChooser();
	private SendableChooser autoChooserTwo = new SendableChooser();
	private SendableChooser leftRightChooser = new SendableChooser();
	
	/**
	 * Creates sections of dashboard
	 */
	public Dashboard(VisionCore vision, RobotCore robotCore) {
		this.vision = vision;
		this.robotCore = robotCore;
		
		autoChooserOne.addDefault("Do Nothing", new ChooserType(DashboardConfig.idDoNothing));
		autoChooserOne.addObject("To Auto Zone", new ChooserType(DashboardConfig.idDriveForward));
		autoChooserOne.addObject("Rock Wall", new ChooserType(DashboardConfig.idRockWall));
		autoChooserOne.addObject("Rough Terrain", new ChooserType(DashboardConfig.idRoughTerrain));
		autoChooserOne.addObject("Cheval de Frise", new ChooserType(DashboardConfig.idChevalDeFrise));
		autoChooserOne.addObject("Moat", new ChooserType(DashboardConfig.idMoat));
		autoChooserOne.addObject("Portcullis", new ChooserType(DashboardConfig.idPortcullis));
		autoChooserOne.addObject("Ramparts", new ChooserType(DashboardConfig.idRamparts));
		SmartDashboard.putData("Auto Mode", autoChooserOne);

		autoChooserTwo.addDefault("Don't Shoot", new ChooserType(DashboardConfig.idDontShoot));
		autoChooserTwo.addObject("Shoot", new ChooserType(DashboardConfig.idShoot));
		SmartDashboard.putData("Auto Mode Two", autoChooserTwo);
		
		leftRightChooser.addDefault("Left", new ChooserType(0));
		leftRightChooser.addObject("Right", new ChooserType(1));
		SmartDashboard.putData("Left Right", leftRightChooser);
		
		SmartDashboard.putNumber("shooterSpeed", ShooterConfig.constantSpeed);
		SmartDashboard.putNumber("kPLeft", ShooterConfig.kPLeft);
		SmartDashboard.putNumber("kPRight", ShooterConfig.kPRight);

		SmartDashboard.putNumber("kILeft", ShooterConfig.kILeft);
		SmartDashboard.putNumber("kIRight", ShooterConfig.kIRight);

		SmartDashboard.putNumber("kDLeft", ShooterConfig.kDLeft);
		SmartDashboard.putNumber("kDRight", ShooterConfig.kDRight);
	}
	
	/**
	 * Gives the id of the selected autoType
	 * @return id of auto type
	 */
	public int getAutoType() {
		return ((ChooserType) autoChooserOne.getSelected()).getId();
	}
	
	public void update() {
		SmartDashboard.putNumber("navX Angle", robotCore.navX.getAngle());
		SmartDashboard.putString("Goal One", vision.vs.goals[0].toString());
		SmartDashboard.putString("Goal Two", vision.vs.goals[1].toString());
		SmartDashboard.putString("Goal Three", vision.vs.goals[2].toString());
		SmartDashboard.putString("xml", vision.socket.getXML());
		SmartDashboard.putNumber("pitch", robotCore.navX.getRoll());
		
		SmartDashboard.putNumber("driveEncLeft", robotCore.driveEncLeft.getDistance());
		SmartDashboard.putNumber("driveEncRight", robotCore.driveEncRight.getDistance());
	}
	
	/**
	 * Gives the corresponding file name of the autoType selected
	 * @return file name
	 */
	public String getFileNameOne() {
		int id = ((ChooserType) autoChooserOne.getSelected()).getId();
		
		switch (id) {
			case 1:
				return DashboardConfig.driveForwardFileName;
				
			case 2:
				return DashboardConfig.rockWallFileName;
				
			case 3:
				return DashboardConfig.roughTerrainFileName;
				
			case 4:
				return DashboardConfig.chevalDeFriseFileName;
				
			case 5:
				return DashboardConfig.moatFileName;
				
			case 6:
				return DashboardConfig.portcullisFileName;

			case 7:
				return DashboardConfig.rampartsFileName;
				
			default:
				return DashboardConfig.doNothingFileName;
		}
	}
	
	public String getFileNameTwo() {
		int id = ((ChooserType) autoChooserTwo.getSelected()).getId();
		
		switch (id) {
			case 0:
				return DashboardConfig.shootFileName;
	
			case 1:
				return DashboardConfig.doNothingFileName;
				
			default:
				return DashboardConfig.doNothingFileName;
		}
	}
	
	/**
	 * Returns true for left of goal, false for right
	 * @return
	 */
	public boolean getLeftRight() {
		int id = ((ChooserType) leftRightChooser.getSelected()).getId();
		
		switch (id) {
			case 0:
				return true;
	
			case 1:
				return false;
				
			default:
				return false;
		}
	}
	
	public double getSpeed() {
		return SmartDashboard.getNumber("shooterSpeed");
	}
	
	public double getPLeft() {
		return SmartDashboard.getNumber("kPLeft");
	}
	
	public double getPRight() {
		return SmartDashboard.getNumber("kPRight");
	}
	
	public double getILeft() {
		return SmartDashboard.getNumber("kILeft");
	}
	
	public double getIRight() {
		return SmartDashboard.getNumber("kIRight");
	}
	
	public double getDLeft() {
		return SmartDashboard.getNumber("kDLeft");
	}
	
	public double getDRight() {
		return SmartDashboard.getNumber("kDRight");
	}
	
	public void putDouble(String key, double num) {
		SmartDashboard.putNumber(key, num);
	}
	
	public void putBoolean(String key, Boolean bool) {
		SmartDashboard.putBoolean(key, bool);
	}
}