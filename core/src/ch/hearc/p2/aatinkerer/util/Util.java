package ch.hearc.p2.aatinkerer.util;

public class Util
{
	public static int clamp(int value, int min, int max)
	{
		return (((value <= max) ? value : max) >= min) ? value : min;
	}
}
