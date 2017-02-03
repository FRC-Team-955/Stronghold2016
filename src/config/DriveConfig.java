package config;

/**
 * Drive constants
 * @author Trevor
 *
 */
public class DriveConfig {
	// Channels for left talons
	public static final int leftC1Chn = 3;
	public static final int leftC2Chn = 4;
	
	// Channels for right talons
	public static final int rightC1Chn = 1;
	public static final int rightC2Chn = 2;
	
	public static final boolean leftC1IsFliped = true;
	public static final boolean leftC2IsFlipped = true;
	
	public static final boolean rightC1IsFlipped = false;
	public static final boolean rightC2IsFlipped = false;
	
	
	// Right Encoder
	public static final int chnAEncRight = 0;
	public static final int chnBEncRight = 1;
	
	public static final double encRightDisPerPulse = -0.01138;
	
	// Left Encoder
	public static final int chnAEncLeft = 2; //4
	public static final int chnBEncLeft = 3; //5
	
	public static final double encLeftDisPerPulse = 0.01133;
	
	public static final double driveStraightOffset = 0.18;
	
	public static final double kPDrive = 0.062;
	public static final double kIDrive= 0;
	public static final double kDDrive = 0;
	
	public static final double angChangeThreshold = 300;
	public static final double notMovingThreshold = 0.3;
	public static final double turnNextTime = 0.5;
	
	public static final double kP = 0.1;
	public static final double kI = 0;
	public static final double kD = 0;
}
