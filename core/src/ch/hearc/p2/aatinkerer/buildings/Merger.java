package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Merger extends Building
{
	public Merger(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Merger/", 1, 1, FactoryType.MERGER);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 1) % 4 } };
		this.outputPosition = new int[] { x, y, direction };
	}
}
