package zonetrak.ui;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.location.Location;

import zonetrak.Configuration;
import zonetrak.com.MapConnector;

public class MapItem extends CustomItem
{
	protected final static int DEFAULT_ZOOM = 15;
	protected final static int DEFAULT_WIDTH = 240;
	protected final static int DEFAULT_HEIGHT = 246;
	protected final static int NUM_1 = 49;
	protected final static int NUM_2 = 50;
	protected final static int NUM_3 = 51;
	protected final static int NUM_4 = 52;
	protected final static int NUM_5 = 53;
	protected final static int NUM_6 = 54;
	protected final static int NUM_7 = 55;
	protected final static int NUM_8 = 56;
	protected final static int NUM_9 = 57;
	protected final static int MARKER_WIDTH = 10;
	protected final static int MARKER_HEIGHT = 10;

	private Image mapImage;
	private Image cacheImage;
	private int zoom = DEFAULT_ZOOM;
	private int width = DEFAULT_WIDTH;
	private int height = DEFAULT_HEIGHT;
	
	private Location location;
	
	private Timer timer;
	private static int phase = 1;

	public MapItem()
	{
		super("");
		
		this.timer = new Timer();
        this.timer.schedule(new UpdateTask(), 500, 500);
	}

	public void setImage(Image image)
	{
		this.mapImage = image;
		this.updateData();
	}
	
	public void setLocation(Location location)
	{
		boolean update = !location.equals(this.location);
		this.location = location;
		
		if (update)
		{
			this.repaint();
		}
	}
	
	public int getZoom()
	{
		return this.zoom;
	}
	
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	protected int getMinContentHeight()
	{
		return this.height;
	}

	protected int getMinContentWidth()
	{
		return this.width;
	}

	protected int getPrefContentHeight(int width)
	{
		return this.height;
	}

	protected int getPrefContentWidth(int height)
	{
		return this.width;
	}
	
	private void updateData()
	{
		if (this.mapImage != null)
		{
			String url = Configuration.getInstance().getMiddlewareUrl() + "/Map/getMap/?action=map&width=" + this.getMinContentWidth() + "&height=" + this.getMinContentHeight() + "&lat=" + this.location.getQualifiedCoordinates().getLatitude() + "&lon=" + this.location.getQualifiedCoordinates().getLongitude() + "&zoom=" + this.zoom;
			Image img = MapConnector.getImage(url);
			this.cacheImage = this.drawPosition(img, this.location.getQualifiedCoordinates().getLatitude(), this.location.getQualifiedCoordinates().getLongitude());
		}
	}

	protected void paint(Graphics g, int w, int h)
	{
		if (this.cacheImage != null)
		{
			g.drawImage(this.cacheImage, 0, 0, 0);
		}
		else
		{
			g.drawRect(0, 0, this.getMinContentWidth(), this.getMinContentHeight());
		}
	}
	
	public Image drawPosition(Image image, final double lat, final double lon)
	{
		int coords[] = MapConnector.translateLocation(this.getMinContentWidth(), this.getMinContentHeight(), lat, lon, this.zoom);
		
		/**
		 * Make the image mutable.
		 */
		Image img = Image.createImage(image.getWidth(), image.getHeight());
		
		img.getGraphics().drawImage(image, 0, 0, 0);
		
		img.getGraphics().drawRect(coords[0] - 10, coords[1] - 10, 20, 20);
		
		return img;
	}
	

	protected void keyPressed(int keyCode)
	{
		if (keyCode == NUM_2)
		{
			this.zoom += 1;
		}
		else if (keyCode == NUM_8)
		{
			this.zoom -= 1;
		}
		else if (keyCode == NUM_6)
		{
			this.zoom = DEFAULT_ZOOM;
		}
	}
	
    /**
     * This task is executed periodically to update animated parts of the
     * map.
     */
    private class UpdateTask extends TimerTask
    {
        public void run()
        {
            phase = (phase + 1) % 5;
            updateData();
            repaint();
        }
    }
}
