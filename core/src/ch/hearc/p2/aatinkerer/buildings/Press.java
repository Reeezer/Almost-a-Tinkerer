package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Press extends Building
{
	public Press(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Press/", 1, 6);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPositions = new int[] { x, y, direction };
	}
}