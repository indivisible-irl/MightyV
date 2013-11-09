package com.indivisible.mightyv.util;

import android.util.Log;

//REM level checks to be done inline on call of these methods

public class Logging
{
	//=================================================//
	//		data
	//=================================================//
	
	// log tag prefix
	private static final String TAG_PREFIX = "MightyV:";
	
	// application wide logging level
	public static final int LOG_LEVEL = 10;		// set to -1 to disable all logging (should even be possible??)
	
	// logging levels defined
	public static boolean debug   = LOG_LEVEL > 6;
	public static boolean verbose = LOG_LEVEL > 5;
	public static boolean info    = LOG_LEVEL > 4;
	public static boolean warn    = LOG_LEVEL > 3;
	public static boolean error   = LOG_LEVEL > 2;
	public static boolean wtf     = LOG_LEVEL > 1;
	
	// enumerator for levels
	public static enum logLevel {
		levelDebug,
		levelVerbose,
		levelInfo,
		levelWarn,
		levelError,
		levelWTF
	};
	
	
	
	//=================================================//
	//		calls to Android logging
	//=================================================//
	
	// perform logging.
	public static void d(String tag, String msg)
	{
		Log.d(formattedTag(tag), msg);
	}
	public static void v(String tag, String msg)
	{
		Log.v(formattedTag(tag), msg);
	}
	public static void i(String tag, String msg)
	{
		Log.i(formattedTag(tag), msg);
	}
	public static void w(String tag, String msg)
	{
		Log.w(formattedTag(tag), msg);
	}
	public static void e(String tag, String msg)
	{
		Log.e(formattedTag(tag), msg);
	}
	public static void wtf(String tag, String msg)
	{
		Log.wtf(formattedTag(tag), msg);
	}
	
	
	//=================================================//
	//		custom methods
	//=================================================//
	
	// format tag for log entry
	private static final String formattedTag(String tag)
	{
		return TAG_PREFIX.concat(tag);
	}
	
	
	//=================================================//
}
