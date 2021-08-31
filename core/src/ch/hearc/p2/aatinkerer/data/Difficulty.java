package ch.hearc.p2.aatinkerer.data;

public enum Difficulty
{
	REGULAR(10, 10, 0.6), //
	ABUNDANT(40, 10, 0.6), //
	RARE(4, 3, 0.6), //
	BIGSPARSE(2, 14, 0.75), //
	EVERYWHERE(15000, 1, 0.1), //
	GOODLUCK(1, 1, 0); //

	private int nbSeed;
	private int life;
	private double probability;

	private Difficulty(int nbSeed, int life, double probability)
	{
		this.nbSeed = nbSeed;
		this.life = life;
		this.probability = probability;
	}

	public int getNbSeed()
	{
		return nbSeed;
	}

	public int getLife()
	{
		return life;
	}

	public double getProbability()
	{
		return probability;
	}
}
