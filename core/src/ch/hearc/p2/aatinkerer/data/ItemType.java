package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum ItemType
{
	NONE("item/none.png", 0, "Nothing"),
	WOODLOG("item/wood.png", 10, "Wood Log"),
	COAL("item/coal.png", 10, "Coal"),
	IRONORE("item/ironore.png", 30, "Iron Ore"),
	COPPERORE("item/copperore.png", 50, "Copper Ore"),
	OIL("item/oil.png", 100, "Oil"),
	WATER("item/water.png", 5, "Water"),
	STONE("item/rock.png", 2, "Stone"),
	COTTON("item/cotton.png", 30, "Cotton"),
	IRONPLATE("item/ironingot.png", 60, "Iron Plate"),
	IRONROD("item/ironbar.png", 80, "Iron Rod"),
	COPPERPLATE("item/copperingot.png", 100, "Copper Plate"),
	COPPERWIRE("item/copperwire.png", 120, "Copper Wire"),
	CONCRETE("item/concrete.png", 20, "Concrete"),
	PLANK("item/woodplank.png", 15, "Plank"),
	STICK("item/woodstick.png", 20, "Stick"),
	GLUE("item/glue.png", 40, "Glue"),
	FABRIC("item/fabric.png", 50, "Fabric"),
	GRAPHITE("item/graphite.png", 30, "Graphite"),
	TABLE("item/table.png", 200, "Platta med 4 ben"),
	CHAIR("item/chair.png", 150, "Mysiga grejer"),
	DESK("item/desk.png", 250, "Arbetsplatta"),
	SHELF("item/shelf.png", 200, "Bokhallare"),
	LAMP("item/lamp.png", 200, "Morkhetsborttagare"),
	BED("item/bed.png", 300, "Mansklig laddare"),
	STATUE("item/statue.png", 600, "Tvivelaktig konst"),
	COUCH("item/couch.png", 500, "Personinnehavare"),
	PILLOW("item/pillow.png", 100, "Fluffig sak"),
	BOX("item/crate.png", 90, "Svart hal"),
	PLANT("item/plant.png", 200, "CO2-absorberare"),
	PENCIL("item/pencil.png", 100, "Saker att stjala"),
	WALLPAPER("item/wallpaper.png", 200, "Det ar battre an ingenting"),
	CARPET("item/carpet.png", 200, "Fotskydd");

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
