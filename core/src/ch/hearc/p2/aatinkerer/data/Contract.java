package ch.hearc.p2.aatinkerer.data;

import java.util.HashMap;
import java.util.Map;

public class Contract
{
	private String description;

	private Map<ItemType, Integer> requestedItems;
	private Map<ItemType, Integer> producedItems;

	public Contract()
	{
		this("A default contract but without much to do...");
	}

	public Contract(String description)
	{
		this.description = description;
		requestedItems = new HashMap<ItemType, Integer>();
		producedItems = new HashMap<ItemType, Integer>();
	}

	public String description()
	{
		return this.description;
	}

	public void addRequestedItem(ItemType item, int amount)
	{
		this.requestedItems.put(item, amount);
	}

	public Map<ItemType, Integer> getRequestedItems()
	{
		return requestedItems;
	}

	public void addProducedItem(ItemType type)
	{
		addProducedItem(type, 1);
	}

	public void addProducedItem(ItemType type, int amount)
	{
		if (requestedItems.containsKey(type))
		{
			if (producedItems.containsKey(type))
				producedItems.put(type, producedItems.get(type) + amount);
			else
				producedItems.put(type, amount);
		}
	}

	public int producedItems(ItemType type)
	{
		if (producedItems.containsKey(type))
			return producedItems.get(type);
		return 0;
	}

	public boolean isItemFulfilled(ItemType type)
	{
		if (requestedItems.containsKey(type))
		{
			int amount = requestedItems.get(type);
			return producedItems.containsKey(type) && producedItems.get(type) >= amount;
		}

		else
		{
			return false;
		}
	}

	public boolean isFulfilled()
	{
		boolean success = true;

		for (Map.Entry<ItemType, Integer> requestedItem : requestedItems.entrySet())
		{
			ItemType itemType = requestedItem.getKey();

			// si un seul des items n'est pas produit en quantité suffisante on arrête TOUT
			if (!isItemFulfilled(itemType))
				success = false;
		}

		return success;
	}
}
