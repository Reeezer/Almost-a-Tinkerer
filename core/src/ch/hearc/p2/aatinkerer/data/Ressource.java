package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum Ressource
{
	NONE("Tile/Grid.png", ItemType.NONE),
	COAL("Tile/CoalTile.png", ItemType.COAL),
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
}
