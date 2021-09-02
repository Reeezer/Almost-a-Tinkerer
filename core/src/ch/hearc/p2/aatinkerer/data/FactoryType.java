package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.p2.aatinkerer.ui.HoverableItem;
import ch.hearc.p2.aatinkerer.ui.ToolbarItem;

public enum FactoryType implements ToolbarItem, HoverableItem
{
	CONVEYOR(new String[] { "Tile/Conveyor/", "Tile/ConveyorLeft/", "Tile/ConveyorRight/" }, 1, "Ui/Icons/ConveyorIcon.png", new String[] { "Ui/Hover/Conveyor.png" }, 8, 3), //
	EXTRACTOR(new String[] { "Tile/Extractor/" }, 1, "Ui/Icons/ExtractorIcon.png", new String[] { "Ui/Hover/Extractor.png" }, 8), //
	FURNACE(new String[] { "Tile/Furnace/", "Tile/FurnaceMirror/" }, 1, "Ui/Icons/FurnaceIcon.png", new String[] { "Ui/Hover/Furnace.png", "Ui/Hover/FurnaceMirror.png" }, 8), //
	PRESS(new String[] { "Tile/Press/" }, 1, "Ui/Icons/PressIcon.png", new String[] { "Ui/Hover/Press.png" }, 6), //
	CUTTER(new String[] { "Tile/Cutter/" }, 1, "Ui/Icons/CutterIcon.png", new String[] { "Ui/Hover/Cutter.png" }, 4), //
	ASSEMBLER(new String[] { "Tile/Assembler/" }, 3, "Ui/Icons/AssemblerIcon.png", new String[] { "Ui/Hover/Assembler.png" }, 9), //
	MERGER(new String[] { "Tile/Merger/", "Tile/MergerMirror/" }, 1, "Ui/Icons/MergerIcon.png", new String[] { "Ui/Hover/Merger.png", "Ui/Hover/MergerMirror.png" }, 1), //
	SPLITTER(new String[] { "Tile/Splitter/", "Tile/SplitterMirror/" }, 1, "Ui/Icons/SplitterIcon.png", new String[] { "Ui/Hover/Splitter.png", "Ui/Hover/SplitterMirror.png" }, 1), //
	TUNNEL(new String[] { "Tile/TunnelIn/", "Tile/TunnelOut/" }, 1, "Ui/Icons/TunnelInIcon.png", new String[] { "Ui/Hover/TunnelIn.png", "Ui/Hover/TunnelOut.png" }, 1), //
	MIXER(new String[] { "Tile/Mixer/", "Tile/MixerMirror/" }, 2, "Ui/Icons/MixerIcon.png", new String[] { "Ui/Hover/Mixer.png", "Ui/Hover/MixerMirror.png" }, 6), //
	TRASH(new String[] { "Tile/Trash/" }, 1, "Ui/Icons/TrashIcon.png", new String[] { "Ui/Hover/Trash.png" }, 7);

	private Texture itemTexture;
	private Texture[] hoverTexture;
	private int hoverIndex;

	private boolean enabled; // obligatoire par l'interface ToolbarItem mais on en profite pour l'utiliser pour savoir si on a débloqué la factory en essayant de la placer

	private int level;

	private int transferTimeout;
	private int transferTicks;

	private int currentFrame;
	private int maxFrame;
	private int animationTicks;
	private int animationTimeout;
	private Texture[][][] textures;

	private FactoryType(String[] texturesPaths, int tileCount, String itemTexturePath, String[] hoverTexturePath, int maxFrame, int animationTimeout)
	{
		itemTexture = new Texture(Gdx.files.internal(itemTexturePath));

		hoverTexture = new Texture[hoverTexturePath.length];
		for (int i = 0; i < hoverTexturePath.length; i++)
			hoverTexture[i] = new Texture(Gdx.files.internal(hoverTexturePath[i]));
		hoverIndex = 0;

		level = 1;

		transferTimeout = 70;
		transferTicks = 0;

		currentFrame = 0;
		this.maxFrame = maxFrame;
		animationTicks = 0;
		this.animationTimeout = animationTimeout;

		// Load and store for each building type the textures in the different folders and states of the building
		textures = new Texture[texturesPaths.length][tileCount][maxFrame];
		for (int i = 0; i < texturesPaths.length; i++)
			for (int k = 0; k < tileCount; k++)
				for (int j = 0; j < maxFrame; j++)
					textures[i][k][j] = new Texture(texturesPaths[i] + String.format("%02d/%02d.png", k, j));
	}

	private FactoryType(String[] texturesPaths, int tileCount, String itemTexturePath, String[] hoverTexturePath, int maxFrame)
	{
		this(texturesPaths, tileCount, itemTexturePath, hoverTexturePath, maxFrame, 8);
	}

	@Override
	public Texture getItemTexture()
	{
		return itemTexture;
	}

	@Override
	public Texture getHoverTexture()
	{
		return hoverTexture[hoverIndex];
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

	public void setMirrored(boolean mirrored)
	{
		if (mirrored && hoverTexture.length > 1)
			hoverIndex = 1;
		else
			hoverIndex = 0;
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

	public Texture getTexture(FactoryType factoryType, boolean mirrored, AnimationType animationType, int tile)
	{
		int i = 0;

		if (factoryType == FURNACE || factoryType == MERGER || factoryType == MIXER || factoryType == SPLITTER) {
			if (mirrored)
				i = 1;
		}
		else {
			if (animationType == AnimationType.OUT || animationType == AnimationType.LEFT)
				i = 1;
			else if (animationType == AnimationType.RIGHT)
				i = 2;
		}

		return textures[i][tile][currentFrame];
	}
}
