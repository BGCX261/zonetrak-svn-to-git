package zonetrak.com;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import zonetrak.position.WaypointEx;
import zonetrak.position.Zone;

public class ZoneXMLParser
{
	private final ArrayList<Zone> zones;
	private Document doc;

	public ZoneXMLParser()
	{
		this.zones = new ArrayList<Zone>();
	}

	public ArrayList<Zone> getZones()
	{
		return this.zones;
	}

	public void loadFile(String fileName) throws JDOMException, IOException
	{
		final SAXBuilder builder = new SAXBuilder();
		this.doc = builder.build(fileName);

		this.parseXML();
	}

	public void loadXml(String xmlDoc) throws JDOMException, IOException
	{
		final SAXBuilder builder = new SAXBuilder();
		final InputStream input = new ByteArrayInputStream(xmlDoc.getBytes());
		this.doc = builder.build(input);

		this.parseXML();
	}

	private void parseXML() throws DataConversionException
	{
		final Element root = this.doc.getRootElement();

		for (final Object zone : root.getChildren("zone"))
		{
			this.zones.add(this.parseZone((Element) zone));
		}
	}

	private Zone parseZone(Element e) throws DataConversionException
	{
		final Zone zone = new Zone();

		zone.setId(e.getAttribute("id").getIntValue());
		zone.setType(e.getAttribute("type").getIntValue());
		zone.setDate(new Date(e.getAttribute("date").getLongValue() * 1000L));

		if (e.getAttribute("deleted") != null)
		{
			zone.setDeleted(e.getAttribute("deleted").getBooleanValue());
		}

		for (final Object waypointObject : e.getChild("waypoints").getChildren("waypoint"))
		{
			final Element waypointElement = (Element) waypointObject;
			final double latitude = waypointElement.getAttribute("latitude").getDoubleValue();
			final double longitude = waypointElement.getAttribute("longitude").getDoubleValue();
			zone.addWaypoint(new WaypointEx(latitude, longitude));
		}

		return zone;
	}
}
