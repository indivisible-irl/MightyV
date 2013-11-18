package com.indivisible.mightyv.activities.testing;

import java.util.ArrayList;
import java.util.List;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;
import com.tvrage.api.SearchXMLParser;

import android.os.Bundle;
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
		setContentView(R.layout.activity_test_search);
		
		TAG = this.getClass().getSimpleName();
		
		initViews();
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
	
	
	private void performSearch(String searchTerm)
	{
		SearchXMLParser search = new SearchXMLParser(searchTerm);
		shows = search.performSearch();
		
		List<String> showDetails = new ArrayList<String>();
		for (Show show : shows)
		{
			showDetails.add(show.toString());
		}
		
		adapter = new ArrayAdapter<String>(
				this.getApplicationContext(),
				android.R.layout.simple_list_item_1,
				showDetails)
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
	
	


}
