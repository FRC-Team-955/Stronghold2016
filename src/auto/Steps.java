package auto;

/**
 * Collection of ids for auto steps
 * @author Trevor
 *
 */
public class Steps {

	/**
	 * Gives the id of auto type 
	 * @param type
	 * @return
	 */
	public static int getStep(Type type) {
		
		int i;
		
		switch (type) {
		case DRIVE:
			i = 0;
			break;
		case WAIT_TIMER:
			i=1;
			break;
		case WAIT_GYRO:
			i=2;
			break;
		case TURN:
			i=3;
			break;
		case INTAKE:
			i=4;
			break;
		case WAIT_ENCODER:
			i=5;
			break;
		case SHOOT:
			i=6;
			break;
		case SPIN_GOAL:
			i=7;
			break;
		case END:
			i=8;
			break;
		default:
			i = -1; 
			break;
		}
		return i;
	}
	
}