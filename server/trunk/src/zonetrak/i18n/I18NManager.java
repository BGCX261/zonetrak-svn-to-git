package zonetrak.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NManager
{
	private static I18NManager instance = null;

	public static I18NManager getInstance()
	{
		if (instance == null)
		{
			instance = new I18NManager();
		}

		return instance;
	}

	private final Locale currentLocale;

	private final ResourceBundle messages;

	private I18NManager()
	{
		final String language = System.getProperty("user.language");
		final String country = System.getProperty("user.country");

		this.currentLocale = new Locale(language, country);
		this.messages = ResourceBundle.getBundle("zonetrak", this.currentLocale);
	}

	public String getMessage(String key)
	{
		return this.messages.getString(key);
	}
}
