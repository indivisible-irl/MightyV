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
	
	private String TAG;
	
	private URL url;
	private String rootElement;
	
	// save individual tags in child classes
	
	
	
	//=================================================//
	//		constructor
	//=================================================//
	
	public XMLParser(String urlStr, String root)
	{
		this.TAG = this.getClass().getSimpleName();
		this.rootElement = root;
		
		try
		{
			this.url = new URL(urlStr);
		}
		catch (MalformedURLException e)
		{
			if (MyLog.error) MyLog.e(TAG, "Error forming URL: " +urlStr);
		}

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
	
	private InputStream getXMLStream()
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
	
	private void parseXML()
	{
		throw new UnsupportedOperationException();
	}
}
