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
	
	public RobotCore(){
		driveEncRight.setDistancePerPulse(DriveConfig.encRightDisPerPulse);	
		driveEncLeft.setDistancePerPulse(DriveConfig.encLeftDisPerPulse);
		
		driveEncRight.reset();
		driveEncLeft.reset();

	}

}