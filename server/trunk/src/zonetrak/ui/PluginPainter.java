package zonetrak.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.Painter;

import zonetrak.plugins.DataManager;

public class PluginPainter implements Painter<JXMapViewer>
{
	@SuppressWarnings("unchecked")
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		ArrayList<Painter<JXMapViewer>> painters = (ArrayList<Painter<JXMapViewer>>)DataManager.getInstance().getData("painters");
		
		if (painters != null && painters.size() > 0)
		{
			for (Painter<JXMapViewer> painter : painters)
			{
				painter.paint(g, map, w, h);
			}
		}
	}
}
