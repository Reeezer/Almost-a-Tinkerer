package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.util.Input;

public class AATinkererGame extends Game
{
	public SpriteBatch batch;
	public Input input;

	private GameScreen gameScreen;
	private SplashScreen splashScreen;
	private PauseScreen pauseScreen;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);

		gameScreen = new GameScreen(this);
		splashScreen = new SplashScreen(this);
		pauseScreen = new PauseScreen(this);

		setScreen(gameScreen);
	}

	public void toGameScreen()
	{
		gameScreen = new GameScreen(this); // FIXME no need to always do an initialisation
		setScreen(gameScreen);
	}

	public void toPauseScreen()
	{
		setScreen(pauseScreen);
	}

	public void toMainScreen()
	{
		setScreen(splashScreen); // FIXME to save menu
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		gameScreen.dispose();
		batch.dispose();
	}
}
