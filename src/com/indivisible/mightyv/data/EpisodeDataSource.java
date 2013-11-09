package com.indivisible.mightyv.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EpisodeDataSource {
	
	//=================================================//
	//		data
	//=================================================//

	private String TAG;
	private SQLiteDatabase db = null;
	private DatabaseOpenHelper dbHelper = null;
	private String[] allColumns = {
			DatabaseOpenHelper.COL_KEY,
			DatabaseOpenHelper.COL_SHOW_FK,
			DatabaseOpenHelper.COL_NUM_EPISODE,
			DatabaseOpenHelper.COL_NUM_SEASON
		};

	
	//=================================================//
	//		constructors
	//=================================================//
	
	public EpisodeDataSource(Context context)
	{
		TAG = this.getClass().getSimpleName();
		this.dbHelper = new DatabaseOpenHelper(context); 
	}
	
	
	//=================================================//
	//		open & close db handle
	//=================================================//
	
	public void openReadable() throws SQLException
	{
		db = dbHelper.getReadableDatabase();
	}
	
	public void openWritable() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
	}

	public void close()
	{ 
		if (dbHelper != null) { 
			dbHelper.close();
		}
	}
	
	
	//=================================================//
	//		CRUD
	//=================================================//
	
	
	
	//=================================================//
	//		private methods
	//=================================================//
	
	private static Episode cursorToEpisode(Cursor cursor)
	{
		Episode ep = new Episode();
		ep.setKey(cursor.getLong(0));
		ep.setParentKey((cursor.getInt(1)));
		ep.setEpisodeNum(cursor.getInt(2));
		ep.setTitle(cursor.getString(3));
		return ep;
	}
	
	private static ContentValues getValuesFromShow(Show show)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COL_RAGEID, show.getRageID());
		values.put(DatabaseOpenHelper.COL_STATUS, show.getStatus());
		values.put(DatabaseOpenHelper.COL_TITLE, show.getTitle());
		return values;
	}
	
}
