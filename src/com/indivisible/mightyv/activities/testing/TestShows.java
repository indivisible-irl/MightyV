package com.indivisible.mightyv.activities.testing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.data.ShowDataSource;
import com.indivisible.mightyv.util.MyLog;

//FIXME Use Fragments for actual implementation

/**
 * Activity to test the functionality of the Show database interactions
 * @author indiv
 */
public class TestShows extends Activity implements OnClickListener 
{
	
	//=================================================//
	//		data
	//=================================================//
	
	private String TAG;
	
	// Show and ListView
	ShowDataSource showSource;
	List<Long> savedShows = null;	//FIXME Don't save the ids like this. Manipulate the db directly.
	ArrayAdapter<String> adapter;	//TODO Custom Adapter and better ListView row
	ListView lvShows;
	
	// text fields
	EditText etTitle;
	EditText etStatus;
	EditText etRageID;
	
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
		setContentView(R.layout.activity_test_shows);

		TAG = this.getClass().getSimpleName();
		showSource = new ShowDataSource(this.getApplicationContext());
		
		initViews();
		initList();
	}
	
	// Called when the Activity loses focus
	@Override
	protected void onPause()
	{
		super.onPause();
		MyLog.d(TAG, "onPause() for TestShows");
	}
	
	// Called when the Activity is closing down (removed from memory)
	@Override
	protected void onStop()
	{
		super.onStop();
		MyLog.d(TAG, "onStop() for TestShows");
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
		etTitle  = (EditText) findViewById(R.id.test_show_addTitle);
		etStatus = (EditText) findViewById(R.id.test_show_addStatus);
		etRageID = (EditText) findViewById(R.id.test_show_addRageID);
		bDeleteAll = (Button) findViewById(R.id.test_show_buttonDeleteAll);
		bDeleteOne = (Button) findViewById(R.id.test_show_buttonDelOne);
		bSaveShow  = (Button) findViewById(R.id.test_show_buttonSave);
		
		// ListView to hold the Shows from the database
		lvShows = (ListView) findViewById(R.id.test_show_listview);
		
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
			showSource.openReadable();
			List<Show> allShows = showSource.getAllShows();
			showSource.close();
			
			List<String> showDetails = new ArrayList<String>();
			for (Show show : allShows)
			{
				String info = show.info();
				MyLog.i(TAG, "Show: " +show.getKey()+ " - " +show.getTitle());
				showDetails.add(info);
			}
			
			MyLog.i(TAG, "Shows found: " +showDetails.size());
			MyLog.d(TAG, "Making adapter");
			
			adapter = new ArrayAdapter<String>(
					this.getApplicationContext(),
					android.R.layout.simple_list_item_1,
					showDetails)
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
			lvShows.setAdapter(adapter);
			
		}
		catch (SQLException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Error while attempting to open database to load Shows");
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
			case R.id.test_show_buttonDeleteAll:
				deleteAll();
				initList();		//FIXME temp reuse for testing only. Custom adapter, proper member handling etc later
				break;
				
			case R.id.test_show_buttonDelOne:
				deleteOne();
				initList();
				break;
				
			case R.id.test_show_buttonSave:
				saveShow();
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
		showSource.openWritable();
		List<Long> failedDeletes = showSource.deleteAllShows();
		showSource.close();
		
		if (failedDeletes.size() > 0)
		{
			MyLog.e(TAG, "Failed to delete: " +failedDeletes.size());
		}
	}
	
	/**
	 * Delete a single Show. Only works for saved in current session ATM (testing)
	 */
	private void deleteOne()	// real method should accept long([])
	{
		if (savedShows != null && savedShows.size() > 0) {
			showSource.openWritable();
			showSource.deleteShow(savedShows.get(0));
			showSource.close();
			
			savedShows.remove(0);
		}
	}
	
	/**
	 * Save a new Show with details taken from the EditText fields
	 */
	private void saveShow()
	{
		//TODO do input validation on the EditTexts
		String strRageID = etRageID.getText().toString();
		String status    = etStatus.getText().toString();
		String title     = etTitle.getText().toString();
		int rageID = Integer.parseInt(strRageID);
		
		showSource.openWritable();
		Show newShow = showSource.createShow(rageID, status, title);
		showSource.close();
		
		if (savedShows == null) savedShows = new ArrayList<Long>();
		savedShows.add(newShow.getKey());
	}
	
}
