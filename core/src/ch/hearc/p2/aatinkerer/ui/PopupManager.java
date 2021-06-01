package ch.hearc.p2.aatinkerer.ui;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import ch.hearc.p2.aatinkerer.Popup;

public class PopupManager
{
	private Texture popupTexture;
	private BitmapFont titleFont;
	private BitmapFont descriptionFont;

	private Queue<Popup> queuedPopups;
	private Popup currentlyDisplayedPopup = null;

	private float timeDisplayed;

	public PopupManager()
	{
		queuedPopups = new LinkedList<Popup>();
		popupTexture = new Texture(Gdx.files.internal("Ui/notification_popup.png"));

		// public domain font https://grafxkid.itch.io/at01
		FreeTypeFontParameter descriptionFontParameter = new FreeTypeFontParameter();
		descriptionFontParameter.size = 16;
		descriptionFontParameter.color = Color.BLACK;
		descriptionFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(descriptionFontParameter);
		
		FreeTypeFontParameter titleFontParameter = new FreeTypeFontParameter();
		titleFontParameter.size = 16;
		titleFontParameter.color = Color.BLACK;
		titleFontParameter.borderColor = Color.WHITE;
		titleFontParameter.borderWidth = 1;
		titleFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(titleFontParameter);

		timeDisplayed = 0.f;
	}

	public void displayPopup(Popup popup)
	{
		queuedPopups.add(popup);
		System.out.println("Queueing new popup for display: " + popup);
	}

	// FIXME taille de la zone du texte
	// FIXME titre
	// FIXME taille de la font
	public void render(SpriteBatch batch, float delta, int screenWidth, int screenHeight)
	{
		if (queuedPopups.size() > 0 && currentlyDisplayedPopup == null)
			currentlyDisplayedPopup = queuedPopups.poll();

		if (currentlyDisplayedPopup != null)
		{
			int animationOffset = 0;
			
			// make the notification appear
			if (this.timeDisplayed < 1.f)
				animationOffset = 0 - 10 - 256 + (int) ((10 + 256) * ease_in_out(Math.min(this.timeDisplayed, 1.f)));
			
			// make it disappear
			if (this.timeDisplayed >= this.currentlyDisplayedPopup.duration() - 1.f)
				animationOffset = 0 - 10 - 256 + (int) ((10 + 256) * ease_in_out(Math.min(this.currentlyDisplayedPopup.duration() - this.timeDisplayed, 1.f)));
			
			int xCorner = 0 + 10 + animationOffset;
			int yCorner = ((screenHeight / 2) - popupTexture.getHeight()) - 10;
			batch.draw(popupTexture, xCorner, yCorner);
			
			// render title
			titleFont.draw(batch, currentlyDisplayedPopup.title(), xCorner + 13, yCorner + 92 - 7, 0, currentlyDisplayedPopup.title().length(), 239.f, -1, false);
			
			// render text
			descriptionFont.draw(batch, currentlyDisplayedPopup.description(), xCorner + 8, yCorner + 92 - 22, 0,
					currentlyDisplayedPopup.description().length(), 241.f, -1, true);

			this.timeDisplayed += delta;
			if (this.timeDisplayed >= this.currentlyDisplayedPopup.duration())
			{
				currentlyDisplayedPopup = null;
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
}
