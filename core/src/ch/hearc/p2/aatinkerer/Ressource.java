package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum Ressource
{
	NONE("Tile/grid.png"),
	WOOD("Tile/WoodTile.png"),
	STONE("Tile/RockTile.png"),
	IRON("Tile/IronTile.png"),
	COPPER("Tile/CopperTile.png"),
	OIL("Tile/OilTile.png"),
	WATER("Tile/WaterTile.png"),
	COTTON("Tile/CottonTile.png");

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
