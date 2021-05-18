package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Assembler extends Building
{
	public Assembler(TileMap tilemap, int x, int y, int direction, int x2, int y2, int x3, int y3)
	{
		super(tilemap, x, y, direction, 1, "Tile/Assembler/", 3, 9, FactoryType.ASSEMBLER);
		this.inputPositions = new int[][] { { x, y, (direction + 1) % 4 }, { x2, y2, (direction + 1) % 4 }, { x3, y3, (direction + 1) % 4 } };
		this.outputPosition = new int[] { x2, y2, (direction + 3) % 4 };
	}
}
