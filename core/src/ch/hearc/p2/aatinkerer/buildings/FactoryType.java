package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.p2.aatinkerer.ui.HoverableItem;
import ch.hearc.p2.aatinkerer.ui.ToolbarItem;

// TODO add support for rotations of hover elements for the Merger and the Splitter
public enum FactoryType implements ToolbarItem, HoverableItem
{
	CONVEYOR("Ui/Icons/ConveyorIcon.png", "Ui/Icons/ConveyorHover.png"),
	EXTRACTOR("Ui/Icons/ExtractorIcon.png", "Ui/Icons/ExtractorHover.png"),
	PRESS("Ui/Icons/PressIcon.png", "Ui/Icons/PressHover.png"),
	FURNACE("Ui/Icons/FurnaceIcon.png", "Ui/Icons/FurnaceHover.png"),
	CUTTER("Ui/Icons/CutterIcon.png", "Ui/Icons/CutterHover.png"),
	ASSEMBLER("Ui/Icons/AssemblerIcon.png", "Ui/Icons/AssemblerHover.png"),
	MERGER("Ui/Icons/MergerIcon.png", "Ui/Icons/MergerLeftHover.png"),
	SPLITTER("Ui/Icons/SplitterIcon.png", "Ui/Icons/SplitterLeftHover.png"),
	TUNNEL("Ui/Icons/TunnelInIcon.png", "Ui/Icons/TunnelInHover.png"),
	MIXER("Ui/Icons/MixerIcon.png", "Ui/Icons/MixerHover.png"),
	TRASH("Ui/Icons/TrashIcon.png", "Ui/Icons/TrashHover.png");
	
	private Texture itemTexture;
	private Texture hoverTexture;
	private int level;

	private FactoryType(String itemTexturePath, String hoverTexturePath)
	{
		itemTexture = new Texture(Gdx.files.internal(itemTexturePath));
		hoverTexture = new Texture(Gdx.files.internal(hoverTexturePath));
		level = 1;
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
	
	public int getLevel()
	{
		return level;
	}
	
	public void levelUp()
	{
		level++;
	}
}
