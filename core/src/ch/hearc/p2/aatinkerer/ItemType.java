package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum ItemType
{
	NONE("Item/None.png"),
	WOODLOG("Item/Wood.png"),
	COAL("Item/Coal.png"),
	IRONORE("Item/IronOre.png"),
	COPPERORE("Item/CopperOre.png"),
	OIL("Item/Oil.png"),
	WATER("Item/Water.png"),
	STONE("Item/Rock.png"),
	COTTON("Item/Cotton.png"),
	IRONPLATE("Item/IronIngot.png"),
	IRONROD("Item/IronBar.png"),
	COPPERPLATE("Item/CopperIngot.png"),
	COPPERWIRE("Item/CopperWire.png"),
	CONCRETE("Item/Concrete.png"),
	PLANK("Item/WoodPlank.png"),
	STICK("Item/WoodStick.png"),
	GLUE("Item/Glue.png"),
	FABRIC("Item/None.png"),
	GRAPHITE("Item/Graphite.png"),
	TABLE("Item/None.png"),
	CHAIR("Item/None.png"),
	DESK("Item/None.png"),
	SHELF("Item/None.png"),
	LAMP("Item/None.png"),
	BED("Item/None.png"),
	STATUE("Item/None.png"),
	COUCH("Item/None.png"),
	PILLOW("Item/None.png"),
	BOX("Item/None.png"),
	PLANT("Item/None.png"),
	PENCIL("Item/None.png"),
	WALLPAPER("Item/None.png"),
	CARPET("Item/None.png");

	private Texture texture;

	private ItemType(String texturePath)
	{
		texture = new Texture(Gdx.files.internal(texturePath));
	}

	public void render(SpriteBatch batch, int x, int y)
	{
		batch.draw(texture, x, y);
	}

	// FIXME how to implement dispose() for an enum?
}
