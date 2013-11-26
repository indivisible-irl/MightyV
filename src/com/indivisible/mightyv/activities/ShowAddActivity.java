package com.indivisible.mightyv.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.indivisible.mightyv.R;
import com.indivisible.mightyv.fragments.ShowAddButtonsFragment;
import com.indivisible.mightyv.fragments.ShowListFragment;

/**
 * @author indiv
 * 
 */
public class ShowAddActivity
        extends FragmentActivity
        implements ShowAddButtonsFragment.OnButtonSelectedListener
{

    private String TAG = this.getClass().getSimpleName();
    private ShowListFragment showsFragment;
    private ShowAddButtonsFragment buttonsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_add_activity);

        initFragments();
    }

    private void initFragments()
    {
        showsFragment = (ShowListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_add_fragment_list);
        buttonsFragment = (ShowAddButtonsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_add_fragment_buttons);


    }

    @Override
    public void onButtonSelected(int viewId)
    {
        switch (viewId)
        {
            case R.id.show_fragment_add_clearlist:
                Toast.makeText(this, "selected clearlist", Toast.LENGTH_SHORT)
                        .show();
                break;

            case R.id.show_fragment_add_newSearch:
                Toast.makeText(this, "selected newsearch", Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }
    }


}
