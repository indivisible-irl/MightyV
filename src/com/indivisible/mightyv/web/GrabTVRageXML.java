package com.indivisible.mightyv.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.indivisible.mightyv.util.MyLog;
import com.tvrage.api.ShowSimple;

public class GrabTVRageXML {
	
	//FIXME Don't need this class. Going with XMLPullParser and a more polymorphic Parser design

	//=================================================//
	//		TVRage.com XML Feeds
	//=================================================//
	
	/*
	 * Search				• http://services.tvrage.com/feeds/search.php?show=SHOWNAME
	 * Detailed Search		• http://services.tvrage.com/feeds/full_search.php?show=SHOWNAME
	 * Show Info			• http://services.tvrage.com/feeds/showinfo.php?sid=SHOWID
	 * Episode List			• http://services.tvrage.com/feeds/episode_list.php?sid=SHOWID
	 * Episode Info			• http://services.tvrage.com/feeds/episodeinfo.php?show=Show Name&exact=1&ep=SEASONxEPISODE
	 * Show Info + Ep List	• http://services.tvrage.com/feeds/full_show_info.php?sid=SHOWID
	 * Full Show List		• http://services.tvrage.com/feeds/show_list.php
	 */
	
	//=================================================//
	//		Data
	//=================================================//
	
	private static String TAG_LOG = "GrabTVRageXML";
	
	//====  TVRage API URLs  ====//
	
	private static String rageURL = "http://services.tvrage.com/feeds/";
	private static String urlSearch 		= "%ssearch.php?show=%s";
	private static String urlDetailedSearch = "%sfull_search.php?show=%s";
	private static String urlShowInfo 		= "%sshowinfo.php?sid=%s";
	private static String urlEpisodeList 	= "%sepisode_list.php?sid=%s";
	private static String urlEpisodeInfo	= "%sepisodeinfo.php?show=%s&exact=1&ep=%dx%d";
	private static String urlFullShowInfo	= "%sfull_show_info.php?sid=%s";
	private static String urlShowList		= "%sshow_list.php";
	
	//====  XML tags  ====//	//REM activate as needed
	
	// roots
	private static String TAG_ROOT_RESULTS		= "Results";
//	private static String TAG_ROOT_SHOWINFO		= "Showinfo";		// showinfo
//	private static String TAG_ROOT_FULLSHOWINFO	= "Show";			// episode_list & full_show_info
//	private static String TAG_ROOT_EPISODELIST	= "Episodelist";	// episode_list - root for seasons and episodes 
	// elements
	private static String TAG_ELEMENT_SHOW		= "show";			// search results & 
//	private static String TAG_ELEMENT_SEASON	= "Season";			// episode_list
//	private static String TAG_ELEMENT_EPISODE	= "episode";		// episode_list
	// show attributes
	private static String TAG_SHOW_SHOWID		= "showid";
	private static String TAG_SHOW_TITLE		= "name";
//	private static String TAG_SHOW_SHOWNAME		= "showname";		// showinfo
	private static String TAG_SHOW_LINK			= "link";
//	private static String TAG_SHOW_SHOWLINK		= "showlink";		// showinfo
	private static String TAG_SHOW_COUNTRY		= "country";
//	private static String TAG_SHOW_ORIGIN		= "origin_country";	// showinfo
	private static String TAG_SHOW_START		= "started";		// search results
//	private static String TAG_SHOW_STARTDATE	= "startdate";		// showinfo
	private static String TAG_SHOW_END			= "ended";			// dual use
	private static String TAG_SHOW_SEASONS		= "seasons";
	private static String TAG_SHOW_STATUS		= "status";
//	private static String TAG_SHOW_RUNTIME		= "runtime";		// detailed only
	private static String TAG_SHOW_CLASSIF		= "classification";
	private static String TAG_SHOW_GENRES		= "genres";
	private static String TAG_SHOW_GENRE		= "genre";
//	private static String TAG_SHOW_NETWORK		= "network";		// detailed only
//	private static String TAG_SHOW_AIRTIME		= "airtime";		// detailed only
//	private static String TAG_SHOW_AIRDAY		= "airday";			// detailed only
//	private static String TAG_SHOW_AKAS			= "akas";			// detailed only
//	private static String TAG_SHOW_AKA			= "aka";			// detailed only
//	private static String TAG_SHOW_TIMEZONE		= "timezone";		// showinfo only
	// episode attributes
//	private static String TAG_SEASON_COUNT		= "totalseasons";
//	private static String TAG_SEASON_NUMBER		= "no";				// number attribute for season element
//	private static String TAG_EPISODE_NUM_EP	= "epnum";			// counts all episodes
//	private static String TAG_EPISODE_NUM_SEAS	= "seasonnum";		// resets count for each season
//	private static String TAG_EPISODE_NUM_PROD	= "prodnum";
//	private static String TAG_EPISODE_AIRDATE	= "airdate";
//	private static String TAG_EPISODE_LINK		= "link";
//	private static String TAG_EPISODE_TITLE		= "title";
	
	
	//=================================================//
	//		Public Methods
	//=================================================//
	
	public static List<ShowSimple> searchForShow(String searchTerm)
	{
		List<ShowSimple> shows = null;
		
		
		
		
		
		
		return shows;
	}
	
	//=================================================//
	//		Private Methods
	//=================================================//
	
	//// populate URL strings with appropriate values
	
	private static String formattedUrlShowSearch(String showTitle)
	{
		return String.format(urlSearch, rageURL, showTitle);
	}
	private static String formattedUrlDetailedShowSearch(String showTitle)
	{
		return String.format(urlDetailedSearch, rageURL, showTitle);
	}
	private static String formattedUrlShowInfo(String showId)
	{
		return String.format(urlShowInfo, rageURL, showId);
	}
	private static String formattedUrlEpisodeList(String showId)
	{
		return String.format(urlEpisodeList, rageURL, showId);
	}
	private static String formattedUrlEpisodeInfo(String showTitle, int seasonNum, int episodeNum)
	{
		return String.format(urlEpisodeInfo, rageURL, showTitle, seasonNum, episodeNum);
	}
	private static String formattedUrlFullShowInfo(String showId)
	{
		return String.format(urlFullShowInfo, rageURL, showId);
	}
	private static String formattedUrlShowList()
	{
		return urlShowList;
	}
	
	
	//// parse XML
	private static List<ShowSimple> parseForShowSimples(Document doc)
	{
		List<ShowSimple> shows = new ArrayList<ShowSimple>();
		
		// iterate through "show" elements
		ShowSimple show;
		NodeList showNodes = doc.getElementsByTagName(TAG_ELEMENT_SHOW);	// or should it be TAG_ROOT_RESULTS?
		for (int i=0; i < showNodes.getLength(); i++)
		{
			Element showElement = (Element) showNodes.item(i);
			show = new ShowSimple();
			
			NodeList showDetails = showElement.getElementsByTagName("*");
			
//			String blah = showDetails.
			
//			show = new ShowSimple(
//					element.,
//					name,
//					link,
//					country,
//					started,
//					ended,
//					seasons,
//					status,
//					classification,
//					genres)
		}
		
		return shows;
	}
	
	
	private static Document getXMLDocument(String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			return doc;
		}
		catch (MalformedURLException e)
		{
			if (MyLog.error) MyLog.e(TAG_LOG, "Error while creating URL: " +urlString);;
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			if (MyLog.error) MyLog.e(TAG_LOG, "Error while creating a DocumentBuilder");
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			if (MyLog.error) MyLog.e(TAG_LOG, "Error while parsing InputStream for Document");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			if (MyLog.error) MyLog.e(TAG_LOG, "Error while reading InputStream for Document");
			e.printStackTrace();
		}

		// will only reach here on error
		return null;
	}
}
