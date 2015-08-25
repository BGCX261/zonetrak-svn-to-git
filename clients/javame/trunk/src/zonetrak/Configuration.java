package zonetrak;

import java.util.Date;

public class Configuration
{
	public static final String DEFAULT_MIDDLEWARE_URL = "http://localhost";
	public static final String DEFAULT_FRIENDLYNAME = "Unknown";
	public static final String DEFAULT_IDENTIFIER = "" + (new Date()).getTime();
	public static final double DEFAULT_LATITUDE = 0.0;
	public static final double DEFAULT_LONGITUDE = 0.0;

	private static Configuration instance = null;

	private String middlewareUrl;
	private boolean enabled;

	private String friendlyName;
	private String identifier;
	private double latitude;
	private double longitude;

	public static Configuration getInstance()
	{
		if (instance == null)
		{
			instance = new Configuration();
		}
		
		return instance;
	}

	private Configuration()
	{
		this.middlewareUrl = DEFAULT_MIDDLEWARE_URL;
		this.enabled = false;
		this.friendlyName = DEFAULT_FRIENDLYNAME;
		this.identifier = DEFAULT_IDENTIFIER;
		this.latitude = DEFAULT_LATITUDE;
		this.longitude = DEFAULT_LONGITUDE;
	}

	public String getMiddlewareUrl()
	{
		return this.middlewareUrl;
	}

	public void setMiddlewareUrl(String middlewareUrl)
	{
		this.middlewareUrl = middlewareUrl;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getFriendlyName()
	{
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName)
	{
		this.friendlyName = friendlyName;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
}
