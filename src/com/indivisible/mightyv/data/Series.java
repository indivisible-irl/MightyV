package com.indivisible.mightyv.data;

public class Series {

	//// data
	
	private int    rageID;					// always int?
//	private String rageLink;			// full link or tail end? can create with id??
	private String title;
//	private String country;				// show flags? need source (gather possible codes)
//	private String started;				// just year
//	private String ended;				// just year
	private String status;				// map status to traffic light icons for list display (gather possible options)
//	private String[] genres;			// move to own table and link int[] FK (add as discovered - remove when unused after delete)
//	private String runtime;				// care?
	
//	private String seriesImg;			// need a source for these (tvrage/feeds/full_show_info.php? : Show/image) resize
	
//	private boolean starred;
	
	
	//// constructors
	
	public Series()
	{
		// default
	}
	
	public Series(int rageID, String title, String status)
	{
		this.rageID = rageID;
		this.title  = title;
		this.status = status;
	}
	
	
	//// gets & sets
	
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
	
	
}
