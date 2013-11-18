package com.tvrage.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.indivisible.mightyv.util.MyLog;

/**
 * Parent class to be extended for individual XML Feed Parsing
 * @author indiv
 */
public class XMLParser
{
	
	//=================================================//
	//		data
	//=================================================//
	
	protected String TAG;
	
	protected URL url = null;
	protected String rootElement = null;
	
	protected static String urlRoot = "http://services.tvrage.com/feeds/";
	// save individual tags in child classes
	
	
	//=================================================//
	//		constructor
	//=================================================//
	
	public XMLParser()
	{
		this.TAG = this.getClass().getSimpleName();
	}
	
	
	//=================================================//
	//		gets & sets
	//=================================================//
	
	public URL getURL()
	{
		return this.url;
	}
	public void setURL(URL url)
	{
		this.url = url;
	}
	public void setURL(String urlStr)
	{
		try
		{
			this.url = new URL(urlStr);
		}
		catch (MalformedURLException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Error forming URL: " +urlStr);
		}
	}
	
	public String getRootElement()
	{
		return this.rootElement;
	}
	public void setRootElement(String rootElement)
	{
		this.rootElement = rootElement;
	}
	
	
	//=================================================//
	//		methods
	//=================================================//
	
	protected InputStream getXMLInputStream()
	{
		InputStream inputStream = null;
		try
		{
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        conn.connect();
	        inputStream = conn.getInputStream();
		}
		catch (IOException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Error while getting InputStream: " +url.toString());
		}
		return inputStream;
	}
	
	protected boolean parseXML(InputStream stream)
	{
		throw new UnsupportedOperationException("Implement/Override in child class");
	}
}
