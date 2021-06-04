package ch.hearc.p2.aatinkerer.ui;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;

public class BuildingRecipeDisplay
{
	private Texture spritesheet;
	
	private TextureRegion popupRegion;
	private TextureRegion itemFrameRegion;

	private Recipe selectedRecipe;
	
	private BitmapFont whiteFont;
	private BitmapFont largeWhiteFont;
	private BitmapFont blackFont;
	
	public BuildingRecipeDisplay()
	{
		this.spritesheet = new Texture(Gdx.files.internal("Ui/recipes_menu.png"));
		
		this.popupRegion = new TextureRegion(this.spritesheet, 0, 0, 256, 117);
		this.itemFrameRegion = new TextureRegion(this.spritesheet, 3, 122, 32, 32);
		
		FreeTypeFontParameter whiteDescriptionFontParameter = new FreeTypeFontParameter();
		whiteDescriptionFontParameter.size = 16;
		whiteDescriptionFontParameter.color = Color.WHITE;
		whiteFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(whiteDescriptionFontParameter);
		
		FreeTypeFontParameter largeWhiteDescriptionFontParameter = new FreeTypeFontParameter();
		largeWhiteDescriptionFontParameter.size = 32;
		largeWhiteDescriptionFontParameter.color = Color.WHITE;
		largeWhiteFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(largeWhiteDescriptionFontParameter);
		
		FreeTypeFontParameter blackDescriptionFontParameter = new FreeTypeFontParameter();
		blackDescriptionFontParameter.size = 16;
		blackDescriptionFontParameter.color = Color.BLACK;
		blackFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(blackDescriptionFontParameter);
	
		// FIXME debug
		selectedRecipe = new Recipe(ItemType.BED);
		selectedRecipe.addIngredient(ItemType.FABRIC, 30);
		selectedRecipe.addIngredient(ItemType.WOODLOG, 20);
		selectedRecipe.addIngredient(ItemType.CONCRETE, 10);
		selectedRecipe.addIngredient(ItemType.PLANT, 200);
		selectedRecipe.addIngredient(ItemType.CARPET, 200);
		selectedRecipe.addIngredient(ItemType.COAL, 200);
		selectedRecipe.addIngredient(ItemType.WALLPAPER, 200);

	}
	
	public void render(SpriteBatch batch, int screenWidth, int screenHeight)
	{
		int cornerx = (screenWidth / 4) - (this.popupRegion.getRegionWidth() / 2);
		int cornery = (screenHeight / 4)  - (this.popupRegion.getRegionHeight() / 2);
		
		batch.draw(this.popupRegion, cornerx, cornery);
		
		// draw recipe target
		
		ItemType product = this.selectedRecipe.getProduct();
		product.render(batch, cornerx + 4, cornery + 48);
		this.largeWhiteFont.draw(batch, product.name(), cornerx + 48, cornery + 72);
		
		// draw each required item next to each other
	
		int i = 0;
		for (Map.Entry<ItemType, Integer> recipe : this.selectedRecipe.getIngredients().entrySet())
		{
			// les sprites font 32 de large, et on ajoute encore 3 pixels de marge entre
			int x = cornerx + 6 + ((32 + 3) * i);
			int y = cornery + 6;

			ItemType item = recipe.getKey();
			int amount = recipe.getValue();
			
			batch.draw(this.itemFrameRegion, x, y);
			item.render(batch, x, y);
			
			this.blackFont.draw(batch, Integer.toString(amount), x + 21, y + 9);
			this.whiteFont.draw(batch, Integer.toString(amount), x + 20, y + 10);
			
			i++;
		}
	}
	
}
