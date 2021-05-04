package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.ItemType;
import ch.hearc.p2.aatinkerer.Ressource;
import ch.hearc.p2.aatinkerer.TileMap;

public class Extractor extends Building
{
	private Ressource ressource;
	private int extractTicks = 0;
	private int extractTimeout = 100;

	public Extractor(TileMap tilemap, int x, int y, int direction, Ressource ressource)
	{
		super(tilemap, x, y, direction, 1, "Tile/Extractor/", 1, 1);
		this.ressource = ressource;
		this.inputPositions = null;
		this.outputPositions = new int[] { x, y, direction };
	}

	@Override
	public boolean isFull()
	{
		return false;
	}

	public void extract()
	{
		if (contentSize < maxSize && ressource.getExtractedItem() != ItemType.NONE) {
			System.out.println("Extracting " + ressource);
			Item item = new Item();
			item.type = ressource.getExtractedItem();
			items.add(item);
			contentSize++;
		}
	}

	@Override
	public void update()
	{
		super.update();

		if (extractTicks++ > extractTimeout) {
			extract();
			extractTicks = 0;
		}
	}
}
