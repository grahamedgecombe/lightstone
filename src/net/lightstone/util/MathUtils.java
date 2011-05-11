package net.lightstone.util;

import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * A class that has common math functions.
 * 
 * @author Joe Pritzel
 * 
 */
public class MathUtils {

	/**
	 * Calculates the distance between the points
	 * 
	 * @param x
	 * @param z
	 * @param y
	 * @param x1
	 * @param z1
	 * @param y1
	 * @return
	 */
	public static final double distance(double x, double z, double y,
			double x1, double z1, double y1) {
		return pow(pow(x - x1, 2) + pow(z - z1, 2) + pow(y - y1, 2), .5);
	}

	/**
	 * Calculates the distance between the points
	 * 
	 * @param x
	 * @param z
	 * @param x1
	 * @param z1
	 * @return
	 */
	public static final double distance(double x, double z, double x1, double z1) {
		return pow(pow(x - x1, 2) + pow(z - z1, 2), .5);
	}

	/**
	 * Calculates the distance between the points
	 * 
	 * @param x
	 * @param z
	 * @param y
	 * @param x1
	 * @param z1
	 * @param y1
	 * @return
	 */
	public static final int distance(int x, int z, int y, int x1, int z1, int y1) {
		return (int) round(pow(
				pow(x - x1, 2) + pow(z - z1, 2) + pow(y - y1, 2), .5));
	}

	/**
	 * Calculates the distance between the points
	 * 
	 * @param x
	 * @param z
	 * @param x1
	 * @param z1
	 * @return
	 */
	public static final int distance(int x, int z, int x1, int z1) {
		return (int) round(pow(pow(x - x1, 2) + pow(z - z1, 2), .5));
	}
}
