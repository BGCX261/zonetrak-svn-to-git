package zonetrak.com;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jdom.JDOMException;

import zonetrak.com.EnqueuedZone.ProcessMethod;
import zonetrak.com.rest.RESTClient;
import zonetrak.com.rest.RESTParameters;
import zonetrak.com.rest.RESTResponse;
import zonetrak.log.Logger;
import zonetrak.log.Logger.Level;
import zonetrak.position.Entity;
import zonetrak.position.WaypointEx;
import zonetrak.position.Zone;
import zonetrak.ui.SettingsFrame;

public class Communicator
{
	private static Communicator instance = null;

	public static boolean createZone(Zone zone) throws MiddlewareNotReachableException
	{
		try
		{
			String polygon = "";
			final ArrayList<WaypointEx> waypoints = zone.getWaypointList();

			for (int i = 0; i < waypoints.size(); i++)
			{
				if (i != 0)
				{
					polygon += "|";
				}

				polygon += waypoints.get(i).getPosition().getLatitude() + ";" + waypoints.get(i).getPosition().getLongitude();
			}

			final RESTClient restClient = new RESTClient(SettingsFrame.getInstance().getHost());
			final RESTParameters restParameters = new RESTParameters();

			try
			{
				restParameters.addParameter("polygon", URLEncoder.encode(polygon, "UTF-8"));
			}
			catch (final UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}

			final RESTResponse restResponse = restClient.put("/Zones/createZone/", restParameters);

			if (restResponse.getStatusCode() == 201 && restResponse.getContentType().equals("text/plain"))
			{
				zone.setId(Integer.parseInt(restResponse.getBody()));
				return true;
			}
		}
		catch (final Exception e)
		{
			Communicator.getInstance().setOffline(true);

			throw new MiddlewareNotReachableException();
		}

		return false;
	}

	public static boolean deleteZone(Zone zone) throws MiddlewareNotReachableException
	{
		try
		{
			final RESTClient restClient = new RESTClient(SettingsFrame.getInstance().getHost());
			final RESTParameters restParameters = new RESTParameters();
			restParameters.addParameter("zoneid", String.valueOf(zone.getId()));
			final RESTResponse restResponse = restClient.delete("/Zones/deleteZone/", restParameters);

			if (restResponse.getStatusCode() == 200)
			{
				return true;
			}
		}
		catch (final Exception e)
		{
			Communicator.getInstance().setOffline(true);

			throw new MiddlewareNotReachableException();
		}

		return false;
	}
	
	public static ArrayList<Entity> getEntities() throws MiddlewareNotReachableException
	{
		try
		{
			final RESTClient restClient = new RESTClient(SettingsFrame.getInstance().getHost());
			final RESTResponse restResponse = restClient.get("/Entities/getEntities/", new RESTParameters());

			if (restResponse.getStatusCode() == 200 && restResponse.getContentType().equals("application/xml"))
			{
				final EntityXMLParser entityXmlParser = new EntityXMLParser();

				try
				{
					entityXmlParser.loadXml(restResponse.getBody());
					return entityXmlParser.getEntities();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (final Exception e)
		{
			Communicator.getInstance().setOffline(true);

			throw new MiddlewareNotReachableException();
		}

		return null;
		
	}

	public static ArrayList<Entity> getRecentEntities() throws MiddlewareNotReachableException
	{
		try
		{
			final RESTClient restClient = new RESTClient(SettingsFrame.getInstance().getHost());
			final RESTResponse restResponse = restClient.get("/Entities/getRecentEntities/", new RESTParameters());

			if (restResponse.getStatusCode() == 200 && restResponse.getContentType().equals("application/xml"))
			{
				final EntityXMLParser entityXmlParser = new EntityXMLParser();

				try
				{
					entityXmlParser.loadXml(restResponse.getBody());
					return entityXmlParser.getEntities();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (final Exception e)
		{
			Communicator.getInstance().setOffline(true);

			throw new MiddlewareNotReachableException();
		}

		return null;
	}

	public static Communicator getInstance()
	{
		if (instance == null)
		{
			instance = new Communicator();
		}

		return instance;
	}

	public static ArrayList<Zone> getZones() throws MiddlewareNotReachableException
	{
		try
		{
			final RESTClient restClient = new RESTClient(SettingsFrame.getInstance().getHost());
			final RESTResponse restResponse = restClient.get("/Zones/getZones/", new RESTParameters());

			if (restResponse.getStatusCode() == 200 && restResponse.getContentType().equals("application/xml"))
			{
				final ZoneXMLParser zoneXmlParser = new ZoneXMLParser();

				try
				{
					zoneXmlParser.loadXml(restResponse.getBody());
					return zoneXmlParser.getZones();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (final Exception e)
		{
			Communicator.getInstance().setOffline(true);

			throw new MiddlewareNotReachableException();
		}

		return null;
	}

	@Deprecated
	public static ArrayList<Zone> Init()
	{
		final String fileName = SettingsFrame.getInstance().getZoneFile();
		ArrayList<Zone> zones = null;

		final File file = new File(fileName);

		try
		{
			file.createNewFile();

			final ZoneXMLParser zoneXmlParser = new ZoneXMLParser();
			zoneXmlParser.loadFile(fileName);
			zones = zoneXmlParser.getZones();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final JDOMException e)
		{
			e.printStackTrace();
		}

		return zones;
	}

	@Deprecated
	public static ArrayList<Entity> Read()
	{
		final String fileName = SettingsFrame.getInstance().getEntityFile();
		ArrayList<Entity> entities = null;

		final File file = new File(fileName);

		try
		{
			file.createNewFile();

			final EntityXMLParser entityXmlParser = new EntityXMLParser();
			entityXmlParser.loadFile(fileName);
			entities = entityXmlParser.getEntities();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final JDOMException e)
		{
			e.printStackTrace();
		}

		return entities;
	}

	@Deprecated
	public static void Write(ArrayList<Zone> zones)
	{
		final String fileName = SettingsFrame.getInstance().getZoneFile();

		final File file = new File(fileName);

		try
		{
			file.createNewFile();

			final ZoneXMLWriter zoneXmlWriter = new ZoneXMLWriter(zones);
			zoneXmlWriter.writeZones(fileName);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	private boolean isOffline;

	private final LinkedList<EnqueuedZone> zones;

	private Communicator()
	{
		this.isOffline = false;
		this.zones = new LinkedList<EnqueuedZone>();
	}

	public void enqueueZone(Zone zone, ProcessMethod processMethod)
	{
		this.zones.add(new EnqueuedZone(zone, processMethod));
	}

	public boolean isOffline()
	{
		return this.isOffline;
	}

	public boolean isOnline()
	{
		return !this.isOffline;
	}

	public void processZones()
	{
		if (this.zones != null && this.zones.size() > 0 && this.isOnline())
		{
			for (final EnqueuedZone enqueuedZone : this.zones)
			{
				try
				{
					if (enqueuedZone.getProcessMethod() == ProcessMethod.CREATE)
					{
						createZone(enqueuedZone.getZone());
					}
					else if (enqueuedZone.getProcessMethod() == ProcessMethod.DELETE)
					{
						deleteZone(enqueuedZone.getZone());
					}
				}
				catch (final MiddlewareNotReachableException e)
				{
					break;
				}
			}
		}
	}

	public void setOffline(boolean isOffline)
	{
		final boolean oldState = this.isOffline;
		this.isOffline = isOffline;

		if (this.isOffline != oldState)
		{
			if (this.isOffline)
			{
				Logger.getInstance().log("Switched to offline mode", Level.NOTICE);
			}
			else
			{
				Logger.getInstance().log("Switched to online mode", Level.NOTICE);
			}
		}
	}
}
