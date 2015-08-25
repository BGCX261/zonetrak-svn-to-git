package zonetrak.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import zonetrak.log.LogEntry;
import zonetrak.log.Logger;

public class LogToolBar extends JToolBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static LogToolBar instance;

	public static LogToolBar getInstance()
	{
		if (instance == null)
		{
			instance = new LogToolBar();
		}

		return instance;
	}

	private JList list;

	private DefaultListModel listModel;

	private LogToolBar()
	{
		this.initializeUI();

		Logger.getInstance().setActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.listModel.addElement(Logger.getInstance().getEntries().get(e.getID()));
		this.list.ensureIndexIsVisible(this.listModel.getSize() - 1);
	}

	private void initializeUI()
	{
		this.setVisible(false);

		this.listModel = new DefaultListModel();

		this.list = new JList(this.listModel);
		this.list.setCellRenderer(new LogCellRenderer());
		final JScrollPane listScroller = new JScrollPane(this.list);

		for (final LogEntry entry : Logger.getInstance().getEntries())
		{
			this.listModel.addElement(entry);
		}

		this.add(listScroller);
	}
}
