package ch.hearc.p2.aatinkerer.main;

import java.io.File;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.data.Scale;
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

	private Cursor cursor;
	private Cursor cursorHover;

	public static final Color BLUE = new Color(31.f / 255, 33.f / 255, 59.f / 255, 1);
	public static final Color WHITE = new Color(246.f / 255, 246.f / 255, 246.f / 255, 1);
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);
	public static FreeTypeFontGenerator font;
	public static FreeTypeFontParameter titleFontParam;
	public static FreeTypeFontParameter buttonFontParam;
	public static final float VOLUME_HIGH = 0.4f;
	public static final float VOLUME_LOW = 0.1f;

	public static Difficulty difficulty = Difficulty.REGULAR;
	public static Scale scale = Scale.AUTO;

	private String saveDirBasePath;

	static
	{
		buttonFontParam = new FreeTypeFontParameter();
		buttonFontParam.size = 40;
		buttonFontParam.padLeft = 8;
		buttonFontParam.padRight = 8;

		titleFontParam = new FreeTypeFontParameter();
		titleFontParam.size = 80;
	}

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);

		// Custom cursor
		cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("Ui/cursor.png")), 6, 0);
		cursorHover = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("Ui/cursorhover.png")), 6, 0);
		Gdx.graphics.setCursor(cursor);

		// Initializations
		font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf"));
		Sounds.MUSIC.setVolume(VOLUME_LOW);
		Sounds.MUSIC.play();

		// Screens
		splashScreen = new SplashScreen(this);
		pauseScreen = new PauseScreen(this);
		saveScreen = new SaveScreen(this);
		difficultyScreen = new DifficultyScreen(this);

		String systemName = System.getProperty("os.name").toLowerCase();

		// two options : either windows or unix (might not work on macOS, we don't have it to try)
		if (systemName.contains("windows"))
			saveDirBasePath = Gdx.files.external("AppData/Roaming/almost-a-tinkerer/").file().getAbsolutePath();
		else
			saveDirBasePath = Gdx.files.external(".config/almost-a-tinkerer/").file().getAbsolutePath();

		// Create directory if doesn't exists
		if (!Gdx.files.absolute(saveDirBasePath).exists())
			Gdx.files.absolute(saveDirBasePath).mkdirs();

		System.out.format("detected OS: %s, using save path '%s'%n", systemName, saveDirBasePath);

		setScreen(splashScreen);
	}

	public String saveDirBasePath()
	{
		return this.saveDirBasePath;
	}

	public void addCursorListener(Actor actor)
	{
		actor.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
			{
				Gdx.graphics.setCursor(cursorHover);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
			{
				Gdx.graphics.setCursor(cursor);
			}
		});
	}

	public void save()
	{
		// FIXME
		if (gameScreen != null)
			gameScreen.saveGame();
	}

	public void setHoverCursor()
	{
		Gdx.graphics.setCursor(cursorHover);
	}

	public void setDefaultCursor()
	{
		Gdx.graphics.setCursor(cursor);
	}

	public void toPausedGameScreen()
	{
		setDefaultCursor();
		gameScreen.resume();
		toGameScreen();
	}

	public void toNewGameScreenFromSave(String name, String savepath)
	{
		gameScreen = new GameScreen(this, name, savepath);
		setDefaultCursor();
		toGameScreen();
	}

	public void toNewGameScreen(String name)
	{
		gameScreen = new GameScreen(this, name);
		setDefaultCursor();
		toGameScreen();
	}

	public void toDifficultyScreen()
	{
		setDefaultCursor();
		changeVolume(VOLUME_LOW);
		setScreen(difficultyScreen);
	}

	public void toPauseScreen()
	{
		setDefaultCursor();
		gameScreen.pause();
		changeVolume(VOLUME_LOW);
		setScreen(pauseScreen);
	}

	public void toSaveScreen()
	{
		save();
		setDefaultCursor();
		changeVolume(VOLUME_LOW);
		setScreen(saveScreen);
	}

	private void toGameScreen()
	{
		setDefaultCursor();
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

	public void resizeGameScreen()
	{
		gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		if (gameScreen != null)
			gameScreen.dispose();

		batch.dispose();
	}
}
