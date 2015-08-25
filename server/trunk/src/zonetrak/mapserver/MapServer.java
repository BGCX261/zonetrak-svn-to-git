package zonetrak.mapserver;

import org.jdesktop.swingx.mapviewer.TileFactory;

public class MapServer
{
	private String name;
	private TileFactory tileFactory;
	private int defaultZoom;

	public MapServer()
	{
	}

	protected MapServer(String name, TileFactory tileFactory, int defaultZoom)
	{
		this.name = name;
		this.tileFactory = tileFactory;
		this.defaultZoom = defaultZoom;
	}

	public int getDefaultZoom()
	{
		return this.defaultZoom;
	}

	public String getName()
	{
		return this.name;
	}

	public TileFactory getTileFactory()
	{
		return this.tileFactory;
	}

	public void setDefaultZoom(int defaultZoom)
	{
		this.defaultZoom = defaultZoom;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setTileFactory(TileFactory tileFactory)
	{
		this.tileFactory = tileFactory;
	}
}
