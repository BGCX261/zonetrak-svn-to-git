package zonetrak.plugin;

import java.util.ArrayList;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.common.AbstractPlugin;

public class RoutePlugin extends AbstractPlugin
{
	@SuppressWarnings("unchecked")
	@Override
	public void initPlugin()
	{
		System.out.println("Route Plugin started.");
		
		ArrayList<Painter<JXMapViewer>> painters = (ArrayList<Painter<JXMapViewer>>)this.getDataManager().getData("painters");
		
		if (painters == null)
		{
			painters = new ArrayList<Painter<JXMapViewer>>();
		}
		
		painters.add(new RoutePainter(this.getDataManager()));
		
		this.getDataManager().setData("painters", painters);
	}
}
