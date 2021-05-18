package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.TileMap;

public class Furnace extends Building
{
	public Furnace(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 10, "Tile/Furnace/", 1, 8, FactoryType.FURNACE);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 3) % 4 } };
		this.outputPosition = new int[] { x, y, direction };

		createRecipes();
	}

	private void createRecipes()
	{
		Recipe recipe1 = new Recipe(ItemType.IRONPLATE);
		recipe1.addIngredient(ItemType.IRONORE);
		recipe1.addIngredient(ItemType.COAL);

		Recipe recipe2 = new Recipe(ItemType.COPPERPLATE);
		recipe2.addIngredient(ItemType.COPPERORE);
		recipe2.addIngredient(ItemType.COAL);

		this.recipes = new Recipe[] { recipe1, recipe2 };
	}
}
