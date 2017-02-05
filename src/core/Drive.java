package core;

import components.TwoCimGroup;
import config.DriveConfig;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import util.Dashboard;
import util.PID;
import util.PathPlanner;
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
	//public TwoCimGroup leftCimGroup = new TwoCimGroup(DriveConfig.leftC1Chn, DriveConfig.leftC2Chn, DriveConfig.leftC1IsFliped, DriveConfig.leftC2IsFlipped);
	//public TwoCimGroup rightCimGroup = new TwoCimGroup(DriveConfig.rightC1Chn, DriveConfig.rightC2Chn, DriveConfig.rightC1IsFlipped, DriveConfig.rightC2IsFlipped);
	
	CANTalon lc1 = new CANTalon(DriveConfig.leftC1Chn);
	CANTalon lc2 = new CANTalon(DriveConfig.leftC2Chn);
	CANTalon rc1 = new CANTalon(DriveConfig.rightC1Chn);
	CANTalon rc2 = new CANTalon(DriveConfig.rightC2Chn);
	
	boolean lowGear = true;
	boolean reverseMode = false;
	Dashboard dash;
	
	double xPos;
	double yPos;
	double x;
	double y;
	
	double wantLeftRate = 0;
	double wantRightRate = 0;
	
	// Turning
	private boolean isFirst = true;
	private boolean isFirstTimer = true;
	private double angChange = 0;
	private Timer timer = new Timer();
	private double prevAng = 0;
	
	private boolean start = false;
	
	MotionProfileExample leftExample = new MotionProfileExample(lc1);
	MotionProfileExample rightExample = new MotionProfileExample(rc1);
	
	double[] leftVelocity = {};
	double[] rightVelocity = {};
	
	// Driving straight PID
	private boolean isFirstDrive = true;
	
	public Drive (RobotCore core, Dashboard dash) {
		this.dash = dash;
		robotCore = core;
		encLeft = core.driveEncLeft;
		encRight = core.driveEncRight;

		rc1.changeControlMode(TalonControlMode.MotionProfile);
		rc2.changeControlMode(TalonControlMode.Follower);
		rc2.set(DriveConfig.rightC1Chn);
		
		lc1.changeControlMode(TalonControlMode.MotionProfile);
		lc2.changeControlMode(TalonControlMode.Follower);
		lc2.set(DriveConfig.leftC1Chn);
		
		lc1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rc1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		lc1.reverseSensor(true);
		rc1.reverseSensor(false);
		
		lc1.setPID(DriveConfig.kPDrive, DriveConfig.kIDrive, DriveConfig.kDDrive, DriveConfig.kFDrive, 0, 0.2, 0);
		rc1.setPID(DriveConfig.kPDrive, DriveConfig.kIDrive, DriveConfig.kDDrive, DriveConfig.kFDrive, 0, 0.2, 0);
		
		lc1.enableControl();
		rc1.enableControl();
		
		lc1.set(0);
		rc1.set(0);
	}
	
	public void startMotionProfile() {
		start = true;
	}
	
	public void setVelocityPoints() {
		double[][] waypoints = {
				{0,0},
				{0,1.5},
				{-1.5,3.5},
				{-1.5,7}
		};
		
		double[][] leftRight = PathPlanner.generateSpline(waypoints);
		double[] leftVelocity = new double[leftRight.length];
		double[] rightVelocity = new double[leftRight.length];
		for(int i = 0; i < leftRight.length; i++) {
			leftVelocity[i] = leftRight[i][0];
			rightVelocity[i] = leftRight[i][1];
		}
		this.leftVelocity = leftVelocity;
		this.rightVelocity = rightVelocity;
	}
	
	public void update() {
		//lc1.set(wantLeftRate);
		//rc1.set(wantRightRate);

		leftExample.control();
		rightExample.control();
		
		leftExample.setVelocityPoints(leftVelocity);
		leftExample.setVelocityPoints(rightVelocity);
		
		lc1.changeControlMode(TalonControlMode.MotionProfile);
		rc1.changeControlMode(TalonControlMode.MotionProfile);
		
		CANTalon.SetValueMotionProfile leftSetOutput = leftExample.getSetValue();
		CANTalon.SetValueMotionProfile rightSetOutput = rightExample.getSetValue();
				
		lc1.set(leftSetOutput.value);
		rc1.set(rightSetOutput.value);

		/* if btn is pressed and was not pressed last time,
		 * In other words we just detected the on-press event.
		 * This will signal the robot to start a MP */
		if(start) {
			start = false;
			/* user just tapped button 6 */
			leftExample.startMotionProfile();
			rightExample.startMotionProfile();
		}
		
		dash.putDouble("leftEncVelocity", lc1.getEncVelocity());
		dash.putDouble("rightEncVelocity", rc1.getEncVelocity());
		dash.putDouble("leftEncDist", lc1.getEncPosition());
		dash.putDouble("rightEncDist", rc1.getEncPosition());
		dash.putDouble("wantLeftRate", wantLeftRate);
		dash.putDouble("wantRightRate", wantRightRate);
	}
	
	public void setWantRate(double left, double right) {
		//wantLeftRate = -left * DriveConfig.ticksPerFoot;
		//wantRightRate = right * DriveConfig.ticksPerFoot;
		
		lc1.set(left * DriveConfig.ticksPerFoot);
		rc1.set(right * DriveConfig.ticksPerFoot);
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
        //leftCimGroup.set(left* .7);
        //rightCimGroup.set(right * .7);
        
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
        
        //leftCimGroup.setNoRamp(left);
        //rightCimGroup.setNoRamp(right);
	}
	
	public void set(double left, double right) {
		//leftCimGroup.set(left);
		//rightCimGroup.set(right);
	}
	
	public void setNoRamp(double left, double right) {
		//leftCimGroup.setNoRamp(left);
		//rightCimGroup.setNoRamp(right);
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
