package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum ItemType
{
	NONE("Item/None.png", 0, "Nothing"),
	WOODLOG("Item/Wood.png", 10, "Wood Log"),
	COAL("Item/Coal.png", 10, "Coal"),
	IRONORE("Item/IronOre.png", 30, "Iron Ore"),
	COPPERORE("Item/CopperOre.png", 50, "Copper Ore"),
	OIL("Item/Oil.png", 100, "Oil"),
	WATER("Item/Water.png", 5, "Water"),
	STONE("Item/Rock.png", 2, "Stone"),
	COTTON("Item/Cotton.png", 30, "Cotton"),
	IRONPLATE("Item/IronIngot.png", 60, "Iron Plate"),
	IRONROD("Item/IronBar.png", 80, "Iron Rod"),
	COPPERPLATE("Item/CopperIngot.png", 100, "Copper Plate"),
	COPPERWIRE("Item/CopperWire.png", 120, "Copper Wire"),
	CONCRETE("Item/Concrete.png", 20, "Concrete"),
	PLANK("Item/WoodPlank.png", 15, "Plank"),
	STICK("Item/WoodStick.png", 20, "Stick"),
	GLUE("Item/Glue.png", 40, "Glue"),
	FABRIC("Item/Fabric.png", 50, "Fabric"),
	GRAPHITE("Item/Graphite.png", 30, "Graphite"),
	TABLE("Item/Table.png", 200, "Platta med 4 ben"),
	CHAIR("Item/Chair.png", 150, "Mysiga grejer"),
	DESK("Item/Desk.png", 250, "Arbetsplatta"),
	SHELF("Item/Shelf.png", 200, "Bokhallare"),
	LAMP("Item/Lamp.png", 200, "Morkhetsborttagare"),
	BED("Item/Bed.png", 300, "Mansklig laddare"),
	STATUE("Item/Statue.png", 600, "Tvivelaktig konst"),
	COUCH("Item/Couch.png", 500, "Personinnehavare"),
	PILLOW("Item/Pillow.png", 100, "Fluffig sak"),
	BOX("Item/Crate.png", 90, "Svart hal"),
	PLANT("Item/Plant.png", 200, "CO2-absorberare"),
	PENCIL("Item/Pencil.png", 100, "Saker att stjala"),
	WALLPAPER("Item/Wallpaper.png", 200, "Det ar battre an ingenting"),
	CARPET("Item/Carpet.png", 200, "Fotskydd");

	private Texture texture;
	private int value; // how much does it sell for?
	private String fullname;

	private ItemType(String texturePath, int value, String fullname)
	{
		this.texture = new Texture(Gdx.files.internal(texturePath));
		this.value = value;
		this.fullname = fullname;
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

	public String fullname()
	{
		return this.fullname;
	}
}
