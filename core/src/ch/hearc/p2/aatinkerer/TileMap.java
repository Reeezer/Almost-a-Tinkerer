package ch.hearc.p2.aatinkerer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Conveyor;
import ch.hearc.p2.aatinkerer.buildings.Extractor;
import ch.hearc.p2.aatinkerer.buildings.FactoryType;

public class TileMap
{
	public static final int TILESIZE = 32;

	private int width, height;
	private Ressource[][] map;
	private Building[][] conveyors;
	private Building[][] factories;

	private Set<Building> buildings;

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
		buildings = new HashSet<Building>();

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
		if (!tileExists(x, y))
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

	private boolean tileExists(int x, int y)
	{
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	private boolean isEmpty(int x, int y)
	{
		if (!tileExists(x, y))
			return false;

		// conveyors layer
		if (conveyors[x][y] != null)
			return false;

		// factories layer
		if (factories[x][y] != null)
			return false;

		return true;
	}

	private void updateOutput(int x, int y, Building[][] tabBuilding)
	{
		if (tabBuilding[x][y] != null)
			tabBuilding[x][y].updateOutputs();

		if (tileExists(x + 1, y) && tabBuilding[x + 1][y] != null)
			tabBuilding[x + 1][y].updateOutputs();
		if (tileExists(x - 1, y) && tabBuilding[x - 1][y] != null)
			tabBuilding[x - 1][y].updateOutputs();

		if (tileExists(x, y + 1) && tabBuilding[x][y + 1] != null)
			tabBuilding[x][y + 1].updateOutputs();
		if (tileExists(x, y - 1) && tabBuilding[x][y - 1] != null)
			tabBuilding[x][y - 1].updateOutputs();
	}

	public void updateOutputs(int x, int y)
	{
		updateOutput(x, y, conveyors);
		updateOutput(x, y, factories);
	}

	public Building getNeighbourBuilding(int[] outputPosition)
	{
		int x = outputPosition[0];
		int y = outputPosition[1];
		
		int dx = 0;
		int dy = 0;

		switch (outputPosition[2]) {
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

		if (!tileExists(x + dx, y + dy))
			return null;

		if (conveyors[x + dx][y + dy] != null) {
			for (int[] input : conveyors[x + dx][y + dy].getInputs()) {
				if (input[0] == x + dx && input[1] == y + dy && input[2] == (outputPosition[2] + 2) % 4) {
					return conveyors[x + dx][y + dy];
				}
			}
		}

		if (factories[x + dx][y + dy] != null && factories[x + dx][y + dy].getInputs() != null) {
			for (int[] input : factories[x + dx][y + dy].getInputs()) {
				if (input[0] == x + dx && input[1] == y + dy && input[2] == (outputPosition[2] + 2) % 4) {
					return factories[x + dx][y + dy];
				}
			}
		}

		return null;
	}

	private void checkSurroundings(Building[][] buildings, int x, int dx, int y, int dy, int direction, int addToDirection, boolean isInput, int[][] inputOutputPosition)
	{
		if (!isInput) {
			if (buildings[x + dx][y + dy] != null && buildings[x + dx][y + dy].getInputs() != null) {
				for (int[] input : buildings[x + dx][y + dy].getInputs()) {
					if (input[0] == x + dx && input[1] == y + dy && input[2] == (direction + 1 + addToDirection) % 4) {
						inputOutputPosition[1] = new int[] { x, y, (direction + 3 + addToDirection) % 4 };
					}
				}
			}
		} else {
			if (buildings[x + dx][y + dy] != null && buildings[x + dx][y + dy].getOutput() != null) {
				int[] output = buildings[x + dx][y + dy].getOutput();
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

		switch (direction) {
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

		if (!tileExists(x + dx, y + dy))
			return;
		
		checkSurroundings(conveyors, x, dx, y, dy, direction, addToDirection, isInput, inputOutputPosition);
		checkSurroundings(factories, x, dx, y, dy, direction, addToDirection, isInput, inputOutputPosition);
	}

	private int[][] connexion(int x, int y, int direction)
	{
		int[][] inputOutputPosition = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, direction } };

		connect(x, y, direction, inputOutputPosition, false, false); // output right-side
		connect(x, y, direction, inputOutputPosition, false, true); // output left-side
		connect(x, y, (direction + 2) % 4, inputOutputPosition, true, false); // input right-side
		connect(x, y, (direction + 2) % 4, inputOutputPosition, true, true); // input left-side

		return inputOutputPosition;
	}

	public void placeBuilding(int x, int y, int direction, FactoryType factoryType)
	{
		if (isEmpty(x, y)) {
			switch (factoryType) {
				case EXTRACTOR:
					Extractor extractor = new Extractor(this, x, y, direction, map[x][y]);
					factories[x][y] = extractor;
					buildings.add(extractor);
					break;
				case CONVEYOR:
					Conveyor conveyor = new Conveyor(this, x, y, connexion(x, y, direction));
					conveyors[x][y] = conveyor;
					buildings.add(conveyor);
					break;
				default:
					System.out.println("Wrong factory type : " + factoryType);
					break;
			}
			updateOutputs(x, y);
		}
	}

	public void deleteBuilding(int x, int y)
	{
		if (!tileExists(x, y))
			return;

		// conveyors layer
		if (conveyors[x][y] != null) {
			buildings.remove(conveyors[x][y]);
			conveyors[x][y] = null;
		}

		// factories layer
		if (factories[x][y] != null) {
			buildings.remove(factories[x][y]);
			factories[x][y] = null;
		}
		updateOutputs(x, y);
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
					conveyors[i][j].render(batch, TileMap.TILESIZE);
				}
			}
		}

		// items
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (conveyors[i][j] != null) {
					((Conveyor) conveyors[i][j]).renderItems(batch, TileMap.TILESIZE);
				}
			}
		}

		// factories FIXME ce code va effectuer le rendu des batiments à plus d'une tile plus qu'une fois, utiliser un rendu comme le code de update(), sans les convoyeurs
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (factories[i][j] != null) {
					factories[i][j].render(batch, TileMap.TILESIZE);
				}
			}
		}
	}

	public void update()
	{
		Building.staticUpdate();

		for (Building building : buildings) {
			building.update();
		}
	}

	public void dispose()
	{

	}
}
