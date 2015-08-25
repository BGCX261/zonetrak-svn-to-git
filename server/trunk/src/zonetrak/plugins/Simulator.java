package zonetrak.plugins;

import zonetrak.ifaces.ISimulator;
import zonetrak.log.Logger;

public class Simulator implements ISimulator
{
	public static ISimulator instance = null;
	
	private boolean isEnabled;
	
	public static ISimulator getInstance()
	{
		if (instance == null)
		{
			instance = new Simulator();
		}
		
		return instance;
	}
	
	private Simulator()
	{
		this.isEnabled = false;
		
		DataManager.getInstance().setData("simulator", this);
	}
	
	public void setEnabled(boolean isEnabled)
	{
		if (this.isEnabled != isEnabled)
		{
			Logger.getInstance().log("Switched to " + (isEnabled ? "simulation" : "normal") + " mode.", Logger.Level.INFO);
		}
		
		this.isEnabled = isEnabled;
	}

	public boolean isEnabled()
	{
		return this.isEnabled;
	}
}
