package com.indivisible.mightyv.data;

public class Show
{

    //TODO Make a class that inherits from List<?> with methods to handle, search, retrieve Shows

    //=================================================//
    //		data
    //=================================================//

    private long showKey = -1;        // db table row id (primary key)
    private int rageID = -1;          // always int?							//TODO test all rageIDs for possible types
    private String rageLink = null;   // full link or tail end? can create with id??  //TODO research link formats
    private String title = null;
    private String country = null;    // show flags in listview/listings? need imgs   //TODO gather all possible codes
    private int started = -1;         // just year
    private int ended = -1;           // just year  //TODO test values for current/ended Shows. Set to 0 based on 'status'?
    private String status = null;     // map status to traffic light icons for list display //TODO gather possible options

    //private String[] genres = null;   // move to own table and link int[] FK (add as discovered - rm when unused after delete?)
    //private String seriesImg = null;  // need a source (tvrage/feeds/full_show_info.php? : Show/image)  //REM resize images
    //private Boolean starred = null;

    //=================================================//
    //		constructors
    //=================================================//

    /**
     * Create an empty Show object
     */
    public Show()
    {
        // default constructor
    }

    /**
     * Create a new Show object
     * 
     * @param showKey
     *        Show's Primary Key (unique row identifier) within it's database
     * @param rageID
     *        TVRage.com's assigned ID for the Show
     * @param title
     *        The Show's full title
     * @param status
     *        TVRage's status for the Show
     */
    public Show(long showKey, int rageID, String title, String status)
    {
        this.showKey = showKey;
        this.rageID = rageID;
        this.title = title;
        this.status = status;
    }

    //=================================================//
    //		gets & sets
    //=================================================//

    /**
     * Get the Show's Primary Key for the corresponding database entry
     * 
     * @return long of the PrimaryKey
     */
    public long getKey()
    {
        return this.showKey;
    }

    /**
     * Set the Show's unique database identifier
     * 
     * @param showKey
     *        the Show's PrimaryKey
     */
    public void setKey(long showKey)
    {
        this.showKey = showKey;
    }

    /**
     * Retrieve the Show's ID as assigned by TVRage.com
     * 
     * @return TVRage.com identifier
     */
    public int getRageID()
    {
        return this.rageID;
    }

    /**
     * Set the Show's TVRAge ID
     * 
     * @param rageID
     *        TVRage.com's identifier
     */
    public void setRageID(int rageID)
    {
        this.rageID = rageID;
    }

    /**
     * Get the Show's full title
     * 
     * @return Show title
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * Set the Show's full title
     * 
     * @param title
     *        Show title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Get the Show's current status according to TVRage.com
     * 
     * @return Show status
     */
    public String getStatus()
    {
        return this.status;
    }

    /**
     * Set the Show's current status retrieved from TVRage.com
     * 
     * @param status
     *        Show's current status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Retrieve the TVRage.com's link for the show
     * 
     * @return String representation of link's URL
     */
    public String getRageLink()
    {
        return this.rageLink;
    }

    /**
     * Set the Show's TVRage.com link
     * 
     * @param rageLink
     */
    public void setRageLink(String rageLink)
    {
        this.rageLink = rageLink;
    }

    /**
     * Retrieve the Show's country
     * 
     * @return Country code
     */
    public String getCountry()
    {
        return this.country;
    }

    /**
     * Set the Show's country code
     * 
     * @param country
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Retrieve the Show's start year
     * 
     * @return Year Show started
     */
    public int getStarted()
    {
        return this.started;
    }

    /**
     * Set the year the Show started
     * 
     * @param started
     *        int
     */
    public void setStarted(int started)
    {
        this.started = started;
    }

    /**
     * Retrieve the year the Show ended. '0' if still running. Maybe.
     * 
     * @return
     */
    public int getEnded()
    {
        return this.ended;
    }

    /**
     * Set the year the Show ended. Set to 0 if still running.
     * 
     * @param ended
     */
    public void setEnded(int ended)
    {
        this.ended = ended;
    }

    //=================================================//
    //		public methods
    //=================================================//

    /**
     * Tests if Show is in/from database or not
     * 
     * @return true if show is from the database
     */
    public boolean isSavedShow()
    {
        return (this.rageID == -1);
    }

    // Override the default implementation from java.lang.Object in favour of a custom representation
    //   Will still be able to return something even if the Show has no info.
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        if (this.showKey != -1)
        {
            sb.append(showKey).append(": ");
        }
        else if (this.rageID != -1)
        {
            sb.append("[").append(rageID).append("]: ");
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

    /**
     * Get a two line String that contains the Show's details. Used to display
     * in the (very) basic testing ListView
     * 
     * @return String with Show's info
     */
    public String info()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(showKey).append(" : ").append(title).append("\n");
        sb.append(rageID).append(", ").append(status);
        return sb.toString();
    }

    public String getYearsString()
    {
        String template = "%s - %s";
        String startStr;
        String endedStr;
        if (started < 1)
            startStr = "??";
        else
            startStr = Integer.toString(this.started);
        if (ended < 1)      //TODO Add check for status here too to switch '??' and 'now'
            endedStr = "now";
        else
            endedStr = Integer.toString(this.ended);

        return String.format(template, startStr, endedStr);
    }

    //=================================================//
}
