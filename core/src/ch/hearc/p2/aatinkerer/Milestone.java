package ch.hearc.p2.aatinkerer;

import java.util.LinkedList;
import java.util.List;

import ch.hearc.p2.aatinkerer.buildings.FactoryType;

public enum Milestone
{
	START("Unlocks base items", FactoryType.CONVEYOR, FactoryType.EXTRACTOR),
	UNLOCK_CUTTERTUNNEL("The cutter and tunnel are now unlocked", FactoryType.CUTTER, FactoryType.TUNNEL),
	UNLOCK_FURNACETRASH("The furnace is now unlocked", FactoryType.FURNACE),
	UNLOCK_MIXER("The mixer is now unlocked", FactoryType.MIXER),
	UNLOCK_PRESS("The press is now unlocked", FactoryType.PRESS),
	UNLOCK_ASSEMBLERMERGER("The assembler and the merger are now unlocked", FactoryType.ASSEMBLER, FactoryType.MERGER),
	UNLOCK_SPLITTER("The splitter is now unlocked", FactoryType.SPLITTER);
	
	private final List<FactoryType> factoryTypes;
	private final String description;

	private Milestone(String description, FactoryType ...types)
	{
		this.description = description;
		factoryTypes = new LinkedList<FactoryType>();
		
		for (FactoryType factoryType : types)
			factoryTypes.add(factoryType);
	}

	public List<FactoryType> getUnlockedFactoryTypes()
	{
		return factoryTypes;
	}
	
	public String description()
	{
		return description;
	}
}
