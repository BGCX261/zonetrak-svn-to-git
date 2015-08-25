package zonetrak.events;

import zonetrak.ifaces.ISubscriber;

public class Subscription
{
	private final String type;
	private final ISubscriber subscriber;

	public Subscription(String type, ISubscriber subscriber)
	{
		this.type = type;
		this.subscriber = subscriber;
	}

	protected ISubscriber getSubscriber()
	{
		return this.subscriber;
	}

	protected String getType()
	{
		return this.type;
	}
}
