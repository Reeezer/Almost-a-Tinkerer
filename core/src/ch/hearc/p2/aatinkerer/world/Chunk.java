package ch.hearc.p2.aatinkerer.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Conveyor;
import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.Ressource;
import ch.hearc.p2.aatinkerer.data.Tile;
import ch.hearc.p2.aatinkerer.data.TileType;

public class Chunk
{
	public static final int TILESIZE = 32;
	public static final int CHUNKSIZE = 64;

	private Map<TileType, Tile[][]> tiles;
	private Map<Long, Ressource> cachedGenerationRessources;

	private Random random;

	private TileMap tilemap; // the tilemap that contains the chunk for easy access to surrounding chunks

	private long key;

	public Chunk(Random random, TileMap tilemap, long key)
	{
		this.random = random;
		this.tilemap = tilemap;
		this.key = key;

		// - all the tile layers
		tiles = new HashMap<TileType, Tile[][]>();
		tiles.put(TileType.RESSOURCE, new Ressource[CHUNKSIZE][CHUNKSIZE]);
		tiles.put(TileType.FACTORY, new Building[CHUNKSIZE][CHUNKSIZE]);
		tiles.put(TileType.CONVEYOR, new Building[CHUNKSIZE][CHUNKSIZE]);

		this.cachedGenerationRessources = new HashMap<Long, Ressource>();

		// - generate the map by generating seeds and growing them
		// - attempt to spawn around 1 seed per x tiles (actual numbers are lower than
		// this due to collisions)

		for (int x = 0; x < CHUNKSIZE; x++)
			for (int y = 0; y < CHUNKSIZE; y++)
				setLocalTile(TileType.RESSOURCE, x, y, Ressource.NONE);

		List<Chunk> neighbours = tilemap.getNeighbours(key);

		for (Chunk neighbour : neighbours)
		{
			int chunkX = TileMap.chunkKeyToX(this.key);
			int chunkY = TileMap.chunkKeyToY(this.key);

			int neighbourX = TileMap.chunkKeyToX(neighbour.key);
			int neighbourY = TileMap.chunkKeyToY(neighbour.key);

			System.out.format("chunk at (%d, %d) is reading cached ressources from its neighbour (%d, %d)%n", chunkX, chunkY, neighbourX, neighbourY);

			int coordsOffsetX = (neighbourX - chunkX) * CHUNKSIZE;
			int coordsOffsetY = (neighbourY - chunkY) * CHUNKSIZE;

			// since we shouldn't modify a map while it's being iterated, store all consumed ressources to be deleted later
			List<Long> toDeleteRessourceKeys = new LinkedList<Long>();

			for (Map.Entry<Long, Ressource> cachedRessource : neighbour.cachedGenerationRessources.entrySet())
			{
				long ressourceKey = cachedRessource.getKey();
				Ressource ressource = cachedRessource.getValue();

				int neighbourRessourceX = keyToX(ressourceKey);
				int neighbourRessourceY = keyToY(ressourceKey);

				int ressourceX = coordsOffsetX + neighbourRessourceX;
				int ressourceY = coordsOffsetY + neighbourRessourceY;

				if (ressourceX >= 0 && ressourceX < CHUNKSIZE && ressourceY >= 0 && ressourceY < CHUNKSIZE)
				{
					System.out.format(" - read new cached ressource %s neighbour's local coords (%d, %d) converted to chunk local (%d,%d)%n", ressource.toString(), neighbourRessourceX, neighbourRessourceY, ressourceX, ressourceY);

					setLocalTile(TileType.RESSOURCE, ressourceX, ressourceY, ressource);
					toDeleteRessourceKeys.add(ressourceKey);
				}
			}

			for (Long toDeleteKey : toDeleteRessourceKeys)
				neighbour.cachedGenerationRessources.remove(toDeleteKey);
		}

		final int seeds = (CHUNKSIZE * CHUNKSIZE) / 100;
		final int max_life = 10; // + 2

		for (int i = 0; i < seeds; i++)
		{
			int x = random.nextInt(CHUNKSIZE);
			int y = random.nextInt(CHUNKSIZE);
			int life = random.nextInt(max_life) + 2;
			// choose a random resource to spawn excluding the first value which is NONE
			Ressource ressource = Ressource.values()[random.nextInt(Ressource.values().length - 1) + 1];

			// make it so seeds cannot spawn in a way that will make them reach the center (= the hub) so it stays clear
			if (Math.abs(x - (CHUNKSIZE / 2)) > (life + 3) || Math.abs(y - (CHUNKSIZE / 2)) > (life + 3))
				generate(ressource, life, x, y);
		}

	}

	// - returns the tile based on local coordinates, also works if the coordinates are outside the chunk since
	// it first goes through the tilemap to determine what chunk the tile is in
	// - checking if the coordinates are local is necessary because redirection may not work in the constructor otherwise
	// (=> generating the chunk while it's being added to the hashmap will cause it to be unable to find itself and set its own tiles)
	public Tile getLocalTile(TileType type, int localX, int localY)
	{
		Chunk target = this;

		// if the coordinates are outside the chunk, fetch it first
		if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64)
		{
			int worldX = localX + keyToX(key) * CHUNKSIZE;
			int worldY = localY + keyToY(key) * CHUNKSIZE;

			target = tilemap.chunkAtTile(worldX, worldY);

			if (target == null)
				return null;
		}

		return target.tiles.get(type)[localX][localY];
	}

	// - sets the tile, also works with tiles outside the chunk itself
	// - see getLocalTile for more info
	public void setLocalTile(TileType type, int localX, int localY, Tile value)
	{
		Chunk target = this;

		// if the coordinates are outside the chunk, fetch it first
		if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64)
		{
			int worldX = localX + keyToX(key) * CHUNKSIZE;
			int worldY = localY + keyToY(key) * CHUNKSIZE;

			target = tilemap.chunkAtTile(worldX, worldY);

			if (target == null)
				return;
		}

		target.tiles.get(type)[localX][localY] = value;
	}

	private long coordsToKey(int x, int y)
	{
		return (((long) x) << 32) | (y & 0xffffffffL);
	}

	private int keyToX(long key)
	{
		return (int) (key >> 32);
	}

	private int keyToY(long key)
	{
		return (int) key;
	}

	// recursively generate a resource patch from the specified coordinates
	public void generate(Ressource ressource, int life, int x, int y)
	{
		// don't spawn if life below 0
		if (life < 0)
			return;

		// if the tile doesn't exist first try to set it in the neighbouring chunk, if the chunk doesn't exist, store it for later
		if (!tileExists(x, y))
		{
			int chunkX = TileMap.chunkKeyToX(this.key);
			int chunkY = TileMap.chunkKeyToY(this.key);

			int targetChunkX = chunkX + ((x + CHUNKSIZE) / CHUNKSIZE) - 1;
			int targetChunkY = chunkY + ((y + CHUNKSIZE) / CHUNKSIZE) - 1;

			long targetChunkKey = TileMap.chunkCoordsToKey(targetChunkX, targetChunkY);

			// if neighbouring chunk already exists
			if (tilemap.chunkExists(targetChunkKey))
			{
				Chunk targetChunk = tilemap.getChunk(targetChunkKey);

				int targetChunkTileX = (x + CHUNKSIZE) % CHUNKSIZE;
				int targetChunkTileY = (y + CHUNKSIZE) % CHUNKSIZE;

				if (targetChunk.getLocalTile(TileType.RESSOURCE, targetChunkTileX, targetChunkTileY) == Ressource.NONE)
				{
					System.out.format("adding outbound ressource %s at (%d, %d) from chunk (%d, %d) to already existing chunk at (%d, %d) at local coordinates (%d, %d)%n", ressource, x, y, chunkX, chunkY, targetChunkX, targetChunkY, targetChunkTileX, targetChunkTileY);
					targetChunk.setLocalTile(TileType.RESSOURCE, targetChunkTileX, targetChunkTileY, ressource);
				}
			}

			// if the tile is outside of the chunk and that location doesn't contain anything already cached
			else if (!cachedGenerationRessources.containsKey(coordsToKey(x, y)))
				cachedGenerationRessources.put(coordsToKey(x, y), ressource);
		}

		// only spawn if there's nothing
		if (tileExists(x, y) && getLocalTile(TileType.RESSOURCE, x, y) != Ressource.NONE)
			return;

		if (tileExists(x, y))
			setLocalTile(TileType.RESSOURCE, x, y, ressource);

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

	public void render(SpriteBatch batch, int x, int y)
	{
		// ressources
		for (int i = 0; i < CHUNKSIZE; i++)
		{
			for (int j = 0; j < CHUNKSIZE; j++)
			{
				Ressource ressource = (Ressource) getLocalTile(TileType.RESSOURCE, i, j);

				if (ressource != null)
					batch.draw(ressource.texture(), x + i * TILESIZE, y + j * TILESIZE);
				else
					System.err.format("critical: chunk render attempted to render a null ressource at (%d,%d) in chunk (%d,%d)%n", i, j, keyToX(key), keyToY(key));
			}
		}

		// conveyors
		for (int i = 0; i < CHUNKSIZE; i++)
		{
			for (int j = 0; j < CHUNKSIZE; j++)
			{
				Conveyor conveyor = (Conveyor) getLocalTile(TileType.CONVEYOR, i, j);

				if (conveyor != null)
					conveyor.render(batch, TILESIZE);
			}
		}

		// items
		for (int i = 0; i < CHUNKSIZE; i++)
		{
			for (int j = 0; j < CHUNKSIZE; j++)
			{
				Conveyor conveyor = (Conveyor) getLocalTile(TileType.CONVEYOR, i, j);

				if (conveyor != null)
					conveyor.renderItems(batch, TILESIZE);
			}
		}

		// factories
		for (int i = 0; i < CHUNKSIZE; i++)
		{
			for (int j = 0; j < CHUNKSIZE; j++)
			{
				Building building = (Building) getLocalTile(TileType.FACTORY, i, j);

				if (building != null)
					building.render(batch, TILESIZE);
			}
		}
	}

	public void update()
	{

		// update transfer ticks
		for (FactoryType type : FactoryType.values())
		{
			type.transferTicksIncrease();
			if (type.getTransferTicks() > type.getTransferTimeout())
			{
				type.resetTransferTicks();
			}
		}

		// update animation ticks
		for (FactoryType type : FactoryType.values())
		{
			type.animationTicksIncrease();
			if (type.getAnimationTicks() > type.getAnimationTimeout())
			{
				type.resetAnimationTicks();
				type.frameIncrease();
			}
		}

	}

	public long key()
	{
		return key;
	}
}
