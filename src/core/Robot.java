
package core;

import util.Dashboard;
import vision.VisionCore;

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
	
	VisionCore vision = new VisionCore(robotCore);
	Dashboard dashboard = new Dashboard(vision, robotCore);
	Drive drive = new Drive(robotCore, dashboard); 
	
	VisionDriving visionDriving = new VisionDriving(vision, drive, robotCore);
	Teleop teleop = new Teleop(robotCore, drive, dashboard, vision, visionDriving);
	int value = 0;
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    }

    public void autonomousInit() {

    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        teleop.run();
    }
    
    public void teleopInit() {
//    	vision.socket.connectServer();
//    	vision.initThread();
    	teleop.init();
    }
    
    /**
     * This function is cSalled periodically during test mode
     */
    public void testPeriodic() {

    }
}
