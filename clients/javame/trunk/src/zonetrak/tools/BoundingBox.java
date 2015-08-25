package zonetrak.tools;

public class BoundingBox
{
    public double north;
    public double south;
    public double east;
    public double west;

	public static BoundingBox tile2boundingBox(final int x, final int y, final int zoom)
	{
		BoundingBox bb = new BoundingBox();
		bb.north = tile2lat(y, zoom);
		bb.south = tile2lat(y + 1, zoom);
		bb.west = tile2lon(x, zoom);
		bb.east = tile2lon(x + 1, zoom);
	
		return bb;
	}
	
	public static double[] getTileCoordinates(final int xTile, final int yTile, final int zoom, final double lat, final double lon)
	{
		BoundingBox bb = BoundingBox.tile2boundingBox(xTile, yTile, zoom);
		double minlat = bb.south;
		double minlon = bb.west;
		double maxlat = bb.north;
		double maxlon = bb.east;
		double deltalat = maxlat - minlat;
		double deltalon = maxlon - minlon;
		
		double x = ((lat - minlat) / deltalat) * 256;
		double y = 256 - ((lon - minlon) / deltalon) * 256;

		return new double[] { x, y };
	}

	private static double tile2lon(int x, int z)
	{
		return (x / MathME.pow(2.0, z) * 360.0) - 180;
	}

	private static double tile2lat(int y, int z)
	{
		double n = Math.PI - ((2.0 * Math.PI * y) / MathME.pow(2.0, z));
		return 180.0 / Math.PI * MathME.atan(0.5 * (MathME.exp(n) - MathME.exp(-n)));
	}
}
