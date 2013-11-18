package com.tvrage.api;

public class ShowDetailed extends ShowSimple
{

	//FIXME Don't need this class. Just go directly to existing Show and Episode classes.
	
	//=================================================//
	//		data
	//=================================================//
	
	//// inherited from ShowSimple:
	// private int		showid;
	// private String	name;
	// private String	link;
	// private String	country;
	// private int		started;
	// private int		ended;
	// private int		seasons;
	// private String 	status;
	// private String	classification;
	// private String[] genres;
	
	private String	startedLong;
	private String	endedLong;
	private int		runtime;
	private String	network;
	private String	airtime;
	private String	airday;
	private String[] akas;
	
	
	//=================================================//
	//		constructor
	//=================================================//
	
	public ShowDetailed()
	{
		// default constructor
		super();
	}
	
	public ShowDetailed(String whatever)
	{
		//TODO this whole class
	}
	
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	
	
	
	//=================================================//
	//		methods
	//=================================================//
	
}
