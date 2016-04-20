package config;

public class ShooterConfig {

	public static final int ChnALeftEnc = 7; //7
	public static final int ChnBLeftEnc = 6; //6
	public static final int ChnBRightEnc = 2;
	public static final int ChnARightEnc = 3;
	public static final int ChnSolA = 0;
	public static final int ChnSolB = 1;
	public static final double distancePerPulseRight = -0.000048; //0.000048
	public static final double distancePerPulseLeft = 0.000048; //53
	public static final int ChnMotorOne = 9;
	public static final int ChnMotorTwo = 8;
	public static final double waitTime = 0.5;
	public static final double waitTimeStop = 0.5;
	
	public static final double kPLeft = 0.005; //0.018
	public static final double kILeft = 0.0;
	public static final double kDLeft = 0.0;
	
	public static final double kPRight = 0.1;  //0.018
	public static final double kIRight = 0.03;
	public static final double kDRight = 0.022;

	public static final double kPDrive = 0.062;
	public static final double kIDrive= 0;
	public static final double kDDrive = 0;

	public static final double kPDriveClose = 0.07;
	public static final double kIDriveClose = 0;
	public static final double kDDriveClose = 0;
		
	public static final double rotationTolerance = 7;
	public static final double velocitySettleTime = 0;
	public static final double distanceSpeedConstant = .009;
	
	public static final double constantSpeed = 0.51;
	public static final double motorSpeedTolerance = .006;
	
	public static final int maxDistanceVision = 100;
	
	public static final double rotateMaxSpeed = 0.55;
	public static final double rotateMinSpeed = 0.3;
	public static final double rotateMaxSpeedClose = 0.35;
	public static final double rotateMinSpeedClose = 0.13;
	public static final double startSpeedScalar = 0.3;
	
	public static final double changePIDAng = 3;
	public static final double angleTolerance = 0.254;
	
	public static final int movingAverageNumbers = 5;
	
}
