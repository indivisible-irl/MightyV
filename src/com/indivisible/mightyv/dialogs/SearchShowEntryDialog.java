package com.indivisible.mightyv.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * @author indiv
 * 
 */
public class SearchShowEntryDialog
        extends DialogFragment
        implements OnEditorActionListener, OnClickListener
{

    private AlertDialog dialog;
    private EditText etSearchTerm;
    private ShowSearchTermEntryListener listener;


    public SearchShowEntryDialog()
    {
        // default constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        etSearchTerm = new EditText(getActivity());
        etSearchTerm.setInputType(InputType.TYPE_CLASS_TEXT);
        etSearchTerm.requestFocus();
        etSearchTerm.setOnEditorActionListener(this);

        dialog = new AlertDialog.Builder(getActivity()).setPositiveButton("Search", this)
                .setNegativeButton("Cancel", null).setView(etSearchTerm).create();
        dialog.setTitle("Add Show");
        dialog.setMessage("Search for a Show to Add by Title:");

        return dialog;
    }

    @Override
    public void onAttach(Activity parentActivity)
    {
        super.onAttach(parentActivity);
        try
        {
            listener = (ShowSearchTermEntryListener) parentActivity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(parentActivity.toString()
                    + " must implement ShowSearchTermEntryListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public String getSearchTerm()
    {
        return etSearchTerm.getText().toString();
    }

    public interface ShowSearchTermEntryListener
    {

        void onSearchTermEntered(String searchTerm);

        void onSearchTermDismissed();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        int numChars = etSearchTerm.getText().toString().length();
        if (numChars < 4)
        {
            //make OK inactive
        }
        else
        {
            //enable OK
        }
        return true;
    }

    /* (non-Javadoc)
     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
     */
    @Override
    public void onClick(DialogInterface dialog, int buttonPosition)
    {
        // Pressing 'Cancel' doesn't appear to trigger an onClick even so any press must be 'OK' 
        Toast.makeText(getActivity(),
                       "button press: " + buttonPosition,
                       Toast.LENGTH_SHORT).show();
        listener.onSearchTermEntered(etSearchTerm.getText().toString());
    }

}
