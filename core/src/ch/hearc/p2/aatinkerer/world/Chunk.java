package ch.hearc.p2.aatinkerer.world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Conveyor;
import ch.hearc.p2.aatinkerer.data.Ressource;
import ch.hearc.p2.aatinkerer.data.Tile;
import ch.hearc.p2.aatinkerer.data.TileType;
import ch.hearc.p2.aatinkerer.main.AATinkererGame;

public class Chunk implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long key;

	public static final int TILESIZE = 32;
	public static final int CHUNKSIZE = 64;

	private Map<Long, Ressource> cachedGenerationRessources;

	private transient Map<TileType, Tile[][]> tiles;
	private transient List<Conveyor> conveyors; // used to animate items and to render the conveyors
	private transient List<Building> buildings; // used to render the buildings - important: if a building is present across two chunks it will be rendered twice due to being present in both chunk's
												// building list, but it is necessary to avoid building not rendering when they should if the player's screen is very close to a chunk border that is
												// not being rendered

	private transient Random random;
	private transient TileMap tilemap; // the tilemap that contains the chunk for easy access to surrounding chunks

	public void setRandom(Random random)
	{
		this.random = random;
	}

	public void setTileMap(TileMap tilemap)
	{
		this.tilemap = tilemap;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException
	{
		oos.defaultWriteObject();

		Tile[][] ressources = tiles.get(TileType.RESSOURCE);

		oos.writeObject(ressources);
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();

		createStorageMembers();

		Tile[][] ressources = (Tile[][]) ois.readObject();
		tiles.put(TileType.RESSOURCE, ressources);
	}

	public Chunk(Random random, TileMap tilemap, long key)
	{
		createStorageMembers();
		tiles.put(TileType.RESSOURCE, new Ressource[CHUNKSIZE][CHUNKSIZE]);

		this.random = random;
		this.tilemap = tilemap;
		this.key = key;

		// - all the tile layers

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

			// System.out.format("chunk at (%d, %d) is reading cached ressources from its neighbour (%d, %d)%n", chunkX, chunkY, neighbourX, neighbourY);

			int coordsOffsetX = (neighbourX - chunkX) * CHUNKSIZE;
			int coordsOffsetY = (neighbourY - chunkY) * CHUNKSIZE;

			// since we shouldn't modify a map while it's being iterated, store all consumed resources to be deleted later
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
					// System.out.format(" - read new cached ressource %s neighbour's local coords (%d, %d) converted to chunk local (%d,%d)%n", ressource.toString(),
					// neighbourRessourceX, neighbourRessourceY, ressourceX, ressourceY);

					setLocalTile(TileType.RESSOURCE, ressourceX, ressourceY, ressource);
					toDeleteRessourceKeys.add(ressourceKey);
				}
			}

			for (Long toDeleteKey : toDeleteRessourceKeys)
				neighbour.cachedGenerationRessources.remove(toDeleteKey);
		}

		final int seeds = AATinkererGame.difficulty.getNbSeed();
		final int max_life = AATinkererGame.difficulty.getLife(); // + 2

		for (int i = 0; i < seeds; i++)
		{
			int x = random.nextInt(CHUNKSIZE);
			int y = random.nextInt(CHUNKSIZE);
			int life = random.nextInt(max_life) + 2;
			// choose a random resource to spawn excluding the first value which is NONE
			Ressource ressource = Ressource.values()[random.nextInt(Ressource.values().length - 1) + 1];

			generate(ressource, life, x, y);
		}

	}

	public void createStorageMembers()
	{
		conveyors = new LinkedList<Conveyor>();
		buildings = new LinkedList<Building>();

		tiles = new HashMap<TileType, Tile[][]>();
		tiles.put(TileType.FACTORY, new Building[CHUNKSIZE][CHUNKSIZE]);
		tiles.put(TileType.CONVEYOR, new Building[CHUNKSIZE][CHUNKSIZE]);
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

		// if it's a conveyor, add/remove it to/from the conveyors list to animate its items more easily
		if (type == TileType.CONVEYOR)
		{
			if (value != null)
				conveyors.add((Conveyor) value);
			else
				conveyors.remove((Conveyor) getLocalTile(TileType.CONVEYOR, localX, localY));
		}

		else if (type == TileType.FACTORY)
		{
			if (value != null && !buildings.contains(value))
				buildings.add((Building) value);
			else
				buildings.remove((Building) getLocalTile(TileType.FACTORY, localX, localY));
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
		final float spawn_probability = (float) AATinkererGame.difficulty.getProbability();
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

	public void renderRessources(SpriteBatch batch, int x, int y)
	{
		for (int i = 0; i < CHUNKSIZE; i++)
		{
			for (int j = 0; j < CHUNKSIZE; j++)
			{
				Tile tile = getLocalTile(TileType.RESSOURCE, i, j);

				if (tile != null)
					tile.render(batch, x + i, y + j);
			}
		}
	}

	public void renderFactories(SpriteBatch batch, int x, int y)
	{
		for (Building building : buildings)
			building.render(batch, x, y);
	}

	public void renderConveyors(SpriteBatch batch, int x, int y)
	{
		for (Conveyor conveyor : conveyors)
			conveyor.render(batch, x, y);
	}

	public void renderItems(SpriteBatch batch, int x, int y)
	{
		for (Conveyor conveyor : conveyors)
			conveyor.renderItems(batch, x, y);
	}

	public long key()
	{
		return key;
	}

	public boolean isEmpty()
	{
		return this.buildings.isEmpty() && this.conveyors.isEmpty();
	}

}
