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

		Contract cutterTunnelContract = new Contract("We need some wood! Please cut some trees");
		cutterTunnelContract.addRequestedItem(ItemType.WOODLOG, 1);
		storyContracts.add(cutterTunnelContract);
		storyMilestones.add(Milestone.UNLOCK_CUTTERTUNNEL);

		Contract furnaceTrashContract = new Contract("Nice! Now please make some planks and sticks so we can make chairs");
		// furnaceTrashContract.addRequestedItem(ItemType.PLANK, 40);
		// furnaceTrashContract.addRequestedItem(ItemType.STICK, 80);
		storyContracts.add(furnaceTrashContract);
		storyMilestones.add(Milestone.UNLOCK_FURNACETRASH);

		Contract mixerContract = new Contract("I can finally sit! Now please make some iron");
		// mixerContract.addRequestedItem(ItemType.IRONPLATE, 100);
		storyContracts.add(mixerContract);
		storyMilestones.add(Milestone.UNLOCK_MIXER);

		Contract pressContract = new Contract("Our company is starting to take off! Nice! We need a runway for planes, so please make concrete");
		// pressContract.addRequestedItem(ItemType.CONCRETE, 200);
		storyContracts.add(pressContract);
		storyMilestones.add(Milestone.UNLOCK_PRESS);

		Contract assemblerMergerContract = new Contract("We have a lot of materials, but the problem is that we cannot assemble them, we need glue. Please compress some petroleum. Yes, exactly.");
		// assemblerMergerContract.addRequestedItem(ItemType.GLUE, 200);
		storyContracts.add(assemblerMergerContract);
		storyMilestones.add(Milestone.UNLOCK_ASSEMBLERMERGER);

		Contract splitterContract = new Contract("Now we can finally glue everything together! Please make furniture");
		// splitterContract.addRequestedItem(ItemType.CHAIR, 200);
		// splitterContract.addRequestedItem(ItemType.FABRIC, 20); // FIXME peut être l'unlock avant comme ça on a déjà la production de matières premières
		// pour les meubles?
		// splitterContract.addRequestedItem(ItemType.DESK, 100);
		// splitterContract.addRequestedItem(ItemType.TABLE, 50);
		// splitterContract.addRequestedItem(ItemType.BED, 30);
		// splitterContract.addRequestedItem(ItemType.SHELF, 50);
		// splitterContract.addRequestedItem(ItemType.PLANT, 80);
		// splitterContract.addRequestedItem(ItemType.TABLE, 50);
		storyContracts.add(splitterContract);
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
		for (MilestoneListener listener : milestoneListeners)
			listener.unlockMilestone(milestone);
	}

	public void tick()
	{
		// 1 - advance story contracts and unlock factories
		if (contractMilestoneIndex < storyMilestones.size()) {
			Milestone currentAttemptedMilestone = storyMilestones.get(contractMilestoneIndex);
			Contract ongoingStoryContract = storyContracts.get(contractMilestoneIndex);

			boolean success = true;

			for (Map.Entry<ItemType, Integer> requestedItem : ongoingStoryContract.getRequestedItems().entrySet()) {
				ItemType itemType = requestedItem.getKey();
				int amount = requestedItem.getValue();

				// si un seul des items n'est pas produit en quantité suffisante on arrête TOUT
				if (!producedItems.containsKey(itemType) || producedItems.get(itemType) < amount)
					success = false;
			}

			if (success) {
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
