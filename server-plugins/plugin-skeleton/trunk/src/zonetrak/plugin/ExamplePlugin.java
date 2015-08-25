package zonetrak.plugin;

import zonetrak.common.AbstractPlugin;
import zonetrak.ifaces.IDataManager;
import zonetrak.ifaces.IEventManager;
import zonetrak.ifaces.ISubscriber;

public class ExamplePlugin extends AbstractPlugin implements ISubscriber
{
	@Override
	public void eventFired(String type, Object... args)
	{
		System.out.println("Event \"" + type + "\" catched.");

		for (Object arg : args)
		{
			System.out.println("Type: " + arg.getClass().getName());
		}
	}

	@Override
	public void initPlugin()
	{
		System.out.println("Plugin started.");

		this.getEventManager().subscribe("example", this);
		this.getDataManager().getData("exampleData");
	}
}
