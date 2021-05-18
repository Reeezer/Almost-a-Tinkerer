package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ch.hearc.p2.aatinkerer.Util;

public class BuildingTile
{
	protected String baseFramesPath;
	protected Texture[] frames;

	protected int frame;
	protected FactoryType type;

	protected static int animationTimeout = 3;
	protected static int animationTicks = 0;

	protected static int conveyorTimeout = 3;
	protected static int conveyorTicks = 0;
	protected static int conveyorFrame = 0;
	protected static int conveyorMaxFrame = 8;

	public BuildingTile(String framesPath, int framecount, FactoryType type)
	{
		baseFramesPath = framesPath;
		frames = new Texture[framecount];

		for (int i = 0; i < framecount; i++) {
			frames[i] = new Texture(baseFramesPath + String.format("%02d.png", i));
		}

		frame = 0;
		this.type = type;
	}

	public void render(SpriteBatch batch, int tileSize, int direction, int x, int y)
	{
		Texture texture = frames[frame];

		if (type != FactoryType.CONVEYOR && BuildingTile.animationTicks == BuildingTile.animationTimeout) {
			frame = (frame + 1) % frames.length;
			texture = frames[frame];
		}

		if (type == FactoryType.CONVEYOR) {
			texture = frames[conveyorFrame];
		}

		// FIXME need a static frame count for each building type to have every building (of a type) displaying the same frame
		// Unless conveyors (for example) don't render very well

		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) tileSize / 2.f, (float) tileSize / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
	}

	public static void staticUpdate()
	{
		System.out.println(conveyorFrame);
		
		if (BuildingTile.animationTicks++ >= BuildingTile.animationTimeout) {
			BuildingTile.animationTicks = 0;
		}

		if (BuildingTile.conveyorTicks++ >= BuildingTile.conveyorTimeout) {
			BuildingTile.conveyorTicks = 0;
			BuildingTile.conveyorFrame = (BuildingTile.conveyorFrame + 1) % BuildingTile.conveyorMaxFrame;
		}
	}
}
