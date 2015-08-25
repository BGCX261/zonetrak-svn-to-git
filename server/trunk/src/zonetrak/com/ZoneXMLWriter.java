package zonetrak.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import zonetrak.position.WaypointEx;
import zonetrak.position.Zone;

public class ZoneXMLWriter
{
	private final ArrayList<Zone> zones;

	public ZoneXMLWriter(ArrayList<Zone> zones)
	{
		this.zones = zones;
	}

	public void writeZones(String fileName) throws FileNotFoundException, IOException
	{
		final Document doc = new Document();

		final Element zonesElement = new Element("zones");

		for (final Zone zone : this.zones)
		{
			final Element zoneElement = new Element("zone");

			zoneElement.setAttribute("id", String.valueOf(zone.getId()));
			zoneElement.setAttribute("type", String.valueOf(zone.getType()));
			zoneElement.setAttribute("date", String.valueOf(zone.getDate().getTime() / 1000L));
			zoneElement.setAttribute("deleted", String.valueOf(zone.isDeleted()));

			final Element waypointsElement = new Element("waypoints");

			for (final WaypointEx waypoint : zone.getWaypointList())
			{
				final Element waypointElement = new Element("waypoint");

				waypointElement.setAttribute("latitude", String.valueOf(waypoint.getPosition().getLatitude()));
				waypointElement.setAttribute("longitude", String.valueOf(waypoint.getPosition().getLongitude()));

				waypointsElement.addContent(waypointElement);
			}

			zoneElement.addContent(waypointsElement);

			zonesElement.addContent(zoneElement);
		}

		doc.addContent(zonesElement);

		final XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		outp.output(doc, new FileOutputStream(new File(fileName)));
	}
}
