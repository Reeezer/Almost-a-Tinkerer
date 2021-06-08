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

// singleton
public class GameManager
{
	private static GameManager instance = null;

	private List<MilestoneListener> milestoneListeners;
	private List<ContractListener> contractListeners;
	private List<MoneyListener> moneyListeners;

	private Map<ItemType, Integer> producedItems;
	private int money;

	// the int is an index for the two other arrays that are both of the same size
	// and match
	private int contractMilestoneIndex;
	private ArrayList<Contract> storyContracts;
	private ArrayList<Milestone> storyMilestones;

	public static GameManager init()
	{
		return (instance = new GameManager());
	}

	public static GameManager getInstance()
	{
		if (instance != null)
			return instance;
		else
			return init();
	}

	private GameManager()
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
		storyContracts.add(new Contract("Hi! Welcome to almost a tinkerer"));
		storyMilestones.add(Milestone.START);

		Contract cutterContract = new Contract("We need some wood! Please cut some trees");
		// cutterContract.addRequestedItem(ItemType.WOODLOG, 10);
		storyContracts.add(cutterContract);
		storyMilestones.add(Milestone.UNLOCK_CUTTERTUNNEL);

		Contract furnaceTrashContract = new Contract("Nice! Now please make some planks and sticks so we can make chairs");
		// furnaceTrashContract.addRequestedItem(ItemType.PLANK, 10);
		// furnaceTrashContract.addRequestedItem(ItemType.STICK, 10);
		storyContracts.add(furnaceTrashContract);
		storyMilestones.add(Milestone.UNLOCK_FURNACETRASH);

		Contract mixerContract = new Contract("I can finally sit! Now please make some iron");
		// mixerContract.addRequestedItem(ItemType.IRONPLATE, 10);
		storyContracts.add(mixerContract);
		storyMilestones.add(Milestone.UNLOCK_MIXER);

		Contract pressContract = new Contract("Our company is starting to take off! Nice! We need a runway for planes, so please make concrete");
		// pressContract.addRequestedItem(ItemType.CONCRETE, 10);
		storyContracts.add(pressContract);
		storyMilestones.add(Milestone.UNLOCK_PRESS);

		Contract assemblerMergerContract = new Contract("We have a lot of materials, but the problem is that we cannot assemble them, we need glue. Please compress some petroleum. Yes, exactly.");
		// assemblerMergerContract.addRequestedItem(ItemType.GLUE, 10);
		storyContracts.add(assemblerMergerContract);
		storyMilestones.add(Milestone.UNLOCK_ASSEMBLERMERGER);

		Contract splitterContract = new Contract("Now we can finally glue everything together! Please make furniture");
		splitterContract.addRequestedItem(ItemType.CHAIR, 1);
		splitterContract.addRequestedItem(ItemType.FABRIC, 1); // FIXME peut être l'unlock avant comme ça on a
																// déjà la production de matières premières pour
																// les meubles?
		splitterContract.addRequestedItem(ItemType.DESK, 1);
		splitterContract.addRequestedItem(ItemType.TABLE, 1);
		splitterContract.addRequestedItem(ItemType.BED, 1);
		splitterContract.addRequestedItem(ItemType.SHELF, 1);
		splitterContract.addRequestedItem(ItemType.PLANT, 1);
		splitterContract.addRequestedItem(ItemType.TABLE, 1);
		storyContracts.add(splitterContract);
		storyMilestones.add(Milestone.UNLOCK_SPLITTER);
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

	public void unlockContract(Contract contract, boolean isStory)
	{
		System.out.println("Adding new contract: " + contract);
		for (ContractListener listener : contractListeners)
			listener.contractAdded(contract, isStory);
	}

	public void unlockMilestone(Milestone milestone)
	{
		System.out.println("Unlocking new milestone: " + milestone);
		for (MilestoneListener listener : milestoneListeners)
			listener.unlockMilestone(milestone);
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
				unlockMilestone(currentAttemptedMilestone);
				contractMilestoneIndex++;

				// si il existe, définir le prochain contrat
				if (contractMilestoneIndex < storyMilestones.size())
					unlockContract(storyContracts.get(contractMilestoneIndex), true);
			}
		}

		// TODO 2 - check extra contracts conditions

		// TODO 3 - reward the player on extra contract completion

		// TODO 4 - generate additional extra contracts every once in a while
	}
}