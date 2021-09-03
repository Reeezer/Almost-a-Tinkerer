package ch.hearc.p2.aatinkerer.listeners;

import ch.hearc.p2.aatinkerer.data.Contract;

public interface ContractListener
{
	public void contractAdded(Contract contract, boolean isStoryContract, boolean notify);
}
