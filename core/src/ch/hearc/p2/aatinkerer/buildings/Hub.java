package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.GameManager;
import ch.hearc.p2.aatinkerer.TileMap;

public class Hub extends Building
{
	public Hub(TileMap tilemap, int x, int y)
	{
		super(tilemap, x, y, 0, Integer.MAX_VALUE, "Tile/Hub/", 9, 1, null);
		this.inputPositions = new int[][] { { x + 1, y + 1, 0 }, { x + 1, y, 0 }, { x + 1, y - 1, 0 }, { x - 1, y + 1, 1 }, { x, y + 1, 1 }, { x + 1, y + 1, 1 }, { x - 1, y + 1, 2 }, { x - 1, y, 2 }, { x - 1, y - 1, 2 }, { x - 1, y - 1, 3 }, { x, y - 1, 3 }, { x + 1, y - 1, 3 } };
		this.outputPosition = null;
	}

	@Override
	public void render(SpriteBatch batch, int tileSize)
	{
		int z = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				BuildingTile tile = tiles[z++];
				int tx = x + i;
				int ty = y + j;

				tile.render(batch, tileSize, direction, tx, ty);
			}
		}
	}

	@Override
	public void addItem(Item item)
	{
		GameManager.getInstance().itemDelivered(item.type);
	}
}
