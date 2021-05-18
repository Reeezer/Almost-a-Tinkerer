package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Furnace extends Building
{
	public Furnace(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Furnace/", 1, 8, FactoryType.FURNACE);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 3) % 4 } };
		this.outputPositions = new int[] { x, y, direction };
	}
}
