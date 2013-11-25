package com.indivisible.mightyv.fragments;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.ShowArrayAdapter;

public class ShowListFragment
        extends ListFragment
        implements OnItemClickListener, OnItemLongClickListener
{

    //=================================================//
    //    Data
    //=================================================//

    private List<Show> shows;
    private ShowArrayAdapter adapter;

    //=================================================//
    //    Fragment Methods
    //=================================================//

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        shows = new ArrayList<Show>();
        adapter = new ShowArrayAdapter(this.getActivity()
                .getApplicationContext(), shows);

        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //=================================================//
    //    Click Methods
    //=================================================//

    @Override
    public void
            onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        Show selectedShow = adapter.getItem(getSelectedItemPosition());
        // Display Show details etc
        Toast.makeText(getActivity().getApplicationContext(),
                       selectedShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> arg0,
                                   View arg1,
                                   int arg2,
                                   long arg3)
    {
        Show selectedShow = adapter.getItem(getSelectedItemPosition());
        // Remove show from list
        adapter.remove(selectedShow);
        return true;
    }

}
