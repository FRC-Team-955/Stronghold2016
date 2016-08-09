package util;

/**
 * Various utility functions
 * @author Trevor
 *
 */
public class Util {

	/**
	 * Limits a number between a min and max
	 * @param num
	 * @param min
	 * @param max
	 * @return
	 */
	public static double limit(double num, double min, double max) {
		if(num < min)
			return min;
		
		if(num > max)
			return max;
		
		return num;
	}
	
	/**
	 * Checks if a number is between a min and max 
	 * @param num
	 * @param min
	 * @param max
	 * @return if num is between min and max
	 */
	public static boolean withinBounds(double num, double min, double max) {
		return (num < max && num > min);
	}
	
	/**
	 * Checks if the currNum is within the a specific threshold around wantNum 
	 * @param currNum
	 * @param wantNum
	 * @param threshold
	 * @return
	 */
	public static boolean withinThreshold(double currNum, double wantNum, double threshold) {
		return (currNum < (wantNum+threshold) && currNum > (wantNum-threshold));
	}
}
