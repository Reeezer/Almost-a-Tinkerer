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
	protected ItemType[] tabItem;

	protected Building output;
	protected int[][] tabInputsPosition;

	public Building(TileMap tilemap, int x, int y, int direction, int size, String spritePath)
	{
		this.tilemap = tilemap;

		this.x = x;
		this.y = y;
		this.direction = direction;

		this.texture = new Texture(Gdx.files.internal(spritePath));

		this.contentSize = 0;
		this.maxSize = size;
		this.tabItem = new ItemType[size];
		for (int i = 0; i < size; i++)
			tabItem[i] = ItemType.NONE;

		// Transferring every 3s
		Timer.Task transferTimer = new Timer.Task()
		{
			@Override
			public void run()
			{
				transferItem();
			}
		};
		Timer.schedule(transferTimer, 3f, 3f);
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
		return tabInputsPosition;
	}

	public void update()
	{
		output = tilemap.getNeighbourBuilding(x, y, direction);
		System.out.println("Output (" + x + ", " + y + ") : " + output);
	}

	public boolean canReceiveItem()
	{
		return contentSize < maxSize;
	}

	public void addItem(ItemType item)
	{
		tabItem[contentSize++] = item;
	}

	public void transferItem()
	{
		if (output != null && output.canReceiveItem() && contentSize > 0) {
			System.out.println("Item transfered " + tabItem[contentSize - 1]);

			output.addItem(tabItem[contentSize - 1]);
			tabItem[contentSize - 1] = ItemType.NONE;
			contentSize--;
		}
	}

}
