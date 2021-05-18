package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Splitter extends Building
{
	public Splitter(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Splitter/", 1, 1, FactoryType.SPLITTER);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPositions = new int[] { x, y, direction }; // FIXME change to multiple outputs
	}
}