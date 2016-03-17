
package core;

import sensors.SharpIR;
import util.Dashboard;
import vision.VisionCore;
import auto.Auto;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotCore robotCore = new RobotCore();
	
	Drive drive = new Drive(robotCore); 
	
	VisionCore vision = new VisionCore(robotCore);
	Dashboard dashboard = new Dashboard(vision);
	IntakeArm arm = new IntakeArm(robotCore, dashboard);
	IntakeRoller roller = new IntakeRoller(arm, robotCore.sharp);
	Intake intake = new Intake(arm, roller);
	Climber climber = new Climber(robotCore);
	Shooter shooter = new Shooter(robotCore, drive, vision, dashboard);
	Teleop teleop = new Teleop(robotCore, drive, intake, shooter, climber, dashboard, vision);
	Test test = new Test(robotCore, drive, intake, shooter, climber, dashboard, vision);
	Auto auto = new Auto(robotCore, drive, intake, shooter, dashboard, vision);
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	try {
    		vision.socket.visionSocket.close();  
    	} catch (Exception e) {

    	}
    	vision.socket.initSockets();
    }

    public void autonomousInit() {
    	auto.interpreter.interpInit();
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	auto.run();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        teleop.run();
    }
    
    /**
     * This function is cSalled periodically during test mode
     */
    public void testPeriodic() {
    	
    }
    
    public void disabledInit() {
    	
    }
}
