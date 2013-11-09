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
		Show newShow = getShowByKey(showKey);
		if (MyLog.info) MyLog.i(TAG, "Created new Show: " +newShow.toString());
		return newShow;
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
		
		if (MyLog.verbose) MyLog.v(TAG, "Retrieved Show from db using showKey: " +foundShow.toString());
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
		
		if (MyLog.verbose) MyLog.v(TAG, "Retrieved Show from db using RageID: " +foundShow.toString());
		return foundShow;
	}
	
	public boolean updateShow(Show show)
	{
		ContentValues values = getValuesFromShow(show);
		//TODO start a db transaction here to ensure no more than one row is changed
		int rowsAffected = db.update(
				DatabaseOpenHelper.TABLE_SHOWS,
				values,
				DatabaseOpenHelper.COL_KEY +" = "+ show.getKey(),
				null);
		if (rowsAffected == 1)
		{
			if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +show.toString());
			return true;
		}
		else
		{
			if (MyLog.error)
			{
				MyLog.e(TAG, "Attempted but failed to update Show: " +show.toString());
				MyLog.e(TAG, "Rows affected: " +rowsAffected);
			}
			return false;
		}
	}
	
	public boolean deleteShow(long showKey)
	{
		//TODO start a db transaction here to ensure no more than one row is deleted
		int rowsAffected = db.delete(
				DatabaseOpenHelper.TABLE_SHOWS, 
				DatabaseOpenHelper.COL_KEY +" = "+ showKey,
				null);
		if (rowsAffected == 1)
		{
			if (MyLog.info) MyLog.i(TAG, "Deleted Show with Key: " +showKey);
			return true;
		}
		else
		{
			if (MyLog.error)
			{
				MyLog.e(TAG, "Tried to delete Show. Couldn't do it. ShowKey: " +showKey);
				MyLog.e(TAG, "Rows affected: " +rowsAffected);
			}
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
