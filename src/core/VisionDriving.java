package core;

import config.GearDrivingConfig;
import vision.VisionCore;

public class VisionDriving {
	
	private VisionCore vision;
	private Drive drive;
	private RobotCore robotCore;
	
	// Data from the triangle, updated once when started
	private double angChange;
	private double driveOneDist;
	private double secondTurnAng;
	private double driveTwoDist;
	
	private boolean notStarted = true;
	private int gearStep = 100;
	private double startAng = 0;
	
	public VisionDriving (VisionCore vision, Drive drive, RobotCore robotCore) {
		this.vision = vision;
		this.drive = drive;
		this.robotCore = robotCore;
	}
	
	/**
	 * Calculates the data for the triangle from the data gotten from vision
	 */
	public void setTriangleData() {
		double trans = vision.vs.getGoal(0).translation;
		double visionAng = vision.vs.getGoal(0).rotation;
		double dist = vision.vs.getGoal(0).distance;
		
		double vertDist = Math.sqrt((dist*dist) - (trans*trans));
		double wantAngDiff = Math.atan(vertDist/trans) * (GearDrivingConfig.wantDist/vertDist);
		angChange = visionAng + wantAngDiff;
		
		double distDiff = vertDist = GearDrivingConfig.wantDist;
		driveOneDist = Math.sqrt(trans + distDiff);
		
		double ang1 = Math.atan(trans/distDiff);
		
		if(trans <= 0) {
			secondTurnAng = -(180-ang1);
		} else {
			secondTurnAng = 180-ang1;
		}
		driveTwoDist = GearDrivingConfig.wantDist - GearDrivingConfig.visionGearDiff;		
	}
	
	public void startDrivingGear() {
		notStarted = true;
	}
	
	public void driveToGear() {
		if(notStarted) {
			gearStep = 0;
			startAng = robotCore.navX.getAngle();
			setTriangleData();
			notStarted = false;
		} 
		
		switch(gearStep) {
		case 0: 
			double wantAngOne = angChange+startAng;
			if(wantAngOne > 180) {
				wantAngOne -= 360;
			} else if(wantAngOne < -180) {
				wantAngOne +=360;
			}
			if(drive.turnStep(GearDrivingConfig.turnSpeed, wantAngOne)) {
				gearStep++;
			}
			break;
		case 1:
			if(drive.driveDistance(GearDrivingConfig.driveSpeed, driveOneDist)) {
				gearStep++;
				startAng = robotCore.navX.getAngle();
			}
			break;
		case 2:
			double wantAngTwo = secondTurnAng+startAng;
			if(wantAngTwo > 180) {
				wantAngTwo -= 360;
			} else if(wantAngTwo < -180) {
				wantAngTwo +=360;
			}
			if(drive.turnStep(GearDrivingConfig.turnSpeed, wantAngTwo)) {
				gearStep++;
			}
			break;
		case 3:
			if(drive.driveDistance(GearDrivingConfig.driveSpeed, driveTwoDist)) {
				gearStep++;
			}
			break;
		default:
			break;
		}
		
	}
}
