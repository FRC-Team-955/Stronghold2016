package core;

import config.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import sensors.*;

/**
 * Contains various sensors such as joysticks and accelerometers
 * 
 * @author Trevor
 *
 */
public class RobotCore {
	public MyJoystick joy = new MyJoystick(JoyConfig.drivePort);
	public Enc driveEncRight = new Enc(DriveConfig.chnAEncRight,DriveConfig.chnBEncRight,DriveConfig.encRightDisPerPulse);
	public Enc driveEncLeft = new Enc(DriveConfig.chnAEncLeft,DriveConfig.chnBEncLeft,DriveConfig.encLeftDisPerPulse);
	public NavX navX = new NavX();
	public Encoder armEnc = new Encoder(IntakeArmConfig.armEncChnA, IntakeArmConfig.armEncChnB);
	public SharpIR sharp = new SharpIR();
	public Encoder shooterLeftEnc = new Encoder(ShooterConfig.ChnALeftEnc, ShooterConfig.ChnBLeftEnc);
	public Encoder shooterRightEnc = new Encoder(ShooterConfig.ChnARightEnc, ShooterConfig.ChnBRightEnc);
	public DoubleSolenoid shooterSol = new DoubleSolenoid(ShooterConfig.ChnSolA, ShooterConfig.ChnSolB);
	public DoubleSolenoid ballHolder = new DoubleSolenoid(ShooterConfig.ballHolderChnA, ShooterConfig.ballHolderChnB);
	public PowerDistributionPanel pdp = new PowerDistributionPanel();
//	public Encoder climberEnc = new Encoder(ClimberConfig.ChnAEnc,ClimberConfig.ChnBEnc);
//	public Encoder climberEncCIM = new Encoder(ClimberConfig.ChnAEncCIM, ClimberConfig.ChnBEncCIM);
	
	public RobotCore(){
		
		shooterLeftEnc.setDistancePerPulse(ShooterConfig.distancePerPulseLeft);
		shooterRightEnc.setDistancePerPulse(ShooterConfig.distancePerPulseRight);

		shooterLeftEnc.reset();
		shooterRightEnc.reset();
		
		driveEncRight.setDistancePerPulse(DriveConfig.encRightDisPerPulse);	
		driveEncLeft.setDistancePerPulse(DriveConfig.encLeftDisPerPulse);
		
		driveEncRight.reset();
		driveEncLeft.reset();

	}

}