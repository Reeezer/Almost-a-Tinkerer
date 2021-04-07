package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum Ressource
{
	NONE("Tile/grid.png", ItemType.NONE),
	WOOD("Tile/WoodTile.png", ItemType.WOODLOG),
	STONE("Tile/RockTile.png", ItemType.STONE),
	IRON("Tile/IronTile.png", ItemType.IRONORE),
	COPPER("Tile/CopperTile.png", ItemType.COPPERORE),
	OIL("Tile/OilTile.png", ItemType.OIL),
	WATER("Tile/WaterTile.png", ItemType.WATER),
	COTTON("Tile/CottonTile.png", ItemType.COTTON);

	private Texture texture;
	private ItemType itemType;

	private Ressource(String texturePath, ItemType item)
	{
		texture = new Texture(Gdx.files.internal(texturePath));
		itemType = item;
	}

	public void render(SpriteBatch batch, int x, int y)
	{
		batch.draw(texture, x, y);
	}
	
	public ItemType getExtractedItem()
	{
		return itemType;
	}

	// FIXME how to implement dispose() for an enum?
}
