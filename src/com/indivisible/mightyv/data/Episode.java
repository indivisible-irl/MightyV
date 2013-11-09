package com.indivisible.mightyv.data;

public class Episode {

	//=================================================//
	//		data
	//=================================================//
	
	private long _id;
	private int seasonNum;
	private int episodeNum;
	private String title;
//	private String aired;
//	private String tvrageLink;
	
//	private boolean watched;
	
	//=================================================//
	//		constructors
	//=================================================//
	
	public Episode()
	{
		//default
	}
	public Episode(int seasonNum, int episodeNum, String title)
	{
		this.seasonNum = seasonNum;
		this.episodeNum = episodeNum;
		this.title = title;
	}
	
	public Episode(long _id, int seasonNum, int episodeNum, String title)
	{
		this._id = _id;
		this.seasonNum = seasonNum;
		this.episodeNum = episodeNum;
		this.title = title;
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
	
	public int getSeasonNum()
	{
		return this.seasonNum;
	}
	public void setSeasonNum(int seasonNum)
	{
		this.seasonNum = seasonNum;
	}
	
	public int getEpisodeNum()
	{
		return this.episodeNum;
	}
	public void setEpisodeNum(int episodeNum)
	{
		this.episodeNum = episodeNum;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	//=================================================//
}
