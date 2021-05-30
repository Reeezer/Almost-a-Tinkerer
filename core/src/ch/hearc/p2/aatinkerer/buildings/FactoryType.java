package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.p2.aatinkerer.ui.HoverableItem;
import ch.hearc.p2.aatinkerer.ui.ToolbarItem;

// TODO add support for rotations of hover elements for the Merger, Splitter, furnace, mixer
public enum FactoryType implements ToolbarItem, HoverableItem
{
	CONVEYOR("Ui/Icons/ConveyorIcon.png", "Ui/Icons/ConveyorHover.png"), //
	EXTRACTOR("Ui/Icons/ExtractorIcon.png", "Ui/Icons/ExtractorHover.png"), //
	FURNACE("Ui/Icons/FurnaceIcon.png", "Ui/Icons/FurnaceHover.png"), //
	PRESS("Ui/Icons/PressIcon.png", "Ui/Icons/PressHover.png"), //
	CUTTER("Ui/Icons/CutterIcon.png", "Ui/Icons/CutterHover.png"), //
	ASSEMBLER("Ui/Icons/AssemblerIcon.png", "Ui/Icons/AssemblerHover.png"), //
	MERGER("Ui/Icons/MergerIcon.png", "Ui/Icons/MergerLeftHover.png"), //
	SPLITTER("Ui/Icons/SplitterIcon.png", "Ui/Icons/SplitterLeftHover.png"), //
	TUNNEL("Ui/Icons/TunnelInIcon.png", "Ui/Icons/TunnelInHover.png"), //
	MIXER("Ui/Icons/MixerIcon.png", "Ui/Icons/MixerHover.png"), //
	TRASH("Ui/Icons/TrashIcon.png", "Ui/Icons/TrashHover.png");

	private Texture itemTexture;
	private Texture hoverTexture;
	private boolean enabled; // obligatoire par l'interface ToolbarItem mais on en profite pour l'utiliser pour savoir si on a débloqué la factory en essayant de la placer
	private int level;
	private int transferTimeout;
	private int ticks;

	private FactoryType(String itemTexturePath, String hoverTexturePath)
	{
		itemTexture = new Texture(Gdx.files.internal(itemTexturePath));
		hoverTexture = new Texture(Gdx.files.internal(hoverTexturePath));
		level = 1;
		transferTimeout = 70;
		ticks = 0;
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

	public void ticksIncrease()
	{
		ticks++;
	}

	public int getTicks()
	{
		return ticks;
	}

	public void resetTicks()
	{
		ticks = 0;
	}

	public int getTransferTimeout()
	{
		return (transferTimeout - level * 5) < 10 ? 10 : transferTimeout - level * 5;
	}
}
