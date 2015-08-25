package zonetrak.com;

import zonetrak.position.Zone;

public class EnqueuedZone
{
	public enum ProcessMethod
	{
		DELETE, CREATE
	}

	private final Zone zone;
	private final ProcessMethod processMethod;

	protected EnqueuedZone(Zone zone, ProcessMethod processMethod)
	{
		this.zone = zone;
		this.processMethod = processMethod;
	}

	protected ProcessMethod getProcessMethod()
	{
		return this.processMethod;
	}

	protected Zone getZone()
	{
		return this.zone;
	}
}
