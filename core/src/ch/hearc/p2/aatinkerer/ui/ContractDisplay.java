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

public class ContractDisplay implements UIElement
{
	private Texture spritesheet;

	private TextureRegion titlebarArea;
	private TextureRegion contractRowArea;
	private TextureRegion bottomBorderArea;
	private TextureRegion checkmarkArea;

	private BitmapFont descriptionFont;

	private Rectangle bounds;

	private Contract currentContract;
	private int screenHeight; // on est obligés de stocker ça puisque l'élément a une hauteur variable et qu'on le veut en haut de l'écran mais les coordonnées viennent d'en bas

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
		
		this.bounds = new Rectangle();
		this.bounds.width = this.titlebarArea.getRegionWidth();
		// recompute height
		this.setContract(null);
	}

	public void setContract(Contract contract)
	{
		this.currentContract = contract;
		int height = this.titlebarArea.getRegionHeight() + this.bottomBorderArea.getRegionHeight();
		
		if (contract != null)
			height += this.contractRowArea.getRegionHeight() * contract.getRequestedItems().size();
		
		this.bounds.height = height;
		this.bounds.y = this.screenHeight - this.bounds.height - 10;
	}

	public void render(SpriteBatch batch, float delta)
	{
		int y = (int) this.bounds.y + (int) this.bounds.height;
		batch.draw(this.titlebarArea, this.bounds.x, y - this.titlebarArea.getRegionHeight());

		int linecount = 0;
		if (currentContract != null)
		{
			linecount = currentContract.getRequestedItems().size();
			int i = 1;

			for (Map.Entry<ItemType, Integer> entry : currentContract.getRequestedItems().entrySet())
			{
				int xcorner = (int) this.bounds.x;
				int ycorner = (int) y - this.titlebarArea.getRegionHeight() - (i * this.contractRowArea.getRegionHeight());
				
				batch.draw(this.contractRowArea, xcorner, ycorner);

				ItemType itemType = entry.getKey();
				String itemName = itemType.name();
				int itemCount = entry.getValue() - currentContract.producedItems(itemType);

				if (itemCount < 0)
					itemCount = 0;

				String displayedString = String.format("%3dx : %s", itemCount, itemName);

				descriptionFont.draw(batch, displayedString, xcorner + 42, ycorner + 19, 0, displayedString.length(),
						239.f, -1, false);

				itemType.render(batch, xcorner + 5, ycorner + 3);

				if (currentContract.isItemFulfilled(itemType))
				{
					batch.draw(checkmarkArea, xcorner + 7, ycorner + 7);
				}

				i++;
			}
		}
		batch.draw(this.bottomBorderArea, this.bounds.x, y - this.titlebarArea.getRegionHeight() - (linecount * this.contractRowArea.getRegionHeight()) - 5);
	}

	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	@Override
	public void setScreenSize(int w, int h)
	{
		this.bounds.x = w - 256 - 10;
		this.screenHeight = h;
		
		// mettre à jour la position à l'écran
		setContract(currentContract);
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		// not used
	}

	@Override
	public boolean visible()
	{
		return true;
	}


}
