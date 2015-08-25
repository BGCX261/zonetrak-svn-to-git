package zonetrak.plugin;

import zonetrak.common.AbstractPlugin;

public class THWEntitiesPlugin extends AbstractPlugin
{
	@Override
	public void initPlugin()
	{	
		System.out.println("ZoneTrak THW Entity Plugin started.");
		
		THWEntityPainter painter = new THWEntityPainter(this.getDataManager(), this.getURLClassLoader());
		
		this.getDataManager().setData("entityPainter", painter);
	}
}
