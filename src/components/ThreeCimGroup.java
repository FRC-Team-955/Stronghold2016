package components;

/**
 * Wraps set of three motors that make up one side of the robot
 * @author Trevor
 *
 */
public class ThreeCimGroup {

	public Cim c1;
	public Cim c2;
	public Cim c3;
	boolean m1IsReverse;
	boolean m2IsReverse;
	boolean m3IsReverse;
	
	/**
	 * Creates grouping given three motors
	 * @param m1Chn
	 * @param m2Chn
	 * @param m3Chn
	 */
	public ThreeCimGroup(int m1Chn, int m2Chn, int m3Chn, boolean m1IsFlipped, boolean m2IsFlipped ,boolean m3IsFlipped){
		c1 = new Cim(m1Chn, m1IsFlipped);
		c2 = new Cim(m2Chn, m2IsFlipped);
		c3 = new Cim(m3Chn, m3IsFlipped);
	}
	
	/**
	 * Sets all motors to given speed
	 * @param velocity between -1 to 1
	 */
	public void set(double velocity) {
		c1.ramp(velocity);
		c2.ramp(velocity);
		c3.ramp(velocity);
	}
}
