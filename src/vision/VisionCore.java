package vision;

import java.rmi.server.ServerCloneException;

import config.ShooterConfig;
import config.VisionConfig;
import core.RobotCore;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import util.PID;

public class VisionCore {
	private XMLParser xmlParser = new XMLParser();
//	public NetworkTable table;
	public VisionStruct vs = new VisionStruct();
//	private String emptyXML = "<?xml version=\"1.0\"?><vision frameNumber = \"0\"></vision>";
	public SocketCore socket = new SocketCore();
	
	PID turnPID = new PID(ShooterConfig.kPDrive, ShooterConfig.kIDrive, ShooterConfig.kDDrive);

	public VisionCore(RobotCore core) {	
		new Thread(socket).start();		
	}
	
	public void updateTurnPID(int wantGoal){
		turnPID.update(vs.getRotation(wantGoal), 0);
	}
	
	public void updateTurnPID(double currAng, double wantAng) {
		turnPID.update(currAng, wantAng);
	}
	
	public void changePIDConstants(double kP, double kI, double kD) {
		turnPID.updateConstants(kP, kI, kD);
	}
	
	public double getTurnPID(){
		return turnPID.getOutput();
	}
	
	public void startTurnPID(){
		turnPID.start();
	}
	
	public void resetTurnPID(){
		turnPID.stop();
		turnPID.reset();
	}
	
	public void update() {
		System.out.println(socket.getXML());
		try{
			vs = xmlParser.parseString(socket.getXML());
		} catch(Exception e) {
			
		}
	}
	
	public void initThread() {
		new Thread(socket).start();
	}
	
//	public void update() {
//    	try {
//    		vs = xmlParser.parseString(server.getString("value", emptyXML));
//    		System.out.println(table.getString("value", emptyXML));
//    	}
//    	catch (TableKeyNotDefinedException e) {
//    		
//    	}
//	}
	
}
