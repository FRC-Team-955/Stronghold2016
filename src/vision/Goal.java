package vision;

import org.w3c.dom.Element;

import config.VisionConfig;
import edu.wpi.first.wpilibj.Timer;

public class Goal {
	
	public double rotation = 0;
	public double distance = 0;
	public double translation = 0;
	public double area = 0;
	private boolean isGoal = false;
	private Timer timer = new Timer();
	private boolean isFirst = true;
	
	public Goal(){}
	
	public Goal(double[] values){
		translation = values[0];
		rotation = values[1];
		distance = values[2];
		area = values[3];
	}
	
	public Goal(Element n){
		try {
			translation = Integer.parseInt(n.getAttribute("translation"));
			rotation = Integer.parseInt(n.getAttribute("rotation"));
			distance = Integer.parseInt(n.getAttribute("distance"));
			area = Integer.parseInt(n.getAttribute("area"));	
		} catch(NumberFormatException e) {
			//Parsing empty string
		} catch(Exception e) {
			//Do something here
		}
	}
	

	public void update(Element n)	{
		if(Double.parseDouble(n.getAttribute("distance")) != 0) {
			isGoal = true;
		}
		
		else {
			isGoal = false;
		}
		
		if(!isGoal) {
			if(isFirst) {
				timer.start();
				isFirst = false;
			}
			if(timer.get() > VisionConfig.noGoalTime) {
				resetValues();
				timer.stop();
				timer.reset();
			}
		}
		
		else {
			try {
				translation = Double.parseDouble(n.getAttribute("translation"));
				rotation = Double.parseDouble(n.getAttribute("rotation"));
				distance = Double.parseDouble(n.getAttribute("distance"));
				area = Double.parseDouble(n.getAttribute("area"));
				isGoal = false;
				isFirst = true;
				timer.stop();
				timer.reset();
			} catch(Exception e) {
				//Do something here
			}
		}
	}
	
	public void update() {
		if(isFirst) {
			timer.start();
			isFirst = false;
		}
		
		if(timer.get() > VisionConfig.noGoalTime) {
			resetValues();
		}
	}
	
	public void update(double[] values) {
		if(values[2] !=0) {
			isGoal = true;
		}
		
		else {
			isGoal = false;
		}
		
		if(!isGoal) {
			if(isFirst) {
				timer.start();
				isFirst = false;
			}
			if(timer.get() > VisionConfig.noGoalTime) {
				resetValues();
				timer.stop();
				timer.reset();
			}
		}
		
		else {
			translation = values[0];
			rotation = values[1];
			distance = values[2];
			area = values[3];
			isGoal = false;
			isFirst = true;
			timer.stop();
			timer.reset();
		}
	}
	
	public void resetValues() {
		rotation = 0;
		translation = 0;
		distance = 0;
		area = 0;
	}

	@Override
	public String toString() {
		return "Goal [rotation=" + rotation + ", distance=" + distance + ", translation=" + translation + ", area="
				+ area + "]";
	}
}

