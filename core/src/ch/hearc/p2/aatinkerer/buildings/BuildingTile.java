package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BuildingTile
{
	protected String baseFramesPath;
	protected Texture[] frames;

	protected FactoryType type;

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

		if (type != null)
			texture = frames[type.getCurrentFrame()];
		else
			texture = frames[0];

		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) tileSize / 2.f, (float) tileSize / 2.f, (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
	}
}
