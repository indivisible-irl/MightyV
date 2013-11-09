package com.indivisible.mightyv.data;

public class Show {

	//=================================================//
	//		data
	//=================================================//
	
	private long   showKey = -1;		// db table row id (primary key)
	private int    rageID = -1; 		// always int?  //TODO test all rageIDs for possible types
//	private String rageLink = null;		// full link or tail end? can create with id??  //TODO research this
	private String title = null;
//	private String country = null;		// show flags? need source //TODO gather all possible codes
//	private String started = null;		// just year? use int??
//	private String ended = null;		// just year.
	private String status = null;		// map status to traffic light icons for list display //TODO gather possible options
//	private String[] genres = null;		// move to own table and link int[] FK (add as discovered - rm when unused after delete?)
//	private String runtime = null;		// care?
	
//	private String seriesImg = null;	// need a source (tvrage/feeds/full_show_info.php? : Show/image)  //REM resize images
//	private Boolean starred = null;
	
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
	
	public Show(long showKey, int rageID, String title, String status)
	{
		this.showKey = showKey;
		this.rageID  = rageID;
		this.title   = title;
		this.status  = status;
	}
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	public long getKey()
	{
		return this.showKey;
	}
	public void setKey(long key)
	{
		this.showKey = key;
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
	//		public methods
	//=================================================//
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		if (this.showKey != -1)
		{
			sb.append(showKey).append(": ");
		}
		else
		{
			sb.append("NA: ");
		}
		
		if (this.title != null)
		{
			sb.append(this.title);
		}
		else
		{
			sb.append("NO TITLE");
		}
		
		return sb.toString();
	}
	
	//=================================================//
}
