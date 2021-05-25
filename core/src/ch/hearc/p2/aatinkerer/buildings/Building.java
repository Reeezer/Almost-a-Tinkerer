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

	protected static int transferTimeout = 50;
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
		if (outputPosition != null) {
			output = tilemap.getNeighbourBuilding(outputPosition);
			System.out.println("Output (" + x + ", " + y + ") : " + output);
		}
	}

	public FactoryType getType()
	{
		return type;
	}

	public boolean isFull()
	{
		return contentSize >= maxSize;
	}

	public void addItem(Item item)
	{
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
		if (output != null)
			System.out.println(this.type + " - " + (output.isFull() ? "full - " : "empty - ") + contentSize + " * " + maxSize);
		if (output != null && !output.isFull() && contentSize > 0 && !items.peek().justTransfered) {
			if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS) {
				checkRecipes();
			}
			else {
				Item item = items.poll();
				contentSize--;

				System.out.println("Item transfered " + item.type);
				output.addItem(item);
			}
		}
	}

	public void checkRecipes()
	{
		for (Recipe recipe : recipes) {
			boolean makeIt = true;
			Map<ItemType, Integer> ingredients = recipe.getIngredients();
			System.out.println(ingredients);
			for (ItemType item : ingredients.keySet()) {
				if (!currentIngredients.containsKey(item) || currentIngredients.get(item) < ingredients.get(item)) {
					makeIt = false; // if there is not enough item for this recipe
					break;
				}
			}

			// if we have enough ingredients to make the recipe
			if (makeIt) {
				for (ItemType item : ingredients.keySet()) {
					int nb = ingredients.get(item);
					currentIngredients.put(item, currentIngredients.get(item) - nb);
					contentSize -= nb;
				}

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
		if (Building.ticks == Building.transferTimeout) {
			transferItem();
		}

		for (Item item : items) {
			if (item.ticksSpent < transferTimeout) {
				item.ticksSpent++;
				item.justTransfered = false;
			}
		}
	}

	public static void staticUpdate()
	{
		if (Building.ticks++ >= Building.transferTimeout) {
			Building.ticks = 0;
		}
	}
}
