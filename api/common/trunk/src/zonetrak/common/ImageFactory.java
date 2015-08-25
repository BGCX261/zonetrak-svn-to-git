package zonetrak.common;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.ImageIcon;

public class ImageFactory
{
	public static ImageIcon createImageIcon(URLClassLoader urlClassLoader, String path) throws FileNotFoundException
	{
		URL resourceURL = urlClassLoader.getResource(path);
		
		if (resourceURL == null)
		{
			throw new FileNotFoundException(path + " could not be found.");
		}
		
		return new ImageIcon(resourceURL);
	}
	
	public static Image createImage(URLClassLoader urlClassLoader, String path) throws FileNotFoundException
	{
		return createImageIcon(urlClassLoader, path).getImage();
	}
}
