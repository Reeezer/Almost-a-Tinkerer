package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AATinkererGame extends Game
{
	public SpriteBatch batch;
	public Input input;
	
	private GameScreen gameScreen;
	private SplashScreen splashScreen;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);
		
		gameScreen = new GameScreen(this);
		splashScreen = new SplashScreen(this);
		
		setScreen(splashScreen);
	}
	
	public void toGameScreen()
	{
		setScreen(gameScreen);
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
