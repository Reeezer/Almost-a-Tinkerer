package ch.hearc.p2.aatinkerer.world;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.data.Ressource;

public class Chunk
{
	public static final int TILESIZE = 32;
	public static final int CHUNKSIZE = 64;
	
	private Ressource[][] map;
	private Building[][] conveyors;
	private Building[][] factories;
	
	Random random;
	
	public Chunk(Random random)
	{
		this.random = random;
		
		// - initialise the map to have no resources
		// - create an empty table of conveyors
		map = new Ressource[CHUNKSIZE][CHUNKSIZE];
		conveyors = new Building[CHUNKSIZE][CHUNKSIZE];
		factories = new Building[CHUNKSIZE][CHUNKSIZE];
		
		for (int i = 0; i < CHUNKSIZE; i++)
			for (int j = 0; j < CHUNKSIZE; j++)
				map[i][j] = Ressource.NONE; // FIXME plutot que de mettre à none, remplir avec les tiles sauvegardées des chunks autour AVANT
		

		// - generate the map by generating seeds and growing them
		// - attempt to spawn around 1 seed per x tiles (actual numbers are lower than
		// this due to collisions)
		final int seeds = (CHUNKSIZE * CHUNKSIZE) / 100;
		final int max_life = 10; // + 2
		for (int i = 0; i < seeds; i++)
		{
			int x = random.nextInt(CHUNKSIZE);
			int y = random.nextInt(CHUNKSIZE);
			int life = random.nextInt(max_life) + 2;
			// choose a random resource to spawn excluding the first value which is NONE
			Ressource ressource = Ressource.values()[(random.nextInt(Ressource.values().length) - 1) + 1];

			// make it so seeds cannot spawn in a way that will make them reach the center (= the hub) so it stays clear
			if (Math.abs(x - (CHUNKSIZE / 2)) > (life + 3) || Math.abs(y - (CHUNKSIZE / 2)) > (life + 3))
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
		if (!tileExists(x, y)) // FIXME store for generation in neighbouring chunks
			return;

		// only spawn if there's nothing
		if (map[x][y] != Ressource.NONE)
			return;

		map[x][y] = ressource;

		// attempt to spawn more resources around
		final float spawn_probability = 0.6f;
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
	
	private boolean tileExists(int x, int y)
	{
		return x >= 0 && y >= 0 && x < CHUNKSIZE && y < CHUNKSIZE;
	}
	
	public void render(SpriteBatch batch)
	{
		// map
		for (int i = 0; i < CHUNKSIZE; i++)
		{
			for (int j = 0; j < CHUNKSIZE; j++)
			{
				map[i][j].render(batch, i * TILESIZE, j * TILESIZE);
			}
		}
	}
}
