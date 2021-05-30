package ch.hearc.p2.aatinkerer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// singleton
public class ContractManager
{
	private static ContractManager instance = null;
	
	private List<MilestoneListener> milestoneListeners;
	
	private Map<ItemType, Integer> producedItems; 
	
	// the int is an index for the two other arrays that are both of the same size and match
	private int contractMilestoneIndex;
	private ArrayList<Contract> storyContracts;
	private ArrayList<Milestone> storyMilestones;
	
	public static ContractManager init()
	{
		return (instance = new ContractManager());
	}
	
	public static ContractManager getInstance()
	{
		if (instance != null)
			return instance;
		else
			return init();
	}

	private ContractManager()
	{
		milestoneListeners = new LinkedList<MilestoneListener>();
		
		producedItems = new HashMap<ItemType, Integer>();
		
		contractMilestoneIndex = 0;
		storyContracts = new ArrayList<Contract>();
		storyMilestones = new ArrayList<Milestone>();
		
		// No actual requirements for the first contract so we unlock everything we need
		storyContracts.add(new Contract("Hi! Welcome to almost a tinkerer"));
		storyMilestones.add(Milestone.START);
		
		Contract cutterContract = new Contract("We need some wood! Please cut some trees");
		cutterContract.addRequestedItem(ItemType.WOODLOG, 40);
		storyContracts.add(cutterContract);
		storyMilestones.add(Milestone.UNLOCK_CUTTERTUNNEL);
		
		Contract furnaceContract = new Contract("Nice! Now please make some planks and sticks so we can make chairs");
		furnaceContract.addRequestedItem(ItemType.PLANK, 40);
		furnaceContract.addRequestedItem(ItemType.STICK, 80);
		storyContracts.add(furnaceContract);
		storyMilestones.add(Milestone.UNLOCK_FURNACE);
		
		Contract mixerContract = new Contract("I can finally sit! Now please make some iron");
		mixerContract.addRequestedItem(ItemType.IRONPLATE, 100);
		storyContracts.add(mixerContract);
		storyMilestones.add(Milestone.UNLOCK_MIXER);
		
		Contract pressContract = new Contract("Our company is starting to take off! Nice! We need a runway for planes, so please make concrete");
		pressContract.addRequestedItem(ItemType.CONCRETE, 200);
		storyContracts.add(pressContract);
		storyMilestones.add(Milestone.UNLOCK_PRESS);
		
		Contract assemblerContract = new Contract("We have a lot of materials, but the problem is that we cannot assemble them, we need glue. Please compress some petroleum. Yes, exactly.");
		assemblerContract.addRequestedItem(ItemType.GLUE, 200);
		storyContracts.add(assemblerContract);
		storyMilestones.add(Milestone.UNLOCK_ASSEMBLERMERGER);
		
		Contract splitterMergerContract = new Contract("Now we can finally glue everything together! Please make furniture");
		splitterMergerContract.addRequestedItem(ItemType.CHAIR, 200);
		splitterMergerContract.addRequestedItem(ItemType.FABRIC, 20); // FIXME peut être l'unlock avant comme ça on a déjà la production de matières premières pour les meubles?
		splitterMergerContract.addRequestedItem(ItemType.DESK, 100);
		splitterMergerContract.addRequestedItem(ItemType.TABLE, 50);
		splitterMergerContract.addRequestedItem(ItemType.BED, 30);
		splitterMergerContract.addRequestedItem(ItemType.SHELF, 50);
		splitterMergerContract.addRequestedItem(ItemType.PLANT, 80);
		splitterMergerContract.addRequestedItem(ItemType.TABLE, 50);
		storyContracts.add(splitterMergerContract);
		storyMilestones.add(Milestone.UNLOCK_SPLITTER);
	}
	
	public void itemDelivered(ItemType type)
	{
		if (producedItems.containsKey(type))
			producedItems.put(type, producedItems.get(type) + 1);
		else
			producedItems.put(type, 1);
		
		// TODO mettre à jour les contrats secondaires
	}
	
	public void addMilestoneListener(MilestoneListener listener)
	{
		milestoneListeners.add(listener);
	}
	
	public void unlockMilestone(Milestone milestone)
	{
		System.out.println("Unlocking new milestone: " + milestone);
		for (MilestoneListener listener : milestoneListeners)
			listener.unlockMilestone(milestone);
	}

	public void tick()
	{
		Milestone currentAttemptedMilestone = storyMilestones.get(contractMilestoneIndex);
		Contract ongoingStoryContract = storyContracts.get(contractMilestoneIndex);
	
		// 1 - advance story contracts and unlock factories
		if (contractMilestoneIndex < storyMilestones.size())
		{
			boolean success = true;
			
			for (Map.Entry<ItemType, Integer> requestedItem : ongoingStoryContract.getRequestedItems().entrySet())
			{
				ItemType itemType = requestedItem.getKey();
				int amount = requestedItem.getValue();
				
				// si un seul des items n'est pas produit en quantité suffisante on arrête TOUT
				if (!producedItems.containsKey(itemType) || producedItems.get(itemType) < amount)
					success = false;
			}
			
			if (success)
			{
				unlockMilestone(currentAttemptedMilestone);
				contractMilestoneIndex++;
			}
		}
		
		// 2 - check extra contracts conditions
		// TODO
		
		// 4 - reward the player on extra contract completion
		// TODO
		
		// 5 - generate additional extra contracts every once in a while
		// TODO
	}
}
