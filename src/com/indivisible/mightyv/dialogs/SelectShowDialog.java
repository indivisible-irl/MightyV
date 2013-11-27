/**
 * 
 */
package com.indivisible.mightyv.dialogs;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.ListView;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.ShowArrayAdapter;


/**
 * @author indiv
 * 
 */
public class SelectShowDialog
        extends AlertDialog
{

    private String title = "Select a show";
    private ListView list;
    private ShowArrayAdapter adapter;


    public SelectShowDialog(Context context, List<Show> shows)
    {
        super(context);

        list = new ListView(context);
        adapter = new ShowArrayAdapter(context, shows);
        list.setAdapter(adapter);
        this.setView(list);
        this.setTitle(title);
    }


}
