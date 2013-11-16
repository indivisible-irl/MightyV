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
		// insert and get show's key id
		ContentValues values = new ContentValues();
		values.put(DBMediaOpenHelper.COL_SHOW_FK, 	   showKey);
		values.put(DBMediaOpenHelper.COL_NUM_SEASON,   seasonNum);
		values.put(DBMediaOpenHelper.COL_NUM_EPISODE,  episodeNum);
		values.put(DBMediaOpenHelper.COL_TITLE,        title);
		long episodeKey = db.insert(DBMediaOpenHelper.TABLE_EPISODES, null, values);
		
		if (MyLog.debug) MyLog.d(TAG, "Saved Episode. Key: " +episodeKey);
		
		// retrieve saved show and return
		Episode newEpisode =  getEpisodeByKey(episodeKey);
		if (MyLog.verbose) MyLog.v(TAG, "Created new Episode: " +newEpisode.toString());
		return newEpisode;
	}
	
	//==== Read ====//
	
	/**
	 * Retrieve a stored Episode from its database identified by its Primary Key (long)
	 * @param episodeKey  Episode's Primary Key
	 * @return Episode matching Key
	 */
	public Episode getEpisodeByKey(long episodeKey)
	{
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_EPISODES,
				allColumns,
				DBMediaOpenHelper.COL_KEY +" = "+ episodeKey,
				null, null, null, null);
		if (MyLog.debug) MyLog.d(TAG, "Retrieve Episode. Results: " +cursor.getCount());
		
		Episode episode = null;
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			episode = cursorToEpisode(cursor);
		}
		else
		{
			if (MyLog.error) MyLog.e(TAG, "No results in cursor");
		}
		
		if (MyLog.verbose) MyLog.v(TAG, "Retrieved Episode from db: " +episode.toString());
		return episode;
	}
	
	/**
	 * Retrieve a List of all Episode Primary Keys
	 * @return List of Longs of keys
	 */
	public List<Long> getAllEpisodeKeys()
	{
		List<Long> keys = new ArrayList<Long>();
		
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_EPISODES,
				new String[] { DBMediaOpenHelper.COL_KEY },
				null, null, null, null, null);
		if (MyLog.verbose) MyLog.v(TAG, "Gathering all Episode keys. Found: " +cursor.getCount());
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			keys.add(cursor.getLong(0));
			cursor.moveToNext();
		}
		
		if (MyLog.verbose) MyLog.v(TAG, "Processed keys: " +keys.size());
		return keys;
	}
	
	/**
	 * Retrieve all stored Episodes in the database.
	 * Will return every Episode saved and as such should not be used unless explicitly needed.
	 * @return List of all Episodes
	 */
	public List<Episode> getAllEpisodes()
	{
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_EPISODES,
				allColumns,
				null, null, null, null, null);		// null 'selection' param returns all rows in table
		List<Episode> episodes = getEpisodesFromCursor(cursor);
		if (MyLog.verbose) MyLog.v(TAG, "Episodes parsed and returned: " +episodes.size());
		
		return episodes;
	}
	
	/**
	 * Retrieve all Episodes for one Show. Sorts by season and episode numbers.
	 * @param showId The Foreign Key identifying the Episode parent to filter by
	 * @return Collection of Episodes
	 */
	public List<Episode> getAllEpisodesForShow(long showId)
	{
		Cursor cursor = db.query(
				DBMediaOpenHelper.TABLE_EPISODES,
				allColumns,
				DBMediaOpenHelper.COL_SHOW_FK +" = "+ showId,
				null, null, null,
				DBMediaOpenHelper.COL_NUM_SEASON +"ASC, "+ DBMediaOpenHelper.COL_NUM_EPISODE +"ASC");
		if (MyLog.verbose)
		{
			MyLog.v(TAG, "Retrieving Show Episodes for showId: " +showId);
			MyLog.v(TAG, "Found: " +cursor.getCount());
		}
		
		List<Episode> episodes = getEpisodesFromCursor(cursor);
		if (MyLog.verbose) MyLog.v(TAG, "Episodes parsed and returned: " +episodes.size());
		return episodes;
	}
	
	//TODO future Read methods
	public List<Episode> _getAllEpisodesForSeason(long showId, int seasonNum) {return null;}
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
		ContentValues values = getValuesFromEpisode(episode);
		
		//TODO start a db transaction here to ensure only one Episode/row is updated
		int rowsAffected = db.update(
				DBMediaOpenHelper.TABLE_EPISODES,
				values,
				DBMediaOpenHelper.COL_KEY +" = "+ episode.getKey(),
				null);
		if (rowsAffected == 1)
		{
			if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +episode.toString());
			//todo commit changes
			return true;
		}
		else
		{
			if (MyLog.error)
			{
				MyLog.e(TAG, "Failed horribly while attempting to save Episode: " +episode.toString());
				MyLog.e(TAG, "Rows affected: " +rowsAffected);
			}
			//todo revert changes
			return false;
		}
	}
	
	//==== Delete ====//
	
	/**
	 * Delete an Episode's database entry identified by its Primary Key (long)
	 * @param episodeKey The Episode to be deleted's Primary Key
	 * @return boolean indicating successful deletion
	 */
	public boolean deleteEpisode(long episodeKey)
	{
		//TODO start a db transaction here to ensure no more than one row is deleted
		//ASK or do it when calling delete (between open and close of db handle)
		int rowsAffected = db.delete(
				DBMediaOpenHelper.TABLE_EPISODES, 
				DBMediaOpenHelper.COL_KEY +" = "+ episodeKey,
				null);
		if (rowsAffected == 1)
		{
			if (MyLog.info) MyLog.i(TAG, "Deleted Episode with Key: " +episodeKey);
			//todo commit changes
			return true;
		}
		else
		{
			if (MyLog.warn)
			{
				MyLog.w(TAG, "Tried to delete Episode but it didn't go to plan. episodeKey: " +episodeKey);
				MyLog.w(TAG, "Rows affected: " +rowsAffected);
			}
			//todo revert changes
			return false;
		}
	}
	
	/**
	 * Delete a number of Episodes at once
	 * @param episodeKeys List of Logs of Episode Primary Keys
	 * @return List of Longs of any failed deletes
	 */
	public List<Long> deleteMultipleEpisodes(List<Long> episodeKeys)
	{
		if (MyLog.debug) MyLog.d(TAG, "Delete multiple Episodes. Recieved Keys: " +episodeKeys);
		List<Long> failedDeletes = new ArrayList<Long>();
		
		for (Long key : episodeKeys)
		{
			if (!deleteEpisode(key))
			{
				if (MyLog.error) MyLog.e(TAG, "Couldn't delete Episode: " +key);
				failedDeletes.add(key);
			}
		}
		return failedDeletes;
	}
	
	/**
	 * Delete all Episodes in the database
	 * @return List of Episode keys that didn't delete
	 */
	public List<Long> deleteAllEpisodes()
	{
		List<Long> allEpisodeKeys = getAllEpisodeKeys();
		if (MyLog.debug) MyLog.d(TAG, "All Episodes. Found: " +allEpisodeKeys.size());
		
		List<Long> failedDeletes = deleteMultipleEpisodes(allEpisodeKeys);
		if (MyLog.warn && failedDeletes.size() > 0)
		{
			MyLog.w(TAG, "Couldn't delete some Episodes. Total: " +failedDeletes.size());
		}
		return failedDeletes;
	}
	
	//=================================================//
	//		private methods
	//=================================================//
	
	/**
	 * Craft a single Episode object from a Cursor's current result
	 * @param cursor Database query Cursor set to desired position
	 * @return Episode
	 */
	private static Episode cursorToEpisode(Cursor cursor)
	{
		Episode ep = new Episode();
		ep.setKey(cursor.getLong(0));
		ep.setParentKey(cursor.getLong(1));
		ep.setSeasonNum(cursor.getInt(2));
		ep.setEpisodeNum(cursor.getInt(3));
		ep.setTitle(cursor.getString(4));
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
		
		cursor.moveToFirst();
		Episode ep;
		while (!cursor.isAfterLast())
		{
			ep = cursorToEpisode(cursor);
			episodes.add(ep);
			cursor.moveToNext();
		}
		return episodes;
	}
	
}
