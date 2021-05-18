package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.TileMap;

public class Cutter extends Building
{
	public Cutter(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Cutter/", 1, 4, FactoryType.CUTTER);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPosition = new int[] { x, y, direction };

		createRecipes();
	}

	private void createRecipes()
	{
		Recipe recipe1 = new Recipe(ItemType.IRONROD, 2);
		recipe1.addIngredient(ItemType.IRONPLATE);

		Recipe recipe2 = new Recipe(ItemType.COPPERWIRE, 2);
		recipe2.addIngredient(ItemType.COPPERPLATE);

		Recipe recipe3 = new Recipe(ItemType.PLANK, 2);
		recipe3.addIngredient(ItemType.WOODLOG);

		Recipe recipe4 = new Recipe(ItemType.STICK, 2);
		recipe4.addIngredient(ItemType.PLANK);

		this.recipes = new Recipe[] { recipe1, recipe2, recipe3, recipe4 };
	}
}