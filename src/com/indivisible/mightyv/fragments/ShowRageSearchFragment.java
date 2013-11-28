/**
 * 
 */
package com.indivisible.mightyv.fragments;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.dialogs.SelectShowDialog;
import com.indivisible.mightyv.util.MyLog;
import com.tvrage.api.SearchXMLParser;

/*
 * ASK Better to make a base class that extends Fragment and extend that? Use
 * it to hold my common customisations.
 */

/**
 * @author indiv
 * 
 */
public class ShowRageSearchFragment
        extends Fragment
{

    //=================================================//
    //    Data
    //=================================================//

    private String TAG;
    private String searchTerm;
    private List<Show> showResults = null;
    private Context appContext;

    private OnSearchResultChosenListener listener;


    //=================================================//
    //    Fragment Methods
    //=================================================//

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        appContext = this.getActivity();//.getApplicationContext();
    }


    @Override
    public void onAttach(Activity parentActivity)
    {
        super.onAttach(parentActivity);
        try
        {
            listener = (OnSearchResultChosenListener) parentActivity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(parentActivity.toString()
                    + " must implement OnShowChosenListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }


    //=================================================//
    //    Interface / Listener
    //=================================================//
    // Parent Activity must implement our interface
    public interface OnSearchResultChosenListener
    {

        public void onSearchResultChosen(Show chosenShow);
    }


    //=================================================//
    //    Public Methods
    //=================================================//

    /**
     * Get the results from the last performed search
     * 
     * @return List of Shows. Null if search not performed, Empty if no
     *         results
     */
    public List<Show> getResults()
    {
        if (showResults == null)
        {
            if (MyLog.warn) MyLog.w(TAG, "no results, remember to search?");
        }
        else if (showResults.size() == 0)
        {
            if (MyLog.warn) MyLog.w(TAG, "no results, list is empty");
        }
        // return list, whatever state it's in
        //REM test for null to indicate no search performed
        return showResults;
    }

    //=================================================//
    //    Asynchronous api search
    //=================================================//

    /**
     * Trigger the search for a Show by title using the TVRage API.
     * 
     * @param searchFor
     */
    public void performSearch(String searchFor)
    {
        this.searchTerm = searchFor;
        new SearchTask().execute(searchTerm);
    }

    /**
     * Perform search for a Show by title with the TVRage API
     */
    class SearchTask
            extends AsyncTask<String, Void, List<Show>>
    {

        private ProgressDialog progressDialog = new ProgressDialog(appContext);
        private SelectShowDialog selectDialog;


        @Override
        protected void onPreExecute()
        {
            MyLog.v(TAG, "Beginning SearchTask...");
            progressDialog
                    .setMessage(String.format("Searching for\n'%s'...", searchTerm));
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
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (shows == null)
            {
                if (MyLog.error) MyLog.e(TAG, "search results came back null");
            }
            else if (shows.size() == 0)
            {
                if (MyLog.warn) MyLog.w(TAG, "search results came back empty");
            }
            else
            {
                selectDialog = new SelectShowDialog(appContext, shows);
                selectDialog.show();

                Show notRealShow = new Show(99L, 99, "fake show", "doesn't exist");
                listener.onSearchResultChosen(notRealShow);
            }
        }
    }
}
