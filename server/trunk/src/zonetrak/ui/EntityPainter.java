package zonetrak.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Date;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.plugins.DataManager;
import zonetrak.position.Entity;
import zonetrak.position.EntityManager;

public class EntityPainter implements Painter<JXMapViewer>
{
	@SuppressWarnings("unchecked")
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		final Object entityPainter = DataManager.getInstance().getData("entityPainter");

		if (entityPainter != null && entityPainter instanceof Painter<?>)
		{
			((Painter<JXMapViewer>) entityPainter).paint(g, map, w, h);

			return;
		}

		g = (Graphics2D) g.create();
		final Rectangle rect = map.getViewportBounds();
		g.translate(-rect.getX(), -rect.getY());

		final Date d = new Date();

		for (final Entity entity : EntityManager.getInstance().getEntities())
		{
			final Point2D pt = map.getTileFactory().geoToPixel(entity.getPosition(), map.getZoom());

			final long age = (d.getTime() - entity.getDate().getTime()) / 1000L;

			if (age >= 60)
			{
				g.setColor(Color.RED);
			}
			else if (age <= 0)
			{
				g.setColor(Color.GREEN);
			}
			else
			{
				final int red = (int) (255 / (60 - age));
				final int green = (int) (255 / age);
				g.setColor(new Color(red, green, 0));
			}

			// g.setColor(Color.BLACK);
			g.fillOval((int) pt.getX() - 10, (int) pt.getY() - 10, 20, 20);
			g.setColor(Color.WHITE);
			g.fillOval((int) pt.getX() - 6, (int) pt.getY() - 6, 12, 12);
		}

		g.dispose();
	}
}
