package ch.hearc.p2.aatinkerer.buildings;

public enum FactoryType
{
	CONVEYOR,
	EXTRACTOR,
	FURNACE,
	CUTTER,
	PRESS,
	MIXER,
	ASSEMBLER,
	TRASH,
	SPLITTER,
	MERGER,
	TUNNEL,
	NONE;
	
	private int level;
	
	private FactoryType()
	{
		level = 1;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void levelUp()
	{
		level++;
	}
}
