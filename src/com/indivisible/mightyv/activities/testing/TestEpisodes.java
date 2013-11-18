package com.indivisible.mightyv.activities.testing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Episode;
import com.indivisible.mightyv.data.EpisodeDataSource;
import com.indivisible.mightyv.util.MyLog;

//FIXME Use Fragments for actual implementation

/**
 * Activity to test the functionality of the Show database interactions
 * @author indiv
 */
public class TestEpisodes extends Activity implements OnClickListener 
{
	
	//=================================================//
	//		data
	//=================================================//
	
	private String TAG;
	
	// Show and ListView
	EpisodeDataSource episodeSource;
	List<Long> savedEpisodes = null;	//FIXME Don't save the ids like this. Manipulate the db directly.
	ArrayAdapter<String> adapter;	//TODO Custom Adapter and better ListView row
	ListView lvEpisodes;
	
	// text fields
	EditText etShowKey;
	EditText etTitle;
	EditText etNumEpisode;
	EditText etNumSeason;
	
	// buttons
	Button bDeleteAll;
	Button bDeleteOne;
	Button bSaveShow;
	
	//=================================================//
	//		Android Activity methods
	//=================================================//
	
	// Method called as the first action when loading the Activity 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_episodes);

		TAG = this.getClass().getSimpleName();
		episodeSource = new EpisodeDataSource(this.getApplicationContext());
		
		initViews();
		initList();
	}
	
	// Called when the Activity loses focus
	@Override
	protected void onPause()
	{
		super.onPause();
		MyLog.d(TAG, "onPause() for TestEpisodes");
	}
	
	// Called when the Activity is closing down (removed from memory)
	@Override
	protected void onStop()
	{
		super.onStop();
		MyLog.d(TAG, "onStop() for TestEpisodes");
	}
	
	
	//=================================================//
	//		Activity init
	//=================================================//
	
	/**
	 * Method to handle initialising the layout/Activity's different Views
	 */
	private void initViews()
	{
		MyLog.d(TAG, "Init views");
		// assign View fields to the layout's bits
		etShowKey    = (EditText) findViewById(R.id.test_episode_addParentKey);
		etTitle      = (EditText) findViewById(R.id.test_episode_addTitle);
		etNumEpisode = (EditText) findViewById(R.id.test_episode_addNumEpisode);
		etNumSeason  = (EditText) findViewById(R.id.test_episode_addNumSeason);
		bDeleteAll = (Button) findViewById(R.id.test_episode_buttonDeleteAll);
		bDeleteOne = (Button) findViewById(R.id.test_episode_buttonDelOne);
		bSaveShow  = (Button) findViewById(R.id.test_episode_buttonSave);
		
		// ListView to hold the Shows from the database
		lvEpisodes = (ListView) findViewById(R.id.test_episode_listview);
		
		// set the button's to pass their clicks to this class 
		bDeleteAll.setOnClickListener(this);
		bDeleteOne.setOnClickListener(this);
		bSaveShow.setOnClickListener(this);
	}
	
	/**
	 * Populate the ListView
	 */
	private void initList()
	{
		MyLog.d(TAG, "init List");
		try
		{
			episodeSource.openReadable();
			List<Episode> allEpisodes = episodeSource.getAllEpisodes();
			episodeSource.close();
			
			List<String> episodeDetails = new ArrayList<String>();
			for (Episode episode : allEpisodes)
			{
				String info = episode.info();
				MyLog.i(TAG, "Episode: " +episode.getKey()+ " - " +episode.getTitle());
				episodeDetails.add(info);
			}
			
			MyLog.i(TAG, "Episodes found: " +episodeDetails.size());
			MyLog.d(TAG, "Making adapter");
			
			adapter = new ArrayAdapter<String>(
					this.getApplicationContext(),
					android.R.layout.simple_list_item_1,
					episodeDetails)
					{
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							View view =super.getView(position, convertView, parent);

							TextView textView=(TextView) view.findViewById(android.R.id.text1);
							textView.setTextColor(Color.BLACK);

							return view;
						}
					};

			MyLog.d(TAG, "Setting adapter");
			lvEpisodes.setAdapter(adapter);
			
		}
		catch (SQLException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Error while attempting to open database to load Episodes");
			e.printStackTrace();	// wrap with error level?
		}
	}

	//=================================================//
	//		Click handling
	//=================================================//
	
	// Gets called when a click event is passed to the Activity
	// Required when Class implements View.OnClickListener
	@Override
	public void onClick(View v)
	{
		// select what code to execute based on the view's identifier
		switch (v.getId())
		{
			case R.id.test_episode_buttonDeleteAll:
				deleteAll();
				initList();		//FIXME temp reuse for testing only. Custom adapter, proper member handling etc later
				break;
				
			case R.id.test_episode_buttonDelOne:
				deleteOne();
				initList();
				break;
				
			case R.id.test_episode_buttonSave:
				saveEpisode();
				initList();
				break;
		}
	}
	
	//// click methods
	
	/**
	 * Delete ALL Shows from the database.
	 */
	private void deleteAll()
	{
		episodeSource.openWritable();
		boolean successfulDelete = episodeSource.deleteAllEpisodes();
		episodeSource.close();
		
		if (!successfulDelete)
		{
			MyLog.e(TAG, "Failed to delete some Shows");
		}
	}
	
	/**
	 * Delete a single Show. Only works for saved in current session ATM (testing)
	 */
	private void deleteOne()	// real method should accept long([])
	{
		if (savedEpisodes != null && savedEpisodes.size() > 0)
		{
			episodeSource.openWritable();
			episodeSource.deleteOneEpisode(savedEpisodes.get(0));
			episodeSource.close();
			
			savedEpisodes.remove(0);
		}
		else
		{
			Log.w(TAG, "No saved Episodes to delete");
		}
	}
	
	/**
	 * Save a new Show with details taken from the EditText fields
	 */
	private void saveEpisode()
	{
		//TODO do input validation on the EditTexts
		String strShowKey = etShowKey.getText().toString();
		String title     = etTitle.getText().toString();
		String strEpNum = etNumEpisode.getText().toString();
		String strSeasNum = etNumSeason.getText().toString();
		
		long showKey = Long.parseLong(strShowKey);
		int epNum = Integer.parseInt(strEpNum);
		int seaNum = Integer.parseInt(strSeasNum);
		
		episodeSource.openWritable();
		Episode newEpisode = episodeSource.createEpisode(showKey, seaNum, epNum, title);
		episodeSource.close();
		
		if (savedEpisodes == null) savedEpisodes = new ArrayList<Long>();
		savedEpisodes.add(newEpisode.getKey());
	}
	
}
