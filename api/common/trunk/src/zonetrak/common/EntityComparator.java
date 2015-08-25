package zonetrak.common;

import java.util.Comparator;

import zonetrak.ifaces.IEntity;

public class EntityComparator implements Comparator<IEntity>
{
	@Override
	public int compare(IEntity entity1, IEntity entity2)
	{
		return entity1.getDate().compareTo(entity2.getDate());
	}
}