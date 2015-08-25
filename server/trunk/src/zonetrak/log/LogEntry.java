package zonetrak.log;

import java.util.Date;

import zonetrak.log.Logger.Level;

public class LogEntry
{
	private final Date date;
	private final String text;
	private final Level level;

	protected LogEntry(Date date, String text, Level level)
	{
		this.date = date;
		this.text = text;
		this.level = level;
	}

	public Date getDate()
	{
		return this.date;
	}

	public Level getLevel()
	{
		return this.level;
	}

	public String getText()
	{
		return this.text;
	}
}
