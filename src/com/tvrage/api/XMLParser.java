package com.tvrage.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.indivisible.mightyv.util.MyLog;

// =================================================//
// TVRage.com XML Feeds
// =================================================//

/*
 * Search • http://services.tvrage.com/feeds/search.php?show=SHOWNAME Detailed
 * Search • http://services.tvrage.com/feeds/full_search.php?show=SHOWNAME
 * Show Info • http://services.tvrage.com/feeds/showinfo.php?sid=SHOWID
 * Episode List • http://services.tvrage.com/feeds/episode_list.php?sid=SHOWID
 * Episode Info • http://services.tvrage.com/feeds/episodeinfo.php?show=Show
 * Name&exact=1&ep=SEASONxEPISODE Show Info + Ep List •
 * http://services.tvrage.com/feeds/full_show_info.php?sid=SHOWID Full Show
 * List • http://services.tvrage.com/feeds/show_list.php
 */

/**
 * Parent class to be extended for individual XML Feed Parsing
 * 
 * @author indiv
 */
public class XMLParser
{

    // =================================================//
    // data
    // =================================================//

    protected String TAG;
    protected URL url = null;
    protected static String urlRoot = "http://services.tvrage.com/feeds/";

    // REM save individual tags in child classes

    // =================================================//
    // constructor
    // =================================================//

    /**
     * Parent class for all XML Parsers
     */
    public XMLParser()
    {
        // default constructor
    }

    // =================================================//
    // gets & sets
    // =================================================//

    /**
     * Retrieve the instance's URL
     * 
     * @return URL pointing to the desired XML resource
     */
    public URL getURL()
    {
        return this.url;
    }

    /**
     * Set the URL that points to the desired XML resource
     * 
     * @param url
     *        Formed URL
     */
    public void setURL(URL url)
    {
        this.url = url;
    }

    /**
     * Set (and form) the URL pointing to the desired XML resource
     * 
     * @param urlStr
     *        String URL to be formed on set
     */
    public void setURL(String urlStr)
    {
        try
        {
            this.url = new URL(urlStr);
        }
        catch (MalformedURLException e)
        {
            if (MyLog.error) MyLog.e(TAG, "Error forming URL: " + urlStr);
        }
    }

    public List<?> getResults()
    {
        throw new UnsupportedOperationException(
                "Implement/Override in child class");
    }

    // =================================================//
    // methods
    // =================================================//

    /**
     * Open and pass on an InputStream containing the resource's XML data.
     * 
     * @return Stream of XML data set using object's this.url
     */
    protected InputStream getXMLInputStream()
    {
        InputStream inputStream = null;
        try
        {
            // Open connection and set InputStream
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            inputStream = conn.getInputStream();
        }
        catch (IOException e)
        {
            if (MyLog.error)
                MyLog.e(TAG,
                        "Error while getting InputStream: " + url.toString());
        }
        return inputStream;
    }

    /**
     * Method to Override in child classes to parse XML input for desired info
     * 
     * @param stream
     *        InputStream
     * @return boolean of successful parse
     */
    protected boolean parseXML(InputStream stream)
    {
        throw new UnsupportedOperationException(
                "Implement/Override in child class");
    }
}
