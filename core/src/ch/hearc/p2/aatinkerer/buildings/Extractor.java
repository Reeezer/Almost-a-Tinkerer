package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.utils.Timer;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Ressource;
import ch.hearc.p2.aatinkerer.TileMap;

public class Extractor extends Building
{
	private Ressource ressource;

	public Extractor(TileMap tilemap, int x, int y, int direction, Ressource ressource)
	{
		super(tilemap, x, y, direction, 1, "Tile/Extractor.png");
		this.ressource = ressource;
		this.tabInputsPosition = null;

		// Extracting every 5s
		Timer.Task extractTimer = new Timer.Task()
		{
			@Override
			public void run()
			{
				extract();
			}
		};
		Timer.schedule(extractTimer, 5f, 5f);
	}

	public void extract()
	{
		if (contentSize < maxSize) {
			System.out.println("Extracting " + ressource);

			switch (ressource) {
				case NONE:
					tabItem[contentSize++] = ItemType.NONE;
					break;
				case WOOD:
					tabItem[contentSize++] = ItemType.WOODLOG;
					break;
				case STONE:
					tabItem[contentSize++] = ItemType.STONE;
					break;
				case IRON:
					tabItem[contentSize++] = ItemType.IRONORE;
					break;
				case COPPER:
					tabItem[contentSize++] = ItemType.COPPERORE;
					break;
				case OIL:
					tabItem[contentSize++] = ItemType.OIL;
					break;
				case WATER:
					tabItem[contentSize++] = ItemType.WATER;
					break;
				case COTTON:
					tabItem[contentSize++] = ItemType.COTTON;
					break;
				default:
					System.out.println("Wrong type of ressource : " + ressource);
			}
		}
	}
}
