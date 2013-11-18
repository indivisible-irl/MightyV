package com.tvrage.api;

import com.indivisible.mightyv.util.MyLog;

public class ShowSimple
{
	//FIXME Don't need this class. Just go directly to existing Show and Episode classes.

	//=================================================//
	//		data
	//=================================================//
	
	private String TAG = this.getClass().getSimpleName();
	
	private int		showid;
	private String	name;
	private String	link;
	private String	country;
	private int		started;
	private int		ended;
	private int		seasons;
	private String	status;
	private String	classification;
	private String[] genres;
	
	//=================================================//
	//		constructor
	//=================================================//
	
	public ShowSimple()
	{
		// default constructor
	}
	
	public ShowSimple(
			String showId,		String name,		String link,
			String country,		String started,		String ended,
			String seasons,		String status,		String classification,
			String[] genres)
	{
		try
		{
			this.showid		= Integer.parseInt(showId);
			this.name		= name;
			this.link		= link;
			this.country	= country;
			this.started	= Integer.parseInt(started);
			this.ended		= Integer.parseInt(ended);
			this.seasons	= Integer.parseInt(seasons);
			this.status		= status;
			this.classification = classification;
			this.genres		= genres;
		}
		catch (NumberFormatException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Error while converting String to int");
		}
	}
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	public int getShowId() {
		return this.showid;
	}
	public void setShowId(int showId) {
		this.showid = showId;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public int getStarted() {
		return this.started;
	}
	public void setStarted(int started) {
		this.started = started;
	}
	
	public int getEnded() {
		return this.ended;
	}
	public void setEnded(int ended) {
		this.ended = ended;
	}
	
	public int getSeasons() {
		return this.seasons;
	}
	public void setSeasons(int seasons) {
		this.seasons = seasons;
	}
	
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getClassification() {
		return this.classification;
	}
	public void setClassification(String classificiation) {
		this.classification = classificiation;
	}
	
	public String[] getGenres() {
		return this.genres;
	}
	public void setGenres(String[] genres) {
		this.genres = genres;
	}
	
	//=================================================//
	//		methods
	//=================================================//
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	
	public String info()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(name).append(" [").append(status).append("]\n");
		sb.append("Seasons: ").append(seasons).append(" | ").append(started).append(" to ").append(ended);
		
		return sb.toString();
	}
}
