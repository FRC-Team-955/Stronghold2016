package components;

import edu.wpi.first.wpilibj.Talon;
import config.CimConfig;

/**
 * Basic motor controller for regular talon
 * @author Trevor
 *
 */
public class CIM extends Talon {
	boolean isFlipped;
	
	/**
	 * 
	 * @param channel
	 * @param isFlipped
	 */
	public CIM (int channel, boolean isFlipped) {
		super(channel);
		this.isFlipped = isFlipped;
	}
	
	/**
	 * Sets the CIM to a specified speed, using isFlipped boolean
	 * @param speed
	 */
	public void set(double speed){
		if(isFlipped)
			super.set(-speed);
		
		else
			super.set(speed);
			
	}
	
	/**
	 * Returns the value the talon was set to
	 */
	public double get(){	
		if(isFlipped){
			return (-super.get());
		}
		
		return super.get();
	}
	
	/**
	 * Ramps the motors to the specified speed
	 * @param wantSpeed
	 */
	public void ramp(double wantSpeed){
		if(Math.abs(wantSpeed - get()) > CimConfig.rampRate){
			
			if(wantSpeed > get())
				set(get() +  CimConfig.rampRate);
			
			else
				set(get() - CimConfig.rampRate);
			
		}
		
		else {
			set(wantSpeed);
		}
	}
}
