package zonetrak.com;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import zonetrak.Configuration;
import zonetrak.tools.MathME;
import zonetrak.xml.XMLParser;

public class MapConnector
{
	public MapConnector()
	{
	}

	public static Image getImage(String url)
	{
		Image image = null;
		HttpConnection httpConnection = null;

		try
		{
			httpConnection = (HttpConnection) Connector.open(url);
			image = Image.createImage(httpConnection.openInputStream());
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (httpConnection != null)
			{
				httpConnection = null;
			}
		}
		
		return image;
	}
	
	public static String getText(String url, String method)
	{
		StringBuffer result = new StringBuffer();
		HttpConnection httpConnection = null;
		InputStream is = null;
		
		try
		{
			httpConnection = (HttpConnection) Connector.open(url);
			httpConnection.setRequestMethod(method);
			is = httpConnection.openInputStream();
			long len = 0;
			int ch = 0;

			len = httpConnection.getLength();

			if (len != -1)
			{
				for (int i =0 ; i < len ; i++ )
					if ((ch = is.read()) != -1)

				result.append((char) ch);
			}
			else
			{
				while ((ch = is.read()) != -1)
				{
					len = is.available() ;
					result.append((char)ch);
				}
			}
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (httpConnection != null)
			{
				httpConnection = null;
			}
			
			if (is != null)
			{
				is = null;
			}
		}
		
		return result.toString();
	}
	
	public static void sendPosition(final double latitude, final double longitude)
	{
		String url = Configuration.getInstance().getMiddlewareUrl() + "/Entities/updatePosition/?action=position&lat=" + latitude + "&lon=" + longitude + "&friendlyname=" + Configuration.getInstance().getFriendlyName() + "&identifier=" + Configuration.getInstance().getIdentifier();
		
		getText(url, "POST");
	}
	
	public static Vector getZones()
	{
		String url = Configuration.getInstance().getMiddlewareUrl() + "/Zones/getZones";
		
		String result = getText(url, "GET");

		XMLParser xmlParser = new XMLParser();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try
		{
			saxParser = factory.newSAXParser();
			saxParser.parse(new ByteArrayInputStream(result.getBytes("UTF-8")), xmlParser);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return xmlParser.getZones();
		/*
		String[] lines = Tools.split(result, "\n");

		for (int i = 0; i < lines.length; i++)
		{
			String[] data = Tools.split(lines[i], ";");
			
			if (data.length < 5)
			{
				continue;
			}

			Zone zone = null;

			int id = Integer.parseInt(data[0]);

			for (int j = 0; j < zones.size(); j++)
			{
				if (((Zone)zones.elementAt(j)).getId() == id)
				{
					zone = (Zone)zones.elementAt(j);
					break;
				}
			}

			if (zone != null)
			{
				QualifiedCoordinates coords = new QualifiedCoordinates(Double.parseDouble(data[3]), Double.parseDouble(data[4]), 0f, 0f, 0f);
				zone.addWaypoint(coords);
			}
			else
			{
				zone = new Zone();
				zone.setId(id);
				zone.setType(Integer.parseInt(data[1]));
				zones.addElement(zone);
			}
		}
		
		return zones;*/
	}
	
	public static boolean getStatus()
	{
		String url = Configuration.getInstance().getMiddlewareUrl() + "/Status";

		if (getText(url, "GET").toString().equals("OK"))
		{
			return true;
		}
		
		return false;
	}
	
	public static int[] translateLocation(final int width, final int height, final double latitude, final double longitude, final int zoom)
	{
		String url = Configuration.getInstance().getMiddlewareUrl() + "/Map/translateCoordinates/?width=" + width + "&height=" + height + "&lat=" + latitude + "&lon=" + longitude + "&zoom=" + zoom + "&reflat=" + latitude + "&reflon=" + longitude;
		String result = getText(url, "GET");

		XMLParser xmlParser = new XMLParser();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try
		{
			saxParser = factory.newSAXParser();
			saxParser.parse(new ByteArrayInputStream(result.getBytes("UTF-8")), xmlParser);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return xmlParser.getTranslatedLocation();
	}

	public static Image getMapTile(final double latitude, final double longitude, final int zoom)
	{
		String url = "http://tile.openstreetmap.org/" + getTileNumber(latitude, longitude, zoom) + ".png";

		return getImage(url);
	}
	
	public static int[] getTileNumbers(final double lat, final double lon, final int zoom)
	{
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math.floor((1 - MathME.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180)) / Math.PI) / 2 * (1 << zoom));
		
		return new int[] { xtile, ytile };
	}

	public static String getTileNumber(final double lat, final double lon, final int zoom)
	{
		int[] tilePositions = getTileNumbers(lat, lon, zoom);
		return ("" + zoom + "/" + tilePositions[0] + "/" + tilePositions[1]);
	}
}
