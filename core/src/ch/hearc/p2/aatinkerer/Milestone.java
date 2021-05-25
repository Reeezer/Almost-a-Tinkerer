package ch.hearc.p2.aatinkerer;

import java.util.LinkedList;
import java.util.List;

import ch.hearc.p2.aatinkerer.buildings.FactoryType;

public enum Milestone
{
	// TODO add milestone description to tell the player what just happened
	START(FactoryType.CONVEYOR, FactoryType.EXTRACTOR),
	UNLOCK_CUTTER(FactoryType.CUTTER),
	UNLOCK_FURNACE(FactoryType.FURNACE),
	UNLOCK_MIXER(FactoryType.MIXER),
	UNLOCK_PRESS(FactoryType.PRESS),
	UNLOCK_ASSEMBLER(FactoryType.ASSEMBLER),
	UNLOCK_SPLITTERMERGER(FactoryType.SPLITTER, FactoryType.MERGER),
	UNLOCK_TRASH(FactoryType.TRASH),
	UNLOCK_TUNNEL(FactoryType.TUNNEL);
	
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
