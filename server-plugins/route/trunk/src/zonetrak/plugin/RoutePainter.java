package zonetrak.plugin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.common.EntityComparator;
import zonetrak.ifaces.IDataManager;
import zonetrak.ifaces.IEntity;

public class RoutePainter implements Painter<JXMapViewer>
{
	private IDataManager dataManager;

	public RoutePainter(IDataManager dataManager)
	{
		this.dataManager = dataManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		ArrayList<IEntity> entities = (ArrayList<IEntity>) this.dataManager.getData("entities");

		if (entities == null || entities.size() == 0)
		{
			return;
		}

		Collections.sort(entities, new EntityComparator());

		Hashtable<String, ArrayList<IEntity>> sortedEntities = new Hashtable<String, ArrayList<IEntity>>();

		for (IEntity entity : entities)
		{
			if (!sortedEntities.containsKey(entity.getIdentifier()))
			{
				sortedEntities.put(entity.getIdentifier(), new ArrayList<IEntity>());
			}

			sortedEntities.get(entity.getIdentifier()).add(entity);
		}

		Set<Entry<String, ArrayList<IEntity>>> entries = sortedEntities.entrySet();
		Iterator<Entry<String, ArrayList<IEntity>>> entryIterator = entries.iterator();

		while (entryIterator.hasNext())
		{
			Entry<String, ArrayList<IEntity>> entry = entryIterator.next();

			for (int i = 1; i < entry.getValue().size(); i++)
			{
				Point2D previousPosition = map.convertGeoPositionToPoint(entry.getValue().get(i - 1).getPosition());
				Point2D currentPosition = map.convertGeoPositionToPoint(entry.getValue().get(i).getPosition());

				g.setColor(Color.BLACK);
				g.drawLine((int) previousPosition.getX(), (int) previousPosition.getY(), (int) currentPosition.getX(), (int) currentPosition.getY());
				g.fillOval((int) previousPosition.getX() - 1, (int) previousPosition.getY() - 1, 3, 3);
			}
		}
	}
}
