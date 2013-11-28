package com.indivisible.mightyv.activities;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.indivisible.mightyv.R;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.dialogs.SearchShowEntryDialog;
import com.indivisible.mightyv.fragments.ShowAddButtonsFragment;
import com.indivisible.mightyv.fragments.ShowListFragment;
import com.indivisible.mightyv.fragments.ShowRageSearchFragment;
import com.indivisible.mightyv.util.MyLog;

/**
 * @author indiv
 * 
 */
public class ShowAddActivity
        extends FragmentActivity
        implements ShowAddButtonsFragment.OnButtonSelectedListener,
        ShowListFragment.OnShowClickListener,
        ShowRageSearchFragment.OnSearchResultChosenListener,
        SearchShowEntryDialog.ShowSearchTermEntryListener
{

    //=================================================//
    //    Data
    //=================================================//

    private String TAG = this.getClass().getSimpleName();

    private ShowListFragment showsFragment;
    private ShowRageSearchFragment searchFragment;
    private SearchShowEntryDialog searchTermDialog;


    //=================================================//
    //    Activity Methods
    //=================================================//

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_add_activity);

        initFragments();
        // add some shows to the list for testing without full search
        testShows();
    }

    private void initFragments()
    {
        //TODO when list empty show info not listview
        //TODO make that info view/fragment
        showsFragment = (ShowListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_add_fragment_list);
        searchFragment = new ShowRageSearchFragment();
        searchTermDialog = new SearchShowEntryDialog();

        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragManager.beginTransaction();
        fragTrans.add(searchFragment, "search");
        fragTrans.add(searchTermDialog, "searchTermDialog");
        fragTrans.commit();
        fragManager.executePendingTransactions();

        //        searchFragment = (ShowRageSearchFragment) getSupportFragmentManager().findFragmentByTag()

    }

    private void testShows()
    {
        MyLog.d(TAG, "Populating ShowListFragment with some test data");
        List<Show> shows = new ArrayList<Show>();
        Show show;
        for (int i = 0; i < 6; i++)
        {
            show = new Show();
            show.setTitle("show title " + i);
            show.setStatus("status " + i);
            show.setStarted(i);
            show.setEnded(i);
            show.setCountry("A" + i);
            shows.add(show);
        }

        showsFragment.setList(shows);
    }

    //=================================================//
    //    Fragment Interactions
    //=================================================//

    @Override
    public void onButtonSelected(int viewId)
    {
        switch (viewId)
        {
            case R.id.show_fragment_add_clearlist:
                Toast.makeText(getApplicationContext(),
                               "selected clearlist",
                               Toast.LENGTH_SHORT).show();
                showsFragment.clearList();
                break;

            case R.id.show_fragment_add_newSearch:
                Toast.makeText(getApplicationContext(),
                               "selected newsearch",
                               Toast.LENGTH_SHORT).show();
                FragmentManager fm = getSupportFragmentManager();
                SearchShowEntryDialog entryDialog = new SearchShowEntryDialog();
                entryDialog.setRetainInstance(true);
                entryDialog.show(fm, "entryDialog");
                break;

            default:
                break;
        }
    }

    @Override
    public void onSearchTermEntered(String searchTerm)
    {
        Toast.makeText(getApplicationContext(),
                       "Show search term:\n" + searchTerm,
                       Toast.LENGTH_SHORT).show();
        searchFragment.performSearch(searchTerm);
    }

    @Override
    public void onSearchTermDismissed()
    {
        //ASK any need for this? seems to dismiss itself when appropriate
        Toast.makeText(getApplicationContext(),
                       "Show search term dismissed",
                       Toast.LENGTH_SHORT).show();
        searchTermDialog.dismiss();
    }

    @Override
    public void onSearchResultChosen(Show chosenShow)
    {
        Toast.makeText(getApplicationContext(),
                       "Show selected:\n" + chosenShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowShortClick(Show clickedShow)
    {
        Toast.makeText(getApplicationContext(),
                       "Show short clicked:\n" + clickedShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowLongClick(Show clickedShow)
    {
        Toast.makeText(getApplicationContext(),
                       "Show long clicked:\n" + clickedShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }


}
