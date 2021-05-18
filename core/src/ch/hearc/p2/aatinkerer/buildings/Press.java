package ch.hearc.p2.aatinkerer.buildings;

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
		Recipe recipe1 = new Recipe(ItemType.GLUE);
		recipe1.addIngredient(ItemType.OIL);

		Recipe recipe2 = new Recipe(ItemType.GRAPHITE);	
		recipe2.addIngredient(ItemType.COAL);

		this.recipes = new Recipe[] { recipe1, recipe2 };
	}
}