package zonetrak.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SettingsFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static SettingsFrame instance;

	public static SettingsFrame getInstance()
	{
		if (instance == null)
		{
			instance = new SettingsFrame();
		}

		return instance;
	}

	private JTextField zoneFileTextField;
	private JTextField entityFileTextField;
	private JTextField hostTextField;
	private JTextField userTextField;

	private JPasswordField passTextField;

	private SettingsFrame()
	{
		this.initializeUI();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("OK"))
		{
			this.setVisible(false);
		}
	}

	public synchronized String getEntityFile()
	{
		return this.entityFileTextField.getText();
	}

	public synchronized String getHost()
	{
		return this.hostTextField.getText();
	}

	public synchronized String getPass()
	{
		return this.passTextField.getPassword().toString();
	}

	public synchronized String getUser()
	{
		return this.userTextField.getText();
	}

	public synchronized String getZoneFile()
	{
		return this.zoneFileTextField.getText();
	}

	private void initializeUI()
	{
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/settings.png"));

		this.setTitle("Settings");
		this.setSize(310, 280);
		this.setResizable(false);
		this.setLayout(new GridBagLayout());

		this.zoneFileTextField = new JTextField("zones.xml");
		this.zoneFileTextField.setPreferredSize(new Dimension(200, 22));
		this.entityFileTextField = new JTextField("entities.xml");
		this.entityFileTextField.setPreferredSize(new Dimension(200, 22));
		this.hostTextField = new JTextField("localhost");
		this.hostTextField.setPreferredSize(new Dimension(200, 22));
		this.userTextField = new JTextField("zonetrak");
		this.userTextField.setPreferredSize(new Dimension(200, 22));
		this.passTextField = new JPasswordField("zonetrak");
		this.passTextField.setPreferredSize(new Dimension(200, 22));

		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel("Zone file:"), c);

		c.gridx = 1;
		this.add(this.zoneFileTextField, c);

		c.gridx = 0;
		c.gridy = 1;
		this.add(new JLabel("Entity file:"), c);

		c.gridx = 1;
		this.add(this.entityFileTextField, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		this.add(new JLabel("FTP Settings"), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		this.add(new JLabel("Host:"), c);

		c.gridx = 1;
		this.add(this.hostTextField, c);

		c.gridx = 0;
		c.gridy = 4;
		this.add(new JLabel("User:"), c);

		c.gridx = 1;
		this.add(this.userTextField, c);

		c.gridx = 0;
		c.gridy = 5;
		this.add(new JLabel("Pass:"), c);

		c.gridx = 1;
		this.add(this.passTextField, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		final JButton okButton = new JButton("OK");
		okButton.setMnemonic('O');
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		this.add(okButton, c);
	}
}
