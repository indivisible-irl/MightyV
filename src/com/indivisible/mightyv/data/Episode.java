package com.indivisible.mightyv.data;

public class Episode {

	//=================================================//
	//		data
	//=================================================//
	
	private long episodeKey = -1;	// use DAO to ensure always exists in db and has epKey set 
	private long showKey = -1;
	private int seasonNum = -1;
	private int episodeNum = -1;
	private String title = null;
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
	public Episode(long showKey, int seasonNum, int episodeNum, String title)
	{
		this.showKey = showKey;
		this.seasonNum = seasonNum;
		this.episodeNum = episodeNum;
		this.title = title;
	}
	
	public Episode(long episodeKey, long showKey, int seasonNum, int episodeNum, String title)
	{
		this.episodeKey = episodeKey;
		this.showKey = showKey;
		this.seasonNum = seasonNum;
		this.episodeNum = episodeNum;
		this.title = title;
	}
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	public long getKey()
	{
		return this.episodeKey;
	}
	public void setKey(long episodeKey)
	{
		this.episodeKey = episodeKey;
	}
	
	public long getParentKey()
	{
		return this.showKey;
	}
	public void setParentKey(long showKey)
	{
		this.showKey = showKey;
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
	//		public methods
	//=================================================//
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		if (this.episodeKey != -1)
		{
			sb.append(episodeKey).append(": ");
		}
		else
		{
			sb.append("NA: ");
		}
		
		if (this.seasonNum != -1)
		{
			sb.append(this.seasonNum).append("/");
		}
		else
		{
			sb.append("-").append("/");
		}
		
		if (this.episodeNum != -1)
		{
			sb.append(this.episodeNum).append(": ");
		}
		else
		{
			sb.append("-").append(": ");
		}
		
		if (this.title != null || this.title.equals(""))
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
