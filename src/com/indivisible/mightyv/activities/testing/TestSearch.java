package com.indivisible.mightyv.activities.testing;

import java.util.ArrayList;
import java.util.List;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;
import com.tvrage.api.SearchXMLParser;

import android.os.AsyncTask;
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
import android.app.Activity;
import android.graphics.Color;

public class TestSearch extends Activity implements OnClickListener 
{

	private String TAG;
	
	private List<Show> shows;
	private ArrayAdapter<String> adapter;
	
	EditText etSearchTerm;
	Button   bSearch;
	ListView lvShows;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_search);
		
		TAG = this.getClass().getSimpleName();
		
		initViews();
		initListView();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.test_search_searchButton:
				String searchTerm = etSearchTerm.getText().toString();
				MyLog.i(TAG, "Beginning search for: " +searchTerm);
				performSearch(searchTerm);
				break;
			default:
				
				break;
		}
		
	}
	
	
	private void initViews()
	{
		etSearchTerm = (EditText) findViewById(R.id.test_search_searchTerm);
		bSearch		 = (Button)   findViewById(R.id.test_search_searchButton);
		lvShows		 = (ListView) findViewById(R.id.test_search_listview);
		bSearch.setOnClickListener(this);
	}
	
	
	private void initListView()
	{
		adapter = new ArrayAdapter<String>(
				this.getApplicationContext(),
				android.R.layout.simple_list_item_1,
				new ArrayList<String>())
			{
				@Override
				public View getView(int position, View convertView, ViewGroup parent)
				{
					View view = super.getView(position, convertView, parent);
					TextView textView=(TextView) view.findViewById(android.R.id.text1);
					textView.setTextColor(Color.BLACK);
					return view;
				}
			};
		lvShows.setAdapter(adapter);
	}
	
	
	private void performSearch(String searchTerm)
	{
		if (MyLog.debug) MyLog.d(TAG, "Triggering search. Term: " +searchTerm);
		new SearchTask().execute(searchTerm);
	}
	
	//REM Copy into final Search Activity
	class SearchTask extends AsyncTask<String, Void, List<Show> >
	{
		@Override
		protected void onPreExecute()
		{
			MyLog.v(TAG, "Beginning SearchTask...");
			adapter.clear();
		}
		
		@Override
		protected List<Show> doInBackground(String... searchTerms)
		{
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
				adapter.add(show.toString());
			}
			adapter.notifyDataSetChanged();
			Log.v(TAG, "Update complete");
		}
	}


}
