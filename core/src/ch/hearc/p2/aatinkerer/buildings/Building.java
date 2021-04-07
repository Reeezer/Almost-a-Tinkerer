package ch.hearc.p2.aatinkerer.buildings;

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
	protected ItemType[] items;

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
		this.transferTimeout = 60;
		this.ticks = 0;

		this.texture = new Texture(Gdx.files.internal(spritePath));

		this.contentSize = 0;
		this.maxSize = size;
		this.items = new ItemType[size];
		for (int i = 0; i < size; i++)
			items[i] = ItemType.NONE;
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
		items[contentSize++] = item;
	}

	public void transferItem()
	{
		if (output != null && !output.isFull() && contentSize > 0) {
			System.out.println("Item transfered " + items[contentSize - 1]);

			output.addItem(items[contentSize - 1]);
			items[contentSize - 1] = ItemType.NONE;
			contentSize--;
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
