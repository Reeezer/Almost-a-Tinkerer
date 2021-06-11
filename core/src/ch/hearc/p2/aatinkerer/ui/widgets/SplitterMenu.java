package ch.hearc.p2.aatinkerer.ui.widgets;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.p2.aatinkerer.buildings.Splitter;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.ui.UIElement;

public class SplitterMenu implements UIElement
{

	private Texture spritesheet;

	private TextureRegion widgetRegion;
	private TextureRegion itemUnselectedBorderRegion;
	private TextureRegion itemSelectedBorderRegion;

	private Rectangle bounds;

	private Map<ItemType, Rectangle> itemBounds; // pour savoir sur quoi on a cliqué

	private Splitter splitter;

	public SplitterMenu()
	{
		this.spritesheet = new Texture(Gdx.files.internal("Ui/splitter_menu.png"));

		this.widgetRegion = new TextureRegion(this.spritesheet, 0, 0, 256, 224);
		this.itemUnselectedBorderRegion = new TextureRegion(this.spritesheet, 0, 224, 32, 32);
		this.itemSelectedBorderRegion = new TextureRegion(this.spritesheet, 32, 224, 32, 32);
		
		this.bounds = new Rectangle();
		this.bounds.width = this.widgetRegion.getRegionWidth();
		this.bounds.height = this.widgetRegion.getRegionHeight();

		this.itemBounds = new HashMap<ItemType, Rectangle>();
		int i = 0;
		for (ItemType type : ItemType.values())
		{// On n'ignore volontairement pas ItemType.NONE car cela permet de ne rien séparer si on le sélectionne via l'UI
			final int separatorDistance = 3;

			final int itemsPerRow = 7;
			int ix = 7 + ((((int) this.itemUnselectedBorderRegion.getRegionWidth() + separatorDistance) * (i % itemsPerRow)));
			int iy = this.widgetRegion.getRegionHeight() - 42 - (((i / itemsPerRow)) * (this.itemUnselectedBorderRegion.getRegionHeight() + separatorDistance)) - this.itemUnselectedBorderRegion.getRegionHeight();

			this.itemBounds.put(type, new Rectangle(ix, iy, this.itemUnselectedBorderRegion.getRegionWidth(), this.itemUnselectedBorderRegion.getRegionHeight()));

			i++;
		}
	}

	public void setSplitter(Splitter splitter)
	{
		this.splitter = splitter;
	}

	@Override
	public void render(SpriteBatch batch, float delta)
	{
		if (this.splitter != null)
		{
			int x = (int) this.bounds.x;
			int y = (int) this.bounds.y;

			batch.draw(this.widgetRegion, x, y);

			for (Map.Entry<ItemType, Rectangle> entry : this.itemBounds.entrySet())
			{
				int ix = x + (int) entry.getValue().x;
				int iy = y + (int) entry.getValue().y;
				
				if (entry.getKey() == this.splitter.splitType())
					batch.draw(this.itemSelectedBorderRegion, ix, iy);
				else
					batch.draw(this.itemUnselectedBorderRegion, ix, iy);
				
				entry.getKey().render(batch, ix, iy);
			}
		}
	}

	@Override
	public void setScreenSize(int w, int h)
	{
		this.bounds.x = (w - this.widgetRegion.getRegionWidth()) / 2;
		this.bounds.y = (h - this.widgetRegion.getRegionHeight()) / 2;
	}

	@Override
	public Rectangle getBounds()
	{
		return this.bounds;
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		for (Map.Entry<ItemType, Rectangle> entry : this.itemBounds.entrySet())
		{
			Rectangle bounds = entry.getValue();

			if (bounds.contains(new Vector2(x, y)))
			{
				System.out.println("splitter item selected: " + entry.getKey().name());
				
				if (this.splitter != null)
					this.splitter.setSplitType(entry.getKey());
			}
		}
	}

	@Override
	public boolean visible()
	{
		return this.splitter != null;
	}

}
