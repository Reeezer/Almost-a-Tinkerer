package ch.hearc.p2.aatinkerer.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import ch.hearc.p2.aatinkerer.buildings.Assembler;
import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Conveyor;
import ch.hearc.p2.aatinkerer.buildings.Cutter;
import ch.hearc.p2.aatinkerer.buildings.Extractor;
import ch.hearc.p2.aatinkerer.buildings.Hub;
import ch.hearc.p2.aatinkerer.buildings.Furnace;
import ch.hearc.p2.aatinkerer.buildings.Merger;
import ch.hearc.p2.aatinkerer.buildings.Mixer;
import ch.hearc.p2.aatinkerer.buildings.Press;
import ch.hearc.p2.aatinkerer.buildings.Splitter;
import ch.hearc.p2.aatinkerer.buildings.Trash;
import ch.hearc.p2.aatinkerer.buildings.Tunnel;
import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Ressource;
import ch.hearc.p2.aatinkerer.data.Tile;
import ch.hearc.p2.aatinkerer.data.TileType;
import ch.hearc.p2.aatinkerer.util.Sounds;
import ch.hearc.p2.aatinkerer.util.Util;

public class TileMap
{
	private Map<Long, Chunk> chunks;
	private Set<Building> buildings;

	private boolean isInputTunnel;

	Random random;

	public TileMap()
	{
		random = new Random();

		isInputTunnel = false;
		buildings = new HashSet<Building>();

		chunks = new HashMap<Long, Chunk>();

		
		// generate the chunks around the center so we can place the hub
		cameraMovedToPosition(new Vector3(0,0,0), 20, 20);
		
		
		// add the hub back
		Hub hub = new Hub(this, 1, 1); // FIXME essayer de le mettre vraiment au centre sans le bug graphique
		
		for (int x = 0; x <= 2; x++)
			for (int y = 0; y <= 2; y++)
				setTileAt(TileType.FACTORY, x, y, hub);
		
		buildings.add(hub);
		
		// clear out the ressources around the hub
		for (int x = -2; x <= 4; x++)
		{
			for (int y = -2; y <= 4; y++)
			{
				setTileAt(TileType.RESSOURCE, x, y, Ressource.NONE);
			}
		}
	}

	public Chunk chunkAtTile(int x, int y)
	{
		// - determine what chunk the tile is in
		int chunkX = (int) Math.floor(x / (float) Chunk.CHUNKSIZE);
		int chunkY = (int) Math.floor(y / (float) Chunk.CHUNKSIZE);

		// - determine if the chunk exists
		long key = chunkCoordsToKey(chunkX, chunkY);

		if (!chunks.containsKey(key))
			return null;

		return chunks.get(key);
	}

	public Tile tileAt(TileType type, int x, int y)
	{
		Chunk chunk = chunkAtTile(x, y);

		if (chunk == null)
		{
			return null;
		}
		else
		{
			int tileX = Util.negmod(x, Chunk.CHUNKSIZE);
			int tileY = Util.negmod(y, Chunk.CHUNKSIZE);

			return chunk.getLocalTile(type, tileX, tileY);
		}
	}

	
	public void setTileAt(TileType type, int x, int y, Tile tile)
	{
		Chunk chunk = chunkAtTile(x, y);

		if (chunk == null)
		{
			return;
		}
		else
		{
			int tileX = Util.negmod(x, Chunk.CHUNKSIZE);
			int tileY = Util.negmod(y, Chunk.CHUNKSIZE);

			chunk.setLocalTile(type, tileX, tileY, tile);
		}
	}

	// FIXME move these methods to the chunk class instead, and have it generate its own key?
	// a chunk key is computed by merging the 32 bits values of its x and y coordinates into a single 64 bits key, decoding is the inverse function
	public static long chunkCoordsToKey(int x, int y)
	{
		return (((long) x) << 32) | (y & 0xffffffffL);
	}

	public static int chunkKeyToX(long key)
	{
		return (int) (key >> 32);
	}

	public static int chunkKeyToY(long key)
	{
		return (int) key;
	}

	public boolean chunkExists(long key)
	{
		return chunks.containsKey(key);
	}

	public Chunk getChunk(long key)
	{
		return chunks.get(key);
	}

	public List<Chunk> getNeighbours(long key)
	{
		int chunkX = chunkKeyToX(key);
		int chunkY = chunkKeyToY(key);

		LinkedList<Chunk> neighbours = new LinkedList<Chunk>();

		if (chunks.containsKey(chunkCoordsToKey(chunkX + 1, chunkY)))
			neighbours.add(chunks.get(chunkCoordsToKey(chunkX + 1, chunkY)));

		if (chunks.containsKey(chunkCoordsToKey(chunkX - 1, chunkY)))
			neighbours.add(chunks.get(chunkCoordsToKey(chunkX - 1, chunkY)));

		if (chunks.containsKey(chunkCoordsToKey(chunkX, chunkY + 1)))
			neighbours.add(chunks.get(chunkCoordsToKey(chunkX, chunkY + 1)));

		if (chunks.containsKey(chunkCoordsToKey(chunkX, chunkY - 1)))
			neighbours.add(chunks.get(chunkCoordsToKey(chunkX, chunkY - 1)));

		return neighbours;
	}

	/* --- TODO refactor what is below this line --- */

	public void render(SpriteBatch batch, Vector3 position, int screenWidth, int screenHeight, float zoom)
	{
		// box representing what the screen covers on the map in real coordinates (= tile coordinates * TILESIZE)
		Rectangle screenbox = new Rectangle(position.x - (zoom * screenWidth / 2.f), position.y - (zoom * screenHeight / 2.f), (float) screenWidth * zoom, (float) screenHeight * zoom);

		// FIXME improvement : compute the keys for the chunks that must be displayed based on the screenbox instead of iterating through them all,
		// that way the amount of already generated chunks won't even have any importance since they will never be accessed unless strictly necessary

		List<Chunk> chunksToRender = new LinkedList<Chunk>();

		// build a list of chunks that need to be rendered so that each layer can then be rendered at the same time instead of at once with the whole chunk
		for (Map.Entry<Long, Chunk> chunkEntry : chunks.entrySet())
		{
			long key = chunkEntry.getKey();
			Chunk chunk = chunkEntry.getValue();

			int x = chunkKeyToX(key);
			int y = chunkKeyToY(key);

			float realx = x * Chunk.CHUNKSIZE * Chunk.TILESIZE;
			float realy = y * Chunk.CHUNKSIZE * Chunk.TILESIZE;

			// box representing what the chunk covers on the map in real coordinates
			Rectangle chunkbox = new Rectangle(realx, realy, Chunk.CHUNKSIZE * Chunk.TILESIZE, Chunk.CHUNKSIZE * Chunk.TILESIZE);

			// only render the chunk if it is visible on the screen
			if (screenbox.overlaps(chunkbox))
				chunksToRender.add(chunk);
		}

		// - render steps

		// ressources
		for (Chunk chunk : chunksToRender)
			chunk.renderRessources(batch, chunkKeyToX(chunk.key()) * Chunk.CHUNKSIZE, chunkKeyToY(chunk.key()) * Chunk.CHUNKSIZE);

		// conveyors
		for (Chunk chunk : chunksToRender)
			chunk.renderConveyors(batch, chunkKeyToX(chunk.key()) * Chunk.CHUNKSIZE, chunkKeyToY(chunk.key()) * Chunk.CHUNKSIZE);

		// items
		for (Chunk chunk : chunksToRender)
			chunk.renderItems(batch, chunkKeyToX(chunk.key()) * Chunk.CHUNKSIZE, chunkKeyToY(chunk.key()) * Chunk.CHUNKSIZE);

		// factories
		for (Chunk chunk : chunksToRender)
			chunk.renderFactories(batch, chunkKeyToX(chunk.key()) * Chunk.CHUNKSIZE, chunkKeyToY(chunk.key()) * Chunk.CHUNKSIZE);
	}

	// FIXME generate only the strictly required chunks
	// generate chunks around the camera depending on the screen size
	public void cameraMovedToPosition(Vector3 position, int screenWidth, int screenHeight)
	{

		int cameraX = (int) Math.floor(position.x / Chunk.TILESIZE);
		int cameraY = (int) Math.floor(position.y / Chunk.TILESIZE);

		int maxDeltaX = (screenWidth / (Chunk.CHUNKSIZE * Chunk.TILESIZE)) + 3;
		int maxDeltaY = (screenHeight / (Chunk.CHUNKSIZE * Chunk.TILESIZE)) + 3;

		// world coordinates to chunk's coordinates (not coordinates in chunk)
		int chunkX = cameraX / Chunk.CHUNKSIZE;
		int chunkY = cameraY / Chunk.CHUNKSIZE;

		for (int x = chunkX - maxDeltaX; x <= chunkX + maxDeltaX; x++)
		{
			for (int y = chunkY - maxDeltaY; y <= chunkY + maxDeltaY; y++)
			{
				long key = chunkCoordsToKey(x, y);

				if (!chunks.containsKey(key))
					chunks.put(chunkCoordsToKey(x, y), new Chunk(random, this, key));
			}
		}
	}

	private boolean isEmpty(int x, int y)
	{
		// en théorie, impossible que cette condition soit fausse via un clic de souris mais on sait jamais
		if (chunkAtTile(x, y) != null)
		{
			// conveyors layer
			if (tileAt(TileType.CONVEYOR, x, y) != null)
				return false;

			// factories layer
			if (tileAt(TileType.FACTORY, x, y) != null)
				return false;

			return true;
		}

		return false;
	}

	private void updateOutput(int x, int y, TileType type)
	{
		// Update all the outputs of the surrounding buildings
		Building building;

		building = (Building) tileAt(type, x, y);
		if (building != null)
			building.updateOutputs();

		building = (Building) tileAt(type, x + 1, y);
		if (chunkAtTile(x + 1, y) != null && building != null)
			building.updateOutputs();

		building = (Building) tileAt(type, x - 1, y);
		if (chunkAtTile(x - 1, y) != null && building != null)
			building.updateOutputs();

		building = (Building) tileAt(type, x, y + 1);
		if (chunkAtTile(x, y + 1) != null && building != null)
			building.updateOutputs();

		building = (Building) tileAt(type, x, y - 1);
		if (chunkAtTile(x, y - 1) != null && building != null)
			building.updateOutputs();
	}

	public void updateOutputs(int x, int y)
	{
		// update buildings on both table
		updateOutput(x, y, TileType.CONVEYOR);
		updateOutput(x, y, TileType.FACTORY);
	}

	public Building getNeighbourBuilding(int[] outputPosition)
	{
		// Check the surroundings to find a building that might be the output to the building who calls this method

		int x = outputPosition[0];
		int y = outputPosition[1];

		int dx = 0;
		int dy = 0;

		switch (outputPosition[2])
		{
			case 0:
				dx = 1;
				break;
			case 1:
				dy = 1;
				break;
			case 2:
				dx = -1;
				break;
			case 3:
				dy = -1;
				break;
			default:
				System.out.println("Wrong direction : " + outputPosition[2]);
				break;
		}

		if (chunkAtTile(x + dx, y + dy) == null)
			return null;

		Conveyor conveyor = (Conveyor) tileAt(TileType.CONVEYOR, x + dx, y + dy);
		if (conveyor != null && conveyor.getInputs() != null)
		{
			for (int[] input : conveyor.getInputs())
			{
				if (input[0] == x + dx && input[1] == y + dy && input[2] == (outputPosition[2] + 2) % 4)
				{
					return conveyor;
				}
			}
		}

		Building factory = (Building) tileAt(TileType.FACTORY, x + dx, y + dy);
		if (factory != null && factory.getInputs() != null)
		{
			for (int[] input : factory.getInputs())
			{
				if (input[0] == x + dx && input[1] == y + dy && input[2] == (outputPosition[2] + 2) % 4)
				{
					return factory;
				}
			}
		}

		return null;
	}

	private void checkSurroundings(TileType type, int x, int dx, int y, int dy, int direction, int addToDirection, boolean isInput, int[][] inputOutputPosition)
	{
		Building building = (Building) tileAt(type, x + dx, y + dy);

		if (!isInput)
		{
			if (building != null && building.getInputs() != null)
			{
				for (int[] input : building.getInputs())
				{
					if (input[0] == x + dx && input[1] == y + dy && input[2] == (direction + 1 + addToDirection) % 4)
					{
						inputOutputPosition[1] = new int[] { x, y, (direction + 3 + addToDirection) % 4 };
					}
				}
			}
		}
		else
		{
			if (building != null && building.getOutput() != null)
			{
				int[] output = building.getOutput();
				if (output[0] == x + dx && output[1] == y + dy && output[2] == (direction + 1 + addToDirection) % 4)
					inputOutputPosition[0] = new int[] { x, y, (direction + 3 + addToDirection) % 4 };
			}
		}
	}

	private void connect(int x, int y, int direction, int[][] inputOutputPosition, boolean isInput, boolean isLeft)
	{

		int addToDirection = (isLeft) ? 2 : 0;

		int dx = 0;
		int dy = 0;

		switch (direction)
		{
			case 0:
				dy = (isLeft) ? 1 : -1;
				break;
			case 1:
				dx = (isLeft) ? -1 : 1;
				break;
			case 2:
				dy = (isLeft) ? -1 : 1;
				break;
			case 3:
				dx = (isLeft) ? 1 : -1;
				break;
			default:
				System.out.println("Wrong direction : " + direction);
				break;
		}

		if (chunkAtTile(x + dx, y + dy) == null)
			return;

		// Search for a building to connect with (in factories and conveyors)
		checkSurroundings(TileType.CONVEYOR, x, dx, y, dy, direction, addToDirection, isInput, inputOutputPosition);
		checkSurroundings(TileType.FACTORY, x, dx, y, dy, direction, addToDirection, isInput, inputOutputPosition);
	}

	private int[][] connexion(int x, int y, int direction)
	{
		// Default behavior
		int[][] inputOutputPosition = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, direction } };

		// Check in all directions if a corner has to be made
		connect(x, y, direction, inputOutputPosition, false, false); // output right-side
		connect(x, y, direction, inputOutputPosition, false, true); // output left-side
		connect(x, y, (direction + 2) % 4, inputOutputPosition, true, false); // input right-side
		connect(x, y, (direction + 2) % 4, inputOutputPosition, true, true); // input left-side

		return inputOutputPosition;
	}

	public void findInputTunnel(Tunnel outputTunnel, int x, int y, int direction, int distance)
	{

		// Method called by an the outputTunnel to connect himself with an input one

		int dx = 0;
		int dy = 0;

		switch (direction)
		{
			case 0:
				dx = 1;
				break;
			case 1:
				dy = 1;
				break;
			case 2:
				dx = -1;
				break;
			case 3:
				dy = -1;
				break;
			default:
				System.out.println("Wrong direction : " + direction);
				break;
		}

		for (int i = 1; i <= distance; i++)
		{
			int posX = x + dx * i;
			int posY = y + dy * i;

			// If there is no building on the tile FIXME wrong comment
			if (chunkAtTile(posX, posY) == null)
				continue;

			Building factory = (Building) tileAt(TileType.FACTORY, posX, posY);

			if (factory == null)
				continue;

			// If the building is not a tunnel
			if (factory.getType() != FactoryType.TUNNEL)
				continue;

			Tunnel tunnel = (Tunnel) factory;

			// If the tunnel is not an input or the direction is not the right one
			if (!tunnel.isInput() || tunnel.getInputs()[0][2] != direction)
				continue;

			// If all conditions are great the input tunnel can set his output to the tunnel who has called this method
			tunnel.setOutputTunnel(outputTunnel);
			return;
		}
	}

	public int placeBuilding(int x, int y, int direction, FactoryType factoryType, boolean mirrored)
	{
		int ret = 0;

		System.out.format("placing new building of type %s at (%d,%d)%n", factoryType, x, y);

		if (isEmpty(x, y))
		{
			// For multi-tiles (2 or 3 tiles in a row)
			int x2 = (direction % 2 == 0) ? ((direction == 0) ? x + 1 : x - 1) : x;
			int y2 = (direction % 2 != 0) ? ((direction == 1) ? y + 1 : y - 1) : y;
			int x3 = (direction % 2 == 0) ? ((direction == 0) ? x + 2 : x - 2) : x;
			int y3 = (direction % 2 != 0) ? ((direction == 1) ? y + 2 : y - 2) : y;

			switch (factoryType)
			{
				case EXTRACTOR:
					Ressource ressource = (Ressource) tileAt(TileType.RESSOURCE, x, y);
					Extractor extractor = new Extractor(this, x, y, direction, ressource);
					setTileAt(TileType.FACTORY, x, y, extractor);
					buildings.add(extractor);
					break;

				case CONVEYOR: // Making corners automatically
					Conveyor conveyor = new Conveyor(this, x, y, connexion(x, y, direction));
					setTileAt(TileType.CONVEYOR, x, y, conveyor);
					buildings.add(conveyor);
					break;

				case FURNACE:
					Furnace furnace = new Furnace(this, x, y, direction, mirrored);
					setTileAt(TileType.FACTORY, x, y, furnace);
					buildings.add(furnace);
					break;

				case CUTTER:
					Cutter cutter = new Cutter(this, x, y, direction);
					setTileAt(TileType.FACTORY, x, y, cutter);
					buildings.add(cutter);
					break;

				case PRESS:
					Press press = new Press(this, x, y, direction);
					setTileAt(TileType.FACTORY, x, y, press);
					buildings.add(press);
					break;

				case MIXER:
					if (!isEmpty(x2, y2))
						return ret;

					Mixer mixer = new Mixer(this, x, y, direction, mirrored, x2, y2);

					// Multi-tiles building
					setTileAt(TileType.FACTORY, x, y, mixer);
					setTileAt(TileType.FACTORY, x2, y2, mixer);

					buildings.add(mixer);

					updateOutputs(x2, y2);
					break;

				case ASSEMBLER:
					if (!isEmpty(x2, y2) || !isEmpty(x3, y3))
						return ret;

					Assembler assembler = new Assembler(this, x, y, direction, x2, y2, x3, y3);

					// Multi-tiles building
					setTileAt(TileType.FACTORY, x, y, assembler);
					setTileAt(TileType.FACTORY, x2, y2, assembler);
					setTileAt(TileType.FACTORY, x3, y3, assembler);

					buildings.add(assembler);
					updateOutputs(x2, y2);
					updateOutputs(x3, y3);
					break;

				case TRASH:
					Trash trash = new Trash(this, x, y, direction);

					setTileAt(TileType.FACTORY, x, y, trash);

					buildings.add(trash);
					break;

				case SPLITTER:
					Splitter splitter = new Splitter(this, x, y, direction, mirrored);
					setTileAt(TileType.FACTORY, x, y, splitter);

					buildings.add(splitter);
					break;

				case MERGER:
					Merger merger = new Merger(this, x, y, direction, mirrored);

					setTileAt(TileType.FACTORY, x, y, merger);

					buildings.add(merger);
					break;

				case TUNNEL: // For the hover icons rotation
					isInputTunnel = !isInputTunnel;
					ret = isInputTunnel ? 1 : 2; // We do want to place an input tunnel and right after be able to place the output one

					Tunnel tunnel = new Tunnel(this, x, y, direction, isInputTunnel);
					setTileAt(TileType.FACTORY, x, y, tunnel);

					buildings.add(tunnel);
					break;

				default:
					System.out.println("Wrong factory type : " + factoryType);
					break;
			}

			// Check for link buildings already placed
			updateOutputs(x, y);
			
			Sounds.PLACING.play();
		}
		return ret;

	}

	public void deleteBuilding(int x, int y)
	{

		if (chunkAtTile(x, y) == null)
			return;

		Building factory = (Building) tileAt(TileType.FACTORY, x, y);
		Conveyor conveyor = (Conveyor) tileAt(TileType.CONVEYOR, x, y);

		// If it is the hub
		if ((factory != null && factory.getType() == null) || (conveyor != null && conveyor.getType() == null))
			return;

		Building deleted = null;

		// conveyors layer
		if (conveyor != null)
		{
			deleted = conveyor;
			buildings.remove(conveyor);

			setTileAt(TileType.CONVEYOR, x, y, null);
			
			Sounds.DESTROYING.play();
		}

		// factories layer
		if (factory != null)
		{
			deleted = factory;
			buildings.remove(factory);

			setTileAt(TileType.FACTORY, x, y, null);
			
			Sounds.DESTROYING.play();
		}

		// Check for link/unlink buildings already placed
		updateOutputs(x, y);

		// Remove the parts of multi tiles building in the surroundings
		if (deleted != null)
			for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++)
					delete(x + i, y + j, deleted);
	}

	public void delete(int x, int y, Building building)
	{

		if (chunkAtTile(x, y) == null)
			return;

		Building factory = (Building) tileAt(TileType.FACTORY, x, y);
		Conveyor conveyor = (Conveyor) tileAt(TileType.CONVEYOR, x, y);

		Building deleted = null;

		// conveyors layer
		if (conveyor != null && conveyor == building)
		{
			deleted = conveyor;
			buildings.remove(conveyor);

			setTileAt(TileType.CONVEYOR, x, y, null);
		}

		// factories layer
		if (factory != null && factory == building)
		{
			deleted = factory;
			buildings.remove(factory);

			setTileAt(TileType.FACTORY, x, y, null);
		}

		// Check for link/unlink buildings already placed
		updateOutputs(x, y);

		// Remove the parts of multi tiles building in the surroundings
		if (deleted != null)
			for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++)
					delete(x + i, y + j, building);
	}

	public void update()
	{
		// update buildings
		for (Building building : buildings)
			building.update();

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

	public void dispose()
	{
		// FIXME dispose
	}
}
