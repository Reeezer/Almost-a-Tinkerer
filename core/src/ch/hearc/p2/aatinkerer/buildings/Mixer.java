package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Mixer extends Building
{
	public Mixer(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Mixer/", 2, 1);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { (direction % 2 == 0) ? ((direction == 0) ? x + 1 : x - 1) : x, (direction % 2 != 0) ? ((direction == 1) ? y + 1 : y - 1) : y, (direction + 3) % 4 } };
		this.outputPositions = new int[] { x, y, direction };
	}
}
