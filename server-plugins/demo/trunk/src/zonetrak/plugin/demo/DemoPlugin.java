package zonetrak.plugin.demo;

import java.util.ArrayList;
import java.util.Collections;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import zonetrak.common.AbstractPlugin;
import zonetrak.common.EntityComparator;
import zonetrak.ifaces.IEntity;
import zonetrak.ifaces.ISubscriber;

public class DemoPlugin extends AbstractPlugin implements ISubscriber
{
	@Override
	public void initPlugin()
	{
		this.getEventManager().subscribe("entities", this);
	}

	@Override
	public void eventFired(String type, Object... args)
	{
		@SuppressWarnings("unchecked")
		ArrayList<IEntity> entities = (ArrayList<IEntity>)this.getDataManager().getData("entities");

		/**
		 * Events are raised even if the value
		 * is null or the list is empty.
		 */
		if (entities == null || entities.size() == 0)
		{
			return;
		}

		Collections.sort(entities, new EntityComparator());
		
		IEntity entity = entities.get(entities.size() - 1);

		GeoPosition position = entity.getPosition();

		System.out.println("The last entity was located at:");
		System.out.println("Lat: " + position.getLatitude() + " Lon: " + position.getLongitude());
		
	}
}
