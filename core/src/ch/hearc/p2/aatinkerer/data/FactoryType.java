package ch.hearc.p2.aatinkerer.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ch.hearc.p2.aatinkerer.ui.HoverableItem;
import ch.hearc.p2.aatinkerer.ui.ToolbarItem;

public enum FactoryType implements ToolbarItem, HoverableItem
{
	CONVEYOR(new String[] { "tile/conveyor/", "tile/conveyorleft/", "tile/conveyorright/" }, 1, "ui/icons/conveyoricon.png", new String[] { "ui/hover/conveyor.png" }, 8, 1), //
	EXTRACTOR(new String[] { "tile/extractor/" }, 1, "ui/icons/extractoricon.png", new String[] { "ui/hover/extractor.png" }, 8), //
	FURNACE(new String[] { "tile/furnace/", "tile/furnacemirror/" }, 1, "ui/icons/furnaceicon.png", new String[] { "ui/hover/furnace.png", "ui/hover/furnacemirror.png" }, 8), //
	PRESS(new String[] { "tile/press/" }, 1, "ui/icons/pressicon.png", new String[] { "ui/hover/press.png" }, 6), //
	CUTTER(new String[] { "tile/cutter/" }, 1, "ui/icons/cuttericon.png", new String[] { "ui/hover/cutter.png" }, 4), //
	ASSEMBLER(new String[] { "tile/assembler/" }, 3, "ui/icons/assemblericon.png", new String[] { "ui/hover/assembler.png" }, 9), //
	MERGER(new String[] { "tile/merger/", "tile/mergermirror/" }, 1, "ui/icons/mergericon.png", new String[] { "ui/hover/merger.png", "ui/hover/mergermirror.png" }, 1), //
	SPLITTER(new String[] { "tile/splitter/", "tile/splittermirror/" }, 1, "ui/icons/splittericon.png", new String[] { "ui/hover/splitter.png", "ui/hover/splittermirror.png" }, 1), //
	TUNNEL(new String[] { "tile/tunnelin/", "tile/tunnelout/" }, 1, "ui/icons/tunnelinicon.png", new String[] { "ui/hover/tunnelin.png", "ui/hover/tunnelout.png" }, 1), //
	MIXER(new String[] { "tile/mixer/", "tile/mixermirror/" }, 2, "ui/icons/mixericon.png", new String[] { "ui/hover/mixer.png", "ui/hover/mixermirror.png" }, 6), //
	TRASH(new String[] { "tile/trash/" }, 1, "ui/icons/trashicon.png", new String[] { "ui/hover/trash.png" }, 7);

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

		this.transferTimeout = 60;
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

	public int transferTimeout()
	{
		return this.transferTimeout;
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

		if (factoryType == FURNACE || factoryType == MERGER || factoryType == MIXER || factoryType == SPLITTER)
		{
			if (mirrored)
				i = 1;
		}
		else
		{
			if (animationType == AnimationType.OUT || animationType == AnimationType.LEFT)
				i = 1;
			else if (animationType == AnimationType.RIGHT)
				i = 2;
		}

		return textures[i][tile][currentFrame];
	}
}
