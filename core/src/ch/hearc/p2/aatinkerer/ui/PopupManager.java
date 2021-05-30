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
	private BitmapFont font;
	
	private Queue<Popup> queuedPopups;
	private Popup currentlyDisplayedPopup = null;

	public PopupManager()
	{
		queuedPopups = new LinkedList<Popup>();
		popupTexture = new Texture(Gdx.files.internal("Ui/notification_popup.png"));
		
		// public domain font https://grafxkid.itch.io/at01
		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 24;
		fontParameter.color = Color.BLACK;
		font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(fontParameter);
	}

	public void displayPopup(Popup popup)
	{
		queuedPopups.add(popup);
		System.out.println("Queueing new popup for display: " + popup);
	}

	// FIXME taille de la zone du texte
	// FIXME titre
	// FIXME taille de la font
	public void render(SpriteBatch batch, int screenWidth, int screenHeight)
	{
		if (queuedPopups.size() > 0 && currentlyDisplayedPopup == null)
			currentlyDisplayedPopup = queuedPopups.poll();

		if (currentlyDisplayedPopup != null)
		{
			int xCorner = 0 + 10;
			int yCorner = ((screenHeight / 2) - popupTexture.getHeight()) - 10;
			batch.draw(popupTexture, xCorner, yCorner);
			font.draw(batch, currentlyDisplayedPopup.description(), xCorner + 8, yCorner + 92 - 22);
			// currentlyDisplayedPopup = null;
		}
	}
}
