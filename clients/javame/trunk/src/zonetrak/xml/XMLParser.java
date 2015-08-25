package zonetrak.xml;

import java.util.Stack;
import java.util.Vector;

import javax.microedition.location.QualifiedCoordinates;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import zonetrak.position.Zone;

public class XMLParser extends DefaultHandler
{
	private Stack qNameStack;

	private int[] translatedCoordinates;
	private Vector zones;
	
	public XMLParser()
	{
		super();
		
		this.qNameStack = new Stack();
		
		this.translatedCoordinates = null;
		this.zones = new Vector();
	}
	
	private Zone currentZone;

	public void startDocument()
	{
		this.qNameStack.removeAllElements();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attr)
	{
		if (qName.equals("translatedcoordinates"))
		{
			int x = (int)Double.parseDouble(attr.getValue("x"));
			int y = (int)Double.parseDouble(attr.getValue("x"));
			this.translatedCoordinates = new int[] { x, y };
		}
		else if (qName.equals("zone"))
		{
			this.currentZone = new Zone();
			this.currentZone.setId(Integer.parseInt(attr.getValue("id")));
			this.currentZone.setType(Integer.parseInt(attr.getValue("type")));
			this.currentZone.setDeleted(attr.getValue("deleted").equals("true"));
		}
		else if (qName.equals("waypoint"))
		{
			double latitude = Double.parseDouble(attr.getValue("latitude"));
			double longitude = Double.parseDouble(attr.getValue("longitude"));
			QualifiedCoordinates waypoint = new QualifiedCoordinates(latitude, longitude, 0f, 0f, 0f);
			this.currentZone.addWaypoint(waypoint);
		}

		this.qNameStack.push(qName);
	}
	
	public void endElement(String uri, String localName, String qName)
	{
		this.qNameStack.pop();

		if (qName.equals("zone"))
		{
			this.zones.addElement(this.currentZone);
			this.currentZone = null;
		}
	}

	public int[] getTranslatedLocation()
	{
		if (this.translatedCoordinates == null)
		{
			return new int[] { 0, 0 };
		}
		else
		{
			return this.translatedCoordinates;
		}
	}

	public Vector getZones()
	{
		return this.zones;
	}
}
