package zonetrak.com;
import javax.microedition.location.Criteria;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.ProximityListener;

public class BluetoothConnector
{
	private LocationProvider provider;

	public BluetoothConnector(LocationListener locationListener, ProximityListener proxymityListener)
	{
		Criteria criteria = new Criteria();
		this.provider = null;

		try
		{
			this.provider = LocationProvider.getInstance(criteria);
			this.provider.setLocationListener(locationListener, -1, -1, -1);
		}
		catch (LocationException e)
		{
		}
	}
}
