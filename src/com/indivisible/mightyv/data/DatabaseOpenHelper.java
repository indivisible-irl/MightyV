package com.indivisible.mightyv.data;

import com.indivisible.mightyv.util.MyLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	//=================================================//
	//		data
	//=================================================//
	
	// log tag  (using manual TAG as simpler for static class)
	private static final String TAG = "DatabaseOpenHelper";
	// database details	
	private static final String DATABASE_NAME    = "mightyv_shows.db";
	private static final int    DATABASE_VERSION = 1;
	
	// table - common
	public static final String COL_KEY    = "_id";
	public static final String COL_TITLE = "title";
	
	// table - Shows
	public static final String TABLE_SHOWS = "shows";
	public static final String COL_RAGEID  = "rage_id";
	public static final String COL_STATUS  = "status";
	
	// table - Episodes
	public static final String TABLE_EPISODES  = "episodes";
	public static final String COL_SHOW_FK    = "fk_show";
	public static final String COL_NUM_SEASON  = "num_season";
	public static final String COL_NUM_EPISODE = "num_episode";
	
	
	//=================================================//
	//		table create statements
	//=================================================//
	
	private static final String CREATE_TABLE_SHOWS = "create table "
			+TABLE_SHOWS+ "(" 
			+COL_KEY+     " integer primary key autoincrement, "
			+COL_RAGEID+  " integer not null, "
			+COL_STATUS+  " text "
			+");";
	private static final String CREATE_TABLE_EPISODES = "create table "
			+TABLE_EPISODES+  "(" 
			+COL_KEY +        " integer primary key autoincrement, "
			+COL_SHOW_FK+     "integer not null, "
			+COL_NUM_SEASON+  " integer not null, "
			+COL_NUM_EPISODE+ " integer not null "
			+");";
	
	//=================================================//
	//		constructors
	//=================================================//
	
	public DatabaseOpenHelper(Context context)
	{
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	//=================================================//
	//		onCreate
	//=================================================//
	
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE_SHOWS);
		db.execSQL(CREATE_TABLE_EPISODES);
	}
	
	//=================================================//
	//		onUpgrade
	//=================================================//
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (MyLog.warn)
		{
			MyLog.i(TAG, String.format("Upgrading database from v{0} to v{1}", oldVersion, newVersion));
			MyLog.w(TAG, "!! Recreating empty database as still in dev at this time");
		}
		// for now (dev) let's just delete and recreate the whole database
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SHOWS);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_EPISODES);
	}
	
	
	//=================================================//
}
