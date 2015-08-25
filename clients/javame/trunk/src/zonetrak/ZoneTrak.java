package zonetrak;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.ProximityListener;
import javax.microedition.location.QualifiedCoordinates;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import zonetrak.com.BluetoothConnector;
import zonetrak.com.MapConnector;
import zonetrak.i18n.I18NLanguage;
import zonetrak.i18n.Translator;
import zonetrak.position.Zone;
import zonetrak.tools.Geometry;
import zonetrak.ui.MapItem;

public class ZoneTrak extends MIDlet implements CommandListener, LocationListener, ProximityListener
{
	/**
	 * Main display.
	 */
	private Display display;

	/**
	 * Forms.
	 */
	private Form mapForm;
	private Form configForm;

	/**
	 * Items.
	 */
	private MapItem mapItem;
	private TextField middlewareItem;
	private TextField friendlyName;
	private TextField identifier;
	private ChoiceGroup enabledGroup;

	/**
	 * Cached zones.
	 */
	private Vector zones;

	public ZoneTrak()
	{
		String locale = System.getProperty("microedition.locale");

		try
		{
			String className = "zonetrak.i18n.ZoneTrak_" + locale.substring(0, 2) + "_" + locale.substring(3, 5);
			I18NLanguage language = (I18NLanguage) Class.forName(className).newInstance();	
			Translator.getInstance().setLanguage(language);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Timer t = new Timer();
		t.schedule(new UpdateTask(), 0, 30000);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
	}

	protected void pauseApp()
	{
	}

	protected void startApp() throws MIDletStateChangeException
	{
		this.display = Display.getDisplay(this);

		/**
		 * Create the user interface.
		 */
		this.createUI();

		this.display.setCurrent(this.mapForm);

		new BluetoothConnector(this, this);
		MapConnector.getStatus();
	}

	private void createUI()
	{
		/**
		 * Create the map form.
		 */
		this.mapForm = new Form(Translator.getInstance().getMessage("Map"));
		this.mapForm.setCommandListener(this);

		/**
		 * Create the map item.
		 */
		this.mapItem = new MapItem();
		this.mapForm.append(this.mapItem);

		/**
		 * Create map form commands.
		 */
		this.mapForm.addCommand(new Command(Translator.getInstance().getMessage("Configuration"), Command.SCREEN, 0));

		/**
		 * Create the configuration form.
		 */
		this.configForm = new Form(Translator.getInstance().getMessage("Configuration"));
		this.configForm.setCommandListener(this);
		this.configForm.addCommand(new Command(Translator.getInstance().getMessage("Save"), Command.SCREEN, 0));

		/**
		 * Create configuration items.
		 */
		this.middlewareItem = new TextField(Translator.getInstance().getMessage("Middleware"), Configuration.DEFAULT_MIDDLEWARE_URL, 256, 0);
		this.friendlyName = new TextField(Translator.getInstance().getMessage("Name"), Configuration.DEFAULT_FRIENDLYNAME, 256, 0);
		this.identifier = new TextField(Translator.getInstance().getMessage("ID"), Configuration.DEFAULT_IDENTIFIER, 256, 0);
		this.enabledGroup = new ChoiceGroup(Translator.getInstance().getMessage("Enable"), Choice.MULTIPLE);
		this.enabledGroup.append(Translator.getInstance().getMessage("Yes"), null);
		this.configForm.append(this.middlewareItem);
		this.configForm.append(this.friendlyName);
		this.configForm.append(this.identifier);
		this.configForm.append(this.enabledGroup);
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c.getLabel().equals(Translator.getInstance().getMessage("Configuration")))
		{
			this.switchDisplayable(new Alert(Translator.getInstance().getMessage("Warning"),
					Translator.getInstance().getMessage("You should only change the configuration, if you really know what you are doing."), null, AlertType.WARNING),
					this.configForm);
		}
		else if (c.getLabel().equals(Translator.getInstance().getMessage("Save")))
		{
			boolean[] flags = new boolean[this.enabledGroup.size()];
			this.enabledGroup.getSelectedFlags(flags);

			if (flags[0])
			{
				if (MapConnector.getStatus())
				{
					Configuration.getInstance().setEnabled(true);
					Configuration.getInstance().setFriendlyName(this.friendlyName.getString());
					Configuration.getInstance().setIdentifier(this.identifier.getString());
					this.switchDisplayable(new Alert(Translator.getInstance().getMessage("Saved"), Translator.getInstance().getMessage("The configuration has been saved."), null, AlertType.INFO),
							this.mapForm);
				}
				else
				{
					Configuration.getInstance().setEnabled(false);
					this.switchDisplayable(new Alert(Translator.getInstance().getMessage("Error"), Translator.getInstance().getMessage("The middleware is not reachable."), null, AlertType.ERROR),
							this.configForm);
				}
			}
			else
			{
				Configuration.getInstance().setEnabled(false);
				this.switchDisplayable(new Alert(Translator.getInstance().getMessage("Notice"), Translator.getInstance().getMessage("The service is not enabled."), null, AlertType.INFO), this.configForm);
			}
		}
		else if (c.getLabel().equals(Translator.getInstance().getMessage("Back")))
		{
			this.switchDisplayable(null, this.mapForm);
		}
	}

	/**
	 * Switches to a new displayable.
	 * 
	 * @param alert
	 *            The alert to be shown. Can be null if no alert should be
	 *            displayed.
	 * @param nextDisplayable
	 *            The displayable to be shown.
	 */
	public void switchDisplayable(Alert alert, Displayable nextDisplayable)
	{
		if (alert == null)
		{
			this.display.setCurrent(nextDisplayable);
		}
		else
		{
			this.display.setCurrent(alert, nextDisplayable);
		}
	}

	public void locationUpdated(LocationProvider provider, Location location)
	{
		QualifiedCoordinates coord = location.getQualifiedCoordinates();

		if (Configuration.getInstance().isEnabled())
		{
			/**
			 * Set the location.
			 */
			this.mapItem.setLocation(location);

			/**
			 * Load, manipulate and draw the map.
			 */
			Image image = MapConnector.getMapTile(coord.getLatitude(), coord.getLongitude(), this.mapItem.getZoom());
			this.mapItem.setImage(image);

			/**
			 * Set zones.
			 */
			this.zones = MapConnector.getZones();

			/**
			 * Check whether you are in a zone.
			 */
			for (int i = 0; i < this.zones.size(); i++)
			{
				Zone zone = (Zone) this.zones.elementAt(i);

				/**
				 * Skip out-dated zones.
				 */
				if (zone.isDeleted())
				{
					continue;
				}
				
				double[] x = new double[zone.getWaypoints().size()];
				double[] y = new double[zone.getWaypoints().size()];

				for (int j = 0; j < zone.getWaypoints().size(); j++)
				{
					QualifiedCoordinates coords = (QualifiedCoordinates) zone.getWaypoints().elementAt(j);

					x[j] = coords.getLatitude();
					y[j] = coords.getLongitude();
				}

				if (Geometry.isPointInsidePolygon(x, y, location.getQualifiedCoordinates().getLatitude(), location.getQualifiedCoordinates().getLongitude()))
				{
					this.switchDisplayable(new Alert(Translator.getInstance().getMessage("Warning"), Translator.getInstance().getMessage("You have entered a zone!"), null, AlertType.ALARM), this.mapForm);
				}
			}

			/**
			 * Send the current position to the middleware.
			 */
			MapConnector.sendPosition(coord.getLatitude(), coord.getLongitude());
		}
	}

	public void providerStateChanged(LocationProvider provider, int newState)
	{
		System.out.println("providerStateChanged");
	}

	public void monitoringStateChanged(boolean arg0)
	{
		System.out.println("monitoringStateChanged");
	}

	public void proximityEvent(Coordinates arg0, Location arg1)
	{
		System.out.println("proximityEvent");
	}
	
	/**
	 * Update zone cache (every 30 seconds).
	 */
	class UpdateTask extends TimerTask
	{
		public void run()
		{
			zones = MapConnector.getZones();
		}
	}
}
