package core;

import config.*;
import sensors.*;

/**
 * Contains various sensors such as joysticks and accelerometers
 * 
 * @author Trevor
 *
 */
public class RobotCore {
	public MyJoystick joy = new MyJoystick(JoyConfig.drivePort);
	public MyJoystick intakeJoy = new MyJoystick(JoyConfig.intakePort);
	public Enc encRight = new Enc(EncConfig.chnAEncRight,EncConfig.chnBEncRight,EncConfig.encRightDisPerPulse);
	public Enc encLeft = new Enc(EncConfig.chnAEncLeft,EncConfig.chnBEncLeft,EncConfig.encLeftDisPerPulse);
}