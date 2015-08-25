package zonetrak.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;

public class Logger
{
	public enum Level
	{
		ERROR, WARNING, NOTICE, INFO
	}

	private static Logger instance = null;

	public static Logger getInstance()
	{
		if (instance == null)
		{
			instance = new Logger();
		}

		return instance;
	}

	private ActionListener actionListener;

	private final LinkedList<LogEntry> entries;

	private Logger()
	{
		this.entries = new LinkedList<LogEntry>();
	}

	public LinkedList<LogEntry> getEntries()
	{
		return this.entries;
	}

	public void log(String text, Level level)
	{
		this.entries.add(new LogEntry(new Date(), text, level));

		if (this.actionListener != null)
		{
			this.actionListener.actionPerformed(new ActionEvent(this, this.entries.size() - 1, ""));
		}
	}

	public void setActionListener(ActionListener actionListener)
	{
		this.actionListener = actionListener;
	}
}
