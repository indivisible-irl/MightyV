package com.tvrage.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.indivisible.mightyv.data.Episode;
import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;

public class EpisodeListXMLParser extends XMLParser
{

    //=================================================//
    //    data
    //=================================================//

    /*
     * inherits: protected String TAG; protected URL url = null; protected
     * String rootElement = null; protected static String urlRoot =
     * "http://services.tvrage.com/feeds/";
     */

    private static String URL_BASE = XMLParser.urlRoot
            .concat("episode_list.php?sid=%s");

    private List<Episode> episodes = null;
    private long parentShowID = -1;

    // XML elements/tags
    // private static String showTitle = "name"
    // private static String seasonCount = "totalseasons";

    private static String ROOT_EPISODES = "Episodelist";
    private static String ROOT_SEASON = "Season";
    private static String TAG_SEASONNUM = "no";
    private static String ELEMENT_EPISODE = "episode";

    private static String ITEM_EPNUM = "seasonnum";
    private static String ITEM_AIRDATE = "airdate";
    private static String ITEM_LINK = "link";
    private static String ITEM_TITLE = "title";

    //=================================================//
    //    constructor
    //=================================================//

    /**
     * Class to grab and Parse XML for Show's Episodes
     * 
     * @param rageID
     *        TVRage.com's assigned id for the Show
     */
    public EpisodeListXMLParser(Show show)
    {
        super();
        this.TAG = this.getClass().getSimpleName();

        this.parentShowID = show.getKey();
    }

    //=================================================//
    //    gets & sets
    //=================================================//

    /**
     * Retrieve Parsed Episodes from last run. Returns NULL if never run,
     * Empty List if no results.
     * 
     * @return List of Show's Episodes. Returns empty List if no results.
     *         Returns null if no parse was performed.
     */
    @Override
    public List<Episode> getResults()
    {
        return this.episodes;
    }

    /**
     * Set the Show the XMLParser should target. Must be set before attempting
     * to grab any XML source.
     * 
     * @param rageID
     *        Integer ID as assigned by TVRage.com
     */
    public void setShow(Show show)
    {
        String urlStr = XMLParser.urlRoot.concat(URL_BASE);
        this.setURL(String.format(urlStr, show.getRageID()));
        this.parentShowID = show.getKey();
    }

    /**
     * Set the ID of the Show the XMLParser should target. Must be set before
     * attempting to grab any XML source.
     * 
     * @param showkey
     */
    public void setShow(long showkey)
    {
        this.parentShowID = showkey;
    }

    //=================================================//
    //    methods
    //=================================================//

    /*
     * inherits: protected InputStream getXMLInputStream()
     */

    @Override
    protected boolean parseXML(InputStream stream)
    {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        Episode episode = null;
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
            int seasonNumber = 0;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                String tagName = parser.getName();
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(ROOT_EPISODES))
                        {
                            episodes = new ArrayList<Episode>();
                            episode = new Episode();
                        }
                        if (tagName.equalsIgnoreCase(ROOT_SEASON))
                        {
                            String seasonNumberStr = parser.getAttributeValue(
                                    null, TAG_SEASONNUM);
                            seasonNumber = Integer.parseInt(seasonNumberStr);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        itemText = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(ELEMENT_EPISODE))
                        {
                            episode.setSeasonNum(seasonNumber);
                            episode.setParentKey(parentShowID);
                            episodes.add(episode);
                            episode = new Episode();
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_EPNUM))
                        {
                            episode.setEpisodeNum(Integer.parseInt(itemText));
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_TITLE))
                        {
                            episode.setTitle(itemText);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_AIRDATE))
                        {
                            episode.setAired(itemText);
                        }
                        else if (tagName.equalsIgnoreCase(ITEM_LINK))
                        {
                            episode.setRageLink(itemText);
                        }
                        else
                        {
                            // tag ignored
                            //if (MyLog.debug) MyLog.d(TAG, "Skipped item: " + tagName);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
            // parse completed successfully
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
            {
                e.printStackTrace();
            }
        }
        // parse failed somewhere
        return false;
    }

    /**
     * Method to grab the XML for the supplied Show's Episodes and parse the
     * stream for Episodes.
     * 
     * @return List of parsed Episodes
     */
    public List<Episode> performEpisodeRetrieve()
    {
        InputStream stream = getXMLInputStream();
        boolean successfulParse = parseXML(stream);

        if (successfulParse)
        {
            if (MyLog.verbose)
                MyLog.v(TAG, "Parsed XML EpisodeList, Found episodes: "
                        + episodes.size());
            return episodes;
        }
        else
        {
            if (MyLog.error)
                MyLog.e(TAG,
                        "Failed to retrieve Episodes. Returning empty List");
            return new ArrayList<Episode>();
        }
    }

    //=================================================//
    //    Example EpisodeList XML feed
    //=================================================//

    /*
     *  <Show>
     *      <name>Buffy the Vampire Slayer</name>
     *      <totalseasons>7</totalseasons>
     *      <Episodelist>
     *          <Season no="1">
     *              <episode>
     *                  <epnum>1</epnum>
     *                  <seasonnum>01</seasonnum>
     *                  <prodnum>4V01</prodnum>
     *                  <airdate>1997-03-10</airdate>
     *                  <link>http://www.tvrage.com/Buffy_The_Vampire_Slayer/episodes/28077</link>
     *                  <title>Welcome to the Hellmouth (1)</title>
     *              </episode>
     *              <episode>
     *                  ...
     *              </episode>
     *              ...
     *          </Season>
     *          <Season no="2">
     *              ...
     *          </Season>
     *      </Episodelist>
     *  </Show>
     */
}
