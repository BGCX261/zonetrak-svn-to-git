package zonetrak.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import zonetrak.common.AbstractPlugin;
import zonetrak.events.EventManager;
import zonetrak.ifaces.IPlugin;

public class PluginLoader
{
	public PluginLoader()
	{
	}

	public void execute()
	{
		final File pluginDir = new File("plugins/");

		final File[] pluginJars = pluginDir.listFiles(new PluginFileFilter());

		for (final File pluginJar : pluginJars)
		{
			this.loadPlugin(pluginJar);
		}
	}

	public void loadPlugin(File jarFile)
	{
		try
		{
			final JarFile jar = new JarFile(jarFile);
			final URLClassLoader loader = URLClassLoader.newInstance(new URL[] { jarFile.toURI().toURL() });

			Class<?> startClass = null;

			for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();)
			{
				JarEntry entry = entries.nextElement();
				String name = entry.getName();

				if (!name.endsWith(".class"))
				{
					continue;
				}

				String className = name.replaceAll("/", ".");

				className = className.substring(0, className.length() - ".class".length());

				Class<?> possibleStartClass = loader.loadClass(className);
				Class<?>[] interfaces = possibleStartClass.getInterfaces();

				if (possibleStartClass.getSuperclass().equals(AbstractPlugin.class))
				{
					startClass = possibleStartClass;
				}
				else
				{
					for (Class<?> iface : interfaces)
					{
						if (iface.equals(IPlugin.class))
						{
							startClass = possibleStartClass;
							break;
						}
					}
				}
			}

			if (startClass == null)
			{
				System.err.println("Could not find plugin start class for " + jarFile.getAbsolutePath() + ".");
			}
			else
			{
				final IPlugin plugin = (IPlugin) startClass.newInstance();
				plugin.setEventManager(EventManager.getInstance());
				plugin.setDataManager(DataManager.getInstance());
				plugin.setURLClassLoader(loader);
				plugin.initPlugin();
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
