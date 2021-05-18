package ch.hearc.p2.aatinkerer.buildings;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.ItemType;
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

	protected int direction;
	protected int x;
	protected int y;

	protected int contentSize;
	protected int maxSize;
	protected Queue<Item> items;

	protected Building output;
	protected int[][] inputPositions;
	protected int[] outputPosition;

	protected static int transferTimeout = 50;
	protected static int ticks = 0;

	public Building(TileMap tilemap, int x, int y, int direction, int size, String spritePath, int tiles, int frames, FactoryType type)
	{
		this.tilemap = tilemap;

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

	public boolean isFull()
	{
		return contentSize >= maxSize;
	}

	public void addItem(Item item)
	{
		if (contentSize++ >= maxSize)
			System.err.format("Item %s inserted despite building being full\n", item.type.toString());

		items.add(item);
		item.ticksSpent = 0;
		item.justTransfered = true;
	}

	public void transferItem()
	{
		if (output != null && !output.isFull() && contentSize > 0 && !items.peek().justTransfered) {
			Item item = items.poll();
			contentSize--;

			System.out.println("Item transfered " + item.type);
			output.addItem(item);
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
