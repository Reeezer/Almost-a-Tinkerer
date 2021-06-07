package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.TileMap;

public class Assembler extends Building
{
	public Assembler(TileMap tilemap, int x, int y, int direction, int x2, int y2, int x3, int y3)
	{
		super(tilemap, x, y, direction, 10, "Tile/Assembler/", 3, 9, FactoryType.ASSEMBLER);
		this.inputPositions = new int[][] { { x, y, (direction + 1) % 4 }, { x2, y2, (direction + 1) % 4 }, { x3, y3, (direction + 1) % 4 } };
		this.outputPosition = new int[] { x2, y2, (direction + 3) % 4 };

		createRecipes();
		
		this.canSelectRecipe = true;
	}

	private void createRecipes()
	{
		Recipe recipe1 = new Recipe(ItemType.FABRIC);
		recipe1.addIngredient(ItemType.COTTON, 4);

		Recipe recipe2 = new Recipe(ItemType.TABLE);
		recipe2.addIngredient(ItemType.STICK, 4);
		recipe2.addIngredient(ItemType.PLANK, 2);
		recipe2.addIngredient(ItemType.GLUE);

		Recipe recipe3 = new Recipe(ItemType.CHAIR);
		recipe3.addIngredient(ItemType.STICK, 4);
		recipe3.addIngredient(ItemType.PLANK, 1);
		recipe3.addIngredient(ItemType.GLUE);

		Recipe recipe4 = new Recipe(ItemType.DESK);
		recipe4.addIngredient(ItemType.STICK, 4);
		recipe4.addIngredient(ItemType.IRONROD, 2);
		recipe4.addIngredient(ItemType.GLUE, 2);

		Recipe recipe5 = new Recipe(ItemType.SHELF);
		recipe5.addIngredient(ItemType.PLANK, 3);
		recipe5.addIngredient(ItemType.IRONROD, 5);

		Recipe recipe6 = new Recipe(ItemType.LAMP);
		recipe6.addIngredient(ItemType.COPPERWIRE, 3);
		recipe6.addIngredient(ItemType.IRONPLATE, 5);

		Recipe recipe7 = new Recipe(ItemType.BED);
		recipe7.addIngredient(ItemType.STICK, 4);
		recipe7.addIngredient(ItemType.PLANK, 5);
		recipe7.addIngredient(ItemType.PILLOW, 2);
		recipe7.addIngredient(ItemType.FABRIC, 3);

		Recipe recipe8 = new Recipe(ItemType.STATUE);
		recipe8.addIngredient(ItemType.CONCRETE, 3);
		recipe8.addIngredient(ItemType.GLUE, 3);
		recipe8.addIngredient(ItemType.IRONROD, 2);
		recipe8.addIngredient(ItemType.GRAPHITE, 2);

		Recipe recipe9 = new Recipe(ItemType.COUCH);
		recipe9.addIngredient(ItemType.STICK, 4);
		recipe9.addIngredient(ItemType.PLANK, 8);
		recipe9.addIngredient(ItemType.PILLOW, 5);

		Recipe recipe10 = new Recipe(ItemType.PILLOW);
		recipe10.addIngredient(ItemType.FABRIC, 3);
		recipe10.addIngredient(ItemType.GLUE, 2);

		Recipe recipe11 = new Recipe(ItemType.BOX);
		recipe11.addIngredient(ItemType.PLANK, 4);
		recipe11.addIngredient(ItemType.IRONPLATE, 2);

		Recipe recipe12 = new Recipe(ItemType.PLANT);
		recipe12.addIngredient(ItemType.CONCRETE, 2);
		recipe12.addIngredient(ItemType.WOODLOG, 5);
		recipe12.addIngredient(ItemType.WATER, 3);

		Recipe recipe13 = new Recipe(ItemType.PENCIL);
		recipe13.addIngredient(ItemType.GRAPHITE, 3);
		recipe13.addIngredient(ItemType.WOODLOG, 2);

		Recipe recipe14 = new Recipe(ItemType.WALLPAPER);
		recipe14.addIngredient(ItemType.FABRIC, 2);
		recipe14.addIngredient(ItemType.GLUE, 4);

		Recipe recipe15 = new Recipe(ItemType.CARPET);
		recipe15.addIngredient(ItemType.FABRIC, 6);
		recipe15.addIngredient(ItemType.GLUE, 1);

		this.recipes = new Recipe[] { recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7, recipe8, recipe9, recipe10, recipe11, recipe12, recipe13, recipe14, recipe15 };
	}
}
