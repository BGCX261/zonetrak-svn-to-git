package zonetrak.com.rest;

public class RESTResponse
{
	private int statusCode;
	private String body;
	private String contentType;

	protected RESTResponse()
	{
	}

	protected RESTResponse(int statusCode, String body, String contentType)
	{
		this.statusCode = statusCode;
		this.body = body;
		this.contentType = contentType;
	}

	public String getBody()
	{
		return this.body;
	}

	public String getContentType()
	{
		return this.contentType;
	}

	public int getStatusCode()
	{
		return this.statusCode;
	}

	protected void setBody(String body)
	{
		this.body = body;
	}

	protected void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	protected void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}
}
