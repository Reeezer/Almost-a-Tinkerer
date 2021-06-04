package ch.hearc.p2.aatinkerer.ui;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;

import ch.hearc.p2.aatinkerer.Contract;
import ch.hearc.p2.aatinkerer.ItemType;

public class ContractDisplay implements Clickable
{
	private Texture spritesheet;

	private TextureRegion titlebarArea;
	private TextureRegion contractRowArea;
	private TextureRegion bottomBorderArea;
	private TextureRegion checkmarkArea;
	
	private BitmapFont descriptionFont;
	
	private Rectangle bounds;

	private Contract currentContract;

	public ContractDisplay()
	{
		this.spritesheet = new Texture("Ui/contracts_ui.png");

		this.titlebarArea = new TextureRegion(this.spritesheet, 0, 0, 256, 34);
		this.contractRowArea = new TextureRegion(this.spritesheet, 0, 35, 256, 36);
		this.bottomBorderArea = new TextureRegion(this.spritesheet, 0, 72, 256, 5);
		this.checkmarkArea = new TextureRegion(this.spritesheet, 7, 81, 26, 21);

		// public domain font https://grafxkid.itch.io/at01
		FreeTypeFontParameter descriptionFontParameter = new FreeTypeFontParameter();
		descriptionFontParameter.size = 16;
		descriptionFontParameter.color = Color.WHITE;
		descriptionFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(descriptionFontParameter);
	}

	public void setContract(Contract contract)
	{
		this.currentContract = contract;
	}

	public void render(SpriteBatch batch, int screenWidth, int screenHeight)
	{
		batch.draw(this.titlebarArea, (screenWidth / 2) - 256 - 5, (screenHeight / 2) - 34 - 5);

		int linecount = 0;
		if (currentContract != null)
		{
			linecount = currentContract.getRequestedItems().size();
			int i = 1;

			for (Map.Entry<ItemType, Integer> entry : currentContract.getRequestedItems().entrySet())
			{
				int xcorner = (screenWidth / 2) - 256 - 5;
				int ycorner = ((screenHeight / 2) - 34 - 5) - (i * 36);
				batch.draw(this.contractRowArea, xcorner, ycorner);
				
				ItemType itemType = entry.getKey();
				String itemName = itemType.name();
				int itemCount = entry.getValue() - currentContract.producedItems(itemType);
				
				if (itemCount < 0)
					itemCount = 0;
				
				String displayedString = String.format("%3dx : %s", itemCount, itemName);
				
				descriptionFont.draw(batch, displayedString, xcorner + 42, ycorner + 19, 0, displayedString.length(), 239.f, -1, false);
		
				itemType.render(batch, xcorner + 5, ycorner + 3);
		
				if (currentContract.isItemFulfilled(itemType))
				{
					batch.draw(checkmarkArea, xcorner + 7, ycorner + 7);
				}
				
				i++;
			}
		}
		batch.draw(this.bottomBorderArea, (screenWidth / 2) - 256 - 5,
				((screenHeight / 2) - 34 - 5) - (linecount * 36) - 5);
		
		
		/* FIXME debug 
		Pixmap pixmap = new Pixmap((int)this.bounds.width / 2, (int)this.bounds.height / 2, Pixmap.Format.RGB888);
		pixmap.setColor(Color.RED);
		pixmap.fillRectangle(0, 0, (int)this.bounds.width / 2, (int)this.bounds.height / 2);
		batch.draw(new Texture(pixmap), (screenWidth / 2) - 256 - 5, (screenHeight / 2) - 34 - 5);
		pixmap.dispose();//*/
	}
	
	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	@Override
	public void setBounds(int x, int y, int w, int h)
	{
		int lines = 0;
		if (this.currentContract != null)
			lines = currentContract.getRequestedItems().size();
		bounds = new Rectangle(w - 512 - 10, h - 68 - 10, this.titlebarArea.getRegionWidth() * 2, (this.titlebarArea.getRegionHeight() + (this.contractRowArea.getRegionHeight() * lines) + this.bottomBorderArea.getRegionHeight()) * 2);
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		// not used
	}
}
