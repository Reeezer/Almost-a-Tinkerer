package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BuildingTile
{
	protected String baseFramesPath;
	protected Texture[] frames;

	protected FactoryType type;

	protected static int conveyorTimeout = 3;
	protected static int conveyorTicks = 0;
	protected static int conveyorFrame = 0;
	protected static int conveyorMaxFrame = 8;

	protected static int animationTimeout = 8;
	protected static int animationTicks = 0;

	protected static int assemblerFrame = 0;
	protected static int assemblerMaxFrame = 9;

	protected static int cutterFrame = 0;
	protected static int cutterMaxFrame = 4;

	protected static int extractorFrame = 0;
	protected static int extractorMaxFrame = 8;

	protected static int furnaceFrame = 0;
	protected static int furnaceMaxFrame = 8;

	protected static int mixerFrame = 0;
	protected static int mixerMaxFrame = 6;

	protected static int pressFrame = 0;
	protected static int pressMaxFrame = 6;

	protected static int trashFrame = 0;
	protected static int trashMaxFrame = 7;

	protected static int splitterFrame = 0;
	protected static int mergerFrame = 0;
	protected static int tunnelFrame = 0;

	public BuildingTile(String framesPath, int framecount, FactoryType type)
	{
		baseFramesPath = framesPath;
		frames = new Texture[framecount];

		for (int i = 0; i < framecount; i++) {
			frames[i] = new Texture(baseFramesPath + String.format("%02d.png", i));
		}

		this.type = type;
	}

	public void render(SpriteBatch batch, int tileSize, int direction, int x, int y)
	{
		Texture texture = null;

		switch (type) {
			case ASSEMBLER:
				texture = frames[assemblerFrame];
				break;
			case CONVEYOR:
				texture = frames[conveyorFrame];
				break;
			case CUTTER:
				texture = frames[cutterFrame];
				break;
			case EXTRACTOR:
				texture = frames[extractorFrame];
				break;
			case FURNACE:
				texture = frames[furnaceFrame];
				break;
			case MERGER:
				texture = frames[mergerFrame];
				break;
			case MIXER:
				texture = frames[mixerFrame];
				break;
			case PRESS:
				texture = frames[pressFrame];
				break;
			case SPLITTER:
				texture = frames[splitterFrame];
				break;
			case TRASH:
				texture = frames[trashFrame];
				break;
			case TUNNEL:
				texture = frames[tunnelFrame];
				break;
			default:
				System.err.println("Wrong building type");
				break;
		}

		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) tileSize / 2.f, (float) tileSize / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
	}

	public static void staticUpdate()
	{
		if (animationTicks++ >= animationTimeout) {
			animationTicks = 0;

			assemblerFrame = (assemblerFrame + 1) % assemblerMaxFrame;
			cutterFrame = (cutterFrame + 1) % cutterMaxFrame;
			extractorFrame = (extractorFrame + 1) % extractorMaxFrame;
			furnaceFrame = (furnaceFrame + 1) % furnaceMaxFrame;
			mixerFrame = (mixerFrame + 1) % mixerMaxFrame;
			pressFrame = (pressFrame + 1) % pressMaxFrame;
			trashFrame = (trashFrame + 1) % trashMaxFrame;
		}

		if (conveyorTicks++ >= conveyorTimeout) {
			conveyorTicks = 0;
			conveyorFrame = (conveyorFrame + 1) % conveyorMaxFrame;
		}

	}
}
