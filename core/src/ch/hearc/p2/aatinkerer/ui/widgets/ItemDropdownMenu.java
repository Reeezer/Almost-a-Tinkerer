package ch.hearc.p2.aatinkerer.ui.widgets;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;

import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.listeners.ItemDropdownListener;
import ch.hearc.p2.aatinkerer.ui.UIElement;

public class ItemDropdownMenu implements UIElement
{
	private Texture spritesheet;

	private TextureRegion topBorderRegion;
	private TextureRegion itemRegion;
	private TextureRegion bottomBorderRegion;

	private List<ItemType> items;

	private BitmapFont font;

	private Rectangle bounds;
	
	private List<ItemDropdownListener> listeners;

	public ItemDropdownMenu()
	{
		this.spritesheet = new Texture(Gdx.files.internal("Ui/item_dropdown.png"));

		this.topBorderRegion = new TextureRegion(this.spritesheet, 0, 0, 128, 5);
		this.itemRegion = new TextureRegion(this.spritesheet, 0, 6, 128, 32);
		this.bottomBorderRegion = new TextureRegion(this.spritesheet, 0, 39, 128, 5);

		FreeTypeFontParameter descriptionFontParameter = new FreeTypeFontParameter();
		descriptionFontParameter.size = 16;
		descriptionFontParameter.color = Color.WHITE;
		font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(descriptionFontParameter);

		this.bounds = new Rectangle();
		this.listeners = new LinkedList<ItemDropdownListener>();
	}

	public void setItems(List<ItemType> items)
	{
		this.items = items;
		
		// bounds height due to variable height
		this.bounds.height = this.bottomBorderRegion.getRegionHeight() + this.topBorderRegion.getRegionHeight();
		if (this.items != null)
			this.bounds.height += this.itemRegion.getRegionHeight() * this.items.size();
	}

	@Override
	public void render(SpriteBatch batch, float delta)
	{
		if (this.items != null)
		{
			int xcorner = (int) this.bounds.x;
			int ycorner = (int) this.bounds.y;

			batch.draw(this.topBorderRegion, xcorner,
					ycorner + 5 + (this.items.size() * this.itemRegion.getRegionHeight()));

			int i = 0;
			for (ItemType item : this.items)
			{
				int cx = xcorner;
				int cy = ycorner + 5 + (i * this.itemRegion.getRegionHeight());

				batch.draw(this.itemRegion, cx, cy);

				font.draw(batch, item.name(), cx + 42, cy + 19, 0, item.name().length(), 86.f, -1, false);
				item.render(batch, cx + 3, cy);

				i++;
			}

			batch.draw(this.bottomBorderRegion, xcorner, ycorner);
		}

	}
	
	public void addListener(ItemDropdownListener listener)
	{
		this.listeners.add(listener);
	}

	public void selectItem(ItemType type)
	{
		System.out.println("Bonsoir " + type.name());
		for (ItemDropdownListener listener : this.listeners)
			listener.itemSelected(type);
	}
	
	@Override
	public void setScreenSize(int w, int h)
	{
		this.bounds.x = 0;
		this.bounds.y = 0;
		this.bounds.width = this.topBorderRegion.getRegionWidth();

		// update bounds
		setItems(this.items);
	}

	@Override
	public Rectangle getBounds()
	{
		return this.bounds;
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		if (this.items != null)
		{
			int yInList = y - this.bottomBorderRegion.getRegionHeight();
			int itemno = yInList / this.itemRegion.getRegionHeight();
			
			if (itemno < this.items.size())
				selectItem(this.items.get(itemno));
		}
	}

	@Override
	public boolean visible()
	{
		return this.items != null;
	}
}
