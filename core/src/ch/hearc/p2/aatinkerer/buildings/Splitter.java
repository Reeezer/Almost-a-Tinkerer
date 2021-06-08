package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Splitter extends Building
{
	protected Building secondOutput;
	protected int[] secondOutputPosition;

	private ItemType splitType;
	
	public Splitter(TileMap tilemap, int x, int y, int direction, boolean mirrored)
	{
		super(tilemap, x, y, direction, 1, (mirrored ? "Tile/SplitterMirror/" : "Tile/Splitter/"), 1, 1, FactoryType.SPLITTER);
		this.inputPositions = new int[][] { { x, y, (direction + 2) % 4 } };
		this.outputPosition = new int[] { x, y, direction };
		if (!mirrored)
			this.secondOutputPosition = new int[] { x, y, (direction + 1) % 4 };
		else
			this.secondOutputPosition = new int[] { x, y, (direction + 3) % 4 };

		this.splitType = ItemType.NONE;
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
		// FIXME need an item to be selected
		Item itemToTransfer = items.peek();
		
		if (itemToTransfer != null && this.splitType == itemToTransfer.type && secondOutput != null && !secondOutput.isFull(itemToTransfer) && contentSize > 0 && !items.peek().justTransfered)
		{
			Item item = items.poll();
			contentSize--;
			secondOutput.addItem(item);
		}
		
		super.transferItem();
	}
	
	public void setSplitType(ItemType type)
	{
		this.splitType = type;
	}
	
	public ItemType splitType()
	{
		return this.splitType;
	}
}