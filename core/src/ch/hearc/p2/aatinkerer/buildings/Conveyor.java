package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.TileMap;

public class Conveyor extends Building
{
	public Conveyor(TileMap tilemap, int x, int y, int[][] inputOutputPosition)
	{
		super(tilemap, x, y, inputOutputPosition[1][2], 2, getSpritePath(inputOutputPosition[1][2], inputOutputPosition[0][2]), 1, 8, FactoryType.CONVEYOR);
		this.inputPositions = new int[][] { inputOutputPosition[0] };
		this.outputPositions = inputOutputPosition[1];
	}

	public void renderItems(SpriteBatch batch, int tileSize)
	{
		int index = -1;
		for (Item item : items) {
			// FIXME On corner conveyor, move items in diagonal or something else
			index++;
			int xOrientation = (direction == 2 || direction == 0) ? ((direction == 2) ? -1 : 1) : 0;
			int yOrientation = (direction == 3 || direction == 1) ? ((direction == 3) ? -1 : 1) : 0;

			float position = (float) item.ticksSpent / (float) transferTimeout;
			if (index > 0) // FIXME Teleport himself when move after being stopped
				position = position / (float) maxSize;

			int xPixPosition = ((x * tileSize) + (int) (position * (float) tileSize * xOrientation) - (tileSize * xOrientation) / 2);
			int yPixPosition = ((y * tileSize) + (int) (position * (float) tileSize * yOrientation) - (tileSize * yOrientation) / 2);

			item.type.render(batch, xPixPosition, yPixPosition); // FIXME Draw sprite 32x32 instead of 24x24														// correct the +4
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
		return null; // FIXME shouldn't happen, if it happens, then crash, instead check that on return by caller
	}
}
