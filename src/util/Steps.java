package util;

public class Steps {

	public static int getStep(Type type) {
		
		int i;
		
		switch (type) {
		case DRIVE:
			i = 0;
			break;
		case WAIT:
			i=1;
			break;
		default:
			i = -1; 
			break;
		}
		return i;
	}
	
}
