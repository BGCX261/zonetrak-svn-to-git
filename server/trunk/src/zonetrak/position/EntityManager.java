package zonetrak.position;

import java.util.ArrayList;

import zonetrak.plugins.DataManager;

public class EntityManager
{
	private static EntityManager instance = null;
	
	private ArrayList<Entity> entities;

	public static EntityManager getInstance()
	{
		if (instance == null)
		{
			instance = new EntityManager();
		}
		
		return instance;
	}
	
	private EntityManager()
	{
		this.entities = new ArrayList<Entity>();
	}

	public void add(Entity entity)
	{
		if (entity == null)
		{
			throw new NullPointerException("The entity can't be null.");
		}
		
		this.entities.add(entity);
	}

	public ArrayList<Entity> getEntities()
	{
		return this.entities;
	}

	public void setEntities(ArrayList<Entity> entities)
	{
		if (entities == null)
		{
			throw new NullPointerException("The entity list can't be null.");
		}
		
		this.entities = entities;

		/**
		 * Update entity list reference in data manager.
		 */
		DataManager.getInstance().setData("recentEntities", this.entities);
	}
}
