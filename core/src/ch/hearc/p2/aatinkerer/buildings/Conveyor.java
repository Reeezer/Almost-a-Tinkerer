package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Conveyor extends Building
{
	public Conveyor(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 2, "Tile/Conveyor.png");
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
	}
}
