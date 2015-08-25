package zonetrak.mapserver;

public class BoundingBox
{
	@Deprecated
	private static double[] getTileCoordinates(final int xTile, final int yTile, final int zoom, final double lat, final double lon)
	{
		final BoundingBox bb = BoundingBox.tile2boundingBox(xTile, yTile, zoom);
		final double minlat = bb.south;
		final double minlon = bb.west;
		final double maxlat = bb.north;
		final double maxlon = bb.east;
		final double deltalat = maxlat - minlat;
		final double deltalon = maxlon - minlon;

		final double x = (lat - minlat) / deltalat * 256;
		final double y = 256 - (lon - minlon) / deltalon * 256;

		return new double[] { x, y };
	}

	protected static BoundingBox tile2boundingBox(final int x, final int y, final int zoom)
	{
		final BoundingBox bb = new BoundingBox();
		bb.north = tile2lat(y, zoom);
		bb.south = tile2lat(y + 1, zoom);
		bb.west = tile2lon(x, zoom);
		bb.east = tile2lon(x + 1, zoom);

		return bb;
	}

	private static double tile2lat(int y, int z)
	{
		final double n = Math.PI - 2.0 * Math.PI * y / Math.pow(2.0, z);
		return 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
	}

	private static double tile2lon(int x, int z)
	{
		return x / Math.pow(2.0, z) * 360.0 - 180;
	}

	protected double north;

	protected double south;

	protected double east;

	protected double west;
}
