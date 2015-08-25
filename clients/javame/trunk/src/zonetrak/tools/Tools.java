package zonetrak.tools;

import java.util.Vector;

public class Tools
{
	public static String[] split(String original, String separator) {
		Vector nodes = new Vector();
		int index = original.indexOf(separator);

		while (index >= 0)
		{
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}

		nodes.addElement(original);

		String[] result = new String[nodes.size()];
		nodes.copyInto(result);

		return result;
	}

	public static boolean pointInPolygon(Vector xy, double[] point)
	{
		int polySides = xy.size();
		int j = polySides - 1;
		boolean oddNodes = false;
		
		double[] polyX = new double[xy.size()];
		double[] polyY = new double[xy.size()];
		double x = point[0];
		double y = point[1];
		
		for (int i = 0; i < xy.size(); i++)
		{
			polyX[i] = ((double[])xy.elementAt(i))[0];
			polyY[i] = ((double[])xy.elementAt(i))[0];
		}

		for (int i = 0; i < polySides; i++)
		{
			if (polyY[i] < y && polyY[j] >= y || polyY[j] < y && polyY[i] >= y)
			{
				if (polyX[i] + (y - polyY[i]) / (polyY[j] - polyY[i]) * (polyX[j] - polyX[i]) < x)
				{
					oddNodes = !oddNodes;
				}
			}

			j = i;
		}

		return oddNodes;
	}

}
