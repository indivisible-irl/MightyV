package com.indivisible.mightyv.data;

import com.indivisible.mightyv.util.MyLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ShowDataSource {

	//=================================================//
	//		data
	//=================================================//
	
	private String TAG;
	private SQLiteDatabase db = null;
	private DatabaseOpenHelper dbHelper = null;
	private static final String[] allColumns = { 
			DatabaseOpenHelper.COL_KEY,
			DatabaseOpenHelper.COL_RAGEID,
			DatabaseOpenHelper.COL_STATUS,
			DatabaseOpenHelper.COL_TITLE
		};
	
	
	//=================================================//
	//		constructors
	//=================================================//
	
	public ShowDataSource(Context context)
	{
		TAG = this.getClass().getSimpleName();
		dbHelper = new DatabaseOpenHelper(context);
	}
	
	
	//=================================================//
	//		open & close db handle
	//=================================================//
	
	public void openReadable() throws SQLException
	{
		db = dbHelper.getReadableDatabase();
		if (MyLog.verbose) MyLog.v(TAG, "Opened readable database");
	}
	
	public void openWritable() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		if (MyLog.verbose) MyLog.v(TAG, "Opened writable database");
	}

	public void close()
	{ 
		if (dbHelper != null) {
			if (MyLog.verbose) MyLog.v(TAG, "Closed db handle");
			dbHelper.close();
		}
	}
	

	//=================================================//
	//		CRUD
	//=================================================//
	
	public Show createShow(int rageID, String status, String title)
	{
		// insert and get show's key id
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COL_RAGEID, rageID);
		values.put(DatabaseOpenHelper.COL_STATUS, status);
		values.put(DatabaseOpenHelper.COL_TITLE, title);
		long showKey = db.insert(DatabaseOpenHelper.TABLE_SHOWS, null, values);
		
		// retrieve saved show and return
		return getShowByKey(showKey);
	}
	
	public Show getShowByKey(long showKey)
	{
		Cursor cursor = db.query(
				DatabaseOpenHelper.TABLE_SHOWS,
		        allColumns,
		        DatabaseOpenHelper.COL_KEY +" = "+ showKey,
		        null, null, null, null);
		cursor.moveToFirst();
		Show foundShow = cursorToShow(cursor);
		
		cursor.close();
		return foundShow;
	}
	
	public Show getShowByRageID(int rageID)
	{
		Cursor cursor = db.query(
				DatabaseOpenHelper.TABLE_SHOWS,
				allColumns,
				DatabaseOpenHelper.COL_RAGEID +" = "+ rageID,
				null, null, null, null);
		cursor.moveToFirst();
		Show foundShow = cursorToShow(cursor);
		
		cursor.close();
		return foundShow;
	}
	
	public int updateShow(Show show)
	{
		ContentValues values = getValuesFromShow(show);
		//TODO start a db transaction here to ensure no more than one row is changed
		int rowsAffected = db.update(
				DatabaseOpenHelper.TABLE_SHOWS,
				values,
				DatabaseOpenHelper.COL_KEY +" = "+ show.getKey(),
				null);
		if (rowsAffected == 0)
		{
			if (MyLog.error) MyLog.e(TAG, "Attempted but failed to update Show: "
					+show.getKey()+ " - " +show.getTitle());
		}
		else if (rowsAffected == 1)
		{
			if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +show.getKey());
		}
		else
		{
			if (MyLog.error) MyLog.e(TAG, "Failed horribly while attempting to update Show: "
					+show.getKey()+ " - " +show.getTitle());
		}
		return rowsAffected;
	}
	
	public boolean deleteShow(long showKey)
	{
		//TODO start a db transaction here to ensure no more than one row is deleted
		int rowsAffected = db.delete(
				DatabaseOpenHelper.TABLE_SHOWS, 
				DatabaseOpenHelper.COL_KEY +" = "+ showKey,
				null);
		if (rowsAffected == 0)
		{
			if (MyLog.warn) MyLog.w(TAG, "Attempted to delete Show. No Show deleted. ShowKey: " +showKey);
			return false;
		}
		else if (rowsAffected == 1)
		{
			if (MyLog.info) MyLog.i(TAG, "Deleted Show with Key: " +showKey);
			return true;
		}
		else
		{
			if (MyLog.error) MyLog.e(TAG, "Attempted deletion of Show resulted in multiple affected rows. ShowKey: " +showKey);
			return false;
		}
				
	}
	
	
	//=================================================//
	//		public methods
	//=================================================//
	
	private static Show cursorToShow(Cursor cursor)
	{
		Show show = new Show();
		show.setKey(cursor.getLong(0));
		show.setRageID(cursor.getInt(1));
		show.setStatus(cursor.getString(2));
		show.setTitle(cursor.getString(3));
		return show;
	}
	
	private static ContentValues getValuesFromShow(Show show)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COL_RAGEID, show.getRageID());
		values.put(DatabaseOpenHelper.COL_STATUS, show.getStatus());
		values.put(DatabaseOpenHelper.COL_TITLE, show.getTitle());
		return values;
	}
	
	
	//=================================================//
}
