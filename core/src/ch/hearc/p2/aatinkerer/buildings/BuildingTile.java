package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ch.hearc.p2.aatinkerer.Util;

public class BuildingTile
{
	protected String baseFramesPath;
	protected Texture[] frames;

	protected int frametick;

	public BuildingTile(String framesPath, int framecount)
	{
		baseFramesPath = framesPath;
		frames = new Texture[framecount];

		for (int i = 0; i < framecount; i++) {
			frames[i] = new Texture(baseFramesPath + String.format("%02d.png", i));
		}

		frametick = 0;
	}

	public void render(SpriteBatch batch, int tileSize, int direction, int x, int y)
	{
		Texture texture = frames[frametick];
		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x * tileSize, y * tileSize, (float) tileSize / 2.f, (float) tileSize / 2.f,
		        (float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
		frametick = (frametick + 1) % frames.length; //Util.clamp(frametick + 1, 0, frames.length - 1);
	}

}
