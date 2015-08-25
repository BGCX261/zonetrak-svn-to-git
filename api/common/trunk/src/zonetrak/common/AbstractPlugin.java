package zonetrak.common;

import java.net.URLClassLoader;

import zonetrak.ifaces.IDataManager;
import zonetrak.ifaces.IEventManager;
import zonetrak.ifaces.IPlugin;

public abstract class AbstractPlugin implements IPlugin
{
	private IEventManager eventManager;
	private IDataManager dataManager;
	private URLClassLoader urlClassLoader;

	@Override
	public void setEventManager(IEventManager eventManager)
	{
		this.eventManager = eventManager;
	}
	
	public IEventManager getEventManager()
	{
		return this.eventManager;
	}

	@Override
	public void setDataManager(IDataManager dataManager)
	{
		this.dataManager = dataManager;
	}
	
	public IDataManager getDataManager()
	{
		return this.dataManager;
	}
	
	@Override
	public void setURLClassLoader(URLClassLoader urlClassLoader)
	{
		this.urlClassLoader = urlClassLoader;
	}
	
	public URLClassLoader getURLClassLoader()
	{
		return this.urlClassLoader;
	}
}
