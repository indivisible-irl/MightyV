package com.indivisible.mightyv.activities;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;

/**
 * @author indiv
 * 
 */
public class ShowAddActivity extends Activity
{

    List<Show> showAddQueue;

    View mainSection;
    Button bClearShows;
    Button bFindShow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_search_activity);

        initViews();
    }

    private void initViews()
    {
        mainSection = findViewById(R.id.show_add_mainSection);
        bClearShows = (Button) findViewById(R.id.show_add_clearlist);
        bFindShow = (Button) findViewById(R.id.show_add_newSearch);
    }

    private void setinfoView()
    {

    }

    private void setListView()
    {

    }

}
