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

//		Hub hub = new Hub(this, width / 2, height / 2);
//		factories[width / 2 + 1][height / 2] = hub;
//		factories[width / 2 + 1][height / 2 - 1] = hub;
//		factories[width / 2 + 1][height / 2 + 1] = hub;
//		factories[width / 2][height / 2] = hub;
//		factories[width / 2][height / 2 - 1] = hub;
//		factories[width / 2][height / 2 + 1] = hub;
//		factories[width / 2 - 1][height / 2] = hub;
//		factories[width / 2 - 1][height / 2 - 1] = hub;
//		factories[width / 2 - 1][height / 2 + 1] = hub;
//		buildings.add(factories[width / 2][height / 2]);
	}

	private boolean isEmpty(int x, int y)
	{

		// en théorie, impossible que cette condition soit fausse via un clic de souris mais on sait jamais
		if (chunkExists(chunkCoordsToKey(x, y)))
			return false;

		// conveyors layer
		if (conveyorAt(x, y) != null)
			return false;

		// factories layer
		if (factoryAt(x, y) != null)
			return false;

		return true;
	}

	private void updateOutput(int x, int y, Building[][] tabBuilding)
	{
		// Update all the outputs of the surrounding buildings if (tabBuilding[x][y] != null) tabBuilding[x][y].updateOutputs();

		if (chunkExists(chunkCoordsToKey(x + 1, y)) && tabBuilding[x + 1][y] != null)
			tabBuilding[x + 1][y].updateOutputs();
		if (chunkExists(chunkCoordsToKey(x - 1, y)) && tabBuilding[x - 1][y] != null)
			tabBuilding[x - 1][y].updateOutputs();

		if (chunkExists(chunkCoordsToKey(x, y + 1)) && tabBuilding[x][y + 1] != null)
			tabBuilding[x][y + 1].updateOutputs();
		if (chunkExists(chunkCoordsToKey(x, y - 1)) && tabBuilding[x][y - 1] != null)
			tabBuilding[x][y - 1].updateOutputs();
	}

	public void updateOutputs(int x, int y)
	{

		// update buildings on both table
//		updateOutput(x, y, conveyors);
//		updateOutput(x, y, factories);
	}

	public Building getNeighbourBuilding(int[] outputPosition)
	{/*
		 * // Check the surroundings to find a building that might be the output to the building who calls this method
		 * 
		 * int x = outputPosition[0]; int y = outputPosition[1];
		 * 
		 * int dx = 0; int dy = 0;
		 * 
		 * switch (outputPosition[2]) { case 0: dx = 1; break; case 1: dy = 1; break; case 2: dx = -1; break; case 3: dy = -1; break; default:
		 * System.out.println("Wrong direction : " + outputPosition[2]); break; }
		 * 
		 * if (!tileExists(x + dx, y + dy)) return null;
		 * 
		 * if (conveyors[x + dx][y + dy] != null) { for (int[] input : conveyors[x + dx][y + dy].getInputs()) { if (input[0] == x + dx && input[1] == y + dy
		 * && input[2] == (outputPosition[2] + 2) % 4) { return conveyors[x + dx][y + dy]; } } }
		 * 
		 * if (factories[x + dx][y + dy] != null && factories[x + dx][y + dy].getInputs() != null) { for (int[] input : factories[x + dx][y + dy].getInputs())
		 * { if (input[0] == x + dx && input[1] == y + dy && input[2] == (outputPosition[2] + 2) % 4) { return factories[x + dx][y + dy]; } } }
		 * 
		 * return null;
		 */

		return null;
	}

	private void checkSurroundings(Building[][] buildings, int x, int dx, int y, int dy, int direction, int addToDirection, boolean isInput, int[][] inputOutputPosition)
	{
		/*
		 * if (!isInput) { if (buildings[x + dx][y + dy] != null && buildings[x + dx][y + dy].getInputs() != null) { for (int[] input : buildings[x + dx][y +
		 * dy].getInputs()) { if (input[0] == x + dx && input[1] == y + dy && input[2] == (direction + 1 + addToDirection) % 4) { inputOutputPosition[1] = new
		 * int[] { x, y, (direction + 3 + addToDirection) % 4 }; } } } } else { if (buildings[x + dx][y + dy] != null && buildings[x + dx][y + dy].getOutput()
		 * != null) { int[] output = buildings[x + dx][y + dy].getOutput(); if (output[0] == x + dx && output[1] == y + dy && output[2] == (direction + 1 +
		 * addToDirection) % 4) inputOutputPosition[0] = new int[] { x, y, (direction + 3 + addToDirection) % 4 }; } }
		 */}

	private void connect(int x, int y, int direction, int[][] inputOutputPosition, boolean isInput, boolean isLeft)
	{
		/*
		 * int addToDirection = (isLeft) ? 2 : 0; int dx = 0; int dy = 0;
		 * 
		 * switch (direction) { case 0: dy = (isLeft) ? 1 : -1; break; case 1: dx = (isLeft) ? -1 : 1; break; case 2: dy = (isLeft) ? -1 : 1; break; case 3:
		 * dx = (isLeft) ? 1 : -1; break; default: System.out.println("Wrong direction : " + direction); break; }
		 * 
		 * if (!tileExists(x + dx, y + dy)) return;
		 * 
		 * // Search for a building to connect with (in factories and conveyors) checkSurroundings(conveyors, x, dx, y, dy, direction, addToDirection,
		 * isInput, inputOutputPosition); checkSurroundings(factories, x, dx, y, dy, direction, addToDirection, isInput, inputOutputPosition);
		 */}

	private int[][] connexion(int x, int y, int direction)
	{/*
		 * // Default behavior int[][] inputOutputPosition = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, direction } };
		 * 
		 * // Check in all directions if a corner has to be made connect(x, y, direction, inputOutputPosition, false, false); // output right-side connect(x,
		 * y, direction, inputOutputPosition, false, true); // output left-side connect(x, y, (direction + 2) % 4, inputOutputPosition, true, false); // input
		 * right-side connect(x, y, (direction + 2) % 4, inputOutputPosition, true, true); // input left-side
		 * 
		 * return inputOutputPosition;
		 */
		return null;
	}

	public void findInputTunnel(Tunnel outputTunnel, int x, int y, int direction, int distance)
	{
		/*
		 * // Method called by an the outputTunnel to connect himself with an input one
		 * 
		 * int dx = 0; int dy = 0;
		 * 
		 * switch (direction) { case 0: dx = 1; break; case 1: dy = 1; break; case 2: dx = -1; break; case 3: dy = -1; break; default:
		 * System.out.println("Wrong direction : " + direction); break; }
		 * 
		 * for (int i = 1; i <= distance; i++) { int posX = x + dx * i; int posY = y + dy * i;
		 * 
		 * // If there is no building on the tile if (!tileExists(posX, posY) || factories[posX][posY] == null) continue;
		 * 
		 * // If the building is not a tunnel if (factories[posX][posY].getType() != FactoryType.TUNNEL) continue;
		 * 
		 * Tunnel tunnel = (Tunnel) factories[posX][posY];
		 * 
		 * // If the tunnel is not an input or the direction is not the right one if (!tunnel.isInput() || tunnel.getInputs()[0][2] != direction) continue;
		 * 
		 * // If all conditions are great the input tunnel can set his output to the tunnel who has called this method tunnel.setOutputTunnel(outputTunnel);
		 * return; }
		 */}

	public int placeBuilding(int x, int y, int direction, FactoryType factoryType, boolean mirrored)
	{/*
		 * int ret = 0; if (isEmpty(x, y)) { // For multi-tiles (2 or 3 tiles in a row) int x2 = (direction % 2 == 0) ? ((direction == 0) ? x + 1 : x - 1) :
		 * x; int y2 = (direction % 2 != 0) ? ((direction == 1) ? y + 1 : y - 1) : y; int x3 = (direction % 2 == 0) ? ((direction == 0) ? x + 2 : x - 2) : x;
		 * int y3 = (direction % 2 != 0) ? ((direction == 1) ? y + 2 : y - 2) : y;
		 * 
		 * switch (factoryType) { case EXTRACTOR: Extractor extractor = new Extractor(this, x, y, direction, map[x][y]); factories[x][y] = extractor;
		 * buildings.add(extractor); break; case CONVEYOR: // Making corners automatically Conveyor conveyor = new Conveyor(this, x, y, connexion(x, y,
		 * direction)); conveyors[x][y] = conveyor; buildings.add(conveyor); break; case FURNACE: Furnace furnace = new Furnace(this, x, y, direction,
		 * mirrored); factories[x][y] = furnace; buildings.add(furnace); break; case CUTTER: Cutter cutter = new Cutter(this, x, y, direction);
		 * factories[x][y] = cutter; buildings.add(cutter); break; case PRESS: Press press = new Press(this, x, y, direction); factories[x][y] = press;
		 * buildings.add(press); break; case MIXER: if (!isEmpty(x2, y2)) return ret;
		 * 
		 * Mixer mixer = new Mixer(this, x, y, direction, mirrored, x2, y2);
		 * 
		 * // Multi-tiles building factories[x][y] = mixer; factories[x2][y2] = mixer; buildings.add(mixer);
		 * 
		 * updateOutputs(x2, y2); break; case ASSEMBLER: if (!isEmpty(x2, y2) || !isEmpty(x3, y3)) return ret;
		 * 
		 * Assembler assembler = new Assembler(this, x, y, direction, x2, y2, x3, y3);
		 * 
		 * // Multi-tiles building factories[x][y] = assembler; factories[x2][y2] = assembler; factories[x3][y3] = assembler; buildings.add(assembler);
		 * 
		 * updateOutputs(x2, y2); updateOutputs(x3, y3); break; case TRASH: Trash trash = new Trash(this, x, y, direction); factories[x][y] = trash;
		 * buildings.add(trash); break; case SPLITTER: Splitter splitter = new Splitter(this, x, y, direction, mirrored); factories[x][y] = splitter;
		 * buildings.add(splitter); break; case MERGER: Merger merger = new Merger(this, x, y, direction, mirrored); factories[x][y] = merger;
		 * buildings.add(merger); break; case TUNNEL: // For the hover icons rotation isInputTunnel = !isInputTunnel; ret = isInputTunnel ? 1 : 2; // We do
		 * want to place an input tunnel and right after be able to place the output one Tunnel tunnel = new Tunnel(this, x, y, direction, isInputTunnel);
		 * factories[x][y] = tunnel; buildings.add(tunnel); break; default: System.out.println("Wrong factory type : " + factoryType); break; } // Check for
		 * link buildings already placed updateOutputs(x, y); } return ret;
		 */
		return 0;
	}

	public void deleteBuilding(int x, int y)
	{
		/*
		 * if (!tileExists(x, y)) return;
		 * 
		 * // If it is the hub if ((factories[x][y] != null && factories[x][y].getType() == null) || (conveyors[x][y] != null && conveyors[x][y].getType() ==
		 * null)) return;
		 * 
		 * Building deleted = null;
		 * 
		 * // conveyors layer if (conveyors[x][y] != null) { deleted = conveyors[x][y]; buildings.remove(conveyors[x][y]); conveyors[x][y] = null; }
		 * 
		 * // factories layer if (factories[x][y] != null) { deleted = factories[x][y]; buildings.remove(factories[x][y]); factories[x][y] = null; }
		 * 
		 * // Check for link/unlink buildings already placed updateOutputs(x, y);
		 * 
		 * // Remove the parts of multi tiles building in the surroundings if (deleted != null) for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++)
		 * delete(x + i, y + j, deleted);
		 */}

	public void delete(int x, int y, Building building)
	{
		/*
		 * if (!tileExists(x, y)) return;
		 * 
		 * Building deleted = null;
		 * 
		 * // conveyors layer if (conveyors[x][y] != null && conveyors[x][y] == building) { deleted = conveyors[x][y]; buildings.remove(conveyors[x][y]);
		 * conveyors[x][y] = null; }
		 * 
		 * // factories layer if (factories[x][y] != null && factories[x][y] == building) { deleted = factories[x][y]; buildings.remove(factories[x][y]);
		 * factories[x][y] = null; }
		 * 
		 * // Check for link/unlink buildings already placed updateOutputs(x, y);
		 * 
		 * // Remove the parts of multi tiles building in the surroundings if (deleted != null) for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++)
		 * delete(x + i, y + j, building);
		 */}

	public void render(SpriteBatch batch, Vector3 position, int screenWidth, int screenHeight, float zoom)
	{
		// box representing what the screen covers on the map in real coordinates (= tile coordinates * TILESIZE)
		Rectangle screenbox = new Rectangle(position.x - (zoom * screenWidth / 2.f), position.y - (zoom * screenHeight / 2.f), (float) screenWidth * zoom, (float) screenHeight * zoom);

		// FIXME improvement : compute the keys for the chunks that must be displayed based on the screenbox instead of iterating through them all,
		//       that way the amount of already generated chunks won't even have any importance since they will never be accessed unless strictly necessary
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
				chunk.render(batch, x * Chunk.CHUNKSIZE * Chunk.TILESIZE, y * Chunk.CHUNKSIZE * Chunk.TILESIZE);
		}
	}

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

	// FIXME generate only the strictly required chunks
	// generate chunks around the camera depending on the screen size
	public void cameraMovedToPosition(Vector3 position, int screenWidth, int screenHeight)
	{

		int cameraX = (int) Math.floor(position.x / Chunk.TILESIZE);
		int cameraY = (int) Math.floor(position.y / Chunk.TILESIZE);

		int maxDeltaX = (screenWidth / (Chunk.CHUNKSIZE * Chunk.TILESIZE)) + 2;
		int maxDeltaY = (screenHeight / (Chunk.CHUNKSIZE * Chunk.TILESIZE)) + 2;

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

	public void update()
	{
		// update buildings
		for (Building building : buildings)
		{
			building.update();
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

		// - the chunk exists, determine the tile position within the chunk
		int tileX = Util.negmod(x, Chunk.CHUNKSIZE);
		int tileY = Util.negmod(y, Chunk.CHUNKSIZE);

		return chunks.get(key);
	}

	public Building factoryAt(int x, int y)
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

			return chunk.factoryAt(tileX, tileY);
		}
	}

	public ItemType itemAt(int x, int y)
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

			// int chunkX = chunkKeyToX(chunk.key());
			// int chunkY = chunkKeyToY(chunk.key());
			// System.out.format("checking item at (%d, %d) with chunk local coords (%d, %d), chunk is at (%d, %d)%n", x, y, tileX, tileY, chunkX, chunkY);

			return chunk.itemAt(tileX, tileY).getExtractedItem();
		}
	}

	public Building conveyorAt(int x, int y)
	{
		Chunk chunk = chunkAtTile(x, y);

		if (chunk == null)
		{
			return null;
		}
		else
		{
			int tileX = x % Chunk.CHUNKSIZE;
			int tileY = y % Chunk.CHUNKSIZE;

			if (tileX < 0)
				tileX += Chunk.CHUNKSIZE;
			if (tileY < 0)
				tileY += Chunk.CHUNKSIZE;

			int chunkX = chunkKeyToX(chunk.key());
			int chunkY = chunkKeyToY(chunk.key());

			return chunk.conveyorAt(tileX, tileY);
		}
	}

	public void dispose()
	{
		// FIXME
	}
}
