package sensors;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Streamlines encoder creation 
 * @author Trevor
 *
 */
public class Enc extends Encoder {

	/**
	 * 
	 * @param chnA
	 * @param chnB
	 * @param distancePerPulse
	 */
	public Enc(int chnA, int chnB, double distancePerPulse){
		super(chnA,chnB);
		setDistancePerPulse(distancePerPulse);
	}

}
