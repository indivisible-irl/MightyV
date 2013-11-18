package com.indivisible.mightyv.web;

public class GrabTVRageXML {

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
	
	// TVRage API URLs
	private static String rageURL = "http://services.tvrage.com/feeds/";
	private static String urlSearch 		= "%ssearch.php?show=%s";
	private static String urlDetailedSearch = "%sfull_search.php?show=%s";
	private static String urlShowInfo 		= "%sshowinfo.php?sid=%s";
	private static String urlEpisodeList 	= "%sepisode_list.php?sid=%s";
	private static String urlEpisodeInfo	= "%sepisodeinfo.php?show=%s&exact=1&ep=%dx%d";
	private static String urlFullShowInfo	= "%sfull_show_info.php?sid=%s";
	private static String urlShowList		= "%sshow_list.php";
	
	//=================================================//
	//		Public Methods
	//=================================================//
	
	
	
	//=================================================//
	//		Private Methods
	//=================================================//
	
	//// populate URL strings with appropriate values
	
	private String formattedUrlShowSearch(String showTitle)
	{
		return String.format(urlSearch, rageURL, showTitle);
	}
	private String formattedUrlDetailedShowSearch(String showTitle)
	{
		return String.format(urlDetailedSearch, rageURL, showTitle);
	}
	private String formattedUrlShowInfo(String showId)
	{
		return String.format(urlShowInfo, rageURL, showId);
	}
	private String formattedUrlEpisodeList(String showId)
	{
		return String.format(urlEpisodeList, rageURL, showId);
	}
	private String formattedUrlEpisodeInfo(String showTitle, int seasonNum, int episodeNum)
	{
		return String.format(urlEpisodeInfo, rageURL, showTitle, seasonNum, episodeNum);
	}
	private String formattedUrlFullShowInfo(String showId)
	{
		return String.format(urlFullShowInfo, rageURL, showId);
	}
	private String formattedUrlShowList()
	{
		return urlShowList;
	}
	
}
