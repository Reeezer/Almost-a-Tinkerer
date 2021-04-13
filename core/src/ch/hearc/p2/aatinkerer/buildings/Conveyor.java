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
		for (Item item : items) {
			float position = (float) item.ticksSpent / (float) transferTimeout;
			int pixPosition = (x * tileSize) + (int) (position * (float)tileSize) - 12;
			
			item.type.render(batch, pixPosition, y * tileSize);
		}
	}
 }
