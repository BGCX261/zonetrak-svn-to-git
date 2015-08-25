package zonetrak.ifaces;

public interface IDataManager
{
	/**
	 * Gets data set by setData.
	 * 
	 * @see zonetrak.ifaces.IDataManager.setData()
	 * @param name - Object identifier
	 * @return Either an object identified by name or (if not available) null.
	 */
	public abstract Object getData(String name);

	/**
	 * Sets data.
	 * 
	 * @param name - Object identifier
	 * @param data - An object identified by name.
	 */
	public abstract void setData(String name, Object data);
	
	/**
	 * Lists names for data which has already been set.
	 * 
	 * @return A String array containing names.
	 */
	public abstract String[] getAvailableData();
}