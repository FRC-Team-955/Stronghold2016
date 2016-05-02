package core;

import components.TwoCimGroup;
import config.DriveConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
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
	public DoubleSolenoid shiftingSol = new DoubleSolenoid(DriveConfig.shiftSolPortA, DriveConfig.shiftSolPortB);
//	private PID drivePID = new PID(DriveConfig.kP, DriveConfig.kI, DriveConfig.kD);
	boolean lowGear = true;
	boolean reverseMode = false;
	
	double xPos;
	double yPos;
	double x;
	double y;
	
	public Drive (RobotCore core) {
		robotCore = core;
		encLeft = core.driveEncLeft;
		encRight = core.driveEncRight;
		shiftingSol.set(DoubleSolenoid.Value.kReverse);
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
        leftCimGroup.set(left);
        rightCimGroup.set(right);
        
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
	
	public void toLowGear() {
		shiftingSol.set(DoubleSolenoid.Value.kReverse);
		lowGear = true;
	}
	
	public void toHighGear() {
		shiftingSol.set(DoubleSolenoid.Value.kForward);
		lowGear = false;
	}
	
	public void setReverseMode(boolean mode) {
		reverseMode = mode;
	}
}
