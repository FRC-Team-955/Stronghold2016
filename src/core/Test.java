package core;

import edu.wpi.first.wpilibj.Timer;

public class Test {
	
	private Drive drive;
	private Intake intake;
	private Shooter shooter;
	private int step = 0;
	private Timer timer = new Timer();
	
	public Test(Drive drive, Intake intake, Shooter shooter) {
		this.drive = drive;
		this.intake = intake;
		this.shooter = shooter;
		timer.start();
	}
	
	public void run() {
		intake.update();
		shooter.update();
		System.out.println(step);
		
		switch (step) {
		case 0:
			drive.leftCimGroup.c1.set(1);
			if(timer.get() > 1) {
				drive.leftCimGroup.c1.set(0);
				timer.reset();
				timer.start();
				step++;
			}
			break;

		case 1:
			drive.leftCimGroup.c2.set(1);
			if(timer.get() > 1) {
				drive.leftCimGroup.c2.set(0);
				timer.reset();
				timer.start();
				step++;
			}
			break;
			
		case 2:
			drive.rightCimGroup.c1.set(1);
			if(timer.get() > 1) {
				drive.rightCimGroup.c1.set(0);
				timer.reset();
				timer.start();
				step++;
			}
			break;
			
		case 3:
			drive.rightCimGroup.c2.set(1);
			if(timer.get() > 1) {
				drive.rightCimGroup.c2.set(0);
				timer.reset();
				timer.start();
				step++;
			}
			break;
			
		case 4:
			intake.roller.setSpeed(1);
			if(timer.get() > 1) {
				intake.roller.setSpeed(0);
				timer.reset();
				timer.start();
				step++;
			}
			break;
			
		case 5:
			intake.arm.togglePos();
			if(timer.get() > 2) {
				timer.reset();
				timer.start();
				step++;
			}
			
		case 6:
			intake.arm.togglePos();
			if(timer.get() > 2) {
				timer.reset();
				timer.start();
				step++;
			}
			
//		case 7:
//			shooter.leftMotor.set(0.5);
//			if(timer.get() > 1) {
//				shooter.leftMotor.set(0);
//				timer.reset();
//				timer.start();
//				step++;
//			}
//			
//		case 8:
//			shooter.rightMotor.set(0.5);
//			if(timer.get() > 1) {
//				shooter.rightMotor.set(0);
//				timer.reset();
//				timer.start();
//				step++;
//			}
			
		default:
			break;
		}
	}
}
