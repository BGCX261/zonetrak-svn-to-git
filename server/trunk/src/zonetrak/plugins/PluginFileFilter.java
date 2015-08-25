package zonetrak.plugins;

import java.io.File;
import java.io.FileFilter;

public class PluginFileFilter implements FileFilter
{
	@Override
	public boolean accept(File file)
	{
		if (file.getName().endsWith(".jar"))
		{
			return true;
		}

		return false;
	}
}
