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
public class ShowDataSource
{

	//ASK refactor/consolidate queries. All return List<?> and take proper query 'selection' term
	//ASK catch SQLExceptions when performing queries?
	
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
		// format Show's values for insertion
		ContentValues values = new ContentValues();
		values.put(DBMediaOpenHelper.COL_RAGEID, rageID);
		values.put(DBMediaOpenHelper.COL_STATUS, status);
		values.put(DBMediaOpenHelper.COL_TITLE, title);
		
		// insert values and get new Show's Primary Key
		long showKey = db.insert(DBMediaOpenHelper.TABLE_SHOWS, null, values);
		if (MyLog.debug) MyLog.d(TAG, "Created new Show with key: " +showKey);
		
		// retrieve saved show and return as a Show object
		Show newShow = getShowByKey(showKey);
		if (MyLog.info) MyLog.i(TAG, "Created new Show: " +newShow.toString());
		return newShow;
	}
	
	//==== Read ====//
	
	/**
	 * Retrieve an existing Show from the database using the Primary Key (long)
	 * @param showKey Show's Primary Key
	 * @return Retrieved Show or NULL on fail
	 */
	public Show getShowByKey(long showKey)
	{
		Cursor cursor = null;
		try
		{
			// form and perform database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_SHOWS,
			        allColumns,
			        DBMediaOpenHelper.COL_KEY +" = "+ showKey,
			        null, null, null, null);
			// ensure we only have one result
			if (cursor.moveToFirst() && cursor.getCount() == 1)
			{
				// convert result to Show and return
				Show foundShow = cursorToShow(cursor);
				if (MyLog.verbose) MyLog.v(TAG, "Retrieved Show from db using showKey: " +foundShow.toString());
				return foundShow;
			}
			else
			{
				if (MyLog.error) MyLog.e(TAG, "Failed to retrieve Show (" +showKey+ "). Num results: " +cursor.getCount());
				return null;
			}
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
		
		
	}
	
	/**
	 * Retrieve an existing Show from the database using the TVRage Id
	 * @param rageID  TVRage.com's id for the Show
	 * @return Retrieved show or NULL on fail
	 */
	public Show getShowByRageID(int rageID)
	{
		Cursor cursor = null;
		try
		{
			// form and perform a database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_SHOWS,
					allColumns,
					DBMediaOpenHelper.COL_RAGEID +" = "+ rageID,
					null, null, null, null);
			// get Show from cursor
			if (cursor.moveToFirst() && cursor.getCount() == 1)
			{
				Show foundShow = cursorToShow(cursor);
				if (MyLog.verbose) MyLog.v(TAG, "Retrieved Show from db using RageID: " +foundShow.toString());
				return foundShow;
			}
			else // incorrect number of results
			{
				if (MyLog.error) MyLog.e(TAG, "Get Show by rageID - Incorrect num results: " +cursor.getCount());
				return null;
			}
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Gather the Primary Keys for all Shows saved in the database
	 * @return List of Longs that are the Primary Keys
	 */
	private List<Long> getAllShowKeys()
	{
		Cursor cursor = null;
		try
		{
			// form and perform a database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_SHOWS,
					new String[] { DBMediaOpenHelper.COL_KEY },
					null, null, null, null, null);
			if (MyLog.verbose) MyLog.v(TAG, "Collecting all Show keys. Found: " +cursor.getCount());
			
			// iterate through the results and add to list
			List<Long> allShowKeys = new ArrayList<Long>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				allShowKeys.add(cursor.getLong(0));
				cursor.moveToNext();
			}
			
			// return List of Show keys
			if (MyLog.verbose) MyLog.v(TAG, "Parsed keys: " +allShowKeys.size());
			return allShowKeys;
			
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Method to return every show saved in the database
	 * @return List containing all Shows
	 */
	public List<Show> getAllShows()
	{
		Cursor cursor = null;
		try
		{
			// form and perform a db query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_SHOWS,
					allColumns,
					null, null, null, null, null);		// null 'selection' param retrieves all rows in table
			if (MyLog.verbose) MyLog.v(TAG, "Retrieving all Shows. Found: " +cursor.getCount());
	
			// iterate through the query results and add to list
			List<Show> shows = new ArrayList<Show>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				shows.add(cursorToShow(cursor));
				cursor.moveToNext();
			}
			
			// return List of Show objects
			if (MyLog.debug) MyLog.d(TAG, "Parsed and returned shows: " +shows.size());
			return shows;
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	
	//==== Update ====//
	
	/**
	 * Update a Show's database entry
	 * @param show Show to update database entry and state to use
	 * @return boolean indicating successful update
	 */
	public boolean updateShow(Show show)
	{
		// get Show's info in a usable format for database entry
		ContentValues values = getValuesFromShow(show);
		// start a transaction so we can revert changes if necessary
		db.beginTransaction();
		try
		{
			// perform update. returns number of rows that were changed
			int rowsAffected = db.update(
					DBMediaOpenHelper.TABLE_SHOWS,
					values,
					DBMediaOpenHelper.COL_KEY +" = "+ show.getKey(),
					null);
			// one row changed is what we wanted
			if (rowsAffected == 1)
			{
				if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +show.toString());
				db.setTransactionSuccessful();
				return true;
			}
			// more or fewer rows changed means failed update
			else
			{
				if (MyLog.error) MyLog.e(TAG, "Failed to update Show: " +show.toString()+ ". rowsAffected: " +rowsAffected);
				// do not commit changes
				return false;
			}
		}
		// 'finally' block gets executed even after a return
		finally
		{
			db.endTransaction();
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
		
		// start a transaction so we can revert changes if other than one row is affected
		db.beginTransaction();
		try
		{
			// perform delete. returns number of deleted (changed) rows
			int rowsAffected = db.delete(
					DBMediaOpenHelper.TABLE_SHOWS, 
					DBMediaOpenHelper.COL_KEY +" = "+ showKey,
					null);
			// only exactly one row/Show deleted
			if (rowsAffected == 1)
			{
				if (MyLog.verbose) MyLog.v(TAG, "Deleted Show with Key: " +showKey);
				db.setTransactionSuccessful();
				return true;
			}
			// none or more than one row/Show affected
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
		// 'finally' block executes even after a method's return statement
		finally
		{
			db.endTransaction();
		}
	}
	
	/**
	 * Delete a Show's entry. Leaves it's episodes untouched (for now)
	 * @param show Show object containing its Primary Key
	 * @return boolean indicating successful deletion
	 */
	public boolean deleteShow(Show show)
	{
		return deleteShow(show.getKey());
	}
	
	/**
	 * Delete all Shows from the database.
	 * Currently does not delete associated Episodes too
	 * @return List of Longs identifying any Shows that did not delete
	 */
	public List<Long> deleteAllShows()
	{
		// lists for saving any failed deletes and all Show's Primary Keys
		List<Long> failedDeletes = new ArrayList<Long>();
		List<Long> allShowKeys = getAllShowKeys();
		
		// loop Primary Keys and delete
		for (Long showKey : allShowKeys)
		{
			// if Show didn't delete successfully then log it and add to failed list 
			if (!deleteShow(showKey))
			{
				if (MyLog.error) MyLog.e(TAG, "Failed to delete Show with Key: " +showKey);
				failedDeletes.add(showKey);
			}
		}
		// pass back a list of any Shows that didn't delete
		return failedDeletes;
	}
	
	
	//=================================================//
	//		private methods
	//=================================================//
	
	/**
	 * Extract a Show object from a Cursor's current result.
	 * Cursor must contain allColumns and be in that correct order.
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
