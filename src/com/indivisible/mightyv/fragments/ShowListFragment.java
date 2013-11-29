package com.indivisible.mightyv.fragments;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;
import com.indivisible.mightyv.util.ShowArrayAdapter;

public class ShowListFragment
        extends ListFragment
        implements OnItemLongClickListener
{

    //=================================================//
    //    Data
    //=================================================//

    private String TAG;
    private List<Show> shows;
    private ShowArrayAdapter adapter;
    private OnShowClickListener showClickListener;

    //=================================================//
    //    Fragment Methods
    //=================================================//

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        shows = new ArrayList<Show>();
        adapter = new ShowArrayAdapter(this.getActivity().getApplicationContext(), shows);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedState)
    {
        super.onActivityCreated(savedState);

        this.getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onAttach(Activity parentActivity)
    {
        super.onAttach(parentActivity);
        // Ensure the parent activity has implemented our interfaces
        try
        {
            showClickListener = (OnShowClickListener) parentActivity;
            showClickListener = (OnShowClickListener) parentActivity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(parentActivity.toString()
                    + " must implement OnClickListener and OnLongClickListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        showClickListener = null;
        showClickListener = null;
    }


    //=================================================//
    //    Interfaces
    //=================================================//

    public interface OnShowClickListener
    {

        public void onShowShortClick(Show show);

        public void onShowLongClick(Show show);
    }


    //=================================================//
    //    Click Methods
    //=================================================//


    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Show selectedShow = adapter.getItem(position);
        // Display Show details etc
        Toast.makeText(getActivity().getApplicationContext(),
                       "ShowListFragment short click:\n" + selectedShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
        showClickListener.onShowShortClick(selectedShow);
    }

    @Override
    public boolean
            onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Show clickedShow = adapter.getItem(position);
        if (MyLog.verbose)
            MyLog.v(TAG,
                    String.format("long clicked item %d: %s",
                                  position,
                                  clickedShow.getTitle()));

        adapter.remove(clickedShow);
        adapter.notifyDataSetChanged();

        showClickListener.onShowLongClick(shows.get(position));
        return true;
    }


    //=================================================//
    //    Public List Access Methods
    //=================================================//


    public void setList(List<Show> shows)
    {
        adapter.clear();
        adapter.addAll(shows);
        adapter.notifyDataSetChanged();
    }

    public void clearList()
    {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void addToList(Show show)
    {
        adapter.add(show);
        adapter.notifyDataSetChanged();
        //ASK notify outside add?
    }

    public void removeFromList(Show show)
    {
        adapter.remove(show);
    }


}
