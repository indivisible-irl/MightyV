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
	
	private static String rootElement = "Results";
	private static String showElement = "show";
	
	private static String itemRageId  = "showid";
	private static String itemTitle   = "name";
	private static String itemStatus  = "status";
	// implement other fields later
	
	
	//=================================================//
	//		constructor
	//=================================================//

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
	
	public List<Show> getShows()
	{
		return shows;
	}
	
	
	//=================================================//
	//		methods
	//=================================================//
	
	/* 
	 * inherits:
	 *		protected InputStream getXMLInputStream()
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
						if (tagName.equalsIgnoreCase(showElement))
						{
							shows.add(show);
						} 
						else if (tagName.equalsIgnoreCase(itemRageId))
						{
							show.setRageID(Integer.parseInt(itemText));
						}
						else if (tagName.equalsIgnoreCase(itemTitle))
						{
							show.setTitle(itemText);
						}
						else if (tagName.equalsIgnoreCase(itemStatus))
						{
							show.setStatus(itemText);
						}
						else
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
	
	
}
