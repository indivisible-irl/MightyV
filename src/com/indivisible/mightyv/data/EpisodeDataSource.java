package com.indivisible.mightyv.data;

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
	private DatabaseOpenHelper dbHelper = null;
	private String[] allColumns = {						//REM update allColumns on activating new fields
			DatabaseOpenHelper.COL_KEY,
			DatabaseOpenHelper.COL_SHOW_FK,
			DatabaseOpenHelper.COL_NUM_EPISODE,
			DatabaseOpenHelper.COL_NUM_SEASON
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
		this.dbHelper = new DatabaseOpenHelper(context); 
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
		values.put(DatabaseOpenHelper.COL_SHOW_FK, 	    showKey);
		values.put(DatabaseOpenHelper.COL_NUM_SEASON,   seasonNum);
		values.put(DatabaseOpenHelper.COL_NUM_EPISODE,  episodeNum);
		values.put(DatabaseOpenHelper.COL_TITLE,        title);
		long episodeKey = db.insert(DatabaseOpenHelper.TABLE_EPISODES, null, values);
		
		// retrieve saved show and return
		Episode newEpisode =  getEpisodeByKey(episodeKey);
		if (MyLog.verbose) MyLog.v(TAG, "Created new Episode: " +newEpisode.toString());
		return newEpisode;
	}
	
	/**
	 * Retrieve a stored Episode from its database identified by its Primary Key (long)
	 * @param episodeKey  Episode's Primary Key
	 * @return Episode
	 */
	public Episode getEpisodeByKey(long episodeKey)
	{
		Cursor cursor = db.query(
				DatabaseOpenHelper.TABLE_EPISODES,
				allColumns,
				DatabaseOpenHelper.COL_KEY +" = "+ episodeKey,
				null, null, null, null);
		Episode episode = cursorToEpisode(cursor);
		
		if (MyLog.verbose) MyLog.v(TAG, "Retrieved Episode from db: " +episode.toString());
		return episode;
	}
	
	//TODO future methods
	public Episode _getLatestEpisode() {return null;}
	public Episode _getNextUnreleasedEpisode() {return null;}
	public Episode _getLastWatchedEpisode() {return null;}
	public Episode _getNextUnwatchedEpisode() {return null;}
	
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
				DatabaseOpenHelper.TABLE_EPISODES,
				values,
				DatabaseOpenHelper.COL_KEY +" = "+ episode.getKey(),
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
	
	/**
	 * Delete an Episode's database entry identified by its Primary Key (long)
	 * @param episodeKey The Episode to be deleted's Primary Key
	 * @return boolean indicating successful deletion
	 */
	public boolean deleteEpisode(long episodeKey)
	{
		//TODO start a db transaction here to ensure no more than one row is deleted
		int rowsAffected = db.delete(
				DatabaseOpenHelper.TABLE_EPISODES, 
				DatabaseOpenHelper.COL_KEY +" = "+ episodeKey,
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
		ep.setParentKey((cursor.getInt(1)));
		ep.setEpisodeNum(cursor.getInt(2));
		ep.setTitle(cursor.getString(3));
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
		values.put(DatabaseOpenHelper.COL_SHOW_FK, episode.getParentKey());
		values.put(DatabaseOpenHelper.COL_NUM_SEASON, episode.getSeasonNum());
		values.put(DatabaseOpenHelper.COL_NUM_EPISODE, episode.getEpisodeNum());
		values.put(DatabaseOpenHelper.COL_TITLE, episode.getTitle());
		return values;
	}
	
}
