package ch.hearc.p2.aatinkerer.buildings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Recipe;
import ch.hearc.p2.aatinkerer.TileMap;

public abstract class Building
{
	protected class Item
	{
		public ItemType type;
		public long ticksSpent;
		// necessary because otherwise an item might get teleported to the other side of a conveyor system if updates happen in the right order (also know as
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

	protected Recipe[] recipes;

	public Building(TileMap tilemap, int x, int y, int direction, int size, String spritePath, int tiles, int frames, FactoryType type)
	{
		this.tilemap = tilemap;
		this.type = type;

		this.x = x;
		this.y = y;
		this.direction = direction;

		this.tiles = new BuildingTile[tiles];
		for (int i = 0; i < tiles; i++) {
			this.tiles[i] = new BuildingTile(spritePath + String.format("%02d", i) + "/", frames, type);
		}

		this.contentSize = 0;
		this.maxSize = size;
		this.items = new LinkedList<Item>();
		this.currentIngredients = new HashMap<ItemType, Integer>();
		this.recipes = null;
	}

	public void render(SpriteBatch batch, int tileSize)
	{
		for (int i = 0; i < tiles.length; i++) {
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
		if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS) {
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
		if (contentSize++ >= maxSize)
			System.err.format("Item %s inserted despite building being full\n", item.type.toString());

		items.add(item);

		if (currentIngredients.containsKey(item.type))
			currentIngredients.put(item.type, currentIngredients.get(item.type) + 1);
		else
			currentIngredients.put(item.type, 1);

		item.ticksSpent = 0;
		item.justTransfered = true;
	}

	public void transferItem()
	{
		Item itemToTransfer = items.peek();
		// Gives an item to the building linked (output) and verify if there is a recipe for something for that
		if (output != null && !output.isFull(itemToTransfer) && contentSize > 0 && !items.peek().justTransfered) {
			if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS) {
				checkRecipes();
			}
			else {
				Item item = items.poll();
				contentSize--;
				output.addItem(item);
			}
		}
	}

	public void checkRecipes()
	{
		for (Recipe recipe : recipes) {
			// Check if there are enough items for the recipe
			boolean makeIt = true;
			Map<ItemType, Integer> ingredients = recipe.getIngredients();
			for (ItemType item : ingredients.keySet()) {
				if (!currentIngredients.containsKey(item) || currentIngredients.get(item) < ingredients.get(item)) {
					makeIt = false; // if there is not enough item for this recipe
					break;
				}
			}

			// if we have enough ingredients to make the recipe
			if (makeIt) {
				// Make the recipe by decreasing the amount of all the ingredients
				for (ItemType item : ingredients.keySet()) {
					int nb = ingredients.get(item);
					currentIngredients.put(item, currentIngredients.get(item) - nb);
					contentSize -= nb;
				}

				// Adding the item produced
				for (int i = 0; i < recipe.getAmount(); i++) {
					Item item = new Item();
					item.type = recipe.getProduct();
					output.addItem(item);
				}
			}
		}
	}

	public void update()
	{
		if (type != null) {
			if (type.getTransferTicks() == type.getTransferTimeout()) {
				transferItem();
			}
		}

		for (Item item : items) {
			if (item.ticksSpent < type.getTransferTimeout()) {
				item.ticksSpent++;
				item.justTransfered = false;
			}
		}
	}
}
