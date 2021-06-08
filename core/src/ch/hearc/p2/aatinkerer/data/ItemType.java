package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum ItemType
{
	NONE("Item/None.png", 0),
	WOODLOG("Item/Wood.png", 10),
	COAL("Item/Coal.png", 10),
	IRONORE("Item/IronOre.png", 30),
	COPPERORE("Item/CopperOre.png", 50),
	OIL("Item/Oil.png", 100),
	WATER("Item/Water.png", 5),
	STONE("Item/Rock.png", 2),
	COTTON("Item/Cotton.png", 30),
	IRONPLATE("Item/IronIngot.png", 60),
	IRONROD("Item/IronBar.png", 80),
	COPPERPLATE("Item/CopperIngot.png", 100),
	COPPERWIRE("Item/CopperWire.png", 120),
	CONCRETE("Item/Concrete.png", 20),
	PLANK("Item/WoodPlank.png", 15),
	STICK("Item/WoodStick.png", 20),
	GLUE("Item/Glue.png", 40),
	FABRIC("Item/Fabric.png", 50),
	GRAPHITE("Item/Graphite.png", 30),
	TABLE("Item/Table.png", 200),
	CHAIR("Item/Chair.png", 150),
	DESK("Item/Desk.png", 250),
	SHELF("Item/Shelf.png", 200),
	LAMP("Item/Lamp.png", 200),
	BED("Item/Bed.png", 300),
	STATUE("Item/Statue.png", 600),
	COUCH("Item/Couch.png", 500),
	PILLOW("Item/Pillow.png", 100),
	BOX("Item/Crate.png", 90),
	PLANT("Item/Plant.png", 200),
	PENCIL("Item/Pencil.png", 100),
	WALLPAPER("Item/None.png", 200),
	CARPET("Item/None.png", 200);

	private Texture texture;
	private int value; // how much does it sell for?

	private ItemType(String texturePath, int value)
	{
		this.texture = new Texture(Gdx.files.internal(texturePath));
		this.value = value;
	}

	public void render(SpriteBatch batch, int x, int y)
	{
		batch.draw(texture, x, y);
	}

	public int value()
	{
		return this.value;
	}

	public static void dispose()
	{
		for (ItemType type : ItemType.values())
			type.texture.dispose();
	}
}
