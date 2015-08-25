package zonetrak.mapserver;

import java.awt.MediaTracker;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.wms.WMSService;
import org.jdesktop.swingx.mapviewer.wms.WMSTileFactory;

public class MapServerFactory
{
	private static final String URL_OPENSTREETMAP = "http://tile.openstreetmap.org";
	private static final String URL_TILES_AT_HOME_TILES = "http://tah.openstreetmap.org/Tiles/tile";
	private static final String URL_TILES_AT_HOME_MAPLINT = "http://tah.openstreetmap.org/Tiles/maplint";
	private static final String URL_NEW_POPULAR_EDITION = "http://richard.dev.openstreetmap.org/npe";
	private static final String URL_CYCLE_MAP = "http://andy.sandbox.cloudmade.com/tiles/cycle";
	private static final String URL_LOCAL = "http://localhost:8080/tile";

	private static MapServerFactory instance = null;

	private static TileFactory createFactory(final int max, final int maxdiff, String baseURL, String x, String y, String z)
	{
		final TileFactoryInfo info = new TileFactoryInfo(1, max - maxdiff, max, 256, true, true, baseURL, x, y, z)
		{
			public String getTileUrl(int x, int y, int zoom)
			{
				zoom = max - zoom;
				final String url = this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
				return url;
			}
		};

		return new DefaultTileFactory(info);
	}

	private static TileFactory createFactory(String key, int type)
	{
		String mapType = "normal";

		switch (type)
		{
			case 1:
				mapType = "satellite";
				break;

			case 2:
				mapType = "hybrid";
				break;
		}

		final String baseURL = "http://maps.google.com/staticmap?maptype=" + mapType + "&key=" + key;

		final int max = 22;
		final int tilesize = 512;

		final TileFactoryInfo info = new TileFactoryInfo(1, max, max, tilesize, true, true, baseURL, "", "", "")
		{
			public String getTileUrl(int x, int y, int zoom)
			{
				zoom = max - zoom;

				final BoundingBox bb = BoundingBox.tile2boundingBox(x, y, zoom);

				final double uly = (bb.west + bb.east) / 2;
				final double ulx = (bb.north + bb.south) / 2;

				final String url = this.baseURL + "&center=" + ulx + "," + uly + "&zoom=" + (zoom + 1) + "&size=" + tilesize + "x545";

				return url;
			}
		};

		return new DefaultTileFactory(info);
	}

	private static TileFactory createFactory(String baseUrl, String layer)
	{
		final WMSService wms = new WMSService(baseUrl, layer);
		return new WMSTileFactory(wms);
	}

	public static MapServerFactory getInstance()
	{
		if (instance == null)
		{
			instance = new MapServerFactory();
		}

		return instance;
	}

	private final ArrayList<MapServer> mapServers;

	protected MapServerFactory()
	{
		this.mapServers = new ArrayList<MapServer>();

		this.mapServers.add(new MapServer("OpenStreetMap", MapServerFactory.createFactory(18, 2, URL_OPENSTREETMAP, "x", "y", "z"), 1));
		this.mapServers.add(new MapServer("Tiles@Home (tiles)", MapServerFactory.createFactory(17, 2, URL_TILES_AT_HOME_TILES, "x", "y", "z"), 1));
		this.mapServers.add(new MapServer("Tiles@Home (maplint)", MapServerFactory.createFactory(17, 2, URL_TILES_AT_HOME_MAPLINT, "x", "y", "z"), 1));
		this.mapServers.add(new MapServer("New Popular Edition", MapServerFactory.createFactory(17, 2, URL_NEW_POPULAR_EDITION, "x", "y", "z"), 1));
		this.mapServers.add(new MapServer("Cycle Map", MapServerFactory.createFactory(17, 2, URL_CYCLE_MAP, "x", "y", "z"), 1));
		this.mapServers.add(new MapServer("Local", MapServerFactory.createFactory(17, 2, URL_LOCAL, "x", "y", "z"), 1));

		this.mapServers.add(new MapServer("Bodenkarte BK50 (testing)", MapServerFactory.createFactory("http://www.gis.nrw.de/wms/BK50?", "bk50"), 16));
		this.mapServers.add(new MapServer("Befahrbarkeit nach Hangneigung (testing)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/hangneigung?", "hangneigung,Fliessgewaesser"), 16));
		this.mapServers.add(new MapServer("Hochwasser (testing)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/hochwasser?", "0,1,2,3,18,19"), 16));
		this.mapServers.add(new MapServer("NASA (testing)", MapServerFactory.createFactory("http://wms.jpl.nasa.gov/wms.cgi?", "BMNG"), 16));
		this.mapServers.add(new MapServer("Linfos (testing)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/linfos?", "32,31,28,3,1"), 16));
		this.mapServers.add(new MapServer("Waldtypenkarte (testing)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/waldtyp?", "Waldtyp_Real"), 16));
		this.mapServers.add(new MapServer("Wasserschutzgebiete (testing)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/wsg?", "Heilquellenschutzgebiete,Trinkwasserschutzgebiete,"), 16));
		this.mapServers.add(new MapServer("Gebietsentwicklungsplan NRW (experimental)", MapServerFactory.createFactory("http://193.159.218.39/cgi-bin/gep?", "Oberflaechengewaesser,Waldbereiche,Siedlungsraum,Verkehrsinfrastruktur,Verwaltungsgrenzen,Beschriftung"), 16));
		this.mapServers.add(new MapServer("Gebietsentwicklungsplan NRW kompakt (unstable)", MapServerFactory.createFactory("http://193.159.218.39/cgi-bin/gep_tk50?", "tk50"), 16));
		this.mapServers.add(new MapServer("Digitale Topographische Karte 1:10.000 (testing)", MapServerFactory.createFactory("http://www.geoserver.nrw.de/GeoOgcWms1.3/servlet/DTK10?", "Raster:DTK10:Dtk10G,Raster:DTK10:Dtk10RS"), 16));
		this.mapServers.add(new MapServer("Digitale Topographische Karte 1:25.000 (testing)", MapServerFactory.createFactory("http://www.geoserver.nrw.de/GeoOgcWms1.3/servlet/DTK25?", "Raster:DTK25:Grundriss"), 16));
		this.mapServers.add(new MapServer("Strassenkarte NRW 1:100.000 - 1:500.000 (experimental)", MapServerFactory.createFactory("http://www.geoserver.nrw.de/GeoOgcWms1.3/servlet/NRW500?", "Raster:UEK500:UEK500"), 16));
		this.mapServers.add(new MapServer("NRW Uebersicht 1:500.000 - 1:5.000.000 (experimental)", MapServerFactory.createFactory("http://www.geoserver.nrw.de/GeoOgcWms1.3/servlet/NRW_Uebersicht?", "Raster:NRW_Uebersicht:NRW"), 16));
		this.mapServers.add(new MapServer("Digitale Topographische Karte 1:10.000 schwarz/weiss (testing)", MapServerFactory.createFactory("http://www.geoserver.nrw.de/GeoOgcWms1.3/servlet/SWDTK10?", "Raster:SWDTK10:Dtk10G,Raster:SWDTK10:Dtk10RS"), 16));
		this.mapServers.add(new MapServer("DGM50 (ustable)", MapServerFactory.createFactory("http://www.gis.nrw.de/wms/DGM50?", "DGM50"), 16));
		this.mapServers.add(new MapServer("DOP (unstable)", MapServerFactory.createFactory("http://www.gis.nrw.de/wms/dop?", "DOP_3"), 16));
		this.mapServers.add(new MapServer("stobo (experimental)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/stobo?", "stobo"), 16));
		this.mapServers.add(new MapServer("Bleibelastung der Ackerböden (experimental)", MapServerFactory.createFactory("http://www.gis2.nrw.de/wmsconnector/wms/stobo?", "Bleibelastung der Ackerboeden in mg pro kg"), 16));

		this.mapServers.add(new MapServer("Google Static Maps (Normal)", MapServerFactory.createFactory("ABQIAAAAH1-oiYGnRgGli3nyC5ApNBT3ENycL2x-kRuLZU4hmMBJ0rtNMBQg3B0_T6gtqRevEjiGbZOFxiZ65w", 0), 7));
		this.mapServers.add(new MapServer("Google Static Maps (Satellite)", MapServerFactory.createFactory("ABQIAAAAH1-oiYGnRgGli3nyC5ApNBT3ENycL2x-kRuLZU4hmMBJ0rtNMBQg3B0_T6gtqRevEjiGbZOFxiZ65w", 1), 7));
		this.mapServers.add(new MapServer("Google Static Maps (Hybrid)", MapServerFactory.createFactory("ABQIAAAAH1-oiYGnRgGli3nyC5ApNBT3ENycL2x-kRuLZU4hmMBJ0rtNMBQg3B0_T6gtqRevEjiGbZOFxiZ65w", 2), 7));
	}

	public MapServer getFirstAvailableMapServer()
	{

		for (final MapServer mapServer : this.mapServers)
		{
			URL url;

			try
			{
				url = new URL(mapServer.getTileFactory().getInfo().getTileUrl(16475, 16292, 15));

				final ImageIcon icon = new ImageIcon(url);

				if (icon.getImageLoadStatus() == MediaTracker.LOADING)
				{
					do
					{
						Thread.yield();
					} while (icon.getImageLoadStatus() == MediaTracker.LOADING);
				}
				else if (icon.getImageLoadStatus() == MediaTracker.ABORTED || icon.getImageLoadStatus() == MediaTracker.ERRORED)
				{
					continue;
				}

				return mapServer;
			}
			catch (final Exception e)
			{
			}
		}

		return null;
	}

	public MapServer getMapServer(String name)
	{
		for (final MapServer mapServer : this.mapServers)
		{
			if (mapServer.getName().equals(name))
			{
				return mapServer;
			}
		}

		return null;
	}

	public ArrayList<MapServer> getMapServers()
	{
		return this.mapServers;
	}
}
