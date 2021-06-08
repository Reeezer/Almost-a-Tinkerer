package ch.hearc.p2.aatinkerer.ui.widgets;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.ui.ToolbarItem;
import ch.hearc.p2.aatinkerer.ui.UIElement;

public class Toolbar implements UIElement
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

		this.bounds = new Rectangle();
	}

	@Override
	public void render(SpriteBatch batch, float delta)
	{
		int x = (int) this.bounds.x;
		int y = (int) this.bounds.y;

		// background
		for (int i = 0; i < items.size(); i++)
		{
			if (i == activeItemIndex)
				batch.draw(activeBackgroundTexture, x + i * TEXSIZE, y);
			else
				batch.draw(backgroundTexture, x + i * TEXSIZE, y);
		}

		int offset = 0;
		for (ToolbarItem item : items)
		{
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
		if (itemno < 0 || itemno >= items.size())
		{
			activeItemIndex = -1;
			return;
		}
		if (!items.get(itemno).enabled())
		{
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
	public void setScreenSize(int w, int h)
	{
		int toolbarWidth = FactoryType.values().length * Toolbar.TEXSIZE;

		int x = (w - toolbarWidth) / 2;
		int y = 0;

		bounds = new Rectangle(x, y, toolbarWidth, Toolbar.TEXSIZE);
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

	@Override
	public boolean visible()
	{
		return true;
	}
}
