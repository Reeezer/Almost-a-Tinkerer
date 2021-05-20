package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class SplashScreen implements Screen
{
	private AATinkererGame game;
	private Texture background;
	private int width;
	private int height;

	public SplashScreen(AATinkererGame game)
	{
		this.game = game;
		background = new Texture("background.jpg");
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
		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
			game.toGameScreen();

		game.batch.begin();

		game.batch.draw(background, 0, 0, width, height);

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
