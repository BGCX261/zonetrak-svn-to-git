package zonetrak;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.com.Communicator;
import zonetrak.events.EventManager;
import zonetrak.i18n.I18NManager;
import zonetrak.log.Logger;
import zonetrak.log.Logger.Level;
import zonetrak.mapserver.MapServer;
import zonetrak.mapserver.MapServerFactory;
import zonetrak.plugins.DataManager;
import zonetrak.plugins.PluginLoader;
import zonetrak.plugins.Simulator;
import zonetrak.position.Entity;
import zonetrak.position.EntityManager;
import zonetrak.position.WaypointEx;
import zonetrak.position.Zone;
import zonetrak.position.ZoneManager;
import zonetrak.ui.LogToolBar;
import zonetrak.ui.MapControl;
import zonetrak.ui.SettingsFrame;

public class ZoneTrak extends JFrame implements MouseMotionListener, MouseListener, ActionListener, KeyListener, PropertyChangeListener
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				final ZoneTrak zoneTrak = new ZoneTrak();
				zoneTrak.setVisible(true);
				zoneTrak.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
		});
	}

	private JToolBar toolBar;
	private JCheckBoxMenuItem showToolBar;
	private JCheckBoxMenuItem showLogToolBar;
	private JCheckBoxMenuItem showTooltips;

	public ZoneTrak()
	{
		DataManager.getInstance().setData("mainWindow", this);

		Simulator.getInstance();
		
		this.initializeUI();

		final PluginLoader pluginLoader = new PluginLoader();
		pluginLoader.execute();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		EventManager.getInstance().fireEvent(e.getActionCommand(), e);

		if (e.getActionCommand().equals("Settings"))
		{
			SettingsFrame.getInstance().setVisible(true);
		}
		else if (e.getActionCommand().equals("Log"))
		{
			LogToolBar.getInstance().setVisible(true);
		}
		else if (e.getActionCommand().equals("Commit"))
		{
			Logger.getInstance().log("Processing zone queue", Level.INFO);

			Communicator.getInstance().setOffline(false);
			Communicator.getInstance().processZones();
		}
		else if (e.getActionCommand().startsWith("MAPSERVER"))
		{
			final String name = e.getActionCommand().replace("MAPSERVER", "");
			final MapServer mapServer = MapServerFactory.getInstance().getMapServer(name);
			final TileFactory tileFactory = mapServer.getTileFactory();

			if (mapServer != null && tileFactory != null)
			{
				final GeoPosition pos = MapControl.getInstance().getMainMap().getCenterPosition();
				final Painter painter = MapControl.getInstance().getMainMap().getOverlayPainter();

				MapControl.getInstance().setTileFactory(tileFactory);
				MapControl.getInstance().setCenterPosition(pos);
				MapControl.getInstance().getMainMap().setOverlayPainter(painter);
				MapControl.getInstance().setZoom(mapServer.getDefaultZoom());
			}
		}
		else if (e.getActionCommand().equals("Panning"))
		{
			MapControl.getInstance().getMainMap().setPanEnabled(true);

			for (int i = 0; i < this.toolBar.getComponentCount(); i++)
			{
				if (this.toolBar.getComponent(i).getName() != null && this.toolBar.getComponent(i).getName().equals("Edit Zones"))
				{
					final JToggleButton editZonesButton = (JToggleButton) this.toolBar.getComponent(i);
					editZonesButton.getModel().setSelected(false);
				}
			}
		}
		else if (e.getActionCommand().equals("Edit Zones"))
		{
			MapControl.getInstance().getMainMap().setPanEnabled(false);

			for (int i = 0; i < this.toolBar.getComponentCount(); i++)
			{
				if (this.toolBar.getComponent(i).getName() != null && this.toolBar.getComponent(i).getName().equals("Panning"))
				{
					final JToggleButton panningButton = (JToggleButton) this.toolBar.getComponent(i);
					panningButton.getModel().setSelected(false);
				}
			}
		}
		else if (e.getActionCommand().equals("Show Toolbar"))
		{
			this.toolBar.setVisible(this.showToolBar.getModel().isSelected());
		}
		else if (e.getActionCommand().equals("Show Log"))
		{
			LogToolBar.getInstance().setVisible(this.showLogToolBar.getModel().isSelected());
		}
		else if (e.getActionCommand().equals("Show Tooltips"))
		{
			MapControl.getInstance().getLabelPainter().setEnabled(this.showTooltips.getModel().isSelected());
		}
	}

	private void initializeUI()
	{
		final String nativeLF = UIManager.getSystemLookAndFeelClassName();

		try
		{
			UIManager.setLookAndFeel(nativeLF);
		}
		catch (final InstantiationException e)
		{
		}
		catch (final ClassNotFoundException e)
		{
		}
		catch (final UnsupportedLookAndFeelException e)
		{
		}
		catch (final IllegalAccessException e)
		{
		}

		this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/world.png"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setTitle("ZoneTrak");
		this.setSize(new Dimension(800, 600));

		this.setLayout(new BorderLayout());

		final JMenu options = new JMenu(I18NManager.getInstance().getMessage("Options"));
		options.setMnemonic('O');
		options.setActionCommand("Options");
		options.addActionListener(this);

		final JMenuItem settings = new JMenuItem(I18NManager.getInstance().getMessage("Settings"));
		settings.setMnemonic('S');
		settings.setActionCommand("Settings");
		settings.addActionListener(this);
		settings.setIcon(new ImageIcon("res/settings.png"));

		final JMenu mapServers = new JMenu(I18NManager.getInstance().getMessage("MapServer"));
		mapServers.setMnemonic('Z');
		mapServers.setActionCommand("Map Server");
		mapServers.addActionListener(this);
		mapServers.setIcon(new ImageIcon("res/mapserver.png"));

		for (final MapServer mapServer : MapServerFactory.getInstance().getMapServers())
		{
			final JMenuItem mapServerItem = new JMenuItem(mapServer.getName());
			mapServerItem.setActionCommand("MAPSERVER" + mapServer.getName());
			mapServerItem.addActionListener(this);
			mapServers.add(mapServerItem);
		}

		final JMenu mapMode = new JMenu(I18NManager.getInstance().getMessage("MapMode"));
		mapMode.setMnemonic('E');
		mapMode.setActionCommand("Map Mode");
		mapMode.addActionListener(this);
		mapMode.setIcon(new ImageIcon("res/mapmode.png"));

		final JMenuItem mapModePanning = new JMenuItem(I18NManager.getInstance().getMessage("Panning"));
		mapModePanning.setMnemonic('P');
		mapModePanning.setActionCommand("Panning");
		mapModePanning.addActionListener(this);
		mapModePanning.setIcon(new ImageIcon("res/panning.png"));

		final JMenuItem mapModeEditZones = new JMenuItem(I18NManager.getInstance().getMessage("EditZones"));
		mapModeEditZones.setMnemonic('D');
		mapModeEditZones.setActionCommand("Edit Zones");
		mapModeEditZones.addActionListener(this);
		mapModeEditZones.setIcon(new ImageIcon("res/editzones.png"));

		mapMode.add(mapModePanning);
		mapMode.add(mapModeEditZones);

		options.add(settings);
		options.add(mapServers);
		options.add(mapMode);

		final JMenu status = new JMenu(I18NManager.getInstance().getMessage("Status"));
		status.setMnemonic('T');
		status.setActionCommand("Status");
		status.addActionListener(this);

		final JMenuItem commit = new JMenuItem(I18NManager.getInstance().getMessage("Commit"));
		commit.setMnemonic('C');
		commit.setActionCommand("Commit");
		commit.addActionListener(this);
		commit.setIcon(new ImageIcon("res/commit.png"));

		status.add(commit);

		final JMenu view = new JMenu(I18NManager.getInstance().getMessage("View"));
		view.setMnemonic('V');
		view.setActionCommand("View");
		view.addActionListener(this);

		this.showToolBar = new JCheckBoxMenuItem(I18NManager.getInstance().getMessage("ShowToolbar"), true);
		this.showToolBar.setName("Show Toolbar");
		this.showToolBar.setMnemonic('H');
		this.showToolBar.setActionCommand("Show Toolbar");
		this.showToolBar.addActionListener(this);

		this.showLogToolBar = new JCheckBoxMenuItem(I18NManager.getInstance().getMessage("ShowLog"), false);
		this.showLogToolBar.setName("Show Log");
		this.showLogToolBar.setMnemonic('L');
		this.showLogToolBar.setActionCommand("Show Log");
		this.showLogToolBar.addActionListener(this);
		
		this.showTooltips = new JCheckBoxMenuItem(I18NManager.getInstance().getMessage("ShowTooltips"), false);
		this.showTooltips.setName("Show Tooltips");
		this.showTooltips.setMnemonic('O');
		this.showTooltips.setActionCommand("Show Tooltips");
		this.showTooltips.addActionListener(this);

		view.add(this.showToolBar);
		view.add(this.showLogToolBar);
		view.add(this.showTooltips);

		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(options);
		menuBar.add(status);
		menuBar.add(view);

		this.setJMenuBar(menuBar);

		this.toolBar = new JToolBar();
		
		DataManager.getInstance().setData("toolBar", this.toolBar);

		final JToggleButton panningButton = new JToggleButton(I18NManager.getInstance().getMessage("Panning"));
		panningButton.setName("Panning");
		panningButton.setIcon(new ImageIcon("res/panning.png"));
		panningButton.setActionCommand("Panning");
		panningButton.addActionListener(this);
		panningButton.getModel().setSelected(true);

		final JToggleButton editZonesButton = new JToggleButton(I18NManager.getInstance().getMessage("EditZones"));
		editZonesButton.setName("Edit Zones");
		editZonesButton.setIcon(new ImageIcon("res/editzones.png"));
		editZonesButton.setActionCommand("Edit Zones");
		editZonesButton.addActionListener(this);

		this.toolBar.add(panningButton);
		this.toolBar.add(editZonesButton);

		this.add(this.toolBar, BorderLayout.PAGE_START);
		this.add(MapControl.getInstance(this), BorderLayout.CENTER);
		this.add(LogToolBar.getInstance(), BorderLayout.PAGE_END);

		final Timer timer = new Timer();
		timer.schedule(new UpdateTask(), 1000, 1000);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		EventManager.getInstance().fireEvent("keyPressed", e);
		
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			final JXMapViewer map = MapControl.getInstance().getMainMap();

			final ArrayList<Zone> zones = ZoneManager.getInstance().getZones();
			final ArrayList<Zone> removableZones = new ArrayList<Zone>();

			for (final Zone zone : zones)
			{
				if (zone.isSelected())
				{
					removableZones.add(zone);

					Logger.getInstance().log("Removed zone with ID " + zone.getId(), Level.INFO);
				}
			}

			ZoneManager.getInstance().removeZones(removableZones);

			map.invalidate();
			map.updateUI();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		EventManager.getInstance().fireEvent("keyReleased", e);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		EventManager.getInstance().fireEvent("keyTyped", e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mouseClicked", e);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mouseDragged", e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mouseEntered", e);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mouseExited", e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mouseMoved", e);

		final JXMapViewer map = MapControl.getInstance().getMainMap();

		if (map != null)
		{
			/**
			 * Update window title (latitude/longitude).
			 */
			if (map.getMousePosition(true) != null)
			{
				final GeoPosition pos = map.convertPointToGeoPosition(map.getMousePosition(true));
				this.setTitle("ZoneTrak / Lat: " + pos.getLatitude() + " Lon: " + pos.getLongitude());
			}

			/**
			 * Fire entity (un-)hover events.
			 */
			for (final Entity entity : EntityManager.getInstance().getEntities())
			{
				final Point2D point = map.getTileFactory().geoToPixel(entity.getPosition(), map.getZoom());

				final Rectangle rect = map.getViewportBounds();
				final Point translatedPoint = new Point((int) point.getX() - (int) rect.getX(), (int) point.getY() - (int) rect.getY());

				if (translatedPoint.distance(e.getPoint()) < 10)
				{
					EventManager.getInstance().fireEvent("entityHover", entity);
				}
				else
				{
					EventManager.getInstance().fireEvent("entityUnhover", entity);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mousePressed", e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		EventManager.getInstance().fireEvent("mouseReleased", e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		EventManager.getInstance().fireEvent("propertyChange", e);
		
		if (e.getPropertyName().equals("polygon"))
		{
			final LinkedList<GeoPosition> polygon = (LinkedList<GeoPosition>) e.getNewValue();

			final Zone zone = new Zone();

			for (int i = 0; i < polygon.size(); i++)
			{
				zone.addWaypoint(new WaypointEx(polygon.get(i).getLatitude(), polygon.get(i).getLongitude()));
			}

			ZoneManager.getInstance().addZone(zone);

			Logger.getInstance().log("Created a new zone with ID " + zone.getId(), Level.INFO);
		}
		else if (e.getPropertyName().equals("rightClick"))
		{
			final JXMapViewer map = MapControl.getInstance().getMainMap();
			final GeoPosition position = (GeoPosition) e.getNewValue();

			final ArrayList<Zone> zones = ZoneManager.getInstance().getZones();
			final ArrayList<Zone> selectableZones = new ArrayList<Zone>();

			for (final Zone zone : zones)
			{
				if (zone.isDeleted())
				{
					continue;
				}

				final Polygon polygon = new Polygon();

				for (final WaypointEx waypoint : zone.getWaypointList())
				{
					final Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
					polygon.addPoint((int) point.getX(), (int) point.getY());
				}

				if (polygon.contains(map.getTileFactory().geoToPixel(position, map.getZoom())))
				{
					selectableZones.add(zone);
				}
				else
				{
					zone.setSelected(false);
				}
			}

			Zone selectedZone = null;

			if (selectableZones.size() > 1)
			{
				for (int i = 0; i < selectableZones.size(); i++)
				{
					if (selectableZones.get(i).isSelected())
					{
						selectedZone = i + 1 < selectableZones.size() ? selectableZones.get(i + 1) : selectableZones.get(0);
						break;
					}
				}

				if (selectedZone != null)
				{
					for (final Zone zone : zones)
					{
						if (zone.equals(selectedZone))
						{
							zone.setSelected(true);
						}
						else
						{
							zone.setSelected(false);
						}
					}
				}
				else
				{
					selectableZones.get(0).setSelected(true);

					for (int i = 1; i < selectableZones.size(); i++)
					{
						zones.get(i).setSelected(false);
					}
				}
			}
			else if (selectableZones.size() == 1)
			{
				selectableZones.get(0).setSelected(true);
			}

			map.updateUI();
		}
	}
}
