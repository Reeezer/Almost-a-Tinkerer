package ch.hearc.p2.aatinkerer.data;

public enum Difficulty
{
	REGULAR(100, 10, 0.6), //
	ABUNDANT(400, 10, 0.6), //
	RARE(10, 3, 0.6), //
	BIGSPARSE(10, 30, 0.85), //
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
