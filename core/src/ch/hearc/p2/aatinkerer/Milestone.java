package ch.hearc.p2.aatinkerer;

import java.util.LinkedList;
import java.util.List;

import ch.hearc.p2.aatinkerer.buildings.FactoryType;

public enum Milestone
{
	// TODO add milestone description to tell the player what just happened
	START(FactoryType.CONVEYOR, FactoryType.EXTRACTOR),
	UNLOCK_CUTTERTUNNEL(FactoryType.CUTTER, FactoryType.TUNNEL),
	UNLOCK_FURNACE(FactoryType.FURNACE),
	UNLOCK_MIXER(FactoryType.MIXER),
	UNLOCK_PRESS(FactoryType.PRESS),
	UNLOCK_ASSEMBLERMERGER(FactoryType.ASSEMBLER, FactoryType.MERGER),
	UNLOCK_SPLITTER(FactoryType.SPLITTER),
	UNLOCK_TRASH(FactoryType.TRASH);
	
	private final List<FactoryType> factoryTypes;

	private Milestone(FactoryType ...types)
	{
		factoryTypes = new LinkedList<FactoryType>();
		
		for (FactoryType factoryType : types)
			factoryTypes.add(factoryType);
	}

	public List<FactoryType> getUnlockedFactoryTypes()
	{
		return factoryTypes;
	}
	
}
