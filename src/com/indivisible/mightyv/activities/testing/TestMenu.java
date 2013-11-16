package com.indivisible.mightyv.activities.testing;

import com.indivisible.mightyv.util.MyLog;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity;
import android.content.Intent;

/**
 * A basic Activity to contain a menu for use during development.
 * Use it to access Activities (testing or otherwise) directly.
 * Bypasses the need for a robust UI at this early stage. 
 * @author indiv
 */
public class TestMenu extends ListActivity
{

	//=================================================//
	//		data
	//=================================================//
	
	// TAG for logging
	private String TAG;
	
	
	/**
	 * Add all Activities that we would like to access from the TestMenu Activity.
	 * Include path from src/com/indivisible/mightyv/ and above 
	 */
	String[] testActivities = new String[] {
			"activities.testing.TestShows",
			"activities.testing.TestEpisodes"
		};
	
	
	//=================================================//
	//		Activity Lifecycle methods
	//=================================================//
	
	// This method will always get called when an Activity gets created.
	//   See the Android Activity Lifecycle for more info on these sorts of methods and when they trigger
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		
		// populate the ListActivity's ListView with the Strings from testActivities
		super.setListAdapter(
				new ArrayAdapter<String>(
						TestMenu.this,
						android.R.layout.simple_list_item_1,
						testActivities));
	}
	
	
	//=================================================//
	//		ListView methods
	//=================================================//
	
	// This methods gets invoked whenever one of the ListActivity's rows gets clicked.
	//   We will use it to start the assigned Activity directly. 
	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id)
	{
		super.onListItemClick(lv, v, position, id);
		
		String activityPath = "<NULL>";
		Class<?> newActivity = null;
		try
		{
			String activityStr = testActivities[position];
			activityPath = "com.indivisible.mightyv." + activityStr;
			newActivity = Class.forName(activityPath);  // manually create the class path
			if (MyLog.debug) MyLog.d(TAG, "Activity class path: " +newActivity);
			Intent ourIntent = new Intent(TestMenu.this, newActivity);
			startActivity(ourIntent);
		}
		catch (ClassNotFoundException e)	//if the chosen item cannot be formed into a valid class path
		{
			// inform the user that the selected item is not formed correctly and cannot be started as an Activity
			MyLog.e(TAG, "Class failed: " +activityPath);
			Toast toast = Toast.makeText(TestMenu.this, "Class not found:\n" +newActivity, Toast.LENGTH_SHORT);
			toast.show();
		}
	
	}


}
