package com.indivisible.mightyv.data;

import com.indivisible.mightyv.util.MyLog;

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
	
	public Episode createEpisode(long showKey, int seasonNum, int episodeNum, String title)
	{
		// insert and get show's key id
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COL_SHOW_FK, showKey);
		values.put(DatabaseOpenHelper.COL_NUM_SEASON, seasonNum);
		values.put(DatabaseOpenHelper.COL_NUM_EPISODE, episodeNum);
		values.put(DatabaseOpenHelper.COL_TITLE, title);
		long episodeKey = db.insert(DatabaseOpenHelper.TABLE_EPISODES, null, values);
		
		// retrieve saved show and return
		return getEpisodeByKey(episodeKey);
	}
	
	public Episode getEpisodeByKey(long episodeKey)
	{
		Cursor cursor = db.query(
				DatabaseOpenHelper.TABLE_EPISODES,
				allColumns,
				DatabaseOpenHelper.COL_KEY +" = "+ episodeKey,
				null, null, null, null);
		Episode episode = cursorToEpisode(cursor);
		return episode;
	}
	
	// future methods
	public Episode __getLatestEpisode() {return null;}
	public Episode __getLastWatchedEpisode() {return null;}
	public Episode __getNextUnwatchedEpisode() {return null;}
	public Episode __getNextUnreleasedEpisode() {return null;}
	
	public int updateEpisode(Episode episode)
	{
		ContentValues values = getValuesFromEpisode(episode);
		
		//TODO start a db transaction here to ensure only one Episode/row is updated
		int numRowsAffected = db.update(
				DatabaseOpenHelper.TABLE_EPISODES,
				values,
				DatabaseOpenHelper.COL_KEY +" = "+ episode.getKey(),
				null);
		if (numRowsAffected == 0)
		{
			if (MyLog.error) MyLog.e(TAG, "Attempted but failed to update Episode: "
					+episode.getKey()+ " - " +episode.getTitle());
		}
		else if (numRowsAffected == 1)
		{
			if (MyLog.verbose) MyLog.v(TAG, "Updated episode: " +episode.getKey());
		}
		else
		{
			if (MyLog.error) MyLog.e(TAG, "Failed horribly while attempting to save Episode: "
					+episode.getKey()+ " - " +episode.getTitle());
		}
		return numRowsAffected;
	}
	
	public boolean deleteEpisode(long episodeKey)
	{
		//TODO start a db transaction here to ensure no more than one row is deleted
		int rowsAffected = db.delete(
				DatabaseOpenHelper.TABLE_EPISODES, 
				DatabaseOpenHelper.COL_KEY +" = "+ episodeKey,
				null);
		if (rowsAffected == 0)
		{
			if (MyLog.warn) MyLog.w(TAG, "Attempted to delete Episode. Failed. episodeKey: " +episodeKey);
			return false;
		}
		else if (rowsAffected == 1)
		{
			if (MyLog.info) MyLog.i(TAG, "Deleted Episode with Key: " +episodeKey);
			return true;
		}
		else
		{
			if (MyLog.error) MyLog.e(TAG, "Attempted deletion of Episode resulted in multiple affected rows. episodeKey: " +episodeKey);
			return false;
		}
				
	}
	
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
