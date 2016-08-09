package components;

import edu.wpi.first.wpilibj.Talon;
import config.CimConfig;

/**
 * Basic motor controller for regular talon
 * @author Trevor
 *
 */
public class Cim extends Talon {
	boolean isFlipped;
	
	/**
	 * 
	 * @param channel
	 */
	public Cim (int channel, boolean isFlipped) {
		super(channel);
		this.isFlipped = isFlipped;
	}
	
	public void set(double speed){
		if(isFlipped)
			super.set(-speed);
		
		else
			super.set(speed);
			
	}
	
	public double get(){
		
		if(isFlipped){
			return (-super.get());
		}
		
		return super.get();
	}
	
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
