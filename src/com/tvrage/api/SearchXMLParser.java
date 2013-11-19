package com.tvrage.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.indivisible.mightyv.data.Show;
import com.indivisible.mightyv.util.MyLog;

/**
 * Class to perform search for Shows and parse results
 * @author indiv
 */
public class SearchXMLParser extends XMLParser
{
	
	//=================================================//
	//		data
	//=================================================//
	
	/* 
	 * inherits:
	 *		protected String TAG;
	 *		protected URL url = null;
	 *		protected String rootElement = null;
	 *		protected static String urlRoot = "http://services.tvrage.com/feeds/"; 
	 */
	
	private static String urlBase = XMLParser.urlRoot.concat("search.php?show=%s");
	
	private List<Show> shows = null;
	
	// XML Nodes & Tags
	private static String rootElement = "Results";
	private static String showElement = "show";
	
	private static String itemRageId	= "showid";
	private static String itemTitle		= "name";
	private static String itemStatus	= "status";
	private static String itemLink		= "link";
	private static String itemCountry	= "country";
	private static String itemStarted	= "started";
	private static String itemEnded		= "ended";
	
	
	//=================================================//
	//		constructor
	//=================================================//
	
	/**
	 * Class to perform search and parse the results into Shows
	 * @param searchTerm String to search through TVRage.com's collection for
	 */
	public SearchXMLParser(String searchTerm)
	{
		super();
		
		//ASK set search here or in performSearch()??
		String urlStr = String.format(urlBase, searchTerm);
		this.setURL(urlStr);
		this.setRootElement(rootElement);
	}
	
	
	//=================================================//
	//		gets & sets
	//=================================================
	
	/**
	 * Retrieve the Show results from the performed search
	 * @return List of Shows.
	 * NULL if no search performed yet.
	 * Empty List if no results found.
	 */
	public List<Show> getShows()
	{
		return shows;
	}
	
	//TODO Refactor class to enable use of single instance for multiple searches
	
	
	//=================================================//
	//		methods
	//=================================================//
	
	/* 
	 * inherits:
	 *		protected InputStream getXMLInputStream()
	 */
	
	/**
	 * Parse InputStream XML and save result Shows
	 * @param stream InputStream of Search query XML results
	 */
	@Override
	public boolean parseXML(InputStream stream)
	{
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		
		shows = new ArrayList<Show>();
		Show show = null;
		String itemText = null;
		try
		{
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
			
			parser.setInput(stream, null);
			
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				String tagName = parser.getName();
				switch (eventType)
				{
					case XmlPullParser.START_TAG:
						if (tagName.equalsIgnoreCase(showElement))
						{
							show = new Show();
						}
						break;
						
					case XmlPullParser.TEXT:
						itemText = parser.getText();
						break;
						
					case XmlPullParser.END_TAG:
						if (tagName.equalsIgnoreCase(showElement))			// new Show 
						{
							shows.add(show);
						} 
						else if (tagName.equalsIgnoreCase(itemRageId))			// tag rageID
						{
							show.setRageID(Integer.parseInt(itemText));
						}
						else if (tagName.equalsIgnoreCase(itemTitle))			// tag title
						{
							show.setTitle(itemText);
						}
						else if (tagName.equalsIgnoreCase(itemStatus))			// tag status
						{
							show.setStatus(itemText);
						}
						else if (tagName.equalsIgnoreCase(itemLink))			// tag rageLink
						{
							show.setRageLink(itemText);
						}
						else if (tagName.equalsIgnoreCase(itemCountry))			// tag country
						{
							show.setCountry(itemText);
						}
						else if (tagName.equalsIgnoreCase(itemStarted))			// tag started
						{
							show.setStarted(Integer.parseInt(itemText));
						}
						else if (tagName.equalsIgnoreCase(itemEnded))			// tag ended
						{
							show.setEnded(Integer.parseInt(itemText));
						}
						else													// tag ignored
						{
							if (MyLog.debug) MyLog.d(TAG, "Skipped item: " +tagName);
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
			try {
				stream.close();
			} catch (IOException e) {}
		}
		
		return false;
	}
	
	/**
	 * Method that actually grabs the XML and parses the results.
	 * Should not be run on the UI thread.
	 * @return List of Shows received from Search
	 */
	public List<Show> performSearch()
	{
		InputStream XMLStream = getXMLInputStream();
		boolean successfulParse = parseXML(XMLStream);
		
		if (successfulParse)
		{
			if (MyLog.info) MyLog.i(TAG, "Successfully parsed Search results. Found: " +shows.size());
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
	 *	<Results>
	 *		<show>
	 *			<showid>2930</showid>
	 *			<name>Buffy the Vampire Slayer</name>
	 *			<link>http://www.tvrage.com/Buffy_The_Vampire_Slayer</link>
	 *			<country>US</country>
	 *			<started>1997</started>
	 *			<ended>2003</ended>
	 *			<seasons>7</seasons>
	 *			<status>Ended</status>
	 *			<classification>Scripted</classification>
	 *			<genres>
	 *				<genre>Action</genre>
	 *				<genre>Adventure</genre>
	 *				<genre>Comedy</genre>
	 *				<genre>Drama</genre>
	 *				<genre>Mystery</genre>
	 *				<genre>Sci-Fi</genre>
	 *			</genres>
	 *		</show>
	 *		<show>
	 *			...
	 *		</show>
	 */
	
}
