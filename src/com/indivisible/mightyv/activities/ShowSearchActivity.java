package com.indivisible.mightyv.activities;

import java.util.ArrayList;
import java.util.List;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.fragments.ShowListFragment;
import com.indivisible.mightyv.util.MyLog;
import com.indivisible.mightyv.util.ShowArrayAdapter;
import com.tvrage.api.SearchXMLParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ShowSearchActivity extends Activity implements OnClickListener
{

	//// data
	
	private String TAG;
	private List<Show>       showQueue;
	private ShowArrayAdapter adapter;
	
//	private ShowListFragment lvShowQueue;
	private ListView         lvShows;
	private EditText         etSearchTerm;
	private Button           bSearch;
	
	
	//// initialise methods
	
	private void init()
	{	
		lvShows = (ListView) findViewById(R.id.show_search_listview_results);
		etSearchTerm = (EditText) findViewById(R.id.show_search_searchTerm);
		bSearch      = (Button)   findViewById(R.id.show_search_bSearch);
		bSearch.setOnClickListener(this);
		
		showQueue = new ArrayList<Show>();
		adapter   = new ShowArrayAdapter(
				this.getApplicationContext(), showQueue);
		lvShows.setAdapter(adapter);
	}
	//// activity methods
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TAG = this.getClass().getSimpleName();
		if (MyLog.debug) MyLog.d(TAG, "Starting " +TAG);
		
		setContentView(R.layout.activity_show_search);
		init();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//TODO save list
	}

	@Override
	protected void onResume() {
		//TODO restore list
		super.onResume();
	}
	
	//// click handling

	@Override
	public void onClick(View v) {
		if (MyLog.debug) MyLog.d(TAG, "Buttonpress");
		switch (v.getId())
		{
			case R.id.show_search_bSearch:
				String searchTerm = etSearchTerm.getText().toString();
				MyLog.i(TAG, "Beginning search for: " +searchTerm);
				performSearch(searchTerm);
				break;
			default:
				
				break;
		}
	}
	
	//// searching
	
	private void performSearch(String searchTerm)
	{
		new SearchTask().execute(searchTerm);
	}
	

	class SearchTask extends AsyncTask<String, Void, List<Show> >
	{
		private ProgressDialog dialog = new ProgressDialog(ShowSearchActivity.this);
		
		@Override
		protected void onPreExecute()
		{
			MyLog.v(TAG, "Beginning SearchTask...");
			dialog.setMessage("Searching...");
			dialog.show();
			adapter.clear();
		}
		
		@Override
		protected List<Show> doInBackground(String... searchTerms)
		{
			List<Show> shows = new ArrayList<Show>();
			SearchXMLParser search = new SearchXMLParser();
			search.setSearch(searchTerms[0]);
			shows = search.performSearch();
			
			return shows;
		}
		
		@Override
		protected void onPostExecute(List<Show> shows)
		{
			MyLog.v(TAG, "Updating adapter with results: " +shows.size());
			for (Show show : shows)
			{
				if (MyLog.debug) MyLog.d(TAG, "Adding show: " +show.toString());
				adapter.add(show);
			}
//			adapter.addAll(shows);
			adapter.notifyDataSetChanged();
			Log.v(TAG, "Update complete");
			
			if (dialog.isShowing())
				dialog.dismiss();
		}
	}

	
	
	
	
}
