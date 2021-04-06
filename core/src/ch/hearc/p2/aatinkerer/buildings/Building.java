package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Building
{
	protected Texture texture;
	protected int direction;

	public void render(SpriteBatch batch, int x, int y)
	{
		// required to be able to rotate the texture
		TextureRegion textureRegion = new TextureRegion(texture);
		batch.draw(textureRegion, x, y, (float) texture.getWidth() / 2.f, (float) texture.getHeight() / 2.f,
				(float) texture.getWidth(), (float) texture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
	}
}
