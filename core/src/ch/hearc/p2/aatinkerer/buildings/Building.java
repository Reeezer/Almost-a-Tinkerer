package ch.hearc.p2.aatinkerer.buildings;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.TileMap;

public abstract class Building
{
	protected class Item
	{
		public ItemType type;
		public long ticksSpent;
		// necessary because otherwise an item might get teleported to the other side of a conveyor system if updates happen in the right order (also know as black magic)
		public boolean justTransfered;
		
		public Item()
		{
			type = ItemType.NONE;
			ticksSpent = 0;
			justTransfered = false;
		}
	}

	protected TileMap tilemap;
	protected Texture texture;

	protected int direction;
	protected int x;
	protected int y;

	protected int contentSize;
	protected int maxSize;
	protected Queue<Item> items;

	protected Building output;
	protected int[][] inputPositions;

	protected static int transferTimeout = 50;
	protected static int ticks = 0;

	public Building(TileMap tilemap, int x, int y, int direction, int size, String spritePath)
	{
		this.tilemap = tilemap;

		this.x = x;
		this.y = y;
		this.direction = direction;

		this.texture = new Texture(Gdx.files.internal(spritePath));

		this.contentSize = 0;
		this.maxSize = size;
		this.items = new LinkedList<Item>();
	}

	public void render(SpriteBatch batch, int tileSize)
	{
		// required to be able to rotate the texture
		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) tileSize / 2.f,
		        (float) tileSize / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f,
		        (float) direction * 90.f);
	}

	public int[][] getInputs()
	{
		return inputPositions;
	}

	public void updateOutputs()
	{
		output = tilemap.getNeighbourBuilding(x, y, direction);
		System.out.println("Output (" + x + ", " + y + ") : " + output);
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
