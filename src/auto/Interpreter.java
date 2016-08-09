package auto;
import edu.wpi.first.wpilibj.Timer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import config.*;
import core.Drive;
import core.Intake;
import core.RobotCore;
import core.Shooter;
import util.ChooserType;
import util.Dashboard;
import util.Util;
import vision.VisionCore;

/**
 * Interprets and runs scripts parsed for auto
 * @author Trevor
 *
 */
public class Interpreter {
	private int autoStep = 0;
	private double[][] commandsOne;
	private double[][] commandsTwo;
	private Drive drive;
	private Intake intake;
	private Timer timer = new Timer();
	private RobotCore robotCore;
	private boolean isFirst = true;
	private boolean isFirstTimer = true;
	private double prevAng = 0;
	private double angChange = 0;
	private String fileNameOne;
	private String fileNameTwo;
	private Dashboard dashboard;
	private Shooter shooter;
	private VisionCore vision;
	private boolean firstFile = true;
	private boolean secondFile = false;
	private int step = 0;
	private boolean isFirstSpin = true;
	private boolean isFirstShoot = true;
	private int wantGoal = 0;
	private double zeroAngle = 0;
	private Timer shootTimer = new Timer();
	
	/**
	 * 
	 * @param drive
	 * @param robotCore
	 * @param intake
	 * @param dashboard
	 * @param shooter
	 */
	public Interpreter(Drive drive, RobotCore robotCore, Intake intake, Dashboard dashboard, Shooter shooter, VisionCore vision){
		this.drive = drive;
		this.robotCore = robotCore;
		this.intake = intake;
		this.dashboard = dashboard;
		this.shooter = shooter;
		this.vision = vision;
	}
	
	/**
	 * Run at auto init to get and parse script file 
	 */
	public void init() {
		shooter.clampBall();
		zeroAngle = robotCore.navX.getRoll();
		shootTimer.start();
		fileNameOne = dashboard.getFileNameOne();
		commandsOne = Parser.parse(fileNameOne);
		step = 0;
		fileNameTwo = dashboard.getFileNameTwo();
		commandsTwo = Parser.parse(fileNameTwo);
	}
	
	/**
	 * Advances to next step in script
	 */
	public void next(){
		isFirst = true;
		isFirstSpin = true;
		isFirstShoot = true;
		autoStep++;
		isFirstTimer = true;
		timer.stop();
		timer.reset();
	}
	
	/**
	 * Waits for encoder to reach a certain value before advancing
	 * @param leftWant
	 * @param rightWant
	 */
	private void waitEncoder(double leftWant, double rightWant) {
		if(isFirst) {
			robotCore.driveEncLeft.reset();
			robotCore.driveEncRight.reset();
			isFirst = false;
		}
		
		if(Math.abs(robotCore.driveEncLeft.getDistance()) > leftWant && Math.abs(robotCore.driveEncRight.getDistance()) > rightWant)  {
			next();
		}
	}
	
	public void waitPitch() {
		if(Math.abs(robotCore.navX.getRoll()-zeroAngle) < InterpConfig.pitchThreshold) {
			if(isFirst) {
				timer.start();
				isFirst = false;
			}
			if(timer.get() > 0.2) {
				next();				
			}
		}
		
		else {
			timer.reset();
			isFirst = true;
		}
	}
	
	public void waitPitchThreshold(double threshold) {
		if(Math.abs(robotCore.navX.getRoll() - zeroAngle) < threshold) {
			next();
		}
		
	}
	
	/**
	 * Turns the robot to a specific angle before advancing to the next step
	 * @param velocity
	 * @param turnAng
	 */
	private void turn(double velocity, double turnAng) {
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
		drive.set(angVelocity, -angVelocity);
		
		if(isFirst){
			prevAng = robotCore.navX.getAngle();
			isFirst = false;
			prevAng = currAng;
			angChange = 0;
		}
		
		if (Math.abs(prevAng - currAng) > InterpConfig.angChangeThreshold){
			if(prevAng > 0)
				angChange += ((currAng - prevAng) + 360);	
			else
				angChange += -((currAng - prevAng) - 360);	
		}
		
		else {
			angChange += (currAng - prevAng);	
		}
		
		if(Math.abs(angVelocity) < InterpConfig.notMovingThreshold) {
			if(isFirstTimer) {
				timer.start();
				isFirstTimer = false;
			}
			
			if(timer.get() > InterpConfig.turnNextTime) {
				if(Math.abs(angVelocity) < InterpConfig.notMovingThreshold){
					next();
				}
				timer.reset();
				timer.stop();
			}
		}
		
		prevAng = currAng;
	}
	
	private void turnStep(double velocity, double turnAng) {
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
		drive.set(angVelocity, -angVelocity);
		
		if(isFirst){
			prevAng = robotCore.navX.getAngle();
			isFirst = false;
			prevAng = currAng;
			angChange = 0;
		}
		
		if (Math.abs(prevAng - currAng) > InterpConfig.angChangeThreshold){
			if(prevAng > 0)
				angChange += ((currAng - prevAng) + 360);	
			else
				angChange += -((currAng - prevAng) - 360);	
		}
		
		else {
			angChange += (currAng - prevAng);	
		}
		
		if(Math.abs(angVelocity) < InterpConfig.notMovingThreshold) {
			if(isFirstTimer) {
				timer.start();
				isFirstTimer = false;
			}
			
			if(timer.get() > InterpConfig.turnNextTime) {
				if(Math.abs(angVelocity) < InterpConfig.notMovingThreshold){
					step++;
				}
				timer.reset();
				timer.stop();
			}
		}
		
		prevAng = currAng;
	}
	
	/**
	 * Waits for a specified amount of time
	 * @param wantValue time to wait in seconds
	 */
	private void waitTimer(double wantValue){
		if(isFirst){
			timer.start();
			isFirst = false;
		}
		
		if(timer.get() >= wantValue) {
			timer.stop();
			timer.reset();
			next();
		}
	}
	
	public void resetAuto() {
		step = 0;
	}
	
	private void spinToGoal() {	
		double startAng =  0;
		double translate = vision.vs.getTranslation(wantGoal);
		double encLeftCount = 0;
		double secondTurnAng;
		
		if(isFirstSpin) {
			startAng = robotCore.navX.getAngle();
			isFirstSpin = false;
		}
		
		double rotation = vision.vs.getRotation(wantGoal);
		double turnAng;
		if(translate > 0) {
			turnAng = -(rotation + 90);
			secondTurnAng = 90;
		}
		
		else {
			turnAng = -(rotation - 90);
			secondTurnAng = -90;
		}
		
		switch (step) {
			case 0:
				turnStep(InterpConfig.turnSpeed, turnAng);
				encLeftCount = robotCore.driveEncLeft.getDistance();
				break;
	
			case 1:
				drive.setNoRamp(InterpConfig.driveSpeed, InterpConfig.driveSpeed);
				if((robotCore.driveEncLeft.getDistance() - encLeftCount) > translate) {
					drive.set(0, 0);
					step++;
				}
				break;
				
			case 2:
				turnStep(InterpConfig.turnSpeed, secondTurnAng);
				break;
			
			case 3:
				shooter.shoot();
				step++;
				break;
			
			default:
				break;
		}
		
	}
	/**
	 * Waits until the robot is at a specified angle
	 * @param ang
	 */
	private void waitGyro(double ang){

		double currAng = robotCore.navX.getAngle();
		
		if(isFirst){
			prevAng = robotCore.navX.getAngle();
			isFirst = false;
			prevAng = currAng;
			angChange = 0;
		}
		
		if (Math.abs(prevAng - currAng) > InterpConfig.angChangeThreshold){
			if(prevAng > 0)
				angChange += ((currAng - prevAng) + 360);	
			else
				angChange += -((currAng - prevAng) - 360);	
		}
		
		else {
			angChange += (currAng - prevAng);	
		}
		
		if((ang > 0 && angChange > ang) || ang > 0 && (angChange+360) < ang) {
			next();
		}
		
		else if (ang < 0 && angChange < ang || ang < 0 && (angChange-360) > ang) {
			next();
		}
		
		prevAng = currAng;
	}
	
	/**
	 * Run periodically to read through and execute the script
	 */
	public void update(){
		if(shootTimer.get() > 14) {
			shooter.launchBall();
			shooter.cancelShot();
			shootTimer.reset();
		}
		
		if(firstFile) {
			dispatcher(commandsOne);
		}
	
		else if(secondFile) {
			dispatcher(commandsTwo);
		}
		
        dashboard.update();
		intake.update();
		shooter.update();
		vision.update();
	}
	
	public void dispatcher(double[][] commands) {
		if((commands[autoStep][0]) == -1) {	//Dead line
			drive.move(0, 0);
			System.out.println("Dead line at " + autoStep);
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.DRIVE)){	//Drive
			drive.moveNoRamp(-commands[autoStep][1],Math.toRadians(commands[autoStep][2]));
			next();
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_TIMER)) {	//Wait for timer
			waitTimer(commands[autoStep][1]);
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_GYRO)) {	//Wait for gyro
			waitGyro(commands[autoStep][1]);
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.TURN)) {	//Turn
			turn(commands[autoStep][1],commands[autoStep][2]);
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.INTAKE)) {	//Intake
			if(isFirst) {
				intake.pickupBall();
				isFirst = false;
			}
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT_ENCODER)) {	//Wait for encoder
			waitEncoder(0,commands[autoStep][2]);
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.SHOOT)) {	//Shoot using vision
			if(vision.vs.goals[wantGoal].distance == 0) {
				if(isFirst) {
					timer.reset();
					timer.start();
					isFirst = false;
				}
				if(dashboard.getLeftRight()) {
					drive.setNoRamp(0, 0.65);
				}
				
				else if(!dashboard.getLeftRight()) {
					drive.setNoRamp(0.65, 0);
				}
			}
			
			else {
				drive.setNoRamp(0, 0);
				if(isFirstShoot) {
					timer.reset();
					timer.start();
					isFirstShoot = false;
				}
				
				if(timer.get() > 0.75) {
					shooter.shoot();						
					next();				
				}
			}

			if(timer.get() > 0.7) {
				drive.setNoRamp(0, 0);
				isFirst = true;
			}
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.MOVE_GOAL)) {	//Spin until goal is found
			spinToGoal();
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.ARM_TOGGLE)) {
			intake.arm.togglePos();
			next();
		}
		
		else if ((commands[autoStep][0]) == Steps.getStep(Type.END)) {
			firstFile = false;
			secondFile = true;
			autoStep = 0;
		}
		
		else if((commands[autoStep][0]) == Steps.getStep(Type.WAIT_PITCH)) {
			waitPitch();
		}
		
//		else if((commands[autoStep][0]) == Steps.getStep(Type.WAIT_PITCH_THRESHOLD)) {
//			waitPitchThreshold(commands[autoStep][1]);
//		}
	}
}
