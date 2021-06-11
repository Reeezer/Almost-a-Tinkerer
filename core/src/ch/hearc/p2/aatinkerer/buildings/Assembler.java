package ch.hearc.p2.aatinkerer.buildings;

import java.util.ArrayList;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Recipe;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Assembler extends Building
{
	public Assembler(TileMap tilemap, int x, int y, int direction, int x2, int y2, int x3, int y3)
	{
		super(tilemap, x, y, direction, 10, "Tile/Assembler/", 3, 9, FactoryType.ASSEMBLER);
		this.inputPositions = new int[][] { { x, y, (direction + 1) % 4 }, { x2, y2, (direction + 1) % 4 }, { x3, y3, (direction + 1) % 4 } };
		this.outputPosition = new int[] { x2, y2, (direction + 3) % 4 };

		createRecipes();

		this.canSelectRecipe = true;
		this.selectedRecipe = this.recipes.get(0);
	}

	private void createRecipes()
	{
		this.recipes = new ArrayList<Recipe>();

		// since we cannot obtain the NONE item, we can use it so the assembler does not produce anything
		Recipe recipe0 = new Recipe(ItemType.NONE);
		recipe0.addIngredient(ItemType.NONE);
		recipes.add(recipe0);
		
		Recipe recipe1 = new Recipe(ItemType.FABRIC);
		recipe1.addIngredient(ItemType.COTTON, 4);
		recipes.add(recipe1);

		Recipe recipe2 = new Recipe(ItemType.TABLE);
		recipe2.addIngredient(ItemType.STICK, 4);
		recipe2.addIngredient(ItemType.PLANK, 2);
		recipe2.addIngredient(ItemType.GLUE);
		recipes.add(recipe2);

		Recipe recipe3 = new Recipe(ItemType.CHAIR);
		recipe3.addIngredient(ItemType.STICK, 4);
		recipe3.addIngredient(ItemType.PLANK);
		recipe3.addIngredient(ItemType.GLUE);
		recipes.add(recipe3);

		Recipe recipe4 = new Recipe(ItemType.DESK);
		recipe4.addIngredient(ItemType.PLANK, 4);
		recipe4.addIngredient(ItemType.IRONROD, 2);
		recipe4.addIngredient(ItemType.GLUE, 2);
		recipes.add(recipe4);

		Recipe recipe5 = new Recipe(ItemType.SHELF);
		recipe5.addIngredient(ItemType.PLANK, 3);
		recipe5.addIngredient(ItemType.IRONROD, 5);
		recipes.add(recipe5);

		Recipe recipe6 = new Recipe(ItemType.LAMP);
		recipe6.addIngredient(ItemType.COPPERWIRE, 3);
		recipe6.addIngredient(ItemType.IRONPLATE, 5);
		recipes.add(recipe6);

		Recipe recipe7 = new Recipe(ItemType.BED);
		recipe7.addIngredient(ItemType.PLANK, 5);
		recipe7.addIngredient(ItemType.PILLOW, 2);
		recipe7.addIngredient(ItemType.FABRIC, 3);
		recipes.add(recipe7);

		Recipe recipe8 = new Recipe(ItemType.STATUE);
		recipe8.addIngredient(ItemType.CONCRETE, 3);
		recipe8.addIngredient(ItemType.GLUE, 3);
		recipe8.addIngredient(ItemType.IRONROD, 2);
		recipes.add(recipe8);

		Recipe recipe9 = new Recipe(ItemType.COUCH);
		recipe9.addIngredient(ItemType.STICK, 4);
		recipe9.addIngredient(ItemType.PLANK, 8);
		recipe9.addIngredient(ItemType.PILLOW, 5);
		recipes.add(recipe9);

		Recipe recipe10 = new Recipe(ItemType.PILLOW);
		recipe10.addIngredient(ItemType.FABRIC, 3);
		recipe10.addIngredient(ItemType.GLUE, 2);
		recipes.add(recipe10);

		Recipe recipe11 = new Recipe(ItemType.BOX);
		recipe11.addIngredient(ItemType.PLANK, 4);
		recipe11.addIngredient(ItemType.IRONPLATE, 2);
		recipes.add(recipe11);

		Recipe recipe12 = new Recipe(ItemType.PLANT);
		recipe12.addIngredient(ItemType.CONCRETE, 2);
		recipe12.addIngredient(ItemType.WOODLOG, 5);
		recipe12.addIngredient(ItemType.WATER, 3);
		recipes.add(recipe12);

		Recipe recipe13 = new Recipe(ItemType.PENCIL);
		recipe13.addIngredient(ItemType.GRAPHITE, 3);
		recipe13.addIngredient(ItemType.WOODLOG, 2);
		recipes.add(recipe13);

		Recipe recipe14 = new Recipe(ItemType.WALLPAPER);
		recipe14.addIngredient(ItemType.FABRIC, 2);
		recipe14.addIngredient(ItemType.GLUE, 4);
		recipes.add(recipe14);

		Recipe recipe15 = new Recipe(ItemType.CARPET);
		recipe15.addIngredient(ItemType.FABRIC, 6);
		recipe15.addIngredient(ItemType.GLUE, 1);
		recipes.add(recipe15);
	}
}
