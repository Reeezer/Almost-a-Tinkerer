package ch.hearc.p2.aatinkerer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Contract
{
	private String description;

	private Map<ItemType, Integer> requestedItems;

	public Contract()
	{
		this("A default contract but without much to do...");
	}

	public Contract(String description)
	{
		this.description = description;
		requestedItems = new HashMap<ItemType, Integer>();
	}

	public void addRequestedItem(ItemType item, int amount)
	{
		this.requestedItems.put(item, amount);
	}

	public Map<ItemType, Integer> getRequestedItems()
	{
		return requestedItems;
	}

}
