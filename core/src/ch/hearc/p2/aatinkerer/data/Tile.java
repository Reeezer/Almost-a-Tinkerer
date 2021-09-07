package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Tile
{
	public final TileType tiletype = TileType.NONE; // each type should redefine it

	public void render(SpriteBatch batch, int x, int y);
}
