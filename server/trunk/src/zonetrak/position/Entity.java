package zonetrak.position;

import java.util.Date;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import zonetrak.ifaces.IEntity;

public class Entity implements IEntity
{
	private String friendlyName;
	private String identifier;
	private GeoPosition position;
	private Date date;
	private String type;

	public Entity()
	{
	}

	@Override
	public Date getDate()
	{
		return this.date;
	}

	@Override
	public String getFriendlyName()
	{
		return this.friendlyName;
	}

	@Override
	public String getIdentifier()
	{
		return this.identifier;
	}

	@Override
	public GeoPosition getPosition()
	{
		return this.position;
	}

	@Override
	public String getType()
	{
		return this.type;
	}

	@Override
	public void setDate(Date date)
	{
		this.date = date;
	}

	@Override
	public void setFriendlyName(String friendlyName)
	{
		this.friendlyName = friendlyName;
	}

	@Override
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	@Override
	public void setPosition(GeoPosition position)
	{
		this.position = position;
	}

	@Override
	public void setType(String type)
	{
		this.type = type;
	}
}
