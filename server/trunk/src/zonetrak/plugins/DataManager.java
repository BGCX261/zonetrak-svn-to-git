package zonetrak.plugins;

import java.util.Enumeration;
import java.util.Hashtable;

import zonetrak.events.EventManager;
import zonetrak.ifaces.IDataManager;

public class DataManager implements IDataManager
{
	private static IDataManager instance = null;

	public static IDataManager getInstance()
	{
		if (instance == null)
		{
			instance = new DataManager();
		}

		return instance;
	}

	private final Hashtable<String, Object> data;

	private DataManager()
	{
		this.data = new Hashtable<String, Object>();
	}

	@Override
	public Object getData(String name)
	{
		return this.data.get(name);
	}

	@Override
	public void setData(String name, Object data)
	{
		this.data.put(name, data);

		EventManager.getInstance().fireEvent(name, data);
	}

	@Override
	public String[] getAvailableData()
	{
		String[] availableData = new String[this.data.size()];

		Enumeration<String> keys = this.data.keys();
		
		int index = 0;
		while (keys.hasMoreElements() && index < this.data.size())
		{
			availableData[index++] = keys.nextElement();
		}
		
		return availableData;
	}
}
