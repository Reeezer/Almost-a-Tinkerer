package ch.hearc.p2.aatinkerer.buildings;

import ch.hearc.p2.aatinkerer.TileMap;

public class Splitter extends Building
{
	protected Building secondOutput;
	protected int[] secondOutputPosition;

	public Splitter(TileMap tilemap, int x, int y, int direction, boolean mirrored)
	{
		super(tilemap, x, y, direction, 1, (mirrored ? "Tile/SplitterMirror/" : "Tile/Splitter/"), 1, 1, FactoryType.SPLITTER);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPosition = new int[] { x, y, direction };
		if (!mirrored)
			this.secondOutputPosition = new int[] { x, y, (direction + 1) % 4 };
		else
			this.secondOutputPosition = new int[] { x, y, (direction + 3) % 4 };

		updateOutputs();
	}

	@Override
	public void updateOutputs()
	{
		super.updateOutputs();

		if (secondOutputPosition != null) 
			secondOutput = tilemap.getNeighbourBuilding(secondOutputPosition);
	}

	@Override
	public void transferItem()
	{
		super.transferItem();
		
		// FIXME need an item to be selected

		if (secondOutput != null && !secondOutput.isFull() && contentSize > 0 && !items.peek().justTransfered) {
			Item item = items.poll();
			contentSize--;
			secondOutput.addItem(item);
		}
	}
}