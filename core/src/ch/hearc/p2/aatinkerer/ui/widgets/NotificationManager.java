package ch.hearc.p2.aatinkerer.ui.widgets;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;

import ch.hearc.p2.aatinkerer.main.AATinkererGame;
import ch.hearc.p2.aatinkerer.ui.Notification;
import ch.hearc.p2.aatinkerer.ui.UIElement;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class NotificationManager implements UIElement
{
	private Texture notificationTexture;
	private BitmapFont titleFont;
	private BitmapFont descriptionFont;

	private Queue<Notification> queuedPopups;
	private Notification currentlyDisplayedNotification = null;

	private float timeDisplayed;

	private Rectangle bounds;

	private boolean maximising;

	public NotificationManager()
	{
		queuedPopups = new LinkedList<Notification>();
		notificationTexture = new Texture(Gdx.files.internal("ui/notification_popup.png"));

		// public domain font https://grafxkid.itch.io/at01
		FreeTypeFontParameter descriptionFontParameter = new FreeTypeFontParameter();
		descriptionFontParameter.size = 16;
		descriptionFontParameter.color = Color.BLACK;
		descriptionFont = AATinkererGame.font.generateFont(descriptionFontParameter);

		FreeTypeFontParameter titleFontParameter = new FreeTypeFontParameter();
		titleFontParameter.size = 16;
		titleFontParameter.color = Color.BLACK;
		titleFontParameter.borderColor = Color.WHITE;
		titleFontParameter.borderWidth = 1;
		titleFont = AATinkererGame.font.generateFont(titleFontParameter);

		timeDisplayed = 0.f;

		this.bounds = new Rectangle();
		this.bounds.width = this.notificationTexture.getWidth();
		this.bounds.height = this.notificationTexture.getHeight();
		this.maximising = true;
	}

	public void displayPopup(Notification popup)
	{
		queuedPopups.add(popup);
		System.out.println("Queueing new popup for display: " + popup);
	}

	@Override
	public void render(SpriteBatch batch, float delta)
	{
		if (queuedPopups.size() > 0 && currentlyDisplayedNotification == null)
			currentlyDisplayedNotification = queuedPopups.poll();

		if (currentlyDisplayedNotification != null)
		{
			int animationOffset = 0;

			// make the notification appear
			if (this.timeDisplayed < 1.f)
			{
				animationOffset = 0 - 10 - 256 + (int) ((10 + 256) * ease_in_out(Math.min(this.timeDisplayed, 1.f)));
				if (maximising)
				{
					maximising = false;
					Sounds.MAXIMISE.play();
				}
			}

			// make it disappear
			if (this.timeDisplayed >= this.currentlyDisplayedNotification.duration() - 1.f)
			{
				animationOffset = 0 - 10 - 256 + (int) ((10 + 256) * ease_in_out(Math.min(this.currentlyDisplayedNotification.duration() - this.timeDisplayed, 1.f)));
				if (!maximising)
				{
					maximising = true;
					Sounds.MINIMISE.play();
				}
			}

			int xCorner = 0 + 10 + animationOffset;
			int yCorner = (int) this.bounds.y;
			batch.draw(notificationTexture, xCorner, yCorner);

			// update the bounds position
			this.bounds.x = xCorner;

			// render title
			titleFont.draw(batch, currentlyDisplayedNotification.title(), xCorner + 13, yCorner + 92 - 7, 0, currentlyDisplayedNotification.title().length(), 239.f, -1, false);

			// render text
			descriptionFont.draw(batch, currentlyDisplayedNotification.description(), xCorner + 8, yCorner + 92 - 22, 0, currentlyDisplayedNotification.description().length(), 241.f, -1, true);

			this.timeDisplayed += delta;
			if (this.timeDisplayed >= this.currentlyDisplayedNotification.duration())
			{
				currentlyDisplayedNotification = null;
				this.timeDisplayed = 0.f;
			}
		}
	}

	public static float ease_in_out(float x)
	{
		final float a = 3;

		float numerator = (float) Math.pow(x, a);
		float denominator = numerator + (float) Math.pow(1 - x, a);

		return numerator / denominator;
	}

	@Override
	public void setScreenSize(int w, int h)
	{
		this.bounds.y = h - this.notificationTexture.getHeight() - 10;
	}

	@Override
	public Rectangle getBounds()
	{
		return this.bounds;
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		// not used
	}

	@Override
	public boolean visible()
	{
		return this.currentlyDisplayedNotification != null;
	}
}
