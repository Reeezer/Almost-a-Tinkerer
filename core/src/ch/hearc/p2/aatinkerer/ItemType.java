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
	FABRIC("Item/Fabric.png"),
	GRAPHITE("Item/Graphite.png"),
	TABLE("Item/Table.png"),
	CHAIR("Item/Chair.png"),
	DESK("Item/Desk.png"),
	SHELF("Item/Shelf.png"),
	LAMP("Item/Lamp.png"),
	BED("Item/Bed.png"),
	STATUE("Item/Statue.png"),
	COUCH("Item/Couch.png"),
	PILLOW("Item/Pillow.png"),
	BOX("Item/Crate.png"),
	PLANT("Item/Plant.png"),
	PENCIL("Item/Pencil.png"),
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
