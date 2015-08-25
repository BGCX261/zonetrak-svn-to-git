package zonetrak.ifaces;

public interface ISubscriber
{
	void eventFired(String type, Object... args);
}
