package core;

import config.*;
import util.Dashboard;
import vision.VisionCore;

/**
 * Joins user input with core components
 * @author Trevor
 *
 */
public class Teleop {
	private RobotCore robotCore;
	private Drive drive;
	private Intake intake;
	private Shooter shooter;
	private Climber climber;
	private Dashboard dashboard;
	private VisionCore vision;
	
	/**
	 * Creates standard teleop object
	 * 
	 * @param robotCorxe
	 * @param drive
	 */
	public Teleop (RobotCore robotCore, Drive drive, Intake intake, Shooter shooter, Climber climber, Dashboard dashboard, VisionCore vision)
	{
		System.out.println("teleop init");
		this.robotCore = robotCore;
		this.drive = drive;
		this.intake = intake;
		this.shooter = shooter;
		this.dashboard = dashboard;
		this.vision = vision;
		
//		this.climber = climber;
	}
		
	/**
	 * Periodic functionality including drive
	 */
	public void run() {
		robotCore.joy.update();
        dashboard.update();
        vision.update();
		joyDrive();
		joyIntake();
		joyShooter();
//		joyClimber();
	}
	
	/**
	 * Runs drive code 
	 */
	private void joyDrive() {
		double[] rTheta = robotCore.joy.getRTheta();
		drive.move(rTheta[0], rTheta[1]);	

		if(robotCore.joy.getDpadUp()) {
			drive.setReverseMode(true);
		}
		
		if(robotCore.joy.getDpadDown()) {
			drive.setReverseMode(false);
		}
//		System.out.println(rTheta[0] + "\t" + (rTheta[1] * 180/Math.PI));
	}
	
	/**
	 * Runs intake code
	 */
	private void joyIntake() {
		intake.update();
//		System.out.println("hello");
		if(robotCore.joy.getButton(JoyConfig.intakeButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.pickupBall();
			shooter.releaseBall();
		}
		
		if(robotCore.joy.getButton(JoyConfig.cancelIntakeButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setSpeed(0);
		}
		
		if(robotCore.joy.getRawButton(JoyConfig.armUpButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.arm.setArmSpeed(-1);
		}
		
		if(robotCore.joy.getRawButton(JoyConfig.armDownButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.arm.setArmSpeed(1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.armStopButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.arm.setArmSpeed(0);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerInButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setSpeed(1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerOutButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setSpeed(-1);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerStopButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.setSpeed(0);
		}
		
		if(robotCore.joy.getButton(JoyConfig.rollerIntakeButton) && robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			intake.roller.runIntakeRoller();
		}
		
		if(robotCore.joy.getButton(JoyConfig.toggleArmPosButton)) {
			intake.arm.togglePos();
		}
		
		if(robotCore.joy.getDpadLeft()) {
			shooter.releaseBall();
		}
		
		if(robotCore.joy.getDpadRight()) {
			shooter.clampBall();
		}
		
		if(robotCore.joy.getButton(9)) {
			intake.arm.setPos(2);
		}
		
		if(robotCore.joy.getButton(10)) {
			intake.arm.resetArmEnc();
		}
	}
	
	/**
	 * Runs shooter code
	 */
	private void joyShooter() {
		shooter.update();
		
		if(robotCore.joy.getButton(JoyConfig.shootButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.shoot(); 
		}
		
		if(robotCore.joy.getButton(JoyConfig.cancelShotButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.cancelShot();
		}
		
		if(robotCore.joy.getButton(JoyConfig.shooterConstantSpeedButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.setSpeed(ShooterConfig.constantSpeed);
		}
		
		if(robotCore.joy.getButton(JoyConfig.setShooterSpeedButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.setSpeed();
		}
		
		if(robotCore.joy.getButton(JoyConfig.shooterStopButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.stopShooter();
		}
		
		if(robotCore.joy.getButton(JoyConfig.shooterLaunchButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.launchBall();
		}
		
		if(robotCore.joy.getButton(9) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.changeShooterSpeed(-0.01);
		}
		
		if(robotCore.joy.getButton(10) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
			shooter.changeShooterSpeed(0.01);
		}
		
//		if(robotCore.joy.getButton(JoyConfig.climbButton)) {
//			shooter.setVisionUse(false);
//		}
	}
	
	/**
	 * Runs climber code
	 */
	private void joyClimber() {
		climber.update();
		
//		if(robotCore.joy.getButton(JoyConfig.climbButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton))  {
//			climber.climb();
//		}
		
//		if(robotCore.joy.getButton(JoyConfig.climbStepButton) && !robotCore.joy.getRawButton(JoyConfig.manualModeButton)) {
//			climber.advanceStep();
//		}
	}
}
