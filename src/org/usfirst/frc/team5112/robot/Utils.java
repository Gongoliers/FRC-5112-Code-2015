package org.usfirst.frc.team5112.robot;

/**
 * Contains the important constants for the competition
 * 
 * @author Kyle
 *
 */
public class Utils {

	// Xbox Buttons
	public static final int A_BUTTON = 1;
	public static final int B_BUTTON = 2;
	public static final int X_BUTTON = 3;
	public static final int Y_BUTTON = 4;
	public static final int LB = 5;
	public static final int RB = 6;
	public static final int BACK_BUTTON = 7;
	public static final int START_BUTTON = 8;
	public static final int LEFT_STICK_BUTTON = 9;
	public static final int RIGHT_STICK_BUTTON = 10;

	// Xbox Axes
	public static final int LEFT_STICK_X = 0;
	public static final int LEFT_STICK_Y = 1;
	public static final int LT = 2;
	public static final int RT = 3;
	public static final int RIGHT_STICK_X = 4;
	public static final int RIGHT_STICK_Y = 5;
	// Xbox D-pad
	public static final int DPAD_NORTH = 0;
	public static final int DPAD_NORTHEAST = 45;
	public static final int DPAD_EAST = 90;
	public static final int DPAD_SOUTHEAST = 135;
	public static final int DPAD_SOUTH = 180;
	public static final int DPAD_SOUTHWEST = 225;
	public static final int DPAD_WEST = 270;
	public static final int DPAD_NORTHWEST = 315;

	// Threshold for triggers
	public static final double TRIGGER_THRESHOLD = 0.1;

	public static double normalize(double x, double min, double max) {
		if (max == min) {
			return max;
		}
		return (x - min) / (max - min);
	}

}
