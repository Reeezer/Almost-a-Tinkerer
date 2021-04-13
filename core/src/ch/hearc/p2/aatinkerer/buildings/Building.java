package ch.hearc.p2.aatinkerer.buildings;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.TileMap;

public abstract class Building
{
	protected TileMap tilemap;
	protected Texture texture;

	protected int direction;
	protected int x;
	protected int y;

	protected int contentSize;
	protected int maxSize;
	protected Queue<ItemType> items;

	protected Building output;
	protected int[][] inputPositions;
	
	protected int transferTimeout;
	protected int ticks;

	public Building(TileMap tilemap, int x, int y, int direction, int size, String spritePath)
	{
		this.tilemap = tilemap;

		this.x = x;
		this.y = y;
		this.direction = direction;
		this.transferTimeout = 50;
		this.ticks = 0;

		this.texture = new Texture(Gdx.files.internal(spritePath));

		this.contentSize = 0;
		this.maxSize = size;
		this.items = new LinkedList<ItemType>();
	}

	public void render(SpriteBatch batch, int tileSize)
	{
		// required to be able to rotate the texture
		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) texture.getWidth() / 2.f,
		        (float) texture.getHeight() / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f,
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

	public void addItem(ItemType item)
	{
		if (contentSize++ >= maxSize)
			System.err.format("Item %s inserted despite building being full\n", item.toString());
		
		items.add(item);
	}

	public void transferItem()
	{
		if (output != null && !output.isFull() && contentSize > 0) {
			ItemType item = items.poll();
			contentSize--;
			
			System.out.println("Item transfered " + item);
			output.addItem(item);
		}
	}

	public void update()
	{
		if (ticks++ >= transferTimeout) {
			transferItem();
			ticks = 0;
		}
		
	}
}
