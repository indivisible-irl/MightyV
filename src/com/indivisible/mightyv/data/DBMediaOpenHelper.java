package com.indivisible.mightyv.data;

import com.indivisible.mightyv.util.MyLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to take care of database creation, upgrade and naming for the "Media" database
 * @author indivisible
 */
public class DBMediaOpenHelper extends SQLiteOpenHelper
{
	//=================================================//
	//		data
	//=================================================//
	
	// log tag  (using manual TAG here as simpler for static classes)
	private static final String TAG = "DatabaseOpenHelper";
	
	// database details	
	private static final String DATABASE_NAME    = "mightyv_shows.db";
	private static final int    DATABASE_VERSION = 3;
	
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
			+COL_STATUS+  " text, "
			+COL_TITLE+   " text not null "
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
	
	/**
	 * Class to take care of database creation, upgrade and naming for the "Media" database
	 * @param context Android ApplicationContext (do not use ActivityContext)
	 */
	public DBMediaOpenHelper(Context context)
	{
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	//=================================================//
	//		onCreate
	//=================================================//
	//TODO test what happens on corrupt/malformed db file
	
	// run when db doesn't exist 
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		if (MyLog.info) MyLog.i(TAG, "Creating Media database...");
		db.execSQL(CREATE_TABLE_SHOWS);
		db.execSQL(CREATE_TABLE_EPISODES);
		if (MyLog.info) MyLog.i(TAG, "Media database created.");
	}
	
	//=================================================//
	//		onUpgrade
	//=================================================//
	
	// run when database version number has been incremented [++] (can never decrement [--])
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (MyLog.warn)
		{
			MyLog.i(TAG, String.format("Upgrading database from v{0} to v{1}", oldVersion, newVersion));
			MyLog.w(TAG, "!! Recreating empty database as still in dev at this time");
		}
		// for now (development) let's just delete and recreate the whole database
		//   will eventually have methods to gracefully update/migrate database contents when going live
		//   may add some methods to populate the db with test data once I reach that point
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOWS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EPISODES);
		onCreate(db);
	}
	
	
	//=================================================//
}
