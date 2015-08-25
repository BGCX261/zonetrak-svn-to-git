package zonetrak.position;

import java.util.ArrayList;
import java.util.Collection;

import zonetrak.com.Communicator;
import zonetrak.com.MiddlewareNotReachableException;
import zonetrak.com.EnqueuedZone.ProcessMethod;

public class ZoneManager
{
	private static ZoneManager instance = null;

	public static ZoneManager getInstance()
	{
		if (instance == null)
		{
			instance = new ZoneManager();
		}

		return instance;
	}

	private ArrayList<Zone> zones;

	private ZoneManager()
	{
	}

	public void addZone(Zone zone)
	{
		if (Communicator.getInstance().isOnline())
		{
			try
			{
				Communicator.createZone(zone);
			}
			catch (final MiddlewareNotReachableException e)
			{
				Communicator.getInstance().enqueueZone(zone, ProcessMethod.CREATE);
			}
		}
		else
		{
			Communicator.getInstance().enqueueZone(zone, ProcessMethod.CREATE);
		}

		this.zones.add(zone);
	}

	public ArrayList<Zone> getZones()
	{
		return this.zones;
	}

	public void removeZones(Collection<Zone> zones)
	{
		for (final Zone zone : zones)
		{
			if (Communicator.getInstance().isOnline())
			{
				try
				{
					Communicator.deleteZone(zone);
				}
				catch (final MiddlewareNotReachableException e)
				{
					Communicator.getInstance().enqueueZone(zone, ProcessMethod.DELETE);
				}
			}
			else
			{
				Communicator.getInstance().enqueueZone(zone, ProcessMethod.DELETE);
			}

			this.zones.remove(zone);
		}
	}

	public void setZones(ArrayList<Zone> zones)
	{
		this.zones = zones;
	}

	@Override
	public String toString()
	{
		return "Zones";
	}
}
