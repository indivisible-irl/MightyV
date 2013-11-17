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
 * Class to handle the interactions between Episode objects and their database.
 * @author indivisible
 */
public class EpisodeDataSource {
	
	//=================================================//
	//		data
	//=================================================//

	private String TAG;
	private SQLiteDatabase db = null;
	private DBMediaOpenHelper dbHelper = null;
	private String[] allColumns = {						//REM update allColumns on activating new fields
			DBMediaOpenHelper.COL_KEY,
			DBMediaOpenHelper.COL_SHOW_FK,
			DBMediaOpenHelper.COL_NUM_EPISODE,
			DBMediaOpenHelper.COL_NUM_SEASON,
			DBMediaOpenHelper.COL_TITLE
		};

	private String SORT_BY_SEASON_THEN_EP =
			DBMediaOpenHelper.COL_NUM_SEASON +" ASC, "+ DBMediaOpenHelper.COL_NUM_EPISODE +" ASC ";
	private String SORT_BY_EPISODE = 
			DBMediaOpenHelper.COL_NUM_EPISODE +" ASC ";
	
	//=================================================//
	//		constructors
	//=================================================//
	
	/**
	 * Class to handle the interactions between Episode objects and their database
	 * @param context Android ApplicationContext (do not use ActivityContext)
	 */
	public EpisodeDataSource(Context context)
	{
		TAG = this.getClass().getSimpleName();				// Get the Class's name for use in the logs
		this.dbHelper = new DBMediaOpenHelper(context); 
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
	}
	
	/**
	 * Open the database in a writable state.
	 * @throws SQLException
	 */
	public void openWritable() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Close the database handle
	 */
	public void close()
	{ 
		if (dbHelper != null) { 
			dbHelper.close();
		}
	}
	
	
	//=================================================//
	//		CRUD
	//=================================================//
	
	//==== Create ====//
	
	/**
	 * Create a new Episode object and store it in the database.
	 * Returned Episode has its Primary Key within.
	 * @param showKey 		Episode's parent Show's Primary Key (Foreign Key here)
	 * @param seasonNum		The season number containing this Episode
	 * @param episodeNum	The episode number of this Episode within the stated season
	 * @param title			Episode's title
	 * @return Created Episode (retrieved from the database after storing)
	 */
	public Episode createEpisode(long showKey, int seasonNum, int episodeNum, String title)
	{
		// format the Episode's details for database entry
		ContentValues values = new ContentValues();
		values.put(DBMediaOpenHelper.COL_SHOW_FK, 	   showKey);
		values.put(DBMediaOpenHelper.COL_NUM_SEASON,   seasonNum);
		values.put(DBMediaOpenHelper.COL_NUM_EPISODE,  episodeNum);
		values.put(DBMediaOpenHelper.COL_TITLE,        title);
		
		// insert into database and receive new row's key
		long episodeKey = db.insert(DBMediaOpenHelper.TABLE_EPISODES, null, values);
		if (MyLog.debug) MyLog.d(TAG, "Saved Episode. Key: " +episodeKey);
		
		// retrieve saved show from database and return Episode object
		Episode newEpisode =  getEpisodeByKey(episodeKey);
		if (MyLog.verbose) MyLog.v(TAG, "Created new Episode: " +newEpisode.toString());
		return newEpisode;
	}
	
	//==== Read ====//
	
	/**
	 * Retrieve a stored Episode from its database identified by its Primary Key (long)
	 * @param episodeKey  Episode's Primary Key
	 * @return Episode matching Key or NULL on fail
	 */
	public Episode getEpisodeByKey(long episodeKey)
	{
		Cursor cursor = null;
		try
		{
			// form and perform a database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_EPISODES,
					allColumns,
					DBMediaOpenHelper.COL_KEY +" = "+ episodeKey,
					null, null, null, null);
			if (MyLog.debug) MyLog.d(TAG, "Retrieve Episode. Results: " +cursor.getCount());
			// ensure we have only one result
			if (cursor.moveToFirst() && cursor.getCount() == 1)
			{
				Episode episode = cursorToEpisode(cursor);
				if (MyLog.verbose) MyLog.v(TAG, "Retrieved Episode from db: " +episode.toString());
				return episode;
			}
			else
			{
				// return null on fail
				if (MyLog.error) MyLog.e(TAG, "No results in cursor");
				return null;
			}
		}
		finally
		{
			// close cursor regardless of what happens above
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Retrieve a List of all Episode Primary Keys
	 * @return List of Longs of keys (Empty if none)
	 */
	public List<Long> getAllEpisodeKeys()
	{
		Cursor cursor = null;
		List<Long> episodeKeys = null;
		try
		{
			// form and perform a database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_EPISODES,
					new String[] { DBMediaOpenHelper.COL_KEY },
					null, null, null, null, null);
			if (MyLog.verbose) MyLog.v(TAG, "Gathering all Episode keys. Found: " +cursor.getCount());
			// make a List to hold results and iterate through the cursor
			episodeKeys = new ArrayList<Long>();
			if (cursor.moveToFirst())
			{
				while(!cursor.isAfterLast())
				{
					episodeKeys.add(cursor.getLong(0));
					cursor.moveToNext();
				}
			}
			else
			{
				if (MyLog.warn) MyLog.w(TAG, "No results found for all Episode Keys.");
			}
			if (MyLog.verbose) MyLog.v(TAG, "Processed keys: " +episodeKeys.size());
			return episodeKeys;
		}
		finally
		{
			// finally block gets executed even after a method's return statement
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Retrieve all stored Episodes in the database.
	 * Will return every Episode saved and as such should not be used unless explicitly needed.
	 * @return List of all Episodes
	 */
	public List<Episode> getAllEpisodes()
	{
		Cursor cursor = null;
		List<Episode> episodes = null;
		
		try
		{
			// perform the query on the database
			cursor = db.query(
					DBMediaOpenHelper.TABLE_EPISODES,
					allColumns,
					null, null, null, null, null);		// null 'selection' param returns all rows in table

			// get all result Episodes from the cursor and return the List 
			episodes = getEpisodesFromCursor(cursor);
			if (MyLog.verbose) MyLog.v(TAG, "Episodes parsed and returned: " +episodes.size());
			return episodes;
		}
		finally
		{
			// close the cursor, freeing up its resources
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Retrieve all Episodes for one particular Show
	 * @param showKey Show's Primary Key (a Foreign Key here)
	 * @return List of all Episodes for one Show
	 */
	public List<Episode> getAllShowsEpisodes(long showKey)
	{
		Cursor cursor = null;
		List<Episode> episodes = null;
		
		try
		{
			// perform the database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_EPISODES,
					new String[] { DBMediaOpenHelper.COL_KEY },
					DBMediaOpenHelper.COL_SHOW_FK +" = "+ showKey,
					null, null, null,
					SORT_BY_SEASON_THEN_EP);  // get cursor results in a sorted order
			if (MyLog.verbose) MyLog.v(TAG, "Gathering Show's Episodes. Found: " +cursor.getCount());
			
			// get Episodes from cursor and return List
			episodes = getEpisodesFromCursor(cursor);
			return episodes;
		}
		finally
		{
			// free up the cursor's resources
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Retrieve just the keys for all Episodes belonging to one Show
	 * @param showKey Parent Show's Primary Key (a Foreign Key here)
	 * @return List of Longs being the Keys
	 */
	public List<Long> getAllShowsEpisodeKeys(long showKey)
	{
		Cursor cursor = null;
		List<Long> episodeKeys = null;
		try
		{
			// run the database query
			cursor = db.query(
				DBMediaOpenHelper.TABLE_EPISODES,
				new String[] { DBMediaOpenHelper.COL_KEY },
				DBMediaOpenHelper.COL_SHOW_FK +" = "+ showKey,
				null, null, null, null);
			// put the result keys into a List and return
			if (MyLog.verbose) MyLog.v(TAG, "Gathering Show's Episode Keys. Found: " +cursor.getCount());
			episodeKeys = getKeysFromCursor(cursor);
			return episodeKeys;
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Retrieve all Episodes for one Show. Sorts by season and then by episode numbers.
	 * @param showId The Foreign Key identifying the Episodes' parent to filter by
	 * @return Collection of Episodes
	 */
	public List<Episode> getAllEpisodesForShow(long showId)
	{
		Cursor cursor = null;
		List<Episode> episodes = null;
		
		try
		{
			// perform the database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_EPISODES,
					allColumns,
					DBMediaOpenHelper.COL_SHOW_FK +" = "+ showId,
					null, null, null,
					SORT_BY_SEASON_THEN_EP);	// get cursor results in a sorted order
			if (MyLog.verbose)
			{
				MyLog.v(TAG, "Retrieving Show Episodes for showId: " +showId);
				MyLog.v(TAG, "Found: " +cursor.getCount());
			}
			
			// put Episodes in a List and return it
			episodes = getEpisodesFromCursor(cursor);
			if (MyLog.verbose) MyLog.v(TAG, "Episodes parsed and returned: " +episodes.size());
			return episodes;
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	
	/**
	 * Retrieve all Episodes for a single Show's season
	 * @param showId Show's Primary Key
	 * @param seasonNum Season number
	 * @return List of Episodes
	 */
	public List<Episode> getAllEpisodesForSeason(long showId, int seasonNum)
	{
		Cursor cursor = null;
		List<Episode> episodes = null;
		
		try
		{
			// form and perform database query
			cursor = db.query(
					DBMediaOpenHelper.TABLE_EPISODES,
					allColumns,
					DBMediaOpenHelper.COL_SHOW_FK +" = "+ showId
						+" AND "+ DBMediaOpenHelper.COL_NUM_SEASON +" = "+ seasonNum, // compound selection
					null, null, null,
					SORT_BY_EPISODE);  // get cursor results in a sorted order
			
			// get Episodes from cursor's results and return in a List
			episodes = getEpisodesFromCursor(cursor);
			if (MyLog.verbose) MyLog.v(TAG, "Episodes parsed and returned: " +episodes.size());
			return episodes;
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
	}
	
	
	//TODO future Read methods
	public Episode _getLatestEpisode() {return null;}
	public Episode _getNextUnreleasedEpisode() {return null;}
	public Episode _getLastWatchedEpisode() {return null;}
	public Episode _getNextUnwatchedEpisode() {return null;}
	
	//==== Update ====//
	
	/**
	 * Update an Episode's entry in the database
	 * @param episode Episode
	 * @return boolean indicating successful update
	 */
	public boolean updateEpisode(Episode episode)
	{
		// format the Episode's info for database insertion
		ContentValues values = getValuesFromEpisode(episode);
		
		// start a database transaction so we can revert any changes if unhappy with them
		db.beginTransaction();
		try
		{
			// perform the update and get the number of changed rows in return
			int rowsAffected = db.update(
					DBMediaOpenHelper.TABLE_EPISODES,
					values,
					DBMediaOpenHelper.COL_KEY +" = "+ episode.getKey(),
					null);
			// ensure only one row was touched
			if (rowsAffected == 1)
			{
				if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +episode.toString());
				// tell the database to commit the changes
				db.setTransactionSuccessful();
				return true;
			}
			else
			{
				if (MyLog.error)
				{
					MyLog.e(TAG, "Failed horribly while attempting to save Episode: " +episode.toString());
					MyLog.e(TAG, "Rows affected: " +rowsAffected);
				}
				// do not setTransactionSuccessful. changes will be reverted
				return false;
			}
		}
		finally
		{
			// terminate the transaction. if setTransactionSuccessful changes will be saved.
			db.endTransaction();
		}
	}
	
	//==== Delete ====//
	
	/**
	 * Delete an Episode's database entry identified by its Primary Key (long)
	 * @param episodeKey The Episode to be deleted's Primary Key
	 * @return boolean indicating successful deletion
	 */
	private boolean deleteEpisode(long episodeKey)
	{
		// start database transaction to allow us to revert changes if something goes wrong
		db.beginTransaction();
		try
		{
			// perform the delete and get the number of rows removed
			int rowsAffected = db.delete(
					DBMediaOpenHelper.TABLE_EPISODES, 
					DBMediaOpenHelper.COL_KEY +" = "+ episodeKey,
					null);
			// ensure only one row was touched
			if (rowsAffected == 1)
			{
				if (MyLog.verbose) MyLog.v(TAG, "Deleted Episode with Key: " +episodeKey);
				// save the changes made (row deleted)
				db.setTransactionSuccessful();
				return true;
			}
			else
			{
				if (MyLog.warn)
				{
					MyLog.w(TAG, "Tried to delete Episode but it didn't go to plan. episodeKey: " +episodeKey);
					MyLog.w(TAG, "Rows affected: " +rowsAffected);
				}
				// revert all changes as didn't setTransactionSuccessful
				return false;
			}
		}
		finally
		{
			// complete transaction
			db.endTransaction();
		}
	}
	
	/**
	 * Delete a single Episode
	 * @param episodeKey Primary Key of Episode
	 * @return successful deletion of Episode
	 */
	public boolean deleteOneEpisode(long episodeKey)
	{
		// start database transaction
		db.beginTransaction();
		try
		{
			// perform delete and branch by success
			boolean successfulDelete = deleteEpisode(episodeKey);
			if (successfulDelete)
			{
				if (MyLog.verbose) MyLog.v(TAG, "Successfully deleted single Episode: " +episodeKey);
				db.setTransactionSuccessful();
				return true;
			}
			else
			{
				if (MyLog.error) MyLog.e(TAG, "Failed to delete single Episode: " +episodeKey);
				return false;
			}
		}
		finally
		{
			// end transaction
			db.endTransaction();
		}
		
	}
	
	/**
	 * Delete a number of Episodes at once
	 * @param episodeKeys List of Episode Primary Keys to delete
	 * @return List of Longs of any failed deletes. Changes not saved unless List is empty
	 */
	public List<Long> deleteMultipleEpisodes(List<Long> episodeKeys)
	{
		if (MyLog.debug) MyLog.d(TAG, "Delete multiple Episodes...\n Recieved Keys: " +episodeKeys.size());
		List<Long> failedDeletes = new ArrayList<Long>();
		
		//ASK do or don't commit deletes if any fail?
		db.beginTransaction();
		try
		{
			for (Long key : episodeKeys)
			{
				// delete each Episode by Key and add failed deletions to List
				if (!deleteEpisode(key))
				{
					failedDeletes.add(key);
				}
				else
				{
					// successful delete
				}
			}
			if (failedDeletes.size() == 0)
			{
				// if all individual deletions were successful overall deletion was a success
				if (MyLog.verbose) MyLog.v(TAG, "Deleted Episodes successfully. Count: " +episodeKeys.size());
				db.setTransactionSuccessful();
			}
			else
			{
				// not all deletes were successful. do not commit changes, log failures to console
				if (MyLog.error) MyLog.e(TAG, "Could not delete all Episodes -");
				if (MyLog.warn)
				{
					for (Long failedKey : failedDeletes)
					{
						MyLog.w(TAG, "Failed: " +failedKey);
					}
				}
			}
		}
		finally
		{
			// complete transaction
			db.endTransaction();
		}
		// return List of keys that failed to delete. Empty on success (and saved changes)
		return failedDeletes;
	}
	
	/**
	 * Delete ALL Episodes in the database
	 * @return List of Episode keys that didn't delete. Changes reverted if non empty.
	 */
	public boolean deleteAllEpisodes()
	{
		// collect all Episode Keys. 
		List<Long> allEpisodeKeys = getAllEpisodeKeys();
		if (MyLog.debug) MyLog.d(TAG, "All Episodes. Found: " +allEpisodeKeys.size());
		
		// delete those Episodes
		List<Long> failedDeletes = deleteMultipleEpisodes(allEpisodeKeys);
		// test if deletion successful and return boolean
		if (failedDeletes.size() == 0)
		{
			if (MyLog.info) MyLog.i(TAG, "Successfully deleted ALL Episodes");
			return true;
		}
		else
		{
			if (MyLog.error) MyLog.e(TAG, "Couldn't delete some Episodes. Total: " +failedDeletes.size());
			return false;
		}
	}
	
	/**
	 * Delete all episodes belonging to just one Show
	 * @param showKey Show's Primary Key
	 * @return successfully deleted all episodes
	 */
	public boolean deleteAllShowsEpisodes(long showKey)
	{
		// get the Keys for all Episodes belonging to the Show
		List<Long> episodeKeys = getAllShowsEpisodeKeys(showKey);
		// attempt to delete all its Episodes.
		List<Long> failedDeletes = deleteMultipleEpisodes(episodeKeys);
		
		// test if deletion successful
		if (failedDeletes.size() == 0)
		{
			if (MyLog.info) MyLog.i(TAG, "Deleted all Episodes for Show: " +showKey);
			return true;
		}
		else
		{
			if (MyLog.verbose) MyLog.v(TAG, "Failed to delete all Episodes for Show: " +showKey);;
			return false;
		}
	}
	
//	public boolean deleteAllSeasonsEpisodes(long showKey, int seasonNum)
//	{
//		
//	}
	
	//=================================================//
	//		private methods
	//=================================================//
	
	/**
	 * Craft a single Episode object from a Cursor's current result
	 * @param cursor Database query Cursor set to desired position
	 * @return Episode or NULL on failure
	 */
	private static Episode cursorToEpisode(Cursor cursor)
	{
		// Note: relies on cursor's query requesting allColumns.
		Episode ep = null;
		try
		{
			ep = new Episode();
			ep.setKey(cursor.getLong(0));
			ep.setParentKey(cursor.getLong(1));
			ep.setSeasonNum(cursor.getInt(2));
			ep.setEpisodeNum(cursor.getInt(3));
			ep.setTitle(cursor.getString(4));
		}
		catch (NumberFormatException e)
		{
			if (MyLog.error) MyLog.e("EpisodeDataSource", "Failed to get Episode from cursor result.");
		}
		//ASK also catch column number out of bounds?
		
		return ep;
	}
	
	/**
	 * Extract and prepare an Episode's values for database entry
	 * @param episode Episode to prepare for database entry
	 * @return ContentValues
	 */
	private static ContentValues getValuesFromEpisode(Episode episode)
	{
		ContentValues values = new ContentValues();
		values.put(DBMediaOpenHelper.COL_SHOW_FK, episode.getParentKey());
		values.put(DBMediaOpenHelper.COL_NUM_SEASON, episode.getSeasonNum());
		values.put(DBMediaOpenHelper.COL_NUM_EPISODE, episode.getEpisodeNum());
		values.put(DBMediaOpenHelper.COL_TITLE, episode.getTitle());
		return values;
	}
	
	/**
	 * Private method to iterate through a Cursor's result rows and create a List of Episode objects
	 * @param cursor Query result cursor
	 * @return Collection of Episodes
	 */
	private static List<Episode> getEpisodesFromCursor(Cursor cursor)
	{
		List<Episode> episodes = new ArrayList<Episode>();
		
		// ensure there are results and move cursor to the first one
		if(cursor.moveToFirst())
		{
			// iterate through results and add Episodes to the List
			Episode ep;
			while (!cursor.isAfterLast())
			{
				ep = cursorToEpisode(cursor);
				episodes.add(ep);
				cursor.moveToNext();
			}
		}
		else
		{
			// there were no result rows found
			if (MyLog.warn) MyLog.w("EpisodeDataSource", "No results to extract from cursor");
		}
		// return Episodes. Empty if cursor empty.
		return episodes;
	}
	
	/**
	 * Retrieve all Primary Keys from a cursor query.
	 * Query must have key as column 0.
	 * @param cursor
	 * @return
	 */
	private static List<Long> getKeysFromCursor(Cursor cursor)
	{
		List<Long> episodeKeys = new ArrayList<Long>();
		
		// ensure there are results and move cursor to the first one
		if (cursor.moveToFirst())
		{
			// iterate through the results and add Keys to the List
			while (!cursor.isAfterLast())
			{
				episodeKeys.add(cursor.getLong(0));
				cursor.moveToNext();
			}
		}
		else
		{
			// no results found
			if (MyLog.warn) MyLog.w("EpisodeDataSource", "No Keys to extract from cursor");
		}
		// return found Keys. Empty if cursor empty
		return episodeKeys;
	}
	
}
