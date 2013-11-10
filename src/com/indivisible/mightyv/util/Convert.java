package com.indivisible.mightyv.util;

/**
 * Convenience Class to house methods to safely convert between various Classes
 * @author indivisible
 */
public class Convert {

	// static TAGs for static classes
	private static final String TAG = "Convert";
	
	/**
	 * Convert a String to an int. Returns null on error.
	 * @param str String value to try to convert to an integer
	 * @return int conversion or null if failed
	 */
	public static final int stringToInt(String str)
	{ 
		Integer num = null;
		try
		{
			num = Integer.parseInt(str);
			return num;
		}
		catch (NumberFormatException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Failed to convert String to int: " +str);
		}
		return num;
		//TODO test that int return doesn't explode as a null
	}
}
