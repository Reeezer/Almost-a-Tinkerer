package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Mixer extends Building
{
	public Mixer(TileMap tilemap, int x, int y, int direction, int x2, int y2)
	{
		super(tilemap, x, y, direction, 1, "Tile/Mixer/", 2, 6);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x2, y2, (direction + 3) % 4 } };
		this.outputPositions = new int[] { x, y, direction };
	}
}
