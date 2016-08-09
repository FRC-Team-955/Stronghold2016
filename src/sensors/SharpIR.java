package sensors;

import config.IntakeArmConfig;
import config.IntakeRollerConfig;
import edu.wpi.first.wpilibj.AnalogInput;
import config.SharpConfig;

/**
 * SharpIR distance sensor
 * @author Trevor
 *
 */
public class SharpIR
{
	private AnalogInput sharpIR = new AnalogInput(SharpConfig.sharpIRPort);
	
	/**
	 * Tells if there is an object in the intake
	 * @return
	 */
	public boolean isBallInIntake(){	
		return (sharpIR.getVoltage() > IntakeArmConfig.getVoltageThreshold);
	}
}
