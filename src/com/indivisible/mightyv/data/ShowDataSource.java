package com.indivisible.mightyv.data;

import java.util.ArrayList;
import java.util.List;

import com.indivisible.mightyv.util.MyLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class to handle the interactions between Show objects and their database
 * @author indivisible
 */
public class ShowDataSource {

	//=================================================//
	//		data
	//=================================================//
	
	private String TAG;
	private SQLiteDatabase db = null;
	private DBMediaOpenHelper dbHelper = null;
	private static final String[] allColumns = {		//REM update allColumns on activation of new fields
			DBMediaOpenHelper.COL_KEY,
			DBMediaOpenHelper.COL_RAGEID,
			DBMediaOpenHelper.COL_STATUS,
			DBMediaOpenHelper.COL_TITLE
		};
	
	
	//=================================================//
	//		constructors
	//=================================================//
	
	/**
	 * Class to handle the interactions between Show objects and their database
	 * @param context Android ApplicationContext (do not use ActivityContext)
	 */
	public ShowDataSource(Context context)
	{
		TAG = this.getClass().getSimpleName();
		dbHelper = new DBMediaOpenHelper(context);
	}
	
	
	//=================================================//
	//		open & close db handle
	//=================================================//
	
	/**
	 * Open the database in a read-only state.
	 * @throws SQLException
	 */
	public void openReadable() throws SQLException
	{
		db = dbHelper.getReadableDatabase();
		if (MyLog.verbose) MyLog.v(TAG, "Opened readable database");
	}
	
	/**
	 * Open the database in a writable state.
	 * @throws SQLException
	 */
	public void openWritable() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		if (MyLog.verbose) MyLog.v(TAG, "Opened writable database");
	}

	/**
	 * Close the database handle
	 */
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
	
	//==== Create ====//
	
	/**
	 * Create a new Show object and store it in the database
	 * @param rageID   TVRage.com's id for the Show
	 * @param status The Show's current status
	 * @param title  The Show's full title
	 * @return Show retrieved from the database after storing it
	 */
	public Show createShow(int rageID, String status, String title)
	{
		// insert and get show's key id
		ContentValues values = new ContentValues();
		values.put(DBMediaOpenHelper.COL_RAGEID, rageID);
		values.put(DBMediaOpenHelper.COL_STATUS, status);
		values.put(DBMediaOpenHelper.COL_TITLE, title);
		long showKey = db.insert(DBMediaOpenHelper.TABLE_SHOWS, null, values);
		
		// retrieve saved show and return
		Show newShow = getShowByKey(showKey);
		if (MyLog.info) MyLog.i(TAG, "Created new Show: " +newShow.toString());
		return newShow;
	}
	
	//==== Read ====//
	
	/**
	 * Retrieve an existing Show from the database using the Primary Key (long)
	 * @param showKey Show's Primary Key
	 * @return Retrieved Show
	 */
	public Show getShowByKey(long showKey)
	{
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_SHOWS,
		        allColumns,
		        DBMediaOpenHelper.COL_KEY +" = "+ showKey,
		        null, null, null, null);
		cursor.moveToFirst();
		//TODO test results of failed retrievals
		Show foundShow = cursorToShow(cursor);
		cursor.close();
		
		if (MyLog.verbose) MyLog.v(TAG, "Retrieved Show from db using showKey: " +foundShow.toString());
		return foundShow;
	}
	
	/**
	 * Retrieve an existing Show from the database using the TVRage Id
	 * @param rageID  TVRage.com's id for the Show
	 * @return Retrieved show
	 */
	public Show getShowByRageID(int rageID)
	{
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_SHOWS,
				allColumns,
				DBMediaOpenHelper.COL_RAGEID +" = "+ rageID,
				null, null, null, null);
		cursor.moveToFirst();
		//todo test results of failed retrievals
		Show foundShow = cursorToShow(cursor);
		cursor.close();
		
		if (MyLog.verbose) MyLog.v(TAG, "Retrieved Show from db using RageID: " +foundShow.toString());
		return foundShow;
	}
	
	/**
	 * Method to return every show saved in the database
	 * @return List containing all Shows
	 */
	public List<Show> getAllShows()
	{
		List<Show> shows = new ArrayList<Show>();
		
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_SHOWS,
				allColumns,
				null, null, null, null, null);		// null 'selection' param retrieves all rows in table
		if (MyLog.verbose) MyLog.v(TAG, "Retrieving all Shows. Found: " +cursor.getCount());
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			shows.add(cursorToShow(cursor));
			cursor.moveToNext();
		}
		
		if (MyLog.verbose) MyLog.v(TAG, "Parsed and returned shows: " +shows.size());
		return shows;
	}
	
	//==== Update ====//
	
	/**
	 * Update a Show's database entry
	 * @param show Show to update database entry and state to use
	 * @return boolean indicating successful update
	 */
	public boolean updateShow(Show show)
	{
		ContentValues values = getValuesFromShow(show);
		//TODO start a db transaction here to ensure no more than one row is changed
		int rowsAffected = db.update(
				DBMediaOpenHelper.TABLE_SHOWS,
				values,
				DBMediaOpenHelper.COL_KEY +" = "+ show.getKey(),
				null);
		if (rowsAffected == 1)
		{
			if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +show.toString());
			//todo commit changes
			return true;
		}
		else
		{
			if (MyLog.error)
			{
				MyLog.e(TAG, "Attempted but failed to update Show: " +show.toString());
				MyLog.e(TAG, "Rows affected: " +rowsAffected);
			}
			//todo discard changes
			return false;
		}
	}
	
	//==== Delete ====//
	
	/**
	 * Delete a Show's entry. Leaves it's episodes untouched (for now)
	 * @param showKey Show's database Primary Key
	 * @return boolean indicating successful deletion
	 */
	public boolean deleteShow(long showKey)
	{
		//FIXME deleting a show should remove all its episodes too
		//TODO start a db transaction here to ensure no more than one row is deleted
		int rowsAffected = db.delete(
				DBMediaOpenHelper.TABLE_SHOWS, 
				DBMediaOpenHelper.COL_KEY +" = "+ showKey,
				null);
		if (rowsAffected == 1)
		{
			if (MyLog.info) MyLog.i(TAG, "Deleted Show with Key: " +showKey);
			//todo commit changes
			return true;
		}
		else
		{
			if (MyLog.error)
			{
				MyLog.e(TAG, "Tried to delete Show. Couldn't do it. ShowKey: " +showKey);
				MyLog.e(TAG, "Rows affected: " +rowsAffected);
			}
			//todo discard changes
			return false;
		}				
	}
	
	
	//=================================================//
	//		public methods
	//=================================================//
	
	/**
	 * Extract a Show object from a Cursor's current result
	 * @param cursor Database query Cursor set to desired position
	 * @return Show
	 */
	private static Show cursorToShow(Cursor cursor)
	{
		Show show = new Show();
		show.setKey(cursor.getLong(0));
		show.setRageID(cursor.getInt(1));
		show.setStatus(cursor.getString(2));
		show.setTitle(cursor.getString(3));
		return show;
	}
	
	/**
	 * Pair a Show's fields into ContentValues for database entry.
	 * @param show Show to prepare for database entry
	 * @return ContentValues
	 */
	private static ContentValues getValuesFromShow(Show show)
	{
		ContentValues values = new ContentValues();
		values.put(DBMediaOpenHelper.COL_RAGEID, show.getRageID());
		values.put(DBMediaOpenHelper.COL_STATUS, show.getStatus());
		values.put(DBMediaOpenHelper.COL_TITLE, show.getTitle());
		return values;
	}
	
	
	//=================================================//
}
