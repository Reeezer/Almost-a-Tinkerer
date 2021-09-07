package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.world.Chunk;

public enum Ressource implements Tile
{
	NONE("tile/grid.png", ItemType.NONE),
	COAL("tile/coaltile.png", ItemType.COAL),
	WOOD("tile/woodtile.png", ItemType.WOODLOG),
	STONE("tile/rocktile.png", ItemType.STONE),
	IRON("tile/irontile.png", ItemType.IRONORE),
	COPPER("tile/coppertile.png", ItemType.COPPERORE),
	OIL("tile/oiltile.png", ItemType.OIL),
	WATER("tile/watertile.png", ItemType.WATER),
	COTTON("tile/cottontile.png", ItemType.COTTON);

	private final TileType tiletype = TileType.RESSOURCE;

	private Texture texture;
	private ItemType itemType;

	private Ressource(String texturePath, ItemType item)
	{
		texture = new Texture(Gdx.files.internal(texturePath));
		itemType = item;
	}

	public Texture texture()
	{
		return texture;
	}

	public ItemType getExtractedItem()
	{
		return itemType;
	}

	public static void dispose()
	{
		for (Ressource ressource : Ressource.values())
			ressource.texture.dispose();
	}

	@Override
	public void render(SpriteBatch batch, int x, int y)
	{
		batch.draw(texture, x * Chunk.TILESIZE, y * Chunk.TILESIZE);
	}
}
