package zonetrak.i18n;

import java.util.Hashtable;

public class ZoneTrak_de_DE implements I18NLanguage
{
	private Hashtable messages;

	public ZoneTrak_de_DE()
	{
		this.messages = new Hashtable();
		this.initLanguage();
	}
	
	private void initLanguage()
	{
		this.messages.put("Map", "Karte");
		this.messages.put("Configuration", "Einstellungen");
		this.messages.put("Annotations", "Kommentare");
		this.messages.put("Save", "Speichern");
		this.messages.put("Back", "Zurück");
		this.messages.put("View", "Anzeigen");
		this.messages.put("Middleware", "Middleware");
		this.messages.put("Name", "Name");
		this.messages.put("ID", "ID");
		this.messages.put("Enable", "Aktivieren");
		this.messages.put("Yes", "Ja");
		this.messages.put("Back", "Zurück");
		this.messages.put("Saved", "Gespeichert");
		this.messages.put("The configuration has been saved.", "Die Einstellungen wurden gespeichert.");
		this.messages.put("Back", "Zurück");
		this.messages.put("Error", "Fehler");
		this.messages.put("The middleware is not reachable.", "Die Middelware ist nicht erreichbar.");
		this.messages.put("Notice", "Achtung");
		this.messages.put("The service is not enabled.", "Der Dienst ist nicht aktiviert.");
		this.messages.put("Warning", "Warnung");
		this.messages.put("You have entered a zone!", "Sie haben eine Zone betreten!");
		this.messages.put("You should only change the configuration, if you really know what you are doing.", "Sie sollten nur die Einstellungen ändern, wenn sie wirklich wissen was sie tun.");
	}

	public String getMessage(String key)
	{
		Object message = this.messages.get(key);
		
		if (message != null)
		{
			return message.toString();
		}
		
		return key;
	}
}
