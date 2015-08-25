package zonetrak.position;

import org.jdesktop.swingx.mapviewer.Waypoint;

public class WaypointEx extends Waypoint
{
	public WaypointEx(double latitude, double longitude)
	{
		super(latitude, longitude);
	}

	@Override
	public String toString()
	{
		return "Waypoint";
	}
}
