package zonetrak.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.position.Entity;
import zonetrak.position.EntityManager;

public class LabelPainter implements Painter<JXMapViewer>
{
	private boolean isEnabled;

	private final static int HORIZONTAL_PADDING = 10;
	private final static int VERTICAL_PADDING = 10;
	private final static int VERTICAL_MARGIN = 20;
	private final static int VERTICAL_OFFSET = 10;
	
	public LabelPainter()
	{
		this.isEnabled = false;
	}
	
	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
	
	public boolean isEnabled()
	{
		return this.isEnabled;
	}

	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		if (!this.isEnabled)
		{
			return;
		}
		
		for (Entity entity : EntityManager.getInstance().getEntities())
		{
			Point2D position = map.getTileFactory().geoToPixel(entity.getPosition(), map.getZoom());
			Rectangle rect = map.getViewportBounds();
			Point translatedPosition = new Point((int) position.getX() - rect.x, (int) position.getY() - rect.y);
			
			g.translate(translatedPosition.getX(), translatedPosition.getY());
			this.drawEntity(g, entity, translatedPosition);
			g.translate(-translatedPosition.getX(), -translatedPosition.getY());
		}
	}

	private void drawEntity(Graphics2D g, Entity entity, Point translatedPosition)
	{
		g.translate(0, VERTICAL_OFFSET);
		
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(entity.getFriendlyName(), g);
		final int width = (int)bounds.getWidth();
		final int height = (int)bounds.getHeight();

		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.5f));

		final int x = -width / 2 - HORIZONTAL_PADDING / 2;
		final int y = VERTICAL_MARGIN;
		final int w = width + HORIZONTAL_PADDING;
		final int h = height + VERTICAL_PADDING;
		
		g.fillRoundRect(x, y, w, h, 10, 10);

		Polygon triangle = new Polygon();
		triangle.addPoint(0, 0);
		triangle.addPoint(11, 11);
		triangle.addPoint(-11, 11);
		triangle.translate(0, 9);
		g.fill(triangle);

		g.setColor(Color.WHITE);
		g.drawString(entity.getFriendlyName(), -width / 2, height / 2 + VERTICAL_MARGIN + VERTICAL_PADDING);

		g.translate(0, -VERTICAL_OFFSET);
	}
}
