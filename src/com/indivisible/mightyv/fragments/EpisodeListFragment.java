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
import com.indivisible.mightyv.data.Episode;
import com.indivisible.mightyv.util.EpisodeArrayAdapter;
import com.indivisible.mightyv.util.MyLog;

public class EpisodeListFragment
        extends ListFragment
        implements OnItemLongClickListener
{

    //=================================================//
    //    Data
    //=================================================//

    private String TAG;
    private List<Episode> episodes;
    private EpisodeArrayAdapter adapter;
    private OnEpisodeClickListener episodeClickListener;

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
        episodes = new ArrayList<Episode>();
        adapter = new EpisodeArrayAdapter(this.getActivity().getApplicationContext(), episodes);
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
            episodeClickListener = (OnEpisodeClickListener) parentActivity;
            episodeClickListener = (OnEpisodeClickListener) parentActivity;
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
        episodeClickListener = null;
        episodeClickListener = null;
    }


    //=================================================//
    //    Interfaces
    //=================================================//

    public interface OnEpisodeClickListener
    {

        public void onEpisodeShortClick(Episode episode);
        public void onEpisodeLongClick(Episode episode);
    }


    //=================================================//
    //    Click Methods
    //=================================================//


    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Episode selectedEpisode = adapter.getItem(position);
        // Display Show details etc
        Toast.makeText(getActivity().getApplicationContext(),
                       "ShowListFragment short click:\n" + selectedEpisode.formattedTitle(),
                       Toast.LENGTH_SHORT).show();
        episodeClickListener.onEpisodeShortClick(selectedEpisode);
    }

    @Override
    public boolean
            onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Episode selectedEpisode = adapter.getItem(position);
        if (MyLog.verbose)
            MyLog.v(TAG,
                    String.format("long clicked item %d: %s",
                                  position,
                                  selectedEpisode.formattedNumbers()));

        adapter.remove(selectedEpisode);
        adapter.notifyDataSetChanged();

        episodeClickListener.onEpisodeLongClick(episodes.get(position));
        return true;
    }


    //=================================================//
    //    Public List Access Methods
    //=================================================//


    public void setList(List<Episode> episodeList)
    {
        adapter.clear();
        adapter.addAll(episodeList);
        adapter.notifyDataSetChanged();
    }

    public void clearList()
    {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void addToList(Episode episode)
    {
        adapter.add(episode);
        adapter.notifyDataSetChanged();
    }

    public void removeFromList(Episode episode)
    {
        adapter.remove(episode);
    }


}
