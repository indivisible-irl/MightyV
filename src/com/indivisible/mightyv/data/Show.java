package com.indivisible.mightyv.data;

public class Show {

	//=================================================//
	//		data
	//=================================================//
	
	private long   key;					// db table row id (primary key)
	private int    rageID;				// always int?  //TODO test all rageIDs for possible types
//	private String rageLink;			// full link or tail end? can create with id??  //TODO research this
	private String title;
//	private String country;				// show flags? need source //TODO gather all possible codes
//	private String started;				// just year?
//	private String ended;				// just year.
	private String status;				// map status to traffic light icons for list display //TODO gather possible options
//	private String[] genres;			// move to own table and link int[] FK (add as discovered - remove when unused after a delete?)
//	private String runtime;				// care?
	
//	private String seriesImg;			// need a source for these (tvrage/feeds/full_show_info.php? : Show/image)  //REM resize images
	
//	private boolean starred;
	
	//=================================================//
	//		constructors
	//=================================================//
	
	public Show()
	{
		// default
	}
	
	public Show(int rageID, String title, String status)
	{
		this.rageID = rageID;
		this.title  = title;
		this.status = status;
	}
	
	public Show(long key, int rageID, String title, String status)
	{
		this.key    = key;
		this.rageID = rageID;
		this.title  = title;
		this.status = status;
	}
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	public long getKey()
	{
		return this.key;
	}
	public void setKey(long key)
	{
		this.key = key;
	}
	
	public int getRageID()
	{
		return this.rageID;
	}
	public void setRageID(int rageID)
	{
		this.rageID = rageID;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getStatus()
	{
		return this.status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	
	//=================================================//
}
