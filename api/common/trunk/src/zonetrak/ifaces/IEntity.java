package zonetrak.ifaces;

import java.util.Date;

import org.jdesktop.swingx.mapviewer.GeoPosition;

public interface IEntity
{
	public abstract String getFriendlyName();
	public abstract void setFriendlyName(String friendlyName);
	public abstract String getIdentifier();
	public abstract void setIdentifier(String identifier);
	public abstract GeoPosition getPosition();
	public abstract void setPosition(GeoPosition position);
	public abstract Date getDate();
	public abstract void setDate(Date date);
	public abstract String getType();
	public abstract void setType(String type);
}