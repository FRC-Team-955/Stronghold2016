package core;

import components.TwoCimGroup;
import config.DriveConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.PID;
import util.Util;

import java.lang.Math;

/**
 * Core components that make the robot move
 * 
 * @author Trevor
 *
 */
public class Drive {
	
	private RobotCore robotCore;
	private Encoder encLeft;
	private Encoder encRight;
	public TwoCimGroup leftCimGroup = new TwoCimGroup(DriveConfig.leftC1Chn, DriveConfig.leftC2Chn, DriveConfig.leftC1IsFliped, DriveConfig.leftC2IsFlipped);
	public TwoCimGroup rightCimGroup = new TwoCimGroup(DriveConfig.rightC1Chn, DriveConfig.rightC2Chn, DriveConfig.rightC1IsFlipped, DriveConfig.rightC2IsFlipped);
	private PID drivePID = new PID(DriveConfig.kP, DriveConfig.kI, DriveConfig.kD);
	boolean lowGear = true;
	boolean reverseMode = false;
	
	double xPos;
	double yPos;
	double x;
	double y;
	
	double wantLeftRate = 0;
	double wantRightRate = 0;
	
	double leftRate = 0;
	double rightRate = 0;
	
	// Turning
	private boolean isFirst = true;
	private boolean isFirstTimer = true;
	private double angChange = 0;
	private Timer timer = new Timer();
	private double prevAng = 0;
	
	// Driving straight PID
	private boolean isFirstDrive = true;
	
	public Drive (RobotCore core) {
		robotCore = core;
		encLeft = core.driveEncLeft;
		encRight = core.driveEncRight;
	}
	
	public void update() {
		drivePID.update(robotCore.driveEncLeft.getRate(), wantLeftRate);
		leftRate += drivePID.getOutput();
		leftCimGroup.set(leftRate);
		
		drivePID.update(robotCore.driveEncRight.getRate(), wantRightRate);
		rightRate += drivePID.getOutput();
		rightCimGroup.set(rightRate);
	}
	
	public void setWantRate(double left, double right) {
		wantLeftRate = left;
		wantRightRate = right;
	}
	
	/**
	 * Moves the robot in a specified direction at a specified velocity
	 * 
	 * @param r velocity between -1 and 1
	 * @param theta angle of joystick in radians
	 * 
	  */
	public void move(double r, double theta) {
//		if(Util.withinThreshold(Math.abs(theta), Math.PI/2, 0.015)/* && !lowGear*/) {
//			theta+=DriveConfig.driveStraightOffset;
//		}
		
		if(reverseMode) {
			xPos = r*Math.cos(theta);
			yPos = -r*Math.sin(theta);
			
			x = xPos * Math.abs(xPos);
			y = yPos * Math.abs(yPos);
		}
		
		else {
			xPos = r*Math.cos(theta);
			yPos = r*Math.sin(theta);
			
			x = xPos * Math.abs(xPos);
			y = yPos * Math.abs(yPos);
		}
		
		
		double left = y + x;
		double right = y - x;
//		System.out.println("left : " + left + "\tright : " + right);
        leftCimGroup.set(left* .7);
        rightCimGroup.set(right * .7);
        
//        System.out.println("Drive Encoder Left: " + encLeft.getDistance() + "\tDrive Encoder Right: " + encRight.getDistance());
	}
	
	public void moveNoRamp(double r, double theta) {
//		if(Util.withinThreshold(Math.abs(theta), Math.PI/2, 0.015)/* && !lowGear*/) {
//			theta+=DriveConfig.driveStraightOffset;
//		}
		
		double xPos = r*Math.cos(theta);
		double yPos = r*Math.sin(theta);
		
		double x = xPos * Math.abs(xPos);
        double y = yPos * Math.abs(yPos);
		
        double left = y + x;
        double right = y - x;
        
        leftCimGroup.setNoRamp(left);
        rightCimGroup.setNoRamp(right);
	}
	
	public void set(double left, double right) {
		leftCimGroup.set(left);
		rightCimGroup.set(right);
	}
	
	public void setNoRamp(double left, double right) {
		leftCimGroup.setNoRamp(left);
		rightCimGroup.setNoRamp(right);
	}
	
	public void setReverseMode(boolean mode) {
		reverseMode = mode;
	}
	
	public boolean driveDistance(double velocity, double distance) {
		if(isFirstDrive) {
			robotCore.driveEncLeft.reset();
			isFirstDrive = false;
		} 
		double speed = velocity * DriveConfig.kPDrive * (distance - robotCore.driveEncLeft.get());
		set(speed, speed);
		
		if(robotCore.driveEncLeft.get() > distance) {
			isFirstDrive = true;
			return true;
		}
		return false;
	}

	public boolean turnStep(double velocity, double turnAng) {
//		turnAng*=-1;
		double currAng = robotCore.navX.getAngle();
		double error = turnAng+angChange;
		double kP = 0.015;
		double angVelocity = kP*error;
		
		if(angVelocity > 1) 
			angVelocity = 1;
		else if(angVelocity < -1)
			angVelocity = -1;
		
		angVelocity *= velocity;
		set(angVelocity, -angVelocity);
		
		if(isFirst){
			prevAng = robotCore.navX.getAngle();
			isFirst = false;
			prevAng = currAng;
			angChange = 0;
		}
		
		if (Math.abs(prevAng - currAng) > DriveConfig.angChangeThreshold){
			if(prevAng > 0)
				angChange += ((currAng - prevAng) + 360);	
			else
				angChange += -((currAng - prevAng) - 360);	
		}
		
		else {
			angChange += (currAng - prevAng);	
		}
		
		if(Math.abs(angVelocity) < DriveConfig.notMovingThreshold) {
			if(isFirstTimer) {
				timer.start();
				isFirstTimer = false;
			}
			
			if(timer.get() > DriveConfig.turnNextTime) {
				if(Math.abs(angVelocity) < DriveConfig.notMovingThreshold){
					isFirst = true;
					isFirstTimer = true;
					return true;
				}
				timer.reset();
				timer.stop();
			}
		}
		
		prevAng = currAng;
		return false;
	}
}
