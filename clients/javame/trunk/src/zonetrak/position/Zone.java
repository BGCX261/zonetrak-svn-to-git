package zonetrak.position;

import java.util.Vector;

import javax.microedition.location.QualifiedCoordinates;

public class Zone
{
	public final static int TYPE_1 = 1;
	public final static int TYPE_2 = 2;
	public final static int TYPE_3 = 3;
	public final static int TYPE_4 = 4;
	public final static int TYPE_5 = 5;
	
	private int id;
	private int type;
	private boolean isDeleted;
	private Vector waypoints;

	public Zone()
	{
		this.waypoints = new Vector();
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public Vector getWaypoints()
	{
		return this.waypoints;
	}
	
	public void setWaypoints(Vector waypoints)
	{
		this.waypoints = waypoints;
	}
	
	public void addWaypoint(QualifiedCoordinates waypoint)
	{
		this.waypoints.addElement(waypoint);
	}
	
	public void removeWaypoint(QualifiedCoordinates waypoint)
	{
		this.waypoints.removeElement(waypoint);
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}
	
	public boolean isDeleted()
	{
		return this.isDeleted;
	}
}
