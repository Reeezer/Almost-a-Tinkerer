package ch.hearc.p2.aatinkerer;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Conveyor;

public class TileMap
{
	public static final int TILESIZE = 32;

	private int width, height;
	private Ressource[][] map;
	private Building[][] conveyors;
	private Building[][] factories;

	Random random;

	public TileMap(int w, int h)
	{
		random = new Random();

		width = w;
		height = h;

		// - initialise the map to have no resources
		// - create an empty table of conveyors
		map = new Ressource[width][height];
		conveyors = new Building[width][height];
		factories = new Building[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = Ressource.NONE;
			}
		}
		
		// - generate the map by generating seeds and growing them
		// - attempt to spawn around 1 seed per x tiles (actual numbers are lower than
		// this due to collisions)
		final int seeds = (width * height) / 500;
		final int max_life = 10; // + 2
		for (int i = 0; i < seeds; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int life = random.nextInt(max_life) + 2;
			// choose a random resource to spawn excluding the first value which is NONE
			Ressource ressource = Ressource.values()[(random.nextInt(Ressource.values().length) - 1) + 1];

			generate(ressource, life, x, y);
		}
	}

	// recursively generate a resource patch from the specified coordinates
	public void generate(Ressource ressource, int life, int x, int y)
	{
		// don't spawn if life below 0
		if (life < 0)
			return;

		// check bounds
		if (y < 0 || x < 0 || y >= height || x >= width)
			return;

		// only spawn if there's nothing
		if (map[x][y] != Ressource.NONE)
			return;

		map[x][y] = ressource;

		// attempt to spawn more resources around
		final float spawn_probability = 0.75f;
		// north
		if (random.nextFloat() < spawn_probability)
			generate(ressource, life - 1, x, y - 1);
		// south
		if (random.nextFloat() < spawn_probability)
			generate(ressource, life - 1, x, y + 1);
		// east
		if (random.nextFloat() < spawn_probability)
			generate(ressource, life - 1, x + 1, y);
		// west
		if (random.nextFloat() < spawn_probability)
			generate(ressource, life - 1, x - 1, y);
	}

	public void placeConveyor(int x, int y, int direction)
	{
		if (x < 0 || x >= conveyors.length)
			return;
		
		if (y < 0 || y >= conveyors[x].length)
			return;
		
		if (conveyors[x][y] == null)
			conveyors[x][y] = new Conveyor(direction);
	}
	
	public void render(SpriteBatch batch)
	{
		// map
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j].render(batch, i * TileMap.TILESIZE, j * TileMap.TILESIZE);
			}
		}

		// conveyors
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (conveyors[i][j] != null) {
					conveyors[i][j].render(batch, i * TileMap.TILESIZE, j * TileMap.TILESIZE);
				}
			}
		}

		// items

		// factories
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (factories[i][j] != null) {
					factories[i][j].render(batch, i * TileMap.TILESIZE, j * TileMap.TILESIZE);
				}
			}
		}
	}

	public void dispose()
	{

	}
}
