package com.indivisible.mightyv.util;

import java.util.List;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowArrayAdapter extends ArrayAdapter<Show>
{

	//// data
	
	private String TAG;
	private final Context context;
	private final List<Show> shows;
	
	
	//// constructor
	
	public ShowArrayAdapter(Context context, List<Show> shows)
	{
		super(context, R.layout.row_show_simple, shows);
		
		this.TAG = this.getClass().getSimpleName();
		this.context = context;
		this.shows = shows;
	}
	
	
	//// override methods
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (MyLog.debug) MyLog.d(TAG, "Creating Adapter View...");
		
		LayoutInflater inflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowShowSimple = inflater.inflate(R.layout.row_show_simple, parent, false);
	    
		TextView showTitle   = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_title);
	    TextView showStatus  = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_status);
	    TextView showYears   = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_years);
	    TextView showCountry = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_country);
//	    ImageView iconStatus = (ImageView) rowShowSimple.findViewById(R.id.row_showsimple_icon_status);
	    
	    showTitle.setTextColor(Color.BLACK);
	    showStatus.setTextColor(Color.BLACK);
	    showYears.setTextColor(Color.BLACK);
	    showCountry.setTextColor(Color.BLACK);
	    
	    showTitle.setText(shows.get(position).getTitle());
	    showStatus.setText(shows.get(position).getStatus());
	    showYears.setText(shows.get(position).getYearsString());
	    showCountry.setText(shows.get(position).getCountry());
		
	    //TODO assign iconStatus here based on show status
	    
		return rowShowSimple;
	}
	
	
	
}
