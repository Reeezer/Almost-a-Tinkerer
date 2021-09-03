package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Input;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class AATinkererGame extends Game
{
	public SpriteBatch batch;
	public Input input;

	private GameScreen gameScreen;
	private SplashScreen splashScreen;
	private PauseScreen pauseScreen;
	private DifficultyScreen difficultyScreen;
	private SaveScreen saveScreen;

	public static final Color BLUE = new Color(31.f / 255, 33.f / 255, 59.f / 255, 1);
	public static final Color WHITE = new Color(246.f / 255, 246.f / 255, 246.f / 255, 1);
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);
	public static FreeTypeFontGenerator font;
	public static FreeTypeFontParameter titleFontParam;
	public static FreeTypeFontParameter buttonFontParam;
	public static final float VOLUME_HIGH = 0.4f;
	public static final float VOLUME_LOW = 0.1f;

	public static Difficulty difficulty;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);

		Sounds.MUSIC.setVolume(VOLUME_LOW);
		Sounds.MUSIC.play();

		// Initializations
		font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf"));

		buttonFontParam = new FreeTypeFontParameter();
		buttonFontParam.size = 40;
		buttonFontParam.padLeft = 8;
		buttonFontParam.padRight = 8;

		titleFontParam = new FreeTypeFontParameter();
		titleFontParam.size = 80;

		difficulty = Difficulty.REGULAR;

		// Screens
		splashScreen = new SplashScreen(this);
		pauseScreen = new PauseScreen(this);
		saveScreen = new SaveScreen(this);
		difficultyScreen = new DifficultyScreen(this);

		setScreen(splashScreen);
	}

	public void toPausedGameScreen()
	{
		gameScreen.resume();
		toGameScreen();
	}

	public void toNewGameScreen()
	{
		gameScreen = new GameScreen(this, "C:\\Users\\Luca Davide Meyer\\Desktop\\AATsaves\\fichtre.dat");
		// gameScreen = new GameScreen(this);
		toGameScreen();
	}

	public void toDifficultyScreen()
	{
		changeVolume(VOLUME_LOW);
		setScreen(difficultyScreen);
	}

	public void toPauseScreen()
	{
		gameScreen.pause();
		changeVolume(VOLUME_LOW);
		setScreen(pauseScreen);
	}

	public void toSaveScreen()
	{
		// FIXME
		if (gameScreen != null)
			gameScreen.saveGame("C:\\Users\\Luca Davide Meyer\\Desktop\\AATsaves\\fichtre.dat");
		changeVolume(VOLUME_LOW);
		setScreen(saveScreen);
	}

	private void toGameScreen()
	{
		changeVolume(VOLUME_HIGH);
		setScreen(gameScreen);
	}

	private void changeVolume(float volume)
	{
		if (Sounds.muted)
			Sounds.MUSIC.setVolume(0.f);
		else
			Sounds.MUSIC.setVolume(volume);
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
