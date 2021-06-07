package ch.hearc.p2.aatinkerer.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.buildings.Building;

public class BuildingRecipeDisplay implements UIElement
{
	private Texture spritesheet;

	private TextureRegion popupRegion;
	private TextureRegion itemFrameRegion;

	private Recipe selectedRecipe;

	private Building building;

	private BitmapFont whiteFont;
	private BitmapFont largeWhiteFont;
	private BitmapFont blackFont;

	private Rectangle bounds;
	private Rectangle selectedRecipeButtonBounds;

	private ItemDropdownMenu dropdownMenu;

	public BuildingRecipeDisplay(ItemDropdownMenu dropdownMenu)
	{
		this.spritesheet = new Texture(Gdx.files.internal("Ui/recipes_menu.png"));

		this.popupRegion = new TextureRegion(this.spritesheet, 0, 0, 256, 117);
		this.itemFrameRegion = new TextureRegion(this.spritesheet, 3, 122, 32, 32);

		FreeTypeFontParameter whiteDescriptionFontParameter = new FreeTypeFontParameter();
		whiteDescriptionFontParameter.size = 16;
		whiteDescriptionFontParameter.color = Color.WHITE;
		whiteFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf"))
				.generateFont(whiteDescriptionFontParameter);

		FreeTypeFontParameter largeWhiteDescriptionFontParameter = new FreeTypeFontParameter();
		largeWhiteDescriptionFontParameter.size = 32;
		largeWhiteDescriptionFontParameter.color = Color.WHITE;
		largeWhiteFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf"))
				.generateFont(largeWhiteDescriptionFontParameter);

		FreeTypeFontParameter blackDescriptionFontParameter = new FreeTypeFontParameter();
		blackDescriptionFontParameter.size = 16;
		blackDescriptionFontParameter.color = Color.BLACK;
		blackFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf"))
				.generateFont(blackDescriptionFontParameter);

		this.bounds = new Rectangle();
		this.bounds.width = this.popupRegion.getRegionWidth();
		this.bounds.height = this.popupRegion.getRegionHeight();

		this.selectedRecipeButtonBounds = new Rectangle(6, 50, 30, 30);

		this.dropdownMenu = dropdownMenu;
		
		ItemDropdownListener listener = new ItemDropdownListener() {
			
			@Override
			public void itemSelected(ItemType type)
			{
				//if (BuildingRecipeDisplay.this.building != null)
				//	BuildingRecipeDisplay.this.building.setRecipeTarget(type);
			}
		};
	}
	
	@Override
	public void render(SpriteBatch batch, float delta)
	{
		if (building != null)
		{
			int cornerx = (int) this.bounds.x;
			int cornery = (int) this.bounds.y;

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

	public void setBuilding(Building building)
	{
		this.building = building;

		if (this.building != null)
		{
			// aussi récupérer les recettes du batiment pour l'affichage
			Recipe[] recipes = this.building.recipes();

			if (recipes != null && recipes.length > 0)
			{
				this.selectedRecipe = recipes[0];
			}
		}
		else
		{
			//this.dropdownMenu.setItems(null);
		}
	}

	public Building building()
	{
		return this.building;
	}

	@Override
	public boolean visible()
	{
		return this.building != null;
	}

	@Override
	public void setScreenSize(int w, int h)
	{
		this.bounds.x = (w - this.popupRegion.getRegionWidth()) / 2;
		this.bounds.y = (h - this.popupRegion.getRegionHeight()) / 2;
	}

	@Override
	public Rectangle getBounds()
	{
		return this.bounds;
	}

	@Override
	public void passRelativeClick(int x, int y)
	{
		if (this.building != null)
		{
			if (this.selectedRecipeButtonBounds.contains(new Vector2(x, y)))
			{
				List<ItemType> targetItems = new LinkedList<ItemType>();

				for (Recipe recipe : this.building.recipes())
					targetItems.add(recipe.getProduct());

				this.dropdownMenu.setItems(targetItems);
			} else
			{
				//this.dropdownMenu.setItems(null);
			}
		}
	}
}
