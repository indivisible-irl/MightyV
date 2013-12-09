package com.indivisible.mightyv.util;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Episode;

public class EpisodeArrayAdapter
        extends ArrayAdapter<Episode>
{

    //// data

//    private String TAG;
    private final Context context;
    private final List<Episode> episodes;


    //// constructor

    public EpisodeArrayAdapter(Context context, List<Episode> episodes)
    {
        super(context, R.layout.show_row_simple, episodes);

//        this.TAG = this.getClass().getSimpleName();
        this.context = context;
        this.episodes = episodes;
    }


    //// override methods

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
//        if (MyLog.debug) MyLog.d(TAG, "Creating Adapter View...");

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowEpisodeSimple = inflater.inflate(R.layout.episode_row_simple, parent, false);

        TextView episodeTitle = (TextView) rowEpisodeSimple
                .findViewById(R.id.row_episodesimple_text_title);
        TextView episodeInfo = (TextView) rowEpisodeSimple
                .findViewById(R.id.row_episodesimple_text_info);

        episodeTitle.setText(episodes.get(position).formattedTitle());
        episodeInfo.setText(episodes.get(position).getAired());

        //TODO assign iconStatus here based on show status

        return rowEpisodeSimple;
    }


}
