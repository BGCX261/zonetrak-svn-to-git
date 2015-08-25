package zonetrak.plugin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.common.ImageFactory;
import zonetrak.ifaces.IDataManager;
import zonetrak.ifaces.IEntity;

public class THWEntityPainter implements Painter<JXMapViewer>
{
	private IDataManager dataManager;
	private URLClassLoader urlClassLoader;
	
	private Hashtable<String, Image> images;

	public THWEntityPainter(IDataManager dataManager, URLClassLoader urlClassLoader)
	{
		this.dataManager = dataManager;
		this.urlClassLoader = urlClassLoader;
		
		this.images = new Hashtable<String, Image>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
        g = (Graphics2D) g.create();
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.getX(), -rect.getY());
        
        ArrayList<IEntity> entities = (ArrayList<IEntity>)this.dataManager.getData("recentEntities");
        
        if (entities == null)
        {
        	return;
        }
        
        for (IEntity entity : entities)
        {
            Point2D pt = map.getTileFactory().geoToPixel(entity.getPosition(), map.getZoom());
            
            String path = entity.getType() + ".png";
            
            if (!this.images.containsKey(path))
            {
            	try
            	{
            		this.images.put(path, ImageFactory.createImage(this.urlClassLoader, path));
            	}
            	catch (FileNotFoundException e)
            	{
            		e.printStackTrace();
            	}
            }

            Image image = this.images.get(path);
            
            if (image != null)
            {
            	g.drawImage(image, (int)pt.getX() - image.getWidth(null) / 2, (int)pt.getY() - image.getHeight(null) / 2, null);
            }
            else
            {
            	g.setColor(Color.BLUE);
            	g.fillOval((int)pt.getX() - 15, (int)pt.getY() - 15, 30, 30);
            }
        }
	}
}
