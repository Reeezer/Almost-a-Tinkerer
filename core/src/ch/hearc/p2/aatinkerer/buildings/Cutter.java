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

		Recipe recipe = new Recipe(ItemType.IRONROD, 2);
		recipe.addIngredient(ItemType.IRONORE);

		recipes = new Recipe[] { recipe };
	}
}