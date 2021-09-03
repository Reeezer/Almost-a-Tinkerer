package ch.hearc.p2.aatinkerer.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Recipe implements Serializable
{
	private Map<ItemType, Integer> ingredients;
	private ItemType product;
	private int amount;

	public Recipe(ItemType product, int amount)
	{
		this.product = product;
		this.amount = amount;
		this.ingredients = new HashMap<ItemType, Integer>();
	}

	public Recipe(ItemType product)
	{
		this(product, 1);
	}

	public void addIngredient(ItemType ingredient)
	{
		ingredients.put(ingredient, 1);
	}

	public void addIngredient(ItemType ingredient, int amount)
	{
		ingredients.put(ingredient, amount);
	}

	public Map<ItemType, Integer> getIngredients()
	{
		return ingredients;
	}

	public ItemType getProduct()
	{
		return product;
	}

	public int getAmount()
	{
		return amount;
	}
	
	@Override
	public boolean equals(Object object)
	{
		Recipe recipe = (Recipe) object;
		
		boolean same = true;
		
		for (Map.Entry<ItemType, Integer> entry : recipe.ingredients.entrySet())
		{
			ItemType type = entry.getKey();
			int amount = entry.getValue();
			
			if (this.ingredients.containsKey(type))
			{
				if (this.ingredients.get(type) != amount)
					same = false;
			}
			else
			{
				same = false;
			}
		}
		
		return same;
	}
}
