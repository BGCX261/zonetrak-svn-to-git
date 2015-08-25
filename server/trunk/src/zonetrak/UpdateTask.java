package zonetrak;

import java.util.ArrayList;
import java.util.TimerTask;

import zonetrak.com.Communicator;
import zonetrak.com.MiddlewareNotReachableException;
import zonetrak.events.EventManager;
import zonetrak.plugins.DataManager;
import zonetrak.plugins.Simulator;
import zonetrak.position.Entity;
import zonetrak.position.EntityManager;
import zonetrak.position.Zone;
import zonetrak.position.ZoneManager;
import zonetrak.ui.MapControl;

class UpdateTask extends TimerTask
{
	@Override
	public void run()
	{
		if (Simulator.getInstance().isEnabled())
		{
			this.simulate();
		}
		else if (Communicator.getInstance().isOnline())
		{
			this.communicate();
		}
	}

	@SuppressWarnings("unchecked")
	private void simulate()
	{
		final ArrayList<Entity> recentEntities = (ArrayList<Entity>) DataManager.getInstance().getData("recentEntities");

		if (recentEntities != null && recentEntities.size() > 0)
		{
			EntityManager.getInstance().setEntities(recentEntities);
			MapControl.getInstance().updateUI();
		}
	}

	private void communicate()
	{
		try
		{
			if (MapControl.getInstance().getMainMap().isPanEnabled())
			{
				final ArrayList<Zone> zones = Communicator.getZones();

				if (zones != null && zones.size() > 0)
				{
					ZoneManager.getInstance().setZones(zones);
					MapControl.getInstance().updateUI();
				}
			}

			final ArrayList<Entity> recentEntities = Communicator.getRecentEntities();

			if (recentEntities != null && recentEntities.size() > 0)
			{
				EntityManager.getInstance().setEntities(recentEntities);
				MapControl.getInstance().updateUI();
			}

			final ArrayList<Entity> entities = Communicator.getEntities();

			if (entities != null && entities.size() > 0)
			{
				DataManager.getInstance().setData("entities", entities);
			}
		}
		catch (final MiddlewareNotReachableException e)
		{
			EventManager.getInstance().fireEvent("MiddlewareNotReachable", e);
		}
	}
}
