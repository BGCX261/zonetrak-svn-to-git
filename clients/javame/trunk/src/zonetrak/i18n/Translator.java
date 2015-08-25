package zonetrak.i18n;

public class Translator
{
	private static Translator instance = null;
	
	private I18NLanguage language;
	
	public static Translator getInstance()
	{
		if (instance == null)
		{
			instance = new Translator();
		}
		
		return instance;
	}
	
	private Translator()
	{
	}
	
	public void setLanguage(I18NLanguage language)
	{
		this.language = language;
	}
	
	public String getMessage(String key)
	{
		if (this.language != null)
		{
			return this.language.getMessage(key);
		}
		
		return key;
	}
}
