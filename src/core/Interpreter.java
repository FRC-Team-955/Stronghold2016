package core;
import edu.wpi.first.wpilibj.Timer;
import util.*;

public class Interpreter
{
	private int autoStep = 0;
	private double[][] commands;
	private Drive drive;
	private Timer timer = new Timer();
	private boolean isFirst = true;
	
	public Interpreter(Drive drive){
		this.drive = drive;
	}
	
	public void interpInit() {
		commands = Parser.parse();
	}
	
	public void next(){
		isFirst = true;
		autoStep++;
	}
	
	private void wait(double wantValue){
		if(isFirst)
			timer.start();
		
		if(timer.get() >= wantValue) {
			timer.stop();
			timer.reset();
			next();
		}
	}
	
	public void dispatch(){
		
		if((commands[autoStep][0]) == -1) {	//Dead line
			drive.move(0, 0);
			System.out.println("Dead line at " + autoStep);
		}	
		else if ((commands[autoStep][0]) == Steps.getStep(Type.DRIVE)){	//Drive
			drive.move(commands[autoStep][1],commands[autoStep][2]);
			next();
		}
		else if ((commands[autoStep][0]) == Steps.getStep(Type.WAIT)) {	//Wait
			wait(commands[autoStep][1]);
		}
		
		isFirst = false;
	}
}
