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

import zonetrak.position.Entity;

public class EntityXMLWriter
{
	private final ArrayList<Entity> entities;

	public EntityXMLWriter(ArrayList<Entity> entities)
	{
		this.entities = entities;
	}

	public void writeEntities(String fileName) throws FileNotFoundException, IOException
	{
		final Document doc = new Document();

		final Element entitiesElement = new Element("entities");

		for (final Entity entity : this.entities)
		{
			final Element entityElement = new Element("entity");

			entityElement.setAttribute("date", String.valueOf(entity.getDate().getTime() / 1000L));
			entityElement.setAttribute("friendlyname", String.valueOf(entity.getFriendlyName()));
			entityElement.setAttribute("identifier", String.valueOf(entity.getIdentifier()));
			entityElement.setAttribute("latitude", String.valueOf(entity.getPosition().getLatitude()));
			entityElement.setAttribute("longitude", String.valueOf(entity.getPosition().getLongitude()));

			entitiesElement.addContent(entityElement);
		}

		doc.addContent(entitiesElement);

		final XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		outp.output(doc, new FileOutputStream(new File(fileName)));
	}
}
