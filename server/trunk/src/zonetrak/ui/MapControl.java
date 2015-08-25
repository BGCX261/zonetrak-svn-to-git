package zonetrak.ui;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.CompoundPainter;

import zonetrak.mapserver.MapServerFactory;
import zonetrak.plugins.DataManager;
import zonetrak.position.Zone;
import zonetrak.position.ZoneManager;

public class MapControl extends JXMapKit
{
	private static MapControl instance;

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static MapControl getInstance(Object... listeners)
	{
		if (instance == null)
		{
			instance = new MapControl();

			DataManager.getInstance().setData("map", instance.getMainMap());
			
			instance.setTileFactory(MapServerFactory.getInstance().getMapServer("OpenStreetMap").getTileFactory());

			instance.setMiniMapVisible(true);
			instance.setZoomButtonsVisible(true);
			instance.setZoomSliderVisible(true);
			instance.setAddressLocation(new GeoPosition(51.47094, 6.92501));
			instance.setZoom(1);

			instance.zonePainer = new ZonePainter();
			instance.entityPainter = new EntityPainter();
			instance.labelPainter = new LabelPainter();
			instance.pluginPainter = new PluginPainter();
			instance.compoundPainter = new CompoundPainter();
			instance.compoundPainter.setCacheable(false);
			instance.compoundPainter.setPainters(instance.zonePainer, instance.entityPainter, instance.labelPainter, instance.pluginPainter);
			instance.getMainMap().setOverlayPainter(instance.compoundPainter);
			
			for (final Object listener : listeners)
			{
				if (listener instanceof MouseMotionListener)
				{
					instance.getMainMap().addMouseMotionListener((MouseMotionListener) listener);
				}
				if (listener instanceof MouseListener)
				{
					instance.getMainMap().addMouseListener((MouseListener) listener);
				}
				if (listener instanceof KeyListener)
				{
					instance.getMainMap().addKeyListener((KeyListener) listener);
				}
				if (listener instanceof PropertyChangeListener)
				{
					instance.getMainMap().addPropertyChangeListener("polygon", (PropertyChangeListener) listener);
					instance.getMainMap().addPropertyChangeListener("rightClick", (PropertyChangeListener) listener);
				}
			}
		}

		return instance;
	}

	private ZonePainter zonePainer;
	private EntityPainter entityPainter;
	private LabelPainter labelPainter;
	private PluginPainter pluginPainter;

	@SuppressWarnings("unchecked")
	private CompoundPainter compoundPainter;

	private MapControl()
	{
		super();

		ZoneManager.getInstance().setZones(new ArrayList<Zone>());
	}
	
	public LabelPainter getLabelPainter()
	{
		return this.labelPainter;
	}
}
