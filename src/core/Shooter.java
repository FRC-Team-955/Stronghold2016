package core;

import java.util.HashMap;
import java.util.Map;

import components.CIM;
import config.IntakeArmConfig;
import config.ShooterConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import util.Dashboard;
import util.FileSaver;
import util.PID;
import util.Util;
import vision.VisionCore;

/**
 * Controls the shooter mechanism
 * @author Trevor
 *
 */
public class Shooter{
	private VisionCore vision;
	private Drive drive;
	private Dashboard dash;
	private Intake intake;
	private RobotCore robotCore;
	
	public Encoder leftMotorEnc;
	public Encoder rightMotorEnc;
	public DoubleSolenoid solOne;
	public CIM leftMotor = new CIM(ShooterConfig.ChnMotorOne, false);
	public CIM rightMotor = new CIM(ShooterConfig.ChnMotorTwo, true);
	
	private PID leftPID;
	private PID rightPID;
	
	private boolean isFirst;
	private boolean stopping;
	private boolean isFirstTimer;
	private boolean isFirstMotors;
	private boolean usingVision;
	private boolean withinThreshold;
	private boolean adjustWithLeft = false;
	private boolean isShooting;
	private boolean filledRightRate = true;
	private boolean isFirstLeft = true;
	private boolean isFirstRight = true;
	private boolean closeMode = false;
	private boolean resettingShot = true;
	private boolean doneShooting = false;
	
	private Timer timerOne = new Timer();
	private Timer timer = new Timer();
	private Timer leftTimer = new Timer();
	private Timer rightTimer = new Timer();
	private Timer resetTimer = new Timer();
	
	private int wantGoal;
	private int movingAverageStepLeft = 0;
	private int movingAverageStepRight = 0;
	private double leftRate;
	private double rightRate;
	private double shootSpeed = 0;
	private double speedLeft;
	private double speedRight;
	private double encoderLeftDistance;
	private double encoderRightDistance;
	private double wantSpeedLeft = 0;
	private double wantSpeedRight = 0;
	private double wantAng;
	private double driveSpeed = 0;
	private double rightRateFiltered = 0;
	private double leftRateFiltered = 0;
	private double[] rightRateValues = new double[ShooterConfig.movingAverageNumbers];
	private double[] leftRateValues = new double[ShooterConfig.movingAverageNumbers];
	
	FileSaver fileSaverLeft = new FileSaver("shooterDataLeft.txt");
	FileSaver fileSaverRight = new FileSaver("shooterDataRight.txt");

	/**
	 * 
	 * @param core
	 * @param drive
	 * @param vision
	 */
	public Shooter(RobotCore core, Drive drive, VisionCore vision, Dashboard dash, Intake intake){
		leftMotorEnc = core.shooterLeftEnc;
		rightMotorEnc = core.shooterRightEnc;
		solOne = core.shooterSol;
		speedLeft = 0;
		speedRight = 0;
		usingVision = true;
		this.dash = dash;
		this.vision = vision;
		this.robotCore = core;
		this.intake = intake;
		
		this.drive = drive;		
		
		solOne.set(DoubleSolenoid.Value.kForward);
		leftMotor.set(0);
		rightMotor.set(0);
		isShooting = false;
		
		leftPID = new PID(ShooterConfig.kPLeft, ShooterConfig.kILeft, ShooterConfig.kDLeft);
		rightPID = new PID(ShooterConfig.kPRight, ShooterConfig.kIRight, ShooterConfig.kDRight);
		isFirst = true;
	}
	
	public void updateShooterDash() {
		dash.putDouble("leftShooterEncFiltered", leftRateFiltered);
		dash.putDouble("rightShooterEncFiltered", rightRateFiltered);
		dash.putDouble("leftShooterEnc", leftRate);
		dash.putDouble("rightShooterEnc", rightRate);
		dash.putDouble("leftShooterDistance", leftMotorEnc.getDistance());
		dash.putDouble("rightShooterDistance", rightMotorEnc.getDistance());
		dash.putDouble("leftShooterOutput", leftPID.getOutput());
		dash.putDouble("rightShooterOutput", rightPID.getOutput());
		dash.putDouble("shooterWantSpeed", shootSpeed);
		dash.putDouble("shooter drive output", vision.getTurnPID());
		dash.putDouble("rotation", vision.vs.getRotation(wantGoal));
		dash.putDouble("wantAng", wantAng);
		dash.putDouble("leftEncCount", leftMotorEnc.get());
		dash.putDouble("rightEncCount", rightMotorEnc.get());
		
		fileSaverLeft.write(Double.toString(leftMotorEnc.getRate()));
		fileSaverRight.write(Double.toString(rightMotorEnc.getRate()));
	}
	
	public void updateShooterWheels() {
		if(Util.withinThreshold(leftRate, shootSpeed, 0.2)); {
			leftPID.updateConstants(dash.getPLeft(), dash.getILeft(), dash.getDLeft());			
		}
		
		if(Util.withinThreshold(rightRate, shootSpeed, 0.2)); {
			rightPID.updateConstants(dash.getPLeft(), dash.getILeft(), dash.getDLeft());	
		}
		
		if(!Util.withinThreshold(leftRate, shootSpeed, 0.2)); {
			leftPID.updateConstants(dash.getPRight(), dash.getIRight(), dash.getDRight());			
		}
		
		if(!Util.withinThreshold(rightRate, shootSpeed, 0.2)); {
			rightPID.updateConstants(dash.getPRight(), dash.getIRight(), dash.getDRight());			
		}
		
		if(!stopping) {
//			if(isFirstMotors) {
//				timerOne.start();
//				isFirstMotors = false;
//			}
			
			
			
//			if(timerOne.get() > 2) {
				leftPID.update(Math.abs(leftRate), shootSpeed);
				rightPID.update(Math.abs(rightRate), shootSpeed);
				wantSpeedLeft+=leftPID.getOutput();
				wantSpeedRight+=rightPID.getOutput();
//				if(speedLeft - shootSpeed > 0.1) {
//					speedLeft-=0.12;
//				}
//				
//				if(speedRight - shootSpeed > 0.1) {
//					speedRight-=0.12;
//				}
//				speedLeft = Util.limit(speedLeft, dash.getSpeed()-.1, dash.getSpeed()+.1);
//				speedRight = Util.limit(speedRight, dash.getSpeed()-.1, dash.getSpeed()+.1);
//			}
			leftMotor.set(wantSpeedLeft);
			rightMotor.set(wantSpeedRight);
		}
		else {
			leftMotor.ramp(0,0.05);
			rightMotor.ramp(0,0.05);
		}
	}
	
	public void updateTurn() {
		if(isShooting && usingVision) {
			if(Math.abs(Math.abs(robotCore.navX.getAngle()) -  Math.abs(wantAng)) < ShooterConfig.changePIDAng) {
				vision.changePIDConstants(ShooterConfig.kPDriveClose, ShooterConfig.kIDriveClose, ShooterConfig.kDDriveClose);
				closeMode = true;
			}
			
			else {
				vision.changePIDConstants(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);
				closeMode = false;
			}
			
			vision.updateTurnPID(robotCore.navX.getAngle(), wantAng);
			double turnPIDOutput = vision.getTurnPID();
			driveSpeed = 0;
			
			dash.putBoolean("navX within", Util.withinThreshold(robotCore.navX.getAngle(), wantAng, 1));
			dash.putBoolean("drive within", Util.withinThreshold(driveSpeed, 0, 0.2));
			dash.putBoolean("timer within", resetTimer.get() > 0.5);
			dash.putBoolean("resettingShot", resettingShot);
			dash.putBoolean("withinThreshold", withinThreshold);
			
			
			
			if(!withinThreshold && !resettingShot && resetTimer.get() > 0.5 && Util.withinThreshold(robotCore.navX.getAngle(), wantAng, 1) && Util.withinThreshold(driveSpeed, 0, 0.2)) {
//				wantAng = robotCore.navX.getAngle() + ((180/Math.PI)*Math.atan(vision.vs.getRotation(wantGoal)/(vision.vs.getDistance(wantGoal)*1.52)));
				resettingShot = true;
				resetTimer.reset();
				resetTimer.start();
			}
			
			if(resetTimer.get() > 0.5 && resettingShot) {
				resettingShot = false;
//				shoot();
				wantAng = robotCore.navX.getAngle() + ((180/Math.PI)*Math.atan(vision.vs.getRotation(wantGoal)/(vision.vs.getDistance(wantGoal)*1.32)));
				if(wantAng-robotCore.navX.getAngle() >= 0) {
					adjustWithLeft = true;
				}
				else if(wantAng-robotCore.navX.getAngle() < 0) {
					adjustWithLeft = false;
				}
			}
			
			if(closeMode) {	
				if(turnPIDOutput < 0) {
					driveSpeed = -ShooterConfig.rotateMinSpeedClose + turnPIDOutput;
//					driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeedClose, -ShooterConfig.rotateMinSpeedClose);
				}
				
				else {
					driveSpeed = ShooterConfig.rotateMinSpeedClose + turnPIDOutput;
//					driveSpeed = Util.limit(vision.getTurnPID(), ShooterConfig.rotateMinSpeedClose, ShooterConfig.rotateMaxSpeedClose);
				}
			}
			
			else {
				if(turnPIDOutput < 0) {
					driveSpeed = Util.limit(vision.getTurnPID(), -ShooterConfig.rotateMaxSpeed, -ShooterConfig.rotateMinSpeed);
				}
				
				else {
					driveSpeed = Util.limit(vision.getTurnPID(), ShooterConfig.rotateMinSpeed, ShooterConfig.rotateMaxSpeed);
				}
			}
			
			if(Math.abs(Math.abs(vision.vs.getRotation(wantGoal))) < ((ShooterConfig.rotationTolerance/2))) {
				driveSpeed = 0;				
			}
			
			if(Math.abs(Math.abs(wantAng)-Math.abs(robotCore.navX.getAngle())) < 1){
				driveSpeed = 0;
			}
			
			dash.putDouble("driveSpeed", driveSpeed);
			if(closeMode && !adjustWithLeft) {
				drive.setNoRamp(0, driveSpeed);								
			}
			
			else if(closeMode && adjustWithLeft) {
				drive.setNoRamp(-driveSpeed, 0);								
			}
			else {
				drive.setNoRamp(-driveSpeed, driveSpeed);		
			}
		}
	}
	
	public void checkAngle() {
		if(usingVision) {
			if(Util.withinThreshold(vision.vs.getRotation(wantGoal), 0, ShooterConfig.rotationTolerance) && Math.abs(driveSpeed) < 0.02 ) {
				withinThreshold = true;
			}
			
			else {
				withinThreshold = false;
			}
		}
		
		else {
			withinThreshold = true;
		}
	}

	/**)
	 * Run periodically to control shooting process
	 */
	public void update(){
//		updateLeftRate();
//		updateRightRate();
		leftRate = leftMotorEnc.getRate();
		rightRate = rightMotorEnc.getRate();
		dash.putBoolean("isMotorsFast", isMotorsFastEnough(shootSpeed));
		wantGoal = vision.vs.getHighestArea();
		updateShooterDash();
		updateShooterWheels();
		updateTurn();
		checkAngle();
		
		if(isShooting && withinThreshold && isMotorsFastEnough(shootSpeed)){
			if(isFirstTimer){
				timer.start();
				isFirstTimer = false;
			}
			
			if (timer.get() > ShooterConfig.velocitySettleTime){
				solOne.set(DoubleSolenoid.Value.kReverse);

				fileSaverLeft.write("SHOOT");
				fileSaverRight.write("SHOOT");
				
				if(isFirst) {
					timer.reset();
					timer.start();
					isFirst =  false;
					doneShooting = true;
				}
			}
		}
		
		else if (!doneShooting){
			isFirstTimer = true;
			timer.stop();
			timer.reset();
		}
			
		if (timer.get() > ShooterConfig.waitTimeStop && doneShooting){
			solOne.set(DoubleSolenoid.Value.kForward);
			stopping = true;
			timerOne.stop();
			timerOne.reset();
			speedLeft = 0;
			speedRight = 0;
			shootSpeed = 0;
			isShooting = false;
			doneShooting = false;
			isFirst = true;
			isFirstTimer = true;
			vision.resetTurnPID();
		}
	}
	
	/**
	 * Starts the shooting process at a speed from vision
	 */
	public void shoot() {
		resettingShot = false;
		doneShooting = false;
		resetTimer.stop();
		resetTimer.reset();
		resetTimer.start();
		timerOne.stop();
		timerOne.reset();
		intake.arm.setPos(0);
		drive.toLowGear();
		isShooting = true;
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
		isFirstMotors = true;
		isFirstTimer = true;
		isFirst = true;
		wantAng = robotCore.navX.getAngle() + ((180/Math.PI)*Math.atan(vision.vs.getRotation(wantGoal)/(vision.vs.getDistance(wantGoal)*1.32)));
		if(wantAng-robotCore.navX.getAngle() >= 0) {
			adjustWithLeft = true;
		}
		else if(wantAng-robotCore.navX.getAngle() < 0) {
			adjustWithLeft = false;
		}
		
		if(usingVision) {
			setSpeed();
			vision.startTurnPID();
		}
		else {
			setSpeed();
		}
	}
	
	/**
	 * Stops the shooting process
	 */
	public void cancelShot() {
		isShooting = false;
		isFirst = true;
		isFirstMotors = true;
		isFirstTimer = true;
		
		shootSpeed = 0;
		stopping = true;
		vision.resetTurnPID();
	}
	
	/**
	 * Enables or disable vision use within shooter
	 * @param visionUse
	 */
	public void setVisionUse(boolean visionUse) {
		usingVision = visionUse;
	}
	
	public void stopShooter() {
		shootSpeed = 0;
		stopping = true;
	}
	
	public void setRawSpeed(double speed) {
		leftMotor.set(speed);
		rightMotor.set(speed);
		shootSpeed = speed;
		stopping = false;
		leftPID.reset();
		rightPID.reset();
		leftPID.start();
		rightPID.start();
		isFirstTimer = true;
		isFirst = true;
	}
	
	/**
	 * Sets the speed of the shooting motors
	 * @param speed
	 */
	public void setSpeed(double speed) {
		shootSpeed = speed;
		stopping = false;
		speedLeft = shootSpeed * ShooterConfig.startSpeedScalar;
		speedRight = shootSpeed * ShooterConfig.startSpeedScalar;
	}
	
	/**
	 * Starts the motors at a speed determined from vision
	 */
	public void setSpeed() {
		stopping = false;
		shootSpeed = dash.getSpeed();
		wantSpeedLeft = 0;
		wantSpeedRight = 0;
	}

	/**
	 * Actuates a solenoid to launch the ball
	 */
	public void launchBall() {
		if(solOne.get() == DoubleSolenoid.Value.kReverse)
			solOne.set(DoubleSolenoid.Value.kForward);
		else if(solOne.get() == DoubleSolenoid.Value.kForward)
			solOne.set(DoubleSolenoid.Value.kReverse);
	}
	
	/**
	 * Checks if the motors are at a specific speed
	 * @param motorSpeed
	 * @return
	 */
	public boolean isMotorsFastEnough(double motorSpeed){
//		System.out.println("leftRate : "  + leftMotorEnc.getRate() + "\trightRate : " + rightMotorEnc.getDistance() + "\twantSpeed : " + motorSpeed);
		return Util.withinThreshold(Math.abs(leftRate), motorSpeed, ShooterConfig.motorSpeedTolerance) && Util.withinThreshold(Math.abs(rightRate), motorSpeed, ShooterConfig.motorSpeedTolerance);
//		return leftMotorEnc.getRate() > motorSpeed && rightMotorEnc.getRate() > motorSpeed;
//		return true;
	}
	
	/**
	 * returns velocity based off of distance from vision
	 * @param distance distance from 1-100
	 */
	public double getVelocityDistance(double distance){
		return 0.0001*distance*distance*distance - 0.0417*distance*distance + 6.7519*distance - 32.222;

	}
	
	public boolean getState() {
		return isShooting;
	}
	
	public void updateLeftRate() {
		if(isFirstLeft) {
			encoderLeftDistance = leftMotorEnc.getDistance();
			leftTimer.start();
			isFirstLeft = false;
		}
		if(leftTimer.get() > 0.1) {
			isFirstLeft = true;
			leftRate = (leftMotorEnc.getDistance()-encoderLeftDistance)/leftTimer.get();
			leftTimer.stop();
			leftTimer.reset();
		}
	}
	
	public void updateRightRate() {
		if(isFirstRight) {
			encoderRightDistance = rightMotorEnc.getDistance();
			rightTimer.start();
			isFirstRight = false;
		}
		if(rightTimer.get() > 0.1) {
			isFirstRight = true;
			rightRate = -(rightMotorEnc.getDistance()-encoderRightDistance)/rightTimer.get();
			rightTimer.stop();
			rightTimer.reset();
		}
	}
	
	public void updateRightRateFilter() {
		if(isFirstRight) {
			encoderRightDistance = rightMotorEnc.getDistance();
			rightTimer.start();
			isFirstRight = false;
		}
		
		if(rightTimer.get() > 0.015) {
			isFirstRight = true;
			rightRate = -(rightMotorEnc.getDistance()-encoderRightDistance)/rightTimer.get();
			rightTimer.stop();
			rightTimer.reset();
			movingAverageStepRight++;
		}
		
		switch (movingAverageStepRight) {
			case 0:
				rightRateValues[0] = rightRate;	
				break;
	
			case 1:
				rightRateValues[1] = rightRate;
				break;
				
			case 2:
				rightRateValues[2] = rightRate;
				break;
				
			case 3:
				rightRateValues[3] = rightRate;
				break;
				
			case 4:
				rightRateValues[4] = rightRate;
				break;
				
			default:
				break;
		}
		
		rightRateFiltered = 0;
		for(int i = 0; i < rightRateValues.length; i++) {
			rightRateFiltered+=rightRateValues[i];
		}
		rightRateFiltered/=rightRateValues.length;
		if(movingAverageStepRight == 5) {
			movingAverageStepRight = 0;
		}
	}
	
	public void updateLeftRateFilter() {
		if(isFirstLeft) {
			encoderLeftDistance = leftMotorEnc.getDistance();
			leftTimer.start();
			isFirstLeft = false;
		}
		if(leftTimer.get() > 0.015) {
			isFirstLeft = true;
			leftRate = -(leftMotorEnc.getDistance()-encoderLeftDistance)/leftTimer.get();
			leftTimer.stop();
			leftTimer.reset();
			movingAverageStepLeft++;
		}
		
		switch (movingAverageStepLeft) {
			case 0:
				leftRateValues[0] = leftRate;	
				break;
	
			case 1:
				leftRateValues[1] = leftRate;
				break;
				
			case 2:
				leftRateValues[2] = leftRate;
				break;
				
			case 3:
				leftRateValues[3] = leftRate;
				break;
				
			case 4:
				leftRateValues[4] = leftRate;
				break;
				
			default:
				break;
		}
		
		leftRateFiltered = 0;
		for(int i = 0; i < leftRateValues.length; i++) {
			leftRateFiltered+=leftRateValues[i];
		}
		leftRateFiltered/=leftRateValues.length;
		if(movingAverageStepLeft == 5) {
			movingAverageStepLeft = 0;
		}
	}
	
	public void changeShooterSpeed(double changeVal) {
		dash.putDouble("shooterSpeed", (dash.getSpeed()+changeVal));
	}
}
