package zonetrak.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.jdesktop.swingx.JXMapViewer;

import zonetrak.common.EntityComparator;
import zonetrak.ifaces.IDataManager;
import zonetrak.ifaces.IEntity;

public class ReplayTask extends TimerTask
{
	private ReplayPlugin plugin;
	private IDataManager dataManager;

	private Date[] entityDateBounds;
	private int ticks;
	
	private boolean isCanceled;

	public ReplayTask(ReplayPlugin plugin)
	{
		this.plugin = plugin;
		this.dataManager = this.plugin.getDataManager();
		this.entityDateBounds = this.plugin.getEntityDateBounds();
		this.ticks = 0;

		this.isCanceled = false;		
	}

	public void setTicks(int ticks)
	{
		this.ticks = ticks;
	}
	
	@Override
	public boolean cancel()
	{
		this.isCanceled = true;
		
		return super.cancel();
	}
	
	public boolean isCanceled()
	{
		return this.isCanceled;
	}

	@Override
	public void run()
	{
		this.ticks += this.plugin.getSpeed() / 100;

		int diff = (int)(this.entityDateBounds[1].getTime() - this.entityDateBounds[0].getTime());
		
		if (this.ticks >= diff)
		{
			this.cancel();
		}
		
		this.dataManager.setData("recentEntities", this.getCurrentEntities());
		
		this.plugin.getDateTimeBox().setText(this.getCurrentDate().toString());

		long value = (this.ticks * 1000L) / diff;

		this.plugin.getSlider().setValue((int)value);
		
		JXMapViewer map = (JXMapViewer)this.dataManager.getData("map");
		map.updateUI();
	}
	
	public Date getCurrentDate()
	{
		return new Date(this.entityDateBounds[0].getTime() + this.ticks);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<IEntity> getCurrentEntities()
	{
		ArrayList<IEntity> entities = (ArrayList<IEntity>) this.dataManager.getData("entities");
		ArrayList<IEntity> currentEntities = new ArrayList<IEntity>();
		Hashtable<String, IEntity> currentSortedEntities = new Hashtable<String, IEntity>();

		Collections.sort(entities, new EntityComparator());
		
		Date currentDate = this.getCurrentDate();
		
		for (IEntity entity : entities)
		{
			if (!currentSortedEntities.containsKey(entity.getIdentifier()))
			{
				if (currentDate.compareTo(entity.getDate()) < 0)
				{
					currentSortedEntities.put(entity.getIdentifier(), entity);
				}
			}
		}
		
		Set<Entry<String, IEntity>> entries = currentSortedEntities.entrySet();
		Iterator<Entry<String,IEntity>> it = entries.iterator();

		while (it.hasNext())
		{
			Entry<String, IEntity> entry = it.next();
			
			currentEntities.add(entry.getValue());
		}
		
		return currentEntities;
	}
}
