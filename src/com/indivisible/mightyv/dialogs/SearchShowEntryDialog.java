package com.indivisible.mightyv.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.indivisible.mightyv.R;

/**
 * @author indiv
 * 
 */
public class SearchShowEntryDialog extends DialogFragment
{

    private EditText etSearchTerm;

    public SearchShowEntryDialog()
    {
        // default constructor 
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.search_show_dialog_searchterm,
                container);
        etSearchTerm = (EditText) view
                .findViewById(R.id.search_entry_searchTerm);
        getDialog().setTitle("Search for a Show...");

        return view;
    }

    public String getSearchTerm()
    {
        //TODO validate search here? (3+ chars, alphanumeric)
        return etSearchTerm.getText().toString();
    }

}
