package ch.hearc.p2.aatinkerer.util;

import ch.hearc.p2.aatinkerer.world.Chunk;

public class Util
{
	public static int clamp(int value, int min, int max)
	{
		return (((value <= max) ? value : max) >= min) ? value : min;
	}
	
	// modulo that also works on negative numbers
	// in java -1 % 64 = -1 but we want = 63
	public static int negmod(int value, int modulo)
	{
		int result = value % modulo;

		if (result < 0)
			result += modulo;
		
		return result;
	}
}
