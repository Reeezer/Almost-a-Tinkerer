package ch.hearc.p2.aatinkerer.ui;

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

import ch.hearc.p2.aatinkerer.ItemType;

public class ItemDropdownMenu
{
	private Texture spritesheet;

	private TextureRegion topBorderRegion;
	private TextureRegion itemRegion;
	private TextureRegion bottomBorderRegion;
	
	private List<ItemType> items;
	
	private BitmapFont font;
	
	public ItemDropdownMenu()
	{
		this.spritesheet = new Texture(Gdx.files.internal("Ui/item_dropdown.png"));
		
		this.topBorderRegion = new TextureRegion(this.spritesheet, 0, 0, 128, 5);
		this.itemRegion = new TextureRegion(this.spritesheet, 0, 6, 128, 32);
		this.bottomBorderRegion = new TextureRegion(this.spritesheet, 0, 39, 128, 5);
		
		this.items = new LinkedList<ItemType>();
		
		FreeTypeFontParameter descriptionFontParameter = new FreeTypeFontParameter();
		descriptionFontParameter.size = 16;
		descriptionFontParameter.color = Color.WHITE;
		font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(descriptionFontParameter);
		
		
		// FIXME debug
		this.items.add(ItemType.BED);
		this.items.add(ItemType.BOX);
		this.items.add(ItemType.COUCH);
	}
	
	
	public void render(SpriteBatch batch, int x, int y)
	{
		int xcorner = x;
		int ycorner = y;
		
		batch.draw(this.topBorderRegion, xcorner, ycorner + 5 + (this.items.size() * this.itemRegion.getRegionHeight()));
		
		
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
