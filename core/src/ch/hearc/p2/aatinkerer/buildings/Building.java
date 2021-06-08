package ch.hearc.p2.aatinkerer.buildings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Recipe;
import ch.hearc.p2.aatinkerer.world.TileMap;

public abstract class Building
{
	protected class Item
	{
		public ItemType type;
		public long ticksSpent;
		// necessary because otherwise an item might get teleported to the other side of
		// a conveyor system if updates happen in the right order (also know as
		// black magic)
		public boolean justTransfered;

		public Item()
		{
			type = ItemType.NONE;
			ticksSpent = 0;
			justTransfered = false;
		}
	}

	protected TileMap tilemap;
	protected BuildingTile[] tiles;
	protected FactoryType type;

	protected int direction;
	protected int x;
	protected int y;

	protected int contentSize;
	protected int maxSize;
	protected LinkedList<Item> items;
	protected HashMap<ItemType, Integer> currentIngredients;

	protected Building output;
	protected int[][] inputPositions;
	protected int[] outputPosition;

	protected static int ticks = 0;

	protected List<Recipe> recipes;
	protected Recipe selectedRecipe;
	protected boolean canSelectRecipe;

	public Building(TileMap tilemap, int x, int y, int direction, int size, String spritePath, int tiles, int frames, FactoryType type)
	{
		this.tilemap = tilemap;
		this.type = type;

		this.x = x;
		this.y = y;
		this.direction = direction;

		this.tiles = new BuildingTile[tiles];
		for (int i = 0; i < tiles; i++)
		{
			this.tiles[i] = new BuildingTile(spritePath + String.format("%02d", i) + "/", frames, type);
		}

		this.contentSize = 0;
		this.maxSize = size;
		this.items = new LinkedList<Item>();
		this.currentIngredients = new HashMap<ItemType, Integer>();
		this.recipes = null;
		this.canSelectRecipe = false;
	}

	public boolean canSelectRecipe()
	{
		return this.canSelectRecipe;
	}

	public void render(SpriteBatch batch, int tileSize)
	{
		for (int i = 0; i < tiles.length; i++)
		{
			BuildingTile tile = tiles[i];

			int tx = (direction % 2 == 0) ? ((direction == 0) ? x + i : x - i) : x;
			int ty = (direction % 2 != 0) ? ((direction == 1) ? y + i : y - i) : y;

			tile.render(batch, tileSize, direction, tx, ty);
		}
	}

	public int[][] getInputs()
	{
		return inputPositions;
	}

	public int[] getOutput()
	{
		return outputPosition;
	}

	public void updateOutputs()
	{
		if (outputPosition != null)
			output = tilemap.getNeighbourBuilding(outputPosition);
	}

	public FactoryType getType()
	{
		return type;
	}

	public boolean isFull(Item item)
	{
		if (item == null)
			return true;

		// We do want to be able to store multiple itemtype in multiple amount
		if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS || type == FactoryType.MERGER)
		{
			if (!currentIngredients.containsKey(item.type))
				return false;
			else
				return currentIngredients.get(item.type) >= maxSize;
		}
		else
			return contentSize >= maxSize;
	}

	public void addItem(Item item)
	{
		// Called by the building who gives the item to insert in this building the item
		if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS || type == FactoryType.MERGER)
		{
			if (currentIngredients.containsKey(item.type))
				currentIngredients.put(item.type, currentIngredients.get(item.type) + 1);
			else
				currentIngredients.put(item.type, 1);
		}
		items.add(item);

		item.ticksSpent = 0;
		item.justTransfered = true;
		contentSize++;
	}

	public void transferItem()
	{
		Item itemToTransfer = items.peek();
		// Check if the building can make a recipe or otherwise if it has an item to
		// transfer
		if (output != null && contentSize > 0)
		{
			if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS || type == FactoryType.MERGER)
			{
				checkRecipes();
			}
			else
			{
				if (!output.isFull(itemToTransfer) && !itemToTransfer.justTransfered)
				{
					Item item = items.poll();
					contentSize--;
					output.addItem(item);
				}
			}
		}
	}

	private void checkRecipe(Recipe recipe)
	{
		// Check if there are enough items for the recipe
		boolean makeIt = true;
		Map<ItemType, Integer> ingredients = recipe.getIngredients();
		for (ItemType item : ingredients.keySet())
		{
			if (!currentIngredients.containsKey(item) || currentIngredients.get(item) < ingredients.get(item))
			{
				makeIt = false; // if there is not enough item for this recipe
				break;
			}
		}

		// if we have enough ingredients to make the recipe
		if (makeIt)
		{
			// Make the recipe by decreasing the amount of all the ingredients
			for (ItemType item : ingredients.keySet())
			{
				int nb = ingredients.get(item);
				currentIngredients.put(item, currentIngredients.get(item) - nb);
				contentSize -= nb;
			}

			// Adding the item produced
			for (int i = 0; i < recipe.getAmount(); i++)
			{
				Item item = new Item();
				item.type = recipe.getProduct();
				if (!output.isFull(item))
					output.addItem(item);
			}
		}
	}

	protected void checkRecipes()
	{
		if (this.selectedRecipe == null)
		{
			for (Recipe recipe : recipes)
			{
				checkRecipe(recipe);
			}
		}
		else
		{
			checkRecipe(this.selectedRecipe);
		}
	}

	public List<Recipe> getRecipes()
	{
		return recipes;
	}

	public void setRecipe(Recipe recipe)
	{
		if (this.recipes != null && this.recipes.contains(recipe))
			this.selectedRecipe = recipe;
	}

	public void setRecipeTarget(ItemType type)
	{
		for (Recipe recipe : this.recipes)
		{
			if (recipe.getProduct() == type)
				setRecipe(recipe);
		}
	}

	public Recipe activeRecipe()
	{
		return this.selectedRecipe;
	}

	public void update()
	{
		// transfer item if it is the right time
		if (type != null)
		{
			if (type.getTransferTicks() == type.getTransferTimeout())
			{
				transferItem();
			}
		}

		// update items
		for (Item item : items)
		{
			if (item.ticksSpent < type.getTransferTimeout())
			{
				item.ticksSpent++;
				item.justTransfered = false;
			}
		}
	}

	public List<Recipe> recipes()
	{
		return this.recipes;
	}
}
