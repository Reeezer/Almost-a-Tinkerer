package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.TileType;
import ch.hearc.p2.aatinkerer.world.Chunk;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Conveyor extends Building
{
	public final TileType tiletype = TileType.CONVEYOR;
	
	private int inputDirection;
	private int outputDirection;
	
	public Conveyor(TileMap tilemap, int x, int y, int[][] inputOutputPosition)
	{
		super(tilemap, x, y, inputOutputPosition[1][2], 1, getSpritePath(inputOutputPosition[1][2], inputOutputPosition[0][2]), 1, 8, FactoryType.CONVEYOR);
		
		outputDirection = inputOutputPosition[0][2];
		inputDirection = inputOutputPosition[0][2];
		
		this.inputPositions = new int[][] { inputOutputPosition[0] };
		this.outputPosition = inputOutputPosition[1];
	}

	public void renderItems(SpriteBatch batch, int dx, int dy)
	{
		int index = -1;
		for (Item item : items)
		{
			// move item on conveyor depends on time spent on it
			index++;
			int xOrientation = (direction == 2 || direction == 0) ? ((direction == 2) ? -1 : 1) : 0;
			int yOrientation = (direction == 3 || direction == 1) ? ((direction == 3) ? -1 : 1) : 0;

			float position = (float) item.ticksSpent / (float) type.getTransferTimeout();
			if (index > 0) // FIXME item teleports if there are two per conveyor or more
				position = position / (float) maxSize;

			int xPixPosition = ((x * Chunk.TILESIZE) + (int) (position * (float) Chunk.TILESIZE * xOrientation) /*- (tileSize * xOrientation) / 2*/);
			int yPixPosition = ((y * Chunk.TILESIZE) + (int) (position * (float) Chunk.TILESIZE * yOrientation) /*- (tileSize * yOrientation) / 2*/);

			item.type.render(batch, xPixPosition, yPixPosition);
		}
	}

	private static String getSpritePath(int outputDirection, int inputDirection)
	{
		if (outputDirection == (inputDirection + 2) % 4)
			return "Tile/Conveyor/";
		else if (outputDirection == (inputDirection + 1) % 4)
			return "Tile/ConveyorRight/";
		else if (outputDirection == (inputDirection + 3) % 4)
			return "Tile/ConveyorLeft/";
		return null; // shouldn't happen, if it happens, then crash, instead check that on return by
						// caller
	}

	public int getInputDirection()
	{
		return inputDirection;
	}

	public int getOutputDirection()
	{
		return outputDirection;
	}
}
