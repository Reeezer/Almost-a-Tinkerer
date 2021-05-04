package ch.hearc.p2.aatinkerer;

import java.util.Map;

public class Recipe
{
	private Map<ItemType, Integer> ingredients;
	private ItemType product;
	
	public Recipe (ItemType product)
	{
		this.product = product;
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
}
