package zonetrak.position;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jdesktop.swingx.mapviewer.Waypoint;

public class Zone
{
	private ArrayList<WaypointEx> waypoints;
	private int id;
	private int type;
	private Date date;
	private boolean isDeleted;
	private boolean isSelected;

	public Zone()
	{
		this.waypoints = new ArrayList<WaypointEx>();
		this.date = new Date();
		this.isDeleted = false;
		this.isSelected = false;
	}

	public void addWaypoint(WaypointEx waypoint)
	{
		this.waypoints.add(waypoint);
	}

	public Date getDate()
	{
		return this.date;
	}

	public int getId()
	{
		return this.id;
	}

	public int getType()
	{
		return this.type;
	}

	public ArrayList<WaypointEx> getWaypointList()
	{
		return this.waypoints;
	}

	public Set<Waypoint> getWaypoints()
	{
		final Set<Waypoint> waypoints = new HashSet<Waypoint>();

		for (final Waypoint waypoint : this.waypoints)
		{
			waypoints.add(waypoint);
		}

		return waypoints;
	}

	public boolean isDeleted()
	{
		return this.isDeleted;
	}

	public boolean isSelected()
	{
		return this.isSelected;
	}

	public void removeWaypoint(WaypointEx waypoint)
	{
		this.waypoints.remove(waypoint);
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public void setWaypoints(ArrayList<WaypointEx> waypoints)
	{
		this.waypoints = waypoints;
	}

	@Override
	public String toString()
	{
		return "Zone [Type: " + this.type + "]";
	}
}
