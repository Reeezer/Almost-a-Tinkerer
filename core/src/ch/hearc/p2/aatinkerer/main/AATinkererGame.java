package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.hearc.p2.aatinkerer.util.Input;

public class AATinkererGame extends Game
{
	public SpriteBatch batch;
	public Input input;

	private GameScreen gameScreen;
	private SplashScreen splashScreen;
	
	public Music music;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);

		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
		music.setVolume(0.1f);
		music.play();
		
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
