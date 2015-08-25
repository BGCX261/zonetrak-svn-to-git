package zonetrak.ifaces;

import zonetrak.ifaces.ISubscriber;

public interface IEventManager
{
	/**
	 * Subscribes a subscriber to a specific event.
	 * 
	 * @param type Identifies the event.
	 * @param subscriber The subscriber which will be called on any occurance of an event.
	 */
	public abstract void subscribe(String type, ISubscriber subscriber);

	/**
	 * Unsubscribes an subscriber from a specific event.
	 * 
	 * @param type Identifies the event.
	 * @param subscriber The subscriber which will be called on any occurance of an event.
	 */
	public abstract void unsubscribe(String type, ISubscriber subscriber);

	/**
	 * Called whenever an event occurs.
	 * 
	 * @param type Identifies the event.
	 * @param args Event data of any kind.
	 */
	public abstract void fireEvent(String type, Object... args);
}