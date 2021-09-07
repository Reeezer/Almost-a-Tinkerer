package ch.hearc.p2.aatinkerer.buildings;

import java.util.ArrayList;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Recipe;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Mixer extends Building
{
	public Mixer(TileMap tilemap, int x, int y, int direction, boolean mirrored, int x2, int y2)
	{
		super(tilemap, x, y, direction, 10, 2, FactoryType.MIXER);
		if (!mirrored)
			this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x2, y2, (direction + 3) % 4 } };
		else
			this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x2, y2, (direction + 1) % 4 } };
		this.outputPosition = new int[] { x2, y2, direction };

		this.mirrored = mirrored;

		createRecipes();
	}

	private void createRecipes()
	{
		this.recipes = new ArrayList<Recipe>();

		Recipe recipe1 = new Recipe(ItemType.CONCRETE);
		recipe1.addIngredient(ItemType.WATER);
		recipe1.addIngredient(ItemType.STONE);
		recipes.add(recipe1);
	}
}
