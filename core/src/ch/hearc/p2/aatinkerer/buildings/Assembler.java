package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Assembler extends Building
{
	public Assembler(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 1, "Tile/Assembler/", 3, 9);
		this.inputPositions = new int[][] { { x, y, (direction + 1) % 4 }, { (direction % 2 == 0) ? ((direction == 0) ? x + 1 : x - 1) : x, (direction % 2 != 0) ? ((direction == 1) ? y + 1 : y - 1) : y, (direction + 1) % 4 }, { (direction % 2 == 0) ? ((direction == 0) ? x + 2 : x - 2) : x, (direction % 2 != 0) ? ((direction == 1) ? y + 2 : y - 2) : y, (direction + 1) % 4 } };
		this.outputPositions = new int[] { (direction % 2 == 0) ? ((direction == 0) ? x + 1 : x - 1) : x, (direction % 2 != 0) ? ((direction == 1) ? y + 1 : y - 1) : y, (direction + 3) % 4 };
	}
}
