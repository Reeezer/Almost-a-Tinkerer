package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Input;

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

	public static Difficulty difficulty;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);

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
		setScreen(gameScreen);
	}

	public void toNewGameScreen()
	{
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	public void toDifficultyScreen()
	{
		setScreen(difficultyScreen);
	}

	public void toPauseScreen()
	{
		gameScreen.pause();
		setScreen(pauseScreen);
	}

	public void toSaveScreen()
	{
		setScreen(saveScreen);
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
