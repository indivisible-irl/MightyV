package com.indivisible.mightyv.util;

public class Convert {

	private static final String TAG = "Convert";
	
	public static final int stringToInt(String str)
	{
		Integer num = null;
		try
		{
			Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			Logging.e(TAG, "Failed to convert String to int: " +str);
		}

		
		return num;
	}
}
