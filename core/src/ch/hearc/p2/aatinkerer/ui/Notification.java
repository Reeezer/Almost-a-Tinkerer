package ch.hearc.p2.aatinkerer.ui;

public class Notification
{
	private String title;
	private String description;
	private float duration;

	public Notification()
	{
		this("Title", "Description", 2.f);
	}

	public Notification(String title, String description, float duration)
	{
		this.title = title;
		this.description = description;
		this.duration = duration;
	}

	public String description()
	{
		return description;
	}

	public String title()
	{
		return title;
	}
	
	public float duration()
	{
		return duration;
	}

	@Override
	public String toString()
	{
		return String.format("[%s]: %s", title, description);
	}
}
