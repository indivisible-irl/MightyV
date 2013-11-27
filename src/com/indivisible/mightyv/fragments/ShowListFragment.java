package com.indivisible.mightyv.fragments;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.ShowArrayAdapter;

public class ShowListFragment
        extends ListFragment
{

    //=================================================//
    //    Data
    //=================================================//

    private List<Show> shows;
    private ShowArrayAdapter adapter;
    private OnShowShortClickListener shortClickListener;
    private OnShowLongClickListener longClickListener;

    //=================================================//
    //    Fragment Methods
    //=================================================//

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
    public void onAttach(Activity parentActivity)
    {
        super.onAttach(parentActivity);
        // Ensure the parent activity has implemented our interfaces
        try
        {
            shortClickListener = (OnShowShortClickListener) parentActivity;
            longClickListener = (OnShowLongClickListener) parentActivity;
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
        shortClickListener = null;
        longClickListener = null;
    }


    //=================================================//
    //    Interfaces
    //=================================================//

    public interface OnShowShortClickListener
    {

        public void onShowShortClick(Show show);
    }

    public interface OnShowLongClickListener
    {

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
        shortClickListener.onShowShortClick(selectedShow);
    }


    //    @Override
    //    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    //    {
    //        Show selectedShow = adapter.getItem(getSelectedItemPosition());
    //        // Display Show details etc
    //        Toast.makeText(getActivity().getApplicationContext(),
    //                       "ShowListFragment short click:\n" + selectedShow.getTitle(),
    //                       Toast.LENGTH_SHORT).show();
    //        shortClickListener.onShowShortClick(selectedShow);
    //    }
    //
    //
    //    @Override
    //    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    //    {
    //        Show selectedShow = adapter.getItem(getSelectedItemPosition());
    //        // Remove show from list
    //        adapter.remove(selectedShow);
    //        longClickListener.onShowLongClick(selectedShow);
    //        return true;
    //    }


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
        //TODO notify outside add?
    }

    public void removeFromList(Show show)
    {
        adapter.remove(show);
    }


}
