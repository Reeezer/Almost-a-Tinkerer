package ch.hearc.p2.aatinkerer.buildings;

import java.util.ArrayList;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Recipe;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Furnace extends Building
{
	public Furnace(TileMap tilemap, int x, int y, int direction, boolean mirrored)
	{
		super(tilemap, x, y, direction, 10, (mirrored ? "Tile/FurnaceMirror/" : "Tile/Furnace/"), 1, 8, FactoryType.FURNACE);
		if (!mirrored)
			this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 3) % 4 } };
		else
			this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 1) % 4 } };
		this.outputPosition = new int[] { x, y, direction };

		createRecipes();
	}

	private void createRecipes()
	{
		this.recipes = new ArrayList<Recipe>();

		Recipe recipe1 = new Recipe(ItemType.IRONPLATE);
		recipe1.addIngredient(ItemType.IRONORE);
		recipe1.addIngredient(ItemType.COAL);
		recipes.add(recipe1);

		Recipe recipe2 = new Recipe(ItemType.COPPERPLATE);
		recipe2.addIngredient(ItemType.COPPERORE);
		recipe2.addIngredient(ItemType.COAL);
		recipes.add(recipe2);
	}
}
