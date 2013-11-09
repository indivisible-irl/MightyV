package com.indivisible.mightyv.util;

public class Convert {

	private static final String TAG = "Convert";
	
	/**
	 * Convert a String to an int. Returns null on error. (Catch)
	 * @param str
	 * @return
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
			MyLog.e(TAG, "Failed to convert String to int: " +str);
		}

		return num;
	}
}
