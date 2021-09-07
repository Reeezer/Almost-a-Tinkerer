package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Merger extends Building
{
	private boolean toggle;

	public Merger(TileMap tilemap, int x, int y, int direction, boolean mirrored)
	{
		super(tilemap, x, y, direction, 1, 1, FactoryType.MERGER);
		if (!mirrored)
			this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 1) % 4 } };
		else
			this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 }, { x, y, (direction + 3) % 4 } };
		this.outputPosition = new int[] { x, y, direction };
		this.toggle = true;
		this.mirrored = mirrored;
	}

	@Override
	public void transferItem()
	{
		Item itemToTransfer = toggle ? items.peekLast() : items.peekFirst();

		if (output != null && contentSize > 0)
		{
			if (type == FactoryType.ASSEMBLER || type == FactoryType.CUTTER || type == FactoryType.FURNACE || type == FactoryType.MIXER || type == FactoryType.PRESS)
			{
				if (!output.isFull(itemToTransfer))
					checkRecipes();
			}
			else
			{
				if (!output.isFull(itemToTransfer) && !itemToTransfer.justTransfered)
				{
					Item item = toggle ? items.pollLast() : items.pollFirst();
					currentIngredients.put(item.type, currentIngredients.get(item.type) - 1);
					toggle = !toggle;
					contentSize--;
					output.addItem(item);
				}
			}
		}
	}
}
