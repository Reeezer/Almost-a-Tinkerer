package ch.hearc.p2.aatinkerer;

public class Popup
{
	private String title;
	private String description;

	public Popup()
	{
		this("Title", "Description");
	}

	public Popup(String title, String description)
	{
		this.title = title;
		this.description = description;
	}

	public String description()
	{
		return description;
	}

	public String title()
	{
		return title;
	}

	@Override
	public String toString()
	{
		return String.format("[%s]: %s", title, description);
	}
}
