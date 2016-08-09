
package auto;

import core.Drive;
import core.Intake;
import core.RobotCore;
import core.Shooter;
import edu.wpi.first.wpilibj.Timer;
import util.Dashboard;
import vision.VisionCore;

/**
 * Contains all code required for auto
 * @author Trevor
 *
 */
public class Auto {
	public Interpreter interpreter;
	
	private Shooter shooter;
	/**
	 * 
	 * @param robotCore
	 * @param drive
	 * @param intake
	 * @param shooter
	 * @param dashboard
	 */
	public Auto(RobotCore robotCore, Drive drive, Intake intake, Shooter shooter, Dashboard dashboard, VisionCore vision){
		interpreter = new Interpreter(drive, robotCore, intake, dashboard, shooter, vision);
		this.shooter = shooter;
	}
	
	/**
	 * Periodically runs through autonomous actions
	 */
	public void run(){
		interpreter.update();
	}
	
	public void init() {
		interpreter.init();
		shooter.clampBall();
	}
}


