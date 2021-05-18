package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Trash extends Building
{
	public Trash(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Trash/", 1, 7, FactoryType.TRASH);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPositions = null;
	}
}
