package ch.hearc.p2.aatinkerer.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hearc.p2.aatinkerer.data.Contract;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Milestone;
import ch.hearc.p2.aatinkerer.listeners.ContractListener;
import ch.hearc.p2.aatinkerer.listeners.MilestoneListener;
import ch.hearc.p2.aatinkerer.listeners.MoneyListener;
import ch.hearc.p2.aatinkerer.util.Sounds;

// singleton
public class GameManager
{
	private static GameManager instance = null;

	private List<MilestoneListener> milestoneListeners;
	private List<ContractListener> contractListeners;
	private List<MoneyListener> moneyListeners;

	private HashMap<ItemType, Integer> producedItems;
	private int money;

	// the int is an index for the two other arrays that are both of the same size
	// and match
	private int contractMilestoneIndex;
	private ArrayList<Contract> storyContracts;
	private ArrayList<Milestone> storyMilestones;

	public static GameManager getInstance()
	{
		if (instance != null)
			return instance;
		else
			return (instance = new GameManager());
	}

	private GameManager()
	{
		reset();
	}

	public void reset()
	{
		milestoneListeners = new LinkedList<MilestoneListener>();
		contractListeners = new LinkedList<ContractListener>();
		moneyListeners = new LinkedList<MoneyListener>();

		producedItems = new HashMap<ItemType, Integer>();
		money = 0;

		contractMilestoneIndex = 0;
		storyContracts = new ArrayList<Contract>();
		storyMilestones = new ArrayList<Milestone>();

		// No actual requirements for the first contract so we unlock everything we need
		storyContracts.add(new Contract()); // empty contract for the start
		storyMilestones.add(Milestone.START);

		Contract cutterContract = new Contract("We need some wood! Place an extractor where there's wood and transport it all the way to the hub at the center of the map using conveyors. Hover the ressources on the map to know their names.");
		//cutterContract.addRequestedItem(ItemType.WOODLOG, 10);
		storyContracts.add(cutterContract);
		storyMilestones.add(Milestone.UNLOCK_CUTTERTUNNEL);

		Contract furnaceTrashContract = new Contract("Nice! Now please make some planks and sticks so we can make chairs, use a cutter to cut the logs into planks, and planks into sticks.");
		//furnaceTrashContract.addRequestedItem(ItemType.PLANK, 10);
		//furnaceTrashContract.addRequestedItem(ItemType.STICK, 10);
		storyContracts.add(furnaceTrashContract);
		storyMilestones.add(Milestone.UNLOCK_FURNACETRASH);

		Contract mixerContract = new Contract("I can finally sit! Now please make some iron and copper plates by smelting their ores using coal in a furnace. Once you're done, use a cutter to make iron rods and copper wire.");
		//mixerContract.addRequestedItem(ItemType.IRONPLATE, 10);
		//mixerContract.addRequestedItem(ItemType.IRONROD, 30);
		//mixerContract.addRequestedItem(ItemType.COPPERPLATE, 10);
		//mixerContract.addRequestedItem(ItemType.COPPERWIRE, 30);
		storyContracts.add(mixerContract);
		storyMilestones.add(Milestone.UNLOCK_MIXER);

		Contract pressContract = new Contract("Our company is starting to take off! Nice! We need a runway for planes, so please make concrete. You can use the mixer to mix stones and water, which will produce concrete.");
		//pressContract.addRequestedItem(ItemType.CONCRETE, 10);
		storyContracts.add(pressContract);
		storyMilestones.add(Milestone.UNLOCK_PRESS);


		Contract assemblerMergerContract = new Contract("We want to make pencils, but also glue furniture together. That means we need graphite and glue, both can be made with the press, glue needs petroleum, graphite needs coal.");
		//assemblerMergerContract.addRequestedItem(ItemType.GLUE, 10);
		//assemblerMergerContract.addRequestedItem(ItemType.GRAPHITE, 10);
		storyContracts.add(assemblerMergerContract);
		storyMilestones.add(Milestone.UNLOCK_ASSEMBLERMERGER);

		Contract splitterContract = new Contract("Now we can finally glue everything together to make furniture! Place an assembler to select what you want to make, click on it, select a recipe you want to make and it will display the required ingredients.");
		//splitterContract.addRequestedItem(ItemType.PENCIL, 500);
		//splitterContract.addRequestedItem(ItemType.DESK, 10);
		//splitterContract.addRequestedItem(ItemType.TABLE, 10);
		//splitterContract.addRequestedItem(ItemType.BED, 2);
		//splitterContract.addRequestedItem(ItemType.SHELF, 8);
		//splitterContract.addRequestedItem(ItemType.PLANT, 20);
		//splitterContract.addRequestedItem(ItemType.CHAIR, 30);
		//splitterContract.addRequestedItem(ItemType.LAMP, 10);
		//splitterContract.addRequestedItem(ItemType.CARPET, 20);
		storyContracts.add(splitterContract);
		storyMilestones.add(Milestone.UNLOCK_SPLITTER);

		Contract finalContract = new Contract("Thank you for making all the nice furniture! We actually don't have anything else for you to make, just do what you want now.");
		storyContracts.add(finalContract);
		storyMilestones.add(Milestone.END_STORY);
	}
	
	public void itemDelivered(ItemType type)
	{
		if (producedItems.containsKey(type))
			producedItems.put(type, producedItems.get(type) + 1);
		else
			producedItems.put(type, 1);

		receiveMoney(type.value());

		if (contractMilestoneIndex < storyMilestones.size())
		{
			Contract ongoingStoryContract = storyContracts.get(contractMilestoneIndex);
			ongoingStoryContract.addProducedItem(type);
		}

		// TODO mettre à jour les contrats secondaires
	}

	public HashMap<ItemType, Integer> getProducedItems()
	{
		return producedItems;
	}

	public int getContractMilestoneIndex()
	{
		return contractMilestoneIndex;
	}

	public void setProgress(int contractMilestoneIndex, HashMap<ItemType, Integer> producedItems)
	{		
		this.contractMilestoneIndex = contractMilestoneIndex;

		for (int i = 0; i < contractMilestoneIndex; i++)
		{
			unlockMilestone(storyMilestones.get(i), false);
		}

		// update current's contract progress
		if (contractMilestoneIndex < storyMilestones.size())
		{
			Contract currentContract = storyContracts.get(contractMilestoneIndex);
			for (Map.Entry<ItemType, Integer> producedItemsEntry : producedItems.entrySet())
			{
				ItemType type = producedItemsEntry.getKey();
				int amount = producedItemsEntry.getValue();
				
				System.out.format("Adding produced %d units of item %s to contract %s%n", amount, type, currentContract);

				currentContract.addProducedItem(type, amount);
			}
			
			unlockContract(currentContract, true, false);
		}
		
		this.producedItems = new HashMap<ItemType, Integer>(producedItems);		
	}

	public void addMilestoneListener(MilestoneListener listener)
	{
		milestoneListeners.add(listener);
	}

	public void addContractListener(ContractListener listener)
	{
		contractListeners.add(listener);
	}

	public void addMoneyListener(MoneyListener listener)
	{
		moneyListeners.add(listener);
	}

	public void unlockContract(Contract contract, boolean isStory, boolean notify)
	{
		System.out.println("Adding new contract: " + contract);
		for (ContractListener listener : contractListeners)
			listener.contractAdded(contract, isStory, notify);
	}

	public void unlockMilestone(Milestone milestone, boolean notify)
	{
		System.out.println("Unlocking new milestone: " + milestone);
		for (MilestoneListener listener : milestoneListeners)
			listener.unlockMilestone(milestone, notify);
	}

	public void receiveMoney(int amount)
	{
		this.money += amount;

		System.out.println("Received money: " + amount + ", total money: " + this.money);
		for (MoneyListener listener : moneyListeners)
			listener.moneyReceived(amount);
	}

	public void tick()
	{
		// 1 - advance story contracts and unlock factories
		if (contractMilestoneIndex < storyMilestones.size())
		{
			Milestone currentAttemptedMilestone = storyMilestones.get(contractMilestoneIndex);
			Contract ongoingStoryContract = storyContracts.get(contractMilestoneIndex);

			if (ongoingStoryContract.isFulfilled())
			{
				// important : d'abord unlock la milestone, comme ça on a le texte des contrats
				// dans les popups après
				unlockMilestone(currentAttemptedMilestone, true);
				contractMilestoneIndex++;

				// si il existe, définir le prochain contrat
				if (contractMilestoneIndex < storyMilestones.size())
					unlockContract(storyContracts.get(contractMilestoneIndex), true, true);

				Sounds.ACHIEVE.play();
			}
		}

		// TODO 2 - check extra contracts conditions

		// TODO 3 - reward the player on extra contract completion

		// TODO 4 - generate additional extra contracts every once in a while
	}
}
