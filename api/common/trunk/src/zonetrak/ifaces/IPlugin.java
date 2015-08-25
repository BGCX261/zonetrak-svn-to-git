package zonetrak.ifaces;

import java.net.URLClassLoader;

public interface IPlugin
{
	/**
	 * Sets the event manager which allows to (un-)subscribe
	 * to events raised by the main application.
	 * 
	 * @param eventManager
	 */
	public void setEventManager(IEventManager eventManager);
	
	/**
	 * Sets the data manager which allows to access
	 * data managed by the main application.
	 * 
	 * @param dataManager
	 */
	public void setDataManager(IDataManager dataManager);

	/**
	 * Sets the URL class loader which can be used
	 * to load plugin resources.
	 * 
	 * @param urlClassLoader
	 */
	public void setURLClassLoader(URLClassLoader urlClassLoader);
	
	/**
	 * Is called after the event and data managers were
	 * set. The plugin logic should start here.
	 */
	public void initPlugin();
}
