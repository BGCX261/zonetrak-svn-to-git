package zonetrak.events;

import java.util.LinkedList;

import zonetrak.ifaces.IEventManager;
import zonetrak.ifaces.ISubscriber;

public class EventManager implements IEventManager
{
	private static IEventManager instance = null;

	public static IEventManager getInstance()
	{
		if (instance == null)
		{
			instance = new EventManager();
		}

		return instance;
	}

	private final LinkedList<Subscription> subscribers;

	private EventManager()
	{
		this.subscribers = new LinkedList<Subscription>();
	}

	/*
	 * (non-Javadoc)
	 * @see zonetrak.events.IEventManager#fireEvent(java.lang.String,
	 * java.lang.Object)
	 */
	public void fireEvent(String type, Object... args)
	{
		for (final Subscription subscription : this.subscribers)
		{
			if (subscription.getType().equals(type))
			{
				subscription.getSubscriber().eventFired(type, args);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see zonetrak.events.IEventManager#subscribe(java.lang.String,
	 * zonetrak.events.ISubscriber)
	 */
	public void subscribe(String type, ISubscriber subscriber)
	{
		this.subscribers.add(new Subscription(type, subscriber));
	}

	/*
	 * (non-Javadoc)
	 * @see zonetrak.events.IEventManager#unsubscribe(java.lang.String,
	 * zonetrak.events.ISubscriber)
	 */
	public void unsubscribe(String type, ISubscriber subscriber)
	{
		final LinkedList<Subscription> deprecatedSubscriptions = new LinkedList<Subscription>();

		for (final Subscription subscription : this.subscribers)
		{
			if (subscription.getType().equals(type) && subscription.getSubscriber().equals(subscriber))
			{
				deprecatedSubscriptions.add(subscription);
			}
		}

		if (deprecatedSubscriptions.size() > 0)
		{
			this.subscribers.removeAll(deprecatedSubscriptions);
		}
	}
}
