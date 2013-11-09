package com.indivisible.mightyv.data;

public class Show {

	//=================================================//
	//		data
	//=================================================//
	
	private long   _id;					// db table row id
	private int    rageID;				// always int?
//	private String rageLink;			// full link or tail end? can create with id??
	private String title;
//	private String country;				// show flags? need source (gather possible codes)
//	private String started;				// just year
//	private String ended;				// just year
	private String status;				// map status to traffic light icons for list display (gather possible options)
//	private String[] genres;			// move to own table and link int[] FK (add as discovered - remove when unused after delete)
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
	
	public Show(long _id, int rageID, String title, String status)
	{
		this._id    = _id;
		this.rageID = rageID;
		this.title  = title;
		this.status = status;
	}
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	public long getID()
	{
		return this._id;
	}
	public void setID(long _id)
	{
		this._id = _id;
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
