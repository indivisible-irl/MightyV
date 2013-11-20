package com.indivisible.mightyv.fragments;

import java.util.List;

import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.ShowArrayAdapter;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ShowListFragment extends ListFragment
{
	
	//=================================================//
	//		Data
	//=================================================//
	
	private List<Show> shows;
	private ShowArrayAdapter adapter;
	
	
	
	//=================================================//
	//		Fragment Methods
	//=================================================//
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		adapter = new ShowArrayAdapter(this.getActivity().getApplicationContext(), shows);
		setListAdapter(adapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	//=================================================//
	//		data
	//=================================================//
	
	
	
	
	//=================================================//
	//		data
	//=================================================//
	
	
}
