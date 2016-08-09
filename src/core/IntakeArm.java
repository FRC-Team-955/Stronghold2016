package core;

import util.*;
import edu.wpi.first.wpilibj.Encoder;
import components.CIM;
import config.IntakeArmConfig;

/**
 * Controls the pickup arm
 * @author Trevor
 *
 */
public class IntakeArm {
	private PID pidUp = new PID(IntakeArmConfig.kPUp, IntakeArmConfig.kIUp, IntakeArmConfig.kDUp);
	private PID pidDown = new PID(IntakeArmConfig.kPDown, IntakeArmConfig.kIDown, IntakeArmConfig.kDDown);
	private PID pidPortcullis = new PID(1,0,0);
	private Encoder armEnc; 
	private double position [] = 
	{
		IntakeArmConfig.pickupPosition,
		IntakeArmConfig.homePosition,
		IntakeArmConfig.blockPosition
	};
	private CIM armMotor = new CIM(IntakeArmConfig.armMotorChn, IntakeArmConfig.armMotorFlipped);
	private int posIndex = 1;
	private double wantPos = position[posIndex];
	private double currPos = 0;
	private double pidOutput = 0;
	private boolean portcullis = false;
	private Dashboard dash;
	private double encoderOffset = 0;
	
	
	/**
	 * 
	 * @param core
	 */
	public IntakeArm( RobotCore core, Dashboard dash) {
		
		this.dash = dash;
		armEnc = core.armEnc;
		armEnc.setDistancePerPulse(IntakeArmConfig.armEncDistPulse);
		armEnc.reset();
		currPos = armEnc.getDistance();
	}
	
	/**
	 * Sets position to an index in the position array
	 * @param posIndex
	 */
	public void setPos(int posIndex) {
		wantPos = position[posIndex];
		this.posIndex = posIndex;
	}
	
	/**
	 * Sets the arm to a raw position
	 * @param pos
	 */
	public void setRawPos(double pos) {
		wantPos = pos;
	}
	
	/**
	 * Runs periodically to update PID and move arm to wantPos
	 */
	public void update() {
		dash.putDouble("armEnc", (armEnc.getDistance() - encoderOffset));
		currPos = (armEnc.getDistance() - encoderOffset);
		if(wantPos - currPos <= 0) {
			pidUp.update(currPos, wantPos);
			pidOutput = pidUp.getOutput();
		}
		else if(wantPos - currPos > 0) {
			pidDown.update(currPos, wantPos);
			pidOutput = pidDown.getOutput();
		}	

		setArmSpeed(Util.limit(pidOutput, IntakeArmConfig.minArmSpeed, IntakeArmConfig.maxArmSpeed));
		
	}
	
	/**
	 * Sets the speed of the arm motor
	 * @param speed
	 */
	public void setArmSpeed(double speed) {
		armMotor.set(speed);
	}
	
	/**
	 * 
	 * @return Current position of arm 
	 */
	public double getPos() {
		return currPos;
	}
	
	/**
	 * 
	 * @return Wanted position of arm
	 */
	public double getWantPos() {
		return wantPos;
	}
	
	/**
	 * Toggles the arm between up and down position
	 */
	public void togglePos() {
		if(posIndex == 0) {
			posIndex = 1;
		}
		else if(posIndex == 1) {
			posIndex = 0;
		}
		
		else if(posIndex == 2) {
			posIndex = 0;
		}
		
		wantPos = position[posIndex];
	}
	
	public void setPortcullisMode(boolean mode) {
		portcullis = mode;
	}
	
	public void resetArmEnc() {
		encoderOffset = armEnc.getDistance() - 10.5;
	}
	
	
}
