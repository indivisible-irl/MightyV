package com.indivisible.mightyv.data;

/**
 * Class to represent a TV Show's single episode
 * @author indivisible
 */
public class Episode {

	//=================================================//
	//		data
	//=================================================//
	
	// init with default values for toString() method testing
	private long episodeKey = -1; 
	private long showKey    = -1;
	private int seasonNum   = -1;	//TODO run through TVRage entries to test for non integer values
									//ASK use 0 for specials and out of season airings? Order after. Test how TVRage numbers them
	private int episodeNum  = -1;	//todo same as for seasonNum
	private String title    = null;
	private String aired	= null;
	private String rageLink = null;
	
//	private boolean watched;
	
	//TODO private static final Strings used in String.formats for display in various places or user preferences
	
	//=================================================//
	//		constructors
	//=================================================//
	
	/**
	 * Create an empty Episode object. Fairly useless as it is though.
	 */
	public Episode()
	{
		// default constructor
	}
	
	/**
	 * Create a new Object to represent a single Show's episode 
	 * @param episodeKey 	Episode's database Primary Key identifier
	 * @param showKey 		Episode's parent Show's Primary Key (Foreign Key here)
	 * @param seasonNum 	Season number Episode belongs to
	 * @param episodeNum	Episode number of Episode within season
	 * @param title			Episode's full title
	 */
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
	
	/**
	 *  Get the Episode's unique identifier
	 * @return Episode's Primary Key
	 */
	public long getKey()
	{
		return this.episodeKey;
	}
	/**
	 * Set the Episode's unique database identifier
	 * @param episodeKey Episode's primary Key
	 */
	public void setKey(long episodeKey)
	{
		this.episodeKey = episodeKey;
	}
	/**
	 * Get the Episode's parent Show's unique database identifier
	 * @return parent Show's Primary Key (a Foreign Key from this perspective)
	 */
	public long getParentKey()
	{
		return this.showKey;
	}
	/**
	 * Set the Episode's parent Show's unique database identifier
	 * @param showKey The ForeignKey to set as the Episode's parent
	 */
	public void setParentKey(long showKey)
	{
		this.showKey = showKey;
	}
	
	/**
	 * Get the Season number that the Episode belongs to
	 * @return season number (unformatted, raw int)
	 */
	public int getSeasonNum()
	{
		return this.seasonNum;
	}
	/**
	 * Set the Episode's parent Season's number (raw int)
	 * @param seasonNum the number of the season the Episode belongs in
	 */
	public void setSeasonNum(int seasonNum)
	{
		this.seasonNum = seasonNum;
	}
	
	/**
	 * Get the Episode's number within the parent season
	 * @return
	 */
	public int getEpisodeNum()
	{
		return this.episodeNum;
	}
	/**
	 * Set the Episode's number within its parent season
	 * @param episodeNum
	 */
	public void setEpisodeNum(int episodeNum)
	{
		this.episodeNum = episodeNum;
	}
	
	/**
	 * Retrieve the Episode's full title (no number prefix)
	 * @return Episode title
	 */
	public String getTitle()
	{
		return this.title;
	}
	/**
	 * Set the Episode's full title
	 * @param title Episode's title without prefixes or numbering
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Get the Episode's airdate
	 * @return
	 */
	public String getAired()
	{
		return this.aired;
	}
	/**
	 * Set the Episode's airdate
	 * @param aired
	 */
	public void setAired(String aired)
	{
		this.aired = aired;
	}
	
	/**
	 * Get the Episode's TVRage.com Link
	 * @return
	 */
	public String getRageLink()
	{
		return this.rageLink;
	}
	/**
	 * Set the Episode's TVRage.com Link
	 * @param rageLink
	 */
	public void setRageLink(String rageLink)
	{
		this.rageLink = rageLink;
	}
	
	
	//=================================================//
	//		public methods
	//=================================================//
	
	// Override the default implementation from java.lang.Object in favour of a custom representation
	//   Will still be able to return something even if the Episode has no info.
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
	
	/**
	 * Convenience method to display Episode info on two lines
	 * @return String with Episode details
	 */
	public String info()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(showKey).append(": ").append(episodeKey).append(" | ").append(seasonNum).append("/").append(episodeNum).append("\n");
		sb.append(title);
		
		return sb.toString();
	}
	
	/**
	 * Formatted String with season and episode numbers. eg "s01e04"
	 * @return String 
	 */
	public String formattedNumbers()
	{
	    StringBuilder sb = new StringBuilder("s");
	    if (seasonNum == -1)
        {
            sb.append("00");
        }
        else
        {
            sb.append(String.format("%02d", seasonNum));
        }
	    sb.append("e");
	    if (episodeNum == -1)
        {
            sb.append("00");
        }
        else
        {
            sb.append(String.format("%02d", episodeNum));
        }
	    return sb.toString();
	}
	
	/**
	 * Compact, formatted string representing the episode
	 * @return
	 */
	public String formattedTitle()
	{
	    return String.format("%s-%s", formattedNumbers(), title);
	}
	
	//=================================================//
}
