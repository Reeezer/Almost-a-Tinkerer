package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Tunnel extends Building
{
	private static final int DISTANCE = 10;

	public Tunnel(TileMap tilemap, int x, int y, int direction, boolean isInput)
	{
		super(tilemap, x, y, (isInput ? direction : (direction + 2) % 4), 2, 1, FactoryType.TUNNEL);
		// If the tunnel is the input one, it has inputs but no outputs, and vice versa for the output
		this.inputPositions = isInput ? new int[][] { { x, y, (this.direction + 2) % 4 } } : null;
		this.outputPosition = isInput ? null : new int[] { x, y, (this.direction + 2) % 4 };
		this.isInput = isInput;

		// If it is the output, we do want to connect the input tunnel to this one
		if (!isInput)
			tilemap.findInputTunnel(this, x, y, this.direction, DISTANCE);
	}

	public void setOutputTunnel(Tunnel tunnel)
	{
		output = tunnel;
	}
	
	public void reloadConnection()
	{
		if (!isInput)
			tilemap.findInputTunnel(this, x, y, this.direction, DISTANCE);
	}

	public boolean isInput()
	{
		return isInput;
	}
}
