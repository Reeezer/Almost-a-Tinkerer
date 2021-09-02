package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Ressource;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Extractor extends Building
{
	private Ressource ressource;
	private int extractTicks = 0;
	private int extractTimeout = 100;

	public Extractor(TileMap tilemap, int x, int y, int direction, Ressource ressource)
	{
		super(tilemap, x, y, direction, 1, 1, FactoryType.EXTRACTOR);
		this.ressource = ressource;
		this.inputPositions = null;
		this.outputPosition = new int[] { x, y, direction };
	}

	@Override
	public boolean isFull(Item item)
	{
		return false;
	}

	public void extract()
	{
		// extract item if not already full
		if (contentSize < maxSize && ressource.getExtractedItem() != ItemType.NONE) {
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

		// extract items if it is the right time
		if (extractTicks++ > extractTimeout) {
			extract();
			extractTicks = 0;
		}
	}
}
