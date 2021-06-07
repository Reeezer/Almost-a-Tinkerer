package ch.hearc.p2.aatinkerer.buildings;

import java.util.ArrayList;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.TileMap;

public class Press extends Building
{
	public Press(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Press/", 1, 6, FactoryType.PRESS);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPosition = new int[] { x, y, direction };

		createRecipes();
	}

	private void createRecipes()
	{
		this.recipes = new ArrayList<Recipe>();
		
		Recipe recipe1 = new Recipe(ItemType.GLUE);
		recipe1.addIngredient(ItemType.OIL);
		recipes.add(recipe1);

		Recipe recipe2 = new Recipe(ItemType.GRAPHITE);
		recipe2.addIngredient(ItemType.COAL);
		recipes.add(recipe2);
	}
}