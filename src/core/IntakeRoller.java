package core;

import sensors.SharpIR;
import util.*;
import components.CIM;
import config.IntakeRollerConfig;
import edu.wpi.first.wpilibj.Timer;

/**
 * Controlls the roller portion of the intake
 * @author Trevor
 *
 */
public class IntakeRoller {
	private CIM intakeCim = new CIM(IntakeRollerConfig.intakeMotorChn, IntakeRollerConfig.intakeCimFlip);
	private double speed;
	private IntakeArm arm;
	public SharpIR sharp;
	boolean isFirst = true;
	Timer timer = new Timer();

	/**
	 * 
	 * @param arm
	 * @param sharp
	 */
	public IntakeRoller(IntakeArm arm, SharpIR sharp) {
		this.arm = arm;
		speed = 0;
		this.sharp = sharp;
	}
	
	/**
	 * Starts the roller intake process with a constant speed
	 */
	public void runIntakeRoller() { 
		speed = IntakeRollerConfig.intakeSpeed;
		isFirst = false;
	}
	
	/**
	 * Sets the roller motor to a raw speed
	 * @param speed 
	 */
	public void setRawSpeed(double speed) {
		intakeCim.set(speed);
		this.speed = speed;
	}
	
	/**
	 * Starts the roller intake process with the given speed
	 * @param speed
	 */
	public void setSpeed (double speed) {
		this.speed = speed;
		isFirst = false;
	}
	
	/**
	 * Run periodically to control roller intake process
	 */
	public void update() {
		if(isFirst) {
			timer.reset();
			timer.start();
		}
		
		intakeCim.set(speed);
		
		if(getSpeed() == 0) {
			speed = 0;
			isFirst = true;
			timer.stop();
			timer.reset();
		}
		
//		System.out.print("Timer: " + timer.get() + "\t");
	}
	
	/**
	 * Returns the speed that the roller is set to
	 * @return
	 */
	public double getSpeed() {
		return speed;
	}
}