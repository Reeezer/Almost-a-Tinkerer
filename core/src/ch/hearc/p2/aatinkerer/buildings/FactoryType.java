package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.p2.aatinkerer.ui.HoverableItem;
import ch.hearc.p2.aatinkerer.ui.ToolbarItem;

// TODO add support for rotations of hover elements for the Merger, Splitter, furnace, mixer
public enum FactoryType implements ToolbarItem, HoverableItem
{
	CONVEYOR("Ui/Icons/ConveyorIcon.png", "Ui/Icons/ConveyorHover.png", 8, 3), //
	EXTRACTOR("Ui/Icons/ExtractorIcon.png", "Ui/Icons/ExtractorHover.png", 8), //
	FURNACE("Ui/Icons/FurnaceIcon.png", "Ui/Icons/FurnaceHover.png", 8), //
	PRESS("Ui/Icons/PressIcon.png", "Ui/Icons/PressHover.png", 6), //
	CUTTER("Ui/Icons/CutterIcon.png", "Ui/Icons/CutterHover.png", 4), //
	ASSEMBLER("Ui/Icons/AssemblerIcon.png", "Ui/Icons/AssemblerHover.png", 9), //
	MERGER("Ui/Icons/MergerIcon.png", "Ui/Icons/MergerLeftHover.png", 1), //
	SPLITTER("Ui/Icons/SplitterIcon.png", "Ui/Icons/SplitterLeftHover.png", 1), //
	TUNNEL("Ui/Icons/TunnelInIcon.png", "Ui/Icons/TunnelInHover.png", 1), //
	MIXER("Ui/Icons/MixerIcon.png", "Ui/Icons/MixerHover.png", 6), //
	TRASH("Ui/Icons/TrashIcon.png", "Ui/Icons/TrashHover.png", 7);

	private Texture itemTexture;
	private Texture hoverTexture;

	private boolean enabled; // obligatoire par l'interface ToolbarItem mais on en profite pour l'utiliser pour savoir si on a débloqué la factory en essayant de la placer

	private int level;

	private int transferTimeout;
	private int transferTicks;

	private int currentFrame;
	private int maxFrame;
	private int animationTicks;
	private int animationTimeout;

	private FactoryType(String itemTexturePath, String hoverTexturePath, int maxFrame, int animationTimeout)
	{
		itemTexture = new Texture(Gdx.files.internal(itemTexturePath));
		hoverTexture = new Texture(Gdx.files.internal(hoverTexturePath));

		level = 1;

		transferTimeout = 70;
		transferTicks = 0;

		currentFrame = 0;
		this.maxFrame = maxFrame;
		animationTicks = 0;
		this.animationTimeout = animationTimeout;
	}

	private FactoryType(String itemTexturePath, String hoverTexturePath, int maxFrame)
	{
		this(itemTexturePath, hoverTexturePath, maxFrame, 8);
	}

	@Override
	public Texture getItemTexture()
	{
		return itemTexture;
	}

	@Override
	public Texture getHoverTexture()
	{
		return hoverTexture;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	@Override
	public boolean enabled()
	{
		return this.enabled;
	}

	public int getLevel()
	{
		return level;
	}

	public void levelUp()
	{
		level++;
	}

	public void transferTicksIncrease()
	{
		transferTicks++;
	}

	public int getTransferTicks()
	{
		return transferTicks;
	}

	public void resetTransferTicks()
	{
		transferTicks = 0;
	}

	public int getTransferTimeout()
	{
		return (transferTimeout - level * 5) < 10 ? 10 : transferTimeout - level * 5;
	}

	public int getCurrentFrame()
	{
		return currentFrame;
	}

	public void frameIncrease()
	{
		currentFrame = (currentFrame + 1) % maxFrame;
	}

	public int getAnimationTimeout()
	{
		return animationTimeout;
	}

	public void animationTicksIncrease()
	{
		animationTicks++;
	}

	public int getAnimationTicks()
	{
		return animationTicks;
	}

	public void resetAnimationTicks()
	{
		animationTicks = 0;
	}
}
