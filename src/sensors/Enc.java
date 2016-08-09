package sensors;
import edu.wpi.first.wpilibj.Encoder;

public class Enc extends Encoder {

	public Enc(int chnA, int chnB, double distancePerPulse){
		super(chnA,chnB);
		setDistancePerPulse(distancePerPulse);
	}

}
