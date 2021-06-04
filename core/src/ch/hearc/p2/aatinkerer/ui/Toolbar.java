package ch.hearc.p2.aatinkerer.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ch.hearc.p2.aatinkerer.buildings.FactoryType;

public class Toolbar implements Clickable
{
	private ArrayList<ToolbarItem> items;
	private int activeItemIndex;

	private Texture backgroundTexture;
	private Texture activeBackgroundTexture;

	private Rectangle bounds;

	public final static int TEXSIZE = 32;

	public Toolbar(ToolbarItem... items)
	{
		this.items = new ArrayList<ToolbarItem>();
		this.activeItemIndex = -1; // nothing selected by default

		for (ToolbarItem item : items)
			this.items.add(item);

		backgroundTexture = new Texture(Gdx.files.internal("Ui/Icons/IconBackground.png"));
		activeBackgroundTexture = new Texture(Gdx.files.internal("Ui/Icons/IconBackgroundSelected.png"));
	}

	public void render(SpriteBatch batch, int x, int y)
	{
		// background
		for (int i = 0; i < items.size(); i++) {
			if (i == activeItemIndex)
				batch.draw(activeBackgroundTexture, x + i * TEXSIZE, y);
			else
				batch.draw(backgroundTexture, x + i * TEXSIZE, y);
		}

		int offset = 0;
		for (ToolbarItem item : items) {
			Texture texture = item.getItemTexture();

			if (texture != null && item.enabled())
				batch.draw(texture, x + offset, y);

			offset += TEXSIZE;
		}
	}

	public ToolbarItem getActiveItem()
	{
		if (activeItemIndex > -1)
			return items.get(activeItemIndex);
		else
			return null;
	}

	public void setActiveItem(int itemno)
	{
		if (itemno < 0 || itemno >= items.size()) {
			activeItemIndex = -1;
			return;
		}
		if (!items.get(itemno).enabled()) {
			activeItemIndex = -1;
			return;
		}

		activeItemIndex = itemno;
	}

	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	@Override
	public void setBounds(int x, int y, int w, int h)
	{
		bounds = new Rectangle(x, y, w, h);
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		int itemno = x / TEXSIZE;
		setActiveItem(itemno);
	}

	public void setItemEnabled(ToolbarItem item, boolean enabled)
	{
		items.get(items.indexOf(item)).setEnabled(enabled);
	}
}
