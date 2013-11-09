package com.indivisible.mightyv.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ShowDataSource {

	//=================================================//
	//		data
	//=================================================//
	
	private SQLiteDatabase db = null;
	private DatabaseOpenHelper dbHelper = null;
	private static final String[] allColumns = { 
			DatabaseOpenHelper.COL_ID,
			DatabaseOpenHelper.COL_RAGEID,
			DatabaseOpenHelper.COL_STATUS,
			DatabaseOpenHelper.COL_TITLE
		};
	
	
	//=================================================//
	//		constructors
	//=================================================//
	
	public ShowDataSource(Context context)
	{
		dbHelper = new DatabaseOpenHelper(context);
	}
	
	
	//=================================================//
	//		open & close db handle
	//=================================================//
	
	public void open() throws SQLException
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
	
	public Show createShow(int rageID, String status, String title)
	{
		// insert and get show's key id
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COL_RAGEID, rageID);
		values.put(DatabaseOpenHelper.COL_STATUS, status);
		values.put(DatabaseOpenHelper.COL_TITLE, title);
		long showID = db.insert(DatabaseOpenHelper.TABLE_SHOWS, null, values);
		
		// retrieve saved show and return
		Cursor cursor = db.query(
				DatabaseOpenHelper.TABLE_SHOWS,
		        allColumns,
		        DatabaseOpenHelper.COL_ID + " = " + showID,
		        null, null, null, null);
		cursor.moveToFirst();
		Show newShow = cursorToShow(cursor);
		
		cursor.close();
		return newShow;
		
	}
	
	
	//=================================================//
	//		methods
	//=================================================//
	
	private static Show cursorToShow(Cursor cursor)
	{
		Show show = new Show();
		show.setID(cursor.getLong(0));
		show.setRageID(cursor.getInt(1));
		show.setStatus(cursor.getString(2));
		show.setTitle(cursor.getString(3));
		return show;
	}
	
	
	//=================================================//
}
