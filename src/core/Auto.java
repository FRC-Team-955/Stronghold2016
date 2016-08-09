
package core;

import edu.wpi.first.wpilibj.Timer;
import util.*;

public class Auto {
	RobotCore robotcore;
	Drive drive;
	Interpreter interpreter;
	Timer tm = new Timer();
	
	private boolean isRunning = false;	//Whether the timer is running or not
	private int counter = 0;
	
	public Auto(RobotCore robotCore, Drive drive, Interpreter interpreter){
		this.robotcore = robotCore;
		this.drive = drive;
		this.interpreter = interpreter;
	}
	
	/**
	 * Runs through autonomous actions
	 */
	public void run(){
		interpreter.dispatch();
	}
}


