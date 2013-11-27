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
        ShowListFragment.OnShowShortClickListener,
        ShowListFragment.OnShowLongClickListener,
        ShowRageSearchFragment.OnSearchResultChosenListener

{

    //=================================================//
    //    Data
    //=================================================//

    private String TAG = this.getClass().getSimpleName();

    private ShowListFragment showsFragment;
    private ShowAddButtonsFragment buttonsFragment; //ASK need a ref for this?
    private ShowRageSearchFragment searchFragment;


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
        showsFragment = (ShowListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_add_fragment_list);
        buttonsFragment = (ShowAddButtonsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_add_fragment_buttons);
        searchFragment = new ShowRageSearchFragment();

        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragManager.beginTransaction();
        fragTrans.add(searchFragment, "search");
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
                Toast.makeText(this,//getApplicationContext(),
                               "selected clearlist",
                               Toast.LENGTH_SHORT).show();
                break;

            case R.id.show_fragment_add_newSearch:
                Toast.makeText(this,//getApplicationContext(),
                               "selected newsearch",
                               Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onSearchResultChosen(Show chosenShow)
    {
        Toast.makeText(this,//getApplicationContext(),
                       "Show selected:\n" + chosenShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowShortClick(Show clickedShow)
    {
        Toast.makeText(this,//getApplicationContext(),
                       "Show short clicked:\n" + clickedShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowLongClick(Show clickedShow)
    {
        Toast.makeText(this,//getApplicationContext(),
                       "Show long clicked:\n" + clickedShow.getTitle(),
                       Toast.LENGTH_SHORT).show();
    }


}
