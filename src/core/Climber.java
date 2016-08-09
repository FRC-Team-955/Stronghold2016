package core;

import util.PID;
import util.Util;
import components.CIM;
import config.ClimberConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/**
 * Components that control the climber mechanism
 * @author Trevor
 *
 */
public class Climber {
//	CIM climberCIM = new CIM(ClimberConfig.chnClimberCIM, false);
//	CIM climberDeliveryMotor = new CIM(ClimberConfig.chnClimberCIM, false);
//	PID deliveryPID = new PID(ClimberConfig.climberP, ClimberConfig.climberI, ClimberConfig.climberD);
//
//	Encoder climberEnc;
//	Encoder climberEncCIM;
//	
//	boolean isClimb = false;
//	int step = 0;
//	double wantSpeed = 0;

	/**
	 * 
	 * @param core
	 */
	public Climber(RobotCore core) {
//		this.climberEnc = core.climberEnc;
//		this.climberEnc.setDistancePerPulse(ClimberConfig.climberEncDistancePerPulse);
//
//		this.climberEncCIM = core.climberEncCIM;
//		this.climberEncCIM.setDistancePerPulse(ClimberConfig.climberEncCIMDistancePerPulse);
	}

	/**
	 * Run periodically to run through actions for climbing
	 */
	public void update() {
//		switch (step) {
//			case 0:
//				if (isClimb) {
//					deliveryPID.update(climberEnc.getDistance(), ClimberConfig.wantDistance);
//					climberDeliveryMotor.ramp(deliveryPID.getOutput());
//				}
//				break;
//	
//			case 1:
//				if (isClimb) {
//					climberCIM.ramp(ClimberConfig.downSpeed);
//			
//					if (climberEncCIM.getDistance() > ClimberConfig.wantHeight) {
//						climberCIM.set(0);
//						step = 0;
//						isClimb = false;
//					}
//				}
//				break;
//				
//			default:
//				break;
//		}
	}

	/**
	 * Starts the climbing sequence
	 */
	public void climb() {
//		step = 0;
//		isClimb = true;
//		climberEnc.reset();
	}
	
	public void advanceStep() {
//		step++;
	}
}
