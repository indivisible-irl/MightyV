package com.indivisible.mightyv.fragments;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;
import com.tvrage.api.SearchXMLParser;

/**
 * @author indiv
 * 
 */
public class ShowAddButtonsFragment
        extends Fragment
        implements OnClickListener
{

    //=================================================//
    //    Data
    //=================================================//

    private String TAG;

    private Button bClearList;
    private Button bNewSearch;

    private OnButtonSelectedListener listener;

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
        View view = inflater.inflate(R.layout.show_fragment_search_buttons,
                                     container,
                                     false);

        bClearList = (Button) view.findViewById(R.id.show_fragment_add_clearlist);
        bNewSearch = (Button) view.findViewById(R.id.show_fragment_add_newSearch);
        bClearList.setOnClickListener(this);
        bNewSearch.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // Ensure that the parent activity has implemented the our interface.
        try
        {
            listener = (OnButtonSelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonSelectedListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }


    //=================================================//
    //    Listener
    //=================================================//

    // Parent Activity must implement this interface
    public interface OnButtonSelectedListener
    {

        public void onButtonSelected(int viewId);
    }


    //=================================================//
    //    Click Handling
    //=================================================//

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.show_fragment_add_clearlist:
                listener.onButtonSelected(v.getId());
                break;

            case R.id.show_fragment_add_newSearch:
                askForSearchTerm();
                listener.onButtonSelected(v.getId());
                break;

            default:
                break;
        }
    }


    //=================================================//
    //    Dialogs
    //=================================================//

    private String askForSearchTerm()
    {
        //SearchShowEntryDialog termDialog = new SearchShowEntryDialog();
        //termDialog.

        return null;
    }

    private Show askForResultChoice()
    {


        return null;
    }

    //=================================================//
    //    Search Asynchronously
    //=================================================//

    public void performSearch(String searchTerm)
    {
        //TODO searchTerm dialog
        new SearchTask().execute(searchTerm);
    }


    class SearchTask
            extends AsyncTask<String, Void, List<Show>>
    {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity()
                .getApplicationContext());

        @Override
        protected void onPreExecute()
        {
            MyLog.v(TAG, "Beginning SearchTask...");
            progressDialog.setMessage("Searching...");
            progressDialog.show();
        }

        @Override
        protected List<Show> doInBackground(String... searchTerms)
        {
            List<Show> shows = new ArrayList<Show>();
            SearchXMLParser search = new SearchXMLParser();
            search.setSearch(searchTerms[0]);
            shows = search.performSearch();

            return shows;
        }

        @Override
        protected void onPostExecute(List<Show> shows)
        {
            MyLog.v(TAG, "Displaying results. Count: " + shows.size());


            if (progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

}
