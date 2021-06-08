package ch.hearc.p2.aatinkerer.buildings;

import java.util.ArrayList;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Recipe;
import ch.hearc.p2.aatinkerer.world.TileMap;

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
		this.recipes = new ArrayList<Recipe>();

		Recipe recipe1 = new Recipe(ItemType.IRONROD, 2);
		recipe1.addIngredient(ItemType.IRONPLATE);
		recipes.add(recipe1);

		Recipe recipe2 = new Recipe(ItemType.COPPERWIRE, 2);
		recipe2.addIngredient(ItemType.COPPERPLATE);
		recipes.add(recipe2);

		Recipe recipe3 = new Recipe(ItemType.PLANK, 2);
		recipe3.addIngredient(ItemType.WOODLOG);
		recipes.add(recipe3);

		Recipe recipe4 = new Recipe(ItemType.STICK, 2);
		recipe4.addIngredient(ItemType.PLANK);
		recipes.add(recipe4);
	}
}