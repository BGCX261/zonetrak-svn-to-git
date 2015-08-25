package zonetrak.ui;

import java.awt.Component;
import java.text.DateFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import zonetrak.log.LogEntry;

public class LogCellRenderer extends DefaultListCellRenderer
{
	private static final long serialVersionUID = 1L;

	private final ImageIcon logInfoIcon;
	private final ImageIcon logNoticeIcon;
	private final ImageIcon logWarningIcon;
	private final ImageIcon logErrorIcon;

	public LogCellRenderer()
	{
		this.setHorizontalTextPosition(RIGHT);
		this.setHorizontalAlignment(LEFT);

		this.logInfoIcon = new ImageIcon("res/loginfo.png");
		this.logNoticeIcon = new ImageIcon("res/lognotice.png");
		this.logWarningIcon = new ImageIcon("res/logwarning.png");
		this.logErrorIcon = new ImageIcon("res/logerror.png");
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		if (value != null)
		{
			if (value instanceof LogEntry)
			{
				final LogEntry entry = (LogEntry) value;
				ImageIcon icon = null;

				switch (entry.getLevel())
				{
					case INFO:
						icon = this.logInfoIcon;
						break;

					case NOTICE:
						icon = this.logNoticeIcon;
						break;

					case WARNING:
						icon = this.logWarningIcon;
						break;

					case ERROR:
						icon = this.logErrorIcon;
						break;
				}

				this.setIcon(icon);
				this.setText("[" + DateFormat.getInstance().format(entry.getDate()) + "] " + entry.getText());
			}
		}

		return this;
	}
}
