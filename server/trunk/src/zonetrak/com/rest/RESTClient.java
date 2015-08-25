package zonetrak.com.rest;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RESTClient
{
	private final HttpHost target;
	private final DefaultHttpClient httpClient;

	public RESTClient(String hostName)
	{
		this.target = new HttpHost(hostName, 80, "http");

		this.httpClient = new DefaultHttpClient();
	}

	public RESTResponse delete(String resource, RESTParameters restParameters) throws Exception
	{
		final RESTResponse restResponse = new RESTResponse();

		final URI uri = URIUtils.createURI(this.target.getSchemeName(), this.target.getHostName(), this.target.getPort(), resource, restParameters.toString(), null);
		final HttpDelete request = new HttpDelete(uri);

		final HttpResponse httpResponse = this.httpClient.execute(this.target, request);
		final HttpEntity entity = httpResponse.getEntity();
		final Header[] contentTypeHeaders = httpResponse.getHeaders("Content-Type");

		restResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		restResponse.setBody(EntityUtils.toString(entity));
		restResponse.setContentType(contentTypeHeaders[0].getValue());

		return restResponse;
	}

	public RESTResponse get(String resource, RESTParameters restParameters) throws Exception
	{
		final RESTResponse restResponse = new RESTResponse();

		final URI uri = URIUtils.createURI(this.target.getSchemeName(), this.target.getHostName(), this.target.getPort(), resource, restParameters.toString(), null);
		final HttpGet request = new HttpGet(uri);

		final HttpResponse httpResponse = this.httpClient.execute(request);
		final HttpEntity entity = httpResponse.getEntity();
		final Header[] contentTypeHeaders = httpResponse.getHeaders("Content-Type");

		restResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		restResponse.setBody(EntityUtils.toString(entity));
		restResponse.setContentType(contentTypeHeaders[0].getValue());

		return restResponse;
	}

	public RESTResponse put(String resource, RESTParameters restParameters) throws Exception
	{
		final RESTResponse restResponse = new RESTResponse();

		final URI uri = URIUtils.createURI(this.target.getSchemeName(), this.target.getHostName(), this.target.getPort(), resource, restParameters.toString(), null);
		final HttpPut request = new HttpPut(uri);

		final HttpResponse httpResponse = this.httpClient.execute(this.target, request);
		final HttpEntity entity = httpResponse.getEntity();
		final Header[] contentTypeHeaders = httpResponse.getHeaders("Content-Type");

		restResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		restResponse.setBody(EntityUtils.toString(entity));
		restResponse.setContentType(contentTypeHeaders[0].getValue());

		return restResponse;
	}

	public void shutdown()
	{
		this.httpClient.getConnectionManager().shutdown();
	}
}
