package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen
{
	private AATinkererGame game;
	private Texture background[];

	private int width;
	private int height;

	private long lastTime;
	private int frame;

	public SplashScreen(AATinkererGame game)
	{
		this.game = game;

		background = new Texture[] { new Texture("Menus/splash1.png"), new Texture("Menus/splash2.png") };
		frame = 0;
		lastTime = TimeUtils.millis();

		width = 0;
		height = 0;
	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta)
	{
		long firstTime = TimeUtils.millis();

		if (firstTime - lastTime >= 1000) {
			frame = (frame + 1) % background.length;
			lastTime = firstTime;
		}

		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
			game.toGameScreen();

		Gdx.gl.glClearColor(0, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();

		TextureRegion textureRegion = new TextureRegion(background[frame]);
		game.batch.draw(textureRegion, 0, 0, (float) width, (float) height, (float) width, (float) height, 1.f, 1.f, (float) 0 * 90.f);

		game.batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		this.height = height;
		this.width = width;
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}
}
