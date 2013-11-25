package com.tvrage.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.net.Uri;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;

/**
 * Class to perform search for Shows and parse results. Results are in the
 * form of Shows and saved internally.
 * 
 * @author indiv
 */
public class SearchXMLParser extends XMLParser
{

    //=================================================//
    //		data
    //=================================================//

    /*
     * inherits: protected String TAG; protected URL url = null; protected
     * static String urlRoot = "http://services.tvrage.com/feeds/";
     */

    private static String URL_BASE = XMLParser.urlRoot
            .concat("search.php?show=%s");

    private List<Show> shows = null;

    // XML Nodes & Tags
    private static String ELEMENT_SHOW = "show";
    private static String ITEM_RAGEID = "showid";
    private static String ITEM_TITLE = "name";
    private static String ITEM_STATUS = "status";
    private static String ITEM_LINK = "link";
    private static String ITEM_COUNTRY = "country";
    private static String ITEM_STARTED = "started";
    private static String ITEM_ENDED = "ended";

    //=================================================//
    //		constructor
    //=================================================//

    /**
     * Class to perform search and parse the results into Shows
     * 
     * @param searchTerm
     *        String to search through TVRage.com's collection for
     */
    public SearchXMLParser()
    {
        super();
        this.TAG = this.getClass().getSimpleName();
    }

    //=================================================//
    //		gets & sets
    //=================================================

    /**
     * Retrieve the Show results from the performed search
     * 
     * @return List of Shows. NULL if no search performed yet. Empty List if
     *         no results found.
     */
    @Override
    public List<Show> getResults()
    {
        return shows;
    }

    /**
     * Format the URL for the desired search
     * 
     * @param searchTerm
     *        String search
     */
    public void setSearch(String searchTerm)
    {
        String urlStr = String.format(URL_BASE, Uri.encode(searchTerm));
        this.setURL(urlStr);
    }

    //=================================================//
    //		methods
    //=================================================//

    /*
     * inherits: protected InputStream getXMLInputStream()
     */

    /**
     * Parse InputStream XML and save result Shows
     * 
     * @param stream
     *        InputStream of Search query XML results
     */
    @Override
    protected boolean parseXML(InputStream stream)
    {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        shows = new ArrayList<Show>();
        Show show = null;
        String itemText = null;
        try
        {
            // prepare ParserFactory
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            // assign the InputStream to the Factory
            parser.setInput(stream, null);

            // parse the XML input
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                String tagName = parser.getName();
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(ELEMENT_SHOW))
                        {
                            show = new Show();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        itemText = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(ELEMENT_SHOW))
                        {
                            shows.add(show);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_RAGEID))
                        {
                            show.setRageID(Integer.parseInt(itemText));
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_TITLE))
                        {
                            show.setTitle(itemText);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_STATUS))
                        {
                            show.setStatus(itemText);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_LINK))
                        {
                            show.setRageLink(itemText);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_COUNTRY))
                        {
                            show.setCountry(itemText);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_STARTED))
                        {
                            show.setStarted(Integer.parseInt(itemText));
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_ENDED))
                        {
                            show.setEnded(Integer.parseInt(itemText));
                        }
                        else
                        {
                            // tag ignored
                            //if (MyLog.debug) MyLog.d(TAG, "Skipped item: " +tagName);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

            return true;
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {}
        }

        return false;
    }

    /**
     * Method that actually grabs the XML and parses the results. Should not
     * be run on the UI thread.
     * 
     * @return List of Shows received from Search
     */
    public List<Show> performSearch()
    {
        InputStream XMLStream = getXMLInputStream();
        boolean successfulParse = parseXML(XMLStream);

        if (successfulParse)
        {
            if (MyLog.info)
                MyLog.i(TAG, "Successfully parsed Search results. Found: "
                        + shows.size());
            return shows;
        }
        else
        {
            if (MyLog.warn) MyLog.w(TAG, "Failed to parse Search results");
            return new ArrayList<Show>();
        }
    }

    //=================================================//
    //		Example Search XML feed
    //=================================================//

    /*
     * <Results> <show> <showid>2930</showid> <name>Buffy the Vampire
     * Slayer</name>
     * <link>http://www.tvrage.com/Buffy_The_Vampire_Slayer</link>
     * <country>US</country> <started>1997</started> <ended>2003</ended>
     * <seasons>7</seasons> <status>Ended</status>
     * <classification>Scripted</classification> <genres>
     * <genre>Action</genre> <genre>Adventure</genre> <genre>Comedy</genre>
     * <genre>Drama</genre> <genre>Mystery</genre> <genre>Sci-Fi</genre>
     * </genres> </show> <show> ... </show>
     */

}
