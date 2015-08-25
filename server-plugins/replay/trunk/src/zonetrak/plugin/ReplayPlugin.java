package zonetrak.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zonetrak.common.AbstractPlugin;
import zonetrak.common.ImageFactory;
import zonetrak.ifaces.IEntity;
import zonetrak.ifaces.ISimulator;
import zonetrak.ifaces.ISubscriber;

public class ReplayPlugin extends AbstractPlugin implements ISubscriber, ActionListener, ChangeListener
{
	private ISimulator simulator;

	private ReplayTask replayTask;
	private Timer timer;

	private JToolBar toolBar;
	private JSlider slider;
	private JSlider speedSlider;
	private JLabel dateTimeBox;

	private Date[] entityDateBounds;

	@SuppressWarnings("unchecked")
	@Override
	public void eventFired(String type, Object... args)
	{
		if (type.equals("entities") && this.slider == null)
		{
			ArrayList<IEntity> entities = (ArrayList<IEntity>) this.getDataManager().getData("entities");

			if (entities == null)
			{
				return;
			}

			this.toolBar = (JToolBar) this.getDataManager().getData("toolBar");

			this.toolBar.addSeparator();

			ImageIcon playIcon = null;
			ImageIcon stopIcon = null;

			try
			{
				playIcon = ImageFactory.createImageIcon(this.getURLClassLoader(), "play.png");
				stopIcon = ImageFactory.createImageIcon(this.getURLClassLoader(), "stop.png");
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			final JButton playButton = new JButton(playIcon);
			playButton.addActionListener(this);
			playButton.setActionCommand("Play");

			final JButton stopButton = new JButton(stopIcon);
			stopButton.addActionListener(this);
			stopButton.setActionCommand("Stop");

			this.toolBar.add(playButton);
			this.toolBar.add(stopButton);

			this.entityDateBounds = this.getEntityDateBounds(entities);

			this.slider = new JSlider(0, 1000, 0);
			this.slider.setName("TimeSlider");
			this.slider.addChangeListener(this);

			this.toolBar.add(new JLabel("Time:"));
			this.toolBar.add(this.slider);

			this.toolBar.add(new JLabel("Speed:"));
			this.speedSlider = new JSlider(1, 100, 10);
			this.toolBar.add(this.speedSlider);

			this.dateTimeBox = new JLabel();
			this.toolBar.add(this.dateTimeBox);
		}
	}

	@Override
	public void initPlugin()
	{
		System.out.println("Replay Plugin started.");

		this.getEventManager().subscribe("entities", this);

		this.simulator = (ISimulator) this.getDataManager().getData("simulator");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Play"))
		{
			this.simulator.setEnabled(true);

			if (this.replayTask == null)
			{
				this.replayTask = new ReplayTask(this);
			}

			if (this.timer == null || this.replayTask.isCanceled())
			{

				this.timer = new Timer();
				this.timer.schedule(this.replayTask, 0, 1);
			}
		}
		else if (e.getActionCommand().equals("Stop"))
		{
			if (this.timer != null)
			{
				this.timer.cancel();
				this.timer = null;
				this.replayTask = null;
				this.slider.setValue(0);
			}

			this.simulator.setEnabled(false);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (this.replayTask == null)
		{
			this.replayTask = new ReplayTask(this);
		}

		int diff = (int) (this.entityDateBounds[1].getTime() - this.entityDateBounds[0].getTime());
		int ticks = diff / 1000 * this.slider.getValue();

		this.replayTask.setTicks(ticks);

		if (this.timer == null || this.replayTask.isCanceled())
		{
			this.replayTask.run();
			this.replayTask = null;
		}

		JFrame mainWindow = (JFrame) this.getDataManager().getData("mainWindow");
		mainWindow.invalidate();
	}

	private Date[] getEntityDateBounds(ArrayList<IEntity> entities)
	{
		Date firstDate = null;
		Date lastDate = null;

		for (IEntity entity : entities)
		{
			if (firstDate == null)
			{
				firstDate = entity.getDate();
			}

			if (lastDate == null)
			{
				lastDate = entity.getDate();
			}

			if (firstDate.compareTo(entity.getDate()) > 0)
			{
				firstDate = entity.getDate();
			}

			if (lastDate.compareTo(entity.getDate()) < 0)
			{
				lastDate = entity.getDate();
			}
		}

		return new Date[] { firstDate, lastDate };
	}

	public Date[] getEntityDateBounds()
	{
		return this.entityDateBounds;
	}

	public JLabel getDateTimeBox()
	{
		return this.dateTimeBox;
	}

	public int getSpeed()
	{
		return this.speedSlider.getValue() * 100;
	}

	public JSlider getSlider()
	{
		return this.slider;
	}
}
