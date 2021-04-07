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
		this.inputPositions = null;

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

	@Override
	public boolean isFull()
	{
		return false;
	}

	public void extract()
	{
		if (contentSize < maxSize) {
			System.out.println("Extracting " + ressource);
			items[contentSize++] = ressource.getExtractedItem();
		}
	}
}
