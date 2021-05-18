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

	protected static int animationTimeout;
	protected static int animationTicks;

	protected static int conveyorTimeout;
	protected static int conveyorTicks;

	public BuildingTile(String framesPath, int framecount, FactoryType type)
	{
		baseFramesPath = framesPath;
		frames = new Texture[framecount];

		for (int i = 0; i < framecount; i++) {
			frames[i] = new Texture(baseFramesPath + String.format("%02d.png", i));
		}

		frame = 0;
		this.type = type;

		conveyorTimeout = 2;
		conveyorTicks = 0;

		animationTimeout = 3;
		animationTicks = 0;
	}

	public void render(SpriteBatch batch, int tileSize, int direction, int x, int y)
	{
		if (type != FactoryType.CONVEYOR && BuildingTile.animationTicks == BuildingTile.animationTimeout) {
			frame = (frame + 1) % frames.length;
		}

		if (type == FactoryType.CONVEYOR && BuildingTile.conveyorTicks == BuildingTile.conveyorTimeout) {
			frame = (frame + 1) % frames.length;
		}

		// FIXME need a static frame count for each building type to have every building (of a type) displaying the same frame
		// Unless conveyors (for example) don't render very well

		Texture texture = frames[frame];
		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) tileSize / 2.f, (float) tileSize / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
	}

	public static void staticUpdate()
	{
		if (BuildingTile.animationTicks++ >= BuildingTile.animationTimeout) {
			BuildingTile.animationTicks = 0;
		}

		if (BuildingTile.conveyorTicks++ >= BuildingTile.conveyorTimeout) {
			BuildingTile.conveyorTicks = 0;
		}
	}
}
