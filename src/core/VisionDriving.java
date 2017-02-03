package core;

import java.util.Timer;

import config.GearDrivingConfig;
import config.PathConfig;
import util.PathPlanner;
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
	private Timer scheduler;
	private double[][] leftRightVelocity;
	private PathFollower follower;
	private boolean following = false;
	
	public VisionDriving (VisionCore vision, Drive drive, RobotCore robotCore) {
		this.vision = vision;
		this.drive = drive;
		this.robotCore = robotCore;
		scheduler = new Timer();
	}
	
	/**
	 * Calculates the data for the triangle from the data gotten from vision
	 */
	public void setTriangleData() {
//		double trans = vision.vs.getGoal(0).translation;
//		double visionAng = vision.vs.getGoal(0).rotation;
//		double dist = vision.vs.getGoal(0).distance;
		
		double trans = 18;
		double visionAng = 30;
		double dist = 36;
		
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
	
	public void update() {
		if(following) {
			if(follower.getStep() > leftRightVelocity.length) {
				scheduler.cancel();
				following = false;
			}
		}
	}
	
	public void driveToGear() {
		double[][] waypoints = {
			{2,0},
			{2,1},
			{6,8},
			{6,10}
		};
		leftRightVelocity = PathPlanner.generateSpline(waypoints);
		follower = new PathFollower(drive, leftRightVelocity);
		scheduler.schedule(follower, 0L, (long) (PathConfig.timeStep * 1000));
		following = true;
	}
}
