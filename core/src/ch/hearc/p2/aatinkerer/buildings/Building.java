package ch.hearc.p2.aatinkerer.buildings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ch.hearc.p2.aatinkerer.data.AnimationType;
import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Recipe;
import ch.hearc.p2.aatinkerer.data.Tile;
import ch.hearc.p2.aatinkerer.data.TileType;
import ch.hearc.p2.aatinkerer.world.Chunk;
import ch.hearc.p2.aatinkerer.world.TileMap;

public abstract class Building implements Tile, Serializable
{
	private static final long serialVersionUID = 1L;

	public transient final TileType tiletype = TileType.FACTORY;

	public class Item implements Serializable
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

	// - since the whole object will be recreated, we just store the bare minimum to recreate the object and recreate its connections
	// from these, that's why everything is marked transient
	// - FIXME there has to be a better solution

	protected int direction;
	protected int x;
	protected int y;
	protected FactoryType type;
	protected boolean mirrored;
	protected LinkedList<Item> items;
	protected Recipe selectedRecipe;
	protected boolean isInput;

	protected transient boolean canSelectRecipe;

	protected transient TileMap tilemap;

	protected transient int tilecount;
	protected transient int framecount;

	protected transient int contentSize;
	protected transient int maxSize;
	protected transient HashMap<ItemType, Integer> currentIngredients;

	protected transient Building output;
	protected transient int[][] inputPositions;
	protected transient int[] outputPosition;

	protected transient static int ticks = 0;

	protected transient List<Recipe> recipes;

	public Building(TileMap tilemap, int x, int y, int direction, int size, int tiles, FactoryType type)
	{
		this.tilemap = tilemap;
		this.type = type;

		this.x = x;
		this.y = y;
		this.direction = direction;
		this.tilecount = tiles;

		this.contentSize = 0;
		this.maxSize = size;
		this.items = new LinkedList<Item>();
		this.currentIngredients = new HashMap<ItemType, Integer>();
		this.recipes = null;
		this.canSelectRecipe = false;
		this.isInput = false;
		this.mirrored = false;
	}

	public LinkedList<Item> getItems()
	{
		return items;
	}

	public void setItems(List<Item> items)
	{
		if (items != null)
		{
			this.items = new LinkedList<Item>();

			for (Item item : items)
				addItem(item);
		}
	}

	public boolean canSelectRecipe()
	{
		return this.canSelectRecipe;
	}

	public void render(SpriteBatch batch, int dx, int dy)
	{
		for (int i = 0; i < tilecount; i++)
		{
			int tx = (direction % 2 == 0) ? ((direction == 0) ? x + i : x - i) : x;
			int ty = (direction % 2 != 0) ? ((direction == 1) ? y + i : y - i) : y;

			AnimationType animationType = AnimationType.NONE;
			if (type == FactoryType.CONVEYOR)
			{
				int inputDirection = inputPositions[0][2];
				int outputDirection = direction;

				if (outputDirection == (inputDirection + 2) % 4)
					animationType = AnimationType.STRAIGHT;
				else if (outputDirection == (inputDirection + 1) % 4)
					animationType = AnimationType.RIGHT;
				else if (outputDirection == (inputDirection + 3) % 4)
					animationType = AnimationType.LEFT;
			}
			else if (type == FactoryType.TUNNEL)
			{
				animationType = isInput ? AnimationType.IN : AnimationType.OUT;
			}

			Texture texture = type.getTexture(type, mirrored, animationType, i);
			TextureRegion textureRegion = new TextureRegion(texture);
			batch.draw(textureRegion, tx * Chunk.TILESIZE, ty * Chunk.TILESIZE, (float) Chunk.TILESIZE / 2.f, (float) Chunk.TILESIZE / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
		}
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getDirection()
	{
		return direction;
	}

	public boolean getMirrored()

	{
		return mirrored;
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
		{
			// don't tell predecessor that items can be transfered if the following conveyor isn't at least halfway done transfering to prevent the bunching
			// animation bug
			if (output != null && output.getItems() != null && output.getItems().peek() != null && output.getItems().peek().ticksSpent < Math.ceil(this.type.transferTimeout() / 2.f))
				return true;

			return contentSize >= maxSize;
		}
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
				System.out.println(output.type + " - " + output.currentIngredients.get(itemToTransfer.type));
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
			Item product = new Item();
			product.type = recipe.getProduct();
			if (output.isFull(product))
				return;

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
				checkRecipe(recipe);
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
			if (recipe.getProduct() == type)
				setRecipe(recipe);
	}

	public Recipe activeRecipe()
	{
		return this.selectedRecipe;
	}

	public void update()
	{
		// transfer item if it is the right time
		if (type != null)
			if (type.getTransferTicks() == type.getTransferTimeout())
				transferItem();

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
