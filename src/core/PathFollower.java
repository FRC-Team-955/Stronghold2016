package core;

import java.util.TimerTask;

public class PathFollower extends TimerTask {

	int step;
	Drive drive;
	double[][] leftRightVelocity;
	
	public PathFollower(Drive drive, double[][] leftRightVelocity) {
		step = 0;
		this.drive = drive;
		this.leftRightVelocity = leftRightVelocity;
	}
	
	public int getStep() {
		return step;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{ 
			drive.setWantRate(leftRightVelocity[step][0], leftRightVelocity[step][1]);
			step++;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
