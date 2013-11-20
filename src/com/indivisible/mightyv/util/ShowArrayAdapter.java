package com.indivisible.mightyv.util;

import java.util.List;

import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowArrayAdapter extends ArrayAdapter<Show>
{
	private static final int    SHOW_LIST_ROW_SIMPLE = R.layout.row_show_simple;
	private static final String SHOW_YEARS_FORMAT = "%s - %s";
	
	private final Context context;
	private final List<Show> shows;
	
	public ShowArrayAdapter(Context context, List<Show> shows)
	{
		super(context, SHOW_LIST_ROW_SIMPLE, shows);
		
		this.context = context;
		this.shows = shows;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowShowSimple = inflater.inflate(R.layout.row_show_simple, parent, false);
	    
		TextView showTitle   = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_title);
	    TextView showStatus  = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_status);
	    TextView showYears   = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_years);
	    TextView showCountry = (TextView)  rowShowSimple.findViewById(R.id.row_showsimple_text_country);
//	    ImageView iconStatus = (ImageView) rowShowSimple.findViewById(R.id.row_showsimple_icon_status);
	    
	    showTitle.setText(shows.get(position).getTitle());
	    showStatus.setText(shows.get(position).getStatus());
	    showYears.setText(String.format(SHOW_YEARS_FORMAT, 
	    			shows.get(position).getStarted(), shows.get(position).getEnded()));
	    showCountry.setText(shows.get(position).getCountry());
		
	    //TODO assign iconStatus here based on show status
	    
		return rowShowSimple;
	}
	
	
	
}
