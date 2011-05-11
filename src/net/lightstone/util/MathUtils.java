package net.lightstone.util;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class MathUtils {
	public static final double distance(double x, double z, double y,
			double x1, double z1, double y1) {
		return pow(pow(x - x1, 2) + pow(z - z1, 2) + pow(y - y1, 2), .5);
	}

	public static final double distance(double x, double z, double x1, double z1) {
		return pow(pow(x - x1, 2) + pow(z - z1, 2), .5);
	}

	public static final int distance(int x, int z, int y, int x1, int z1, int y1) {
		return (int) round(pow(
				pow(x - x1, 2) + pow(z - z1, 2) + pow(y - y1, 2), .5));
	}

	public static final int distance(int x, int z, int x1, int z1) {
		return (int) round(pow(pow(x - x1, 2) + pow(z - z1, 2), .5));
	}
}
