package ch.hearc.p2.aatinkerer;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileMap
{
	public static final int TILESIZE = 32;

	private int width, height;
	private Ressources[][] map;
	// private Buildings[][] conveyers;
	// private Buildings[][] factories;

	Random random;

	public TileMap(int w, int h)
	{
		random = new Random();

		width = w;
		height = h;

		map = new Ressources[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = Ressources.NONE;
			}
		}

		for (int i = 0; i < 500; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int life = random.nextInt(5) + 2;
			Ressources ressource = Ressources.values()[(random.nextInt(Ressources.values().length) - 1) + 1];

			generate(ressource, life, x, y);
		}
	}

	public void generate(Ressources ressource, int life, int x, int y)
	{
		// don't spawn if life below 0
		if (life < 0)
			return;

		// check bounds
		if (y < 0 || x < 0 || y >= height || x >= width)
			return;

		// only spawn if there's nothing
		if (map[x][y] != Ressources.NONE)
			return;

		map[x][y] = ressource;

		// north
		if (random.nextDouble() < 0.5)
			generate(ressource, life - 1, x, y - 1);
		// south
		if (random.nextDouble() < 0.5)
			generate(ressource, life - 1, x, y + 1);
		// east
		if (random.nextDouble() < 0.5)
			generate(ressource, life - 1, x + 1, y);
		// west
		if (random.nextDouble() < 0.5)
			generate(ressource, life - 1, x - 1, y);
	}

	public void render(SpriteBatch batch)
	{
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				batch.draw(map[i][j].getTexture(), i * TileMap.TILESIZE, j * TileMap.TILESIZE);
			}
		}
	}

	public void dispose()
	{

	}
}
