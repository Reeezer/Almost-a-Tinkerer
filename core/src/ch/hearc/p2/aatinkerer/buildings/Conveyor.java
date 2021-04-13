package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.TileMap;

public class Conveyor extends Building
{
	public Conveyor(TileMap tilemap, int x, int y, int direction)
	{
		super(tilemap, x, y, direction, 2, "Tile/Conveyor.png");
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
	}
	
	public void renderItems(SpriteBatch batch, int tileSize) {
		int index = -1;
		for (Item item : items) {
			index++;
			int xOrientation = (direction == 2 || direction == 0) ? ((direction == 2) ? -1 : 1) : 0; 
			int yOrientation = (direction == 3 || direction == 1) ? ((direction == 3) ? -1 : 1) : 0; 
			
			float position = (float) item.ticksSpent / (float) transferTimeout;
			if (index > 0) // FIXME Teleport himself when move after being stopped
				position = position / (float) maxSize;
			
			int xPixPosition = ((x * tileSize) + (int) (position * (float)tileSize * xOrientation) - (tileSize * xOrientation) / 2);
			int yPixPosition = ((y * tileSize) + (int) (position * (float)tileSize * yOrientation) - (tileSize * yOrientation) / 2);
			
			item.type.render(batch, xPixPosition + 4, yPixPosition + 4); // FIXME Draw sprite 32x32 instead of 24x24 to correct the +4
		}
	}
 }
