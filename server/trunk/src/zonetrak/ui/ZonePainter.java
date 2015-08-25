package zonetrak.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.position.Zone;
import zonetrak.position.ZoneManager;

public class ZonePainter implements Painter<JXMapViewer>
{
	public ZonePainter()
	{
		super();
	}

	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		g = (Graphics2D) g.create();
		final Rectangle rect = map.getViewportBounds();
		g.translate(-rect.getX(), -rect.getY());

		if (ZoneManager.getInstance().getZones() != null)
		{
			for (final Zone zone : ZoneManager.getInstance().getZones())
			{
				if (zone.isDeleted())
				{
					continue;
				}

				final Polygon polygon = new Polygon();

				for (final Waypoint waypoint : zone.getWaypointList())
				{
					final Point2D pt = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
					polygon.addPoint((int) pt.getX(), (int) pt.getY());
				}

				if (zone.isSelected())
				{
					g.setColor(new Color(0, 0, 255, 50));
				}
				else
				{
					g.setColor(new Color(255, 0, 0, 50));
				}

				g.fill(polygon);

				if (zone.isSelected())
				{
					g.setColor(Color.BLUE);
				}
				else
				{
					g.setColor(Color.RED);
				}
				g.draw(polygon);
			}
		}

		g.dispose();
	}
}
