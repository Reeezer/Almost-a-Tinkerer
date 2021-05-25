package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Tunnel extends Building
{
	private boolean isInput;
	private static final int DISTANCE = 5;

	public Tunnel(TileMap tilemap, int x, int y, int direction, boolean isInput)
	{
		super(tilemap, x, y, (isInput ? direction : (direction + 2) % 4), 1, (isInput ? "Tile/TunnelIn/" : "Tile/TunnelOut/"), 1, 1, FactoryType.TUNNEL);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPosition = isInput ? null : new int[] { x, y, (direction + 2) % 4 };
		this.isInput = isInput;

		if (!isInput)
			tilemap.findInputTunnel(this, x, y, this.direction, DISTANCE);
	}

	public void setOutputTunnel(Tunnel tunnel)
	{
		output = tunnel;
	}

	public boolean isInput()
	{
		return isInput;
	}

	@Override
	public void updateOutputs()
	{
		// TODO
//		if (outputPosition != null) {
//			output = tilemap.getNeighbourBuilding(outputPosition);
//			System.out.println("Output (" + x + ", " + y + ") : " + output);
//		}
	}
}
