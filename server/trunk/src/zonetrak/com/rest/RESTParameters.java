package zonetrak.com.rest;

import java.util.Enumeration;
import java.util.Hashtable;

public class RESTParameters
{
	private final Hashtable<String, String> params;

	public RESTParameters()
	{
		this.params = new Hashtable<String, String>();
	}

	public void addParameter(String key, String value)
	{
		this.params.put(key, value);
	}

	@Override
	public String toString()
	{
		String query = "";
		final Enumeration<String> e = this.params.keys();

		boolean first = true;
		while (e.hasMoreElements())
		{
			final String alias = e.nextElement();

			if (first)
			{
				first = false;
			}
			else
			{
				query += "&";
			}

			query += alias + "=" + this.params.get(alias);
		}

		return query;
	}
}
