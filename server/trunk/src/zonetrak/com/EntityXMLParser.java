package zonetrak.com;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import zonetrak.position.Entity;

public class EntityXMLParser
{
	private final ArrayList<Entity> entities;
	private Document doc;

	public EntityXMLParser()
	{
		this.entities = new ArrayList<Entity>();
	}

	public ArrayList<Entity> getEntities()
	{
		return this.entities;
	}

	protected void loadFile(String fileName) throws JDOMException, IOException
	{
		final SAXBuilder builder = new SAXBuilder();
		this.doc = builder.build(fileName);

		this.parseXML();
	}

	protected void loadXml(String xmlDoc) throws JDOMException, IOException
	{
		final SAXBuilder builder = new SAXBuilder();
		final InputStream input = new ByteArrayInputStream(xmlDoc.getBytes());
		this.doc = builder.build(input);

		this.parseXML();
	}

	private Entity parseEntity(Element e) throws DataConversionException
	{
		final Entity entity = new Entity();

		entity.setDate(new Date(e.getAttribute("date").getLongValue() * 1000L));
		entity.setFriendlyName(e.getAttribute("friendlyname").getValue());
		entity.setIdentifier(e.getAttribute("identifier").getValue());

		// TODO: Automate this
		entity.setType("trupp");

		final double latitude = e.getAttribute("lat").getDoubleValue();
		final double longitude = e.getAttribute("lon").getDoubleValue();
		entity.setPosition(new GeoPosition(latitude, longitude));

		return entity;
	}

	private void parseXML() throws DataConversionException
	{
		final Element root = this.doc.getRootElement();

		for (final Object entity : root.getChildren("entity"))
		{
			this.entities.add(this.parseEntity((Element) entity));
		}
	}
}
