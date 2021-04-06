package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum Ressource
{
	NONE("grid.png"),
	WOOD("WoodTile.png"),
	STONE("RockTile.png"),
	IRON("IronTile.png"),
	COPPER("CopperTile.png"),
	OIL("OilTile.png"),
	WATER("WaterTile.png"),
	COTTON("CottonTile.png");

	private Texture texture;

	private Ressource(String texturePath)
	{
		texture = new Texture(Gdx.files.internal(texturePath));
	}

	public void render(SpriteBatch batch, int x, int y)
	{
		batch.draw(texture, x, y);
	}

	// FIXME how to implement dispose() for an enum?
}
