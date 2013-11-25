package com.indivisible.mightyv.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.indivisible.mightyv.R;

/**
 * @author indiv
 * 
 */
public class ShowAddButtonsFragment
        extends Fragment
        implements OnClickListener
{

    private Button bClearList;
    private Button bNewSearch;
    OnButtonSelectedListener callback;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.show_fragment_search_buttons,
                                     container,
                                     false);

        bClearList = (Button) view
                .findViewById(R.id.show_fragment_add_clearlist);
        bNewSearch = (Button) view
                .findViewById(R.id.show_fragment_add_newSearch);
        bClearList.setOnClickListener(this);
        bNewSearch.setOnClickListener(this);

        return view;
    }

    // Container Activity must implement this interface
    public interface OnButtonSelectedListener
    {

        public void onButtonSelected(int viewId);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try
        {
            callback = (OnButtonSelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonSelectedListener");
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.show_fragment_add_clearlist:
                callback.onButtonSelected(v.getId());
                break;

            case R.id.show_fragment_add_newSearch:
                callback.onButtonSelected(v.getId());
                break;

            default:
                break;
        }

    }

}
