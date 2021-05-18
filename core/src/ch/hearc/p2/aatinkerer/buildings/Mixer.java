package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.TileMap;

public class Mixer extends Building
{
	public Mixer(TileMap tilemap, int x, int y, int direction, int x2, int y2)
	{
		super(tilemap, x, y, direction, 10, "Tile/Mixer/", 2, 6, FactoryType.MIXER);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x2, y2, (direction + 3) % 4 } };
		this.outputPosition = new int[] { x2, y2, direction };

		createRecipes();
	}

	private void createRecipes()
	{
		Recipe recipe1 = new Recipe(ItemType.CONCRETE);
		recipe1.addIngredient(ItemType.WATER);
		recipe1.addIngredient(ItemType.STONE);

		this.recipes = new Recipe[] { recipe1 };
	}
}
