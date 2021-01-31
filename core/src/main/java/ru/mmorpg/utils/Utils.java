package ru.mmorpg.utils;

/**
 * @author finfan
 */
public class Utils {
	public static byte boolToInt(boolean value) {
		return (byte) (value ? 1 : 0);
	}

	public static int lim(Number a) {
		return (int) Math.min(Math.max(a.doubleValue(), Integer.MAX_VALUE), 0);
	}
}
