package config;

/**
 * Drive constants
 * @author Trevor
 *
 */
public class DriveConfig {
	// Channels for left talons
	public static final int leftC1Chn = 5;
	public static final int leftC2Chn = 6;
	
	// Channels for right talons
	public static final int rightC1Chn = 3;
	public static final int rightC2Chn = 4;
	
	public static final boolean leftC1IsFliped = false;
	public static final boolean leftC2IsFlipped = true;
	
	public static final boolean rightC1IsFlipped = true;
	public static final boolean rightC2IsFlipped = true;
	
	public static final int shiftSolPortA = 2;
	public static final int shiftSolPortB = 3;
	
	// Right Encoder
	public static final int chnAEncRight = 0;
	public static final int chnBEncRight = 1;
	
	public static final double encRightDisPerPulse = -0.01138;
	
	// Left Encoder
	public static final int chnAEncLeft = 4; //4
	public static final int chnBEncLeft = 5; //5
	
	public static final double encLeftDisPerPulse = 0.01133;
	
	public static final double driveStraightOffset = 0.18;
}
