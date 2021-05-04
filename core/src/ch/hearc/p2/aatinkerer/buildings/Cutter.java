package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Cutter extends Building
{
	public Cutter(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Cutter/", 1, 4);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPositions = new int[] { x, y, direction };
	}
}