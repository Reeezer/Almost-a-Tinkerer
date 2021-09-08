package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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
	private WorldNameScreen worldNameScreen;

	private Cursor cursor;
	private Cursor cursorHover;

	public static NinePatchDrawable normalPatch;
	public static NinePatchDrawable hoverPatch;
	public static NinePatchDrawable disabledPatch;

	public static TextButtonStyle textButtonStyle;
	public static TextFieldStyle textFieldStyle;
	public static LabelStyle normalLabelStyle;
	public static LabelStyle titleLabelStyle;

	private Texture menusBackground;
	private final static float ANIMATION_TIME = 0.5f;
	private float passedTime;
	private int xPosBackground1;
	private int xPosBackground2;

	public static final Color BLUE = new Color(31.f / 255, 33.f / 255, 59.f / 255, 1);
	public static final Color WHITE = new Color(246.f / 255, 246.f / 255, 246.f / 255, 1);
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);
	public static FreeTypeFontGenerator font;
	public static FreeTypeFontParameter titleFontParam;
	public static FreeTypeFontParameter normalFontParam;

	public static final float VOLUME_HIGH = 0.4f;
	public static final float VOLUME_LOW = 0.1f;

	public static Difficulty difficulty = Difficulty.REGULAR;
	public static Scale scale = Scale.AUTO;

	private String saveDirBasePath;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);

		// Initializations
		font = new FreeTypeFontGenerator(Gdx.files.internal("font/at01.ttf"));

		menusBackground = new Texture("ui/menu_background.png");
		normalPatch = new NinePatchDrawable(new NinePatch(new Texture("ui/buttons/textbutton.png"), 2, 2, 2, 2));
		hoverPatch = new NinePatchDrawable(new NinePatch(new Texture("ui/buttons/textbuttonhover.png"), 2, 2, 2, 2));
		disabledPatch = new NinePatchDrawable(new NinePatch(new Texture("ui/buttons/textbuttondisabled.png"), 2, 2, 2, 2));

		normalFontParam = new FreeTypeFontParameter();
		normalFontParam.size = 40;
		normalFontParam.padLeft = 8;
		normalFontParam.padRight = 8;

		titleFontParam = new FreeTypeFontParameter();
		titleFontParam.size = 80;

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font.generateFont(normalFontParam);
		textButtonStyle.fontColor = WHITE;
		textButtonStyle.up = normalPatch;
		textButtonStyle.over = hoverPatch;
		textButtonStyle.disabled = disabledPatch;

		titleLabelStyle = new LabelStyle();
		titleLabelStyle.font = font.generateFont(titleFontParam);
		titleLabelStyle.fontColor = WHITE;

		textFieldStyle = new TextFieldStyle();
		textFieldStyle.background = normalPatch;
		textFieldStyle.font = font.generateFont(normalFontParam);
		textFieldStyle.fontColor = WHITE;
		textFieldStyle.cursor = new TextureRegionDrawable(new Texture("ui/buttons/cursor.png"));

		normalLabelStyle = new LabelStyle();
		normalLabelStyle.font = font.generateFont(normalFontParam);
		normalLabelStyle.fontColor = WHITE;

		// Menus background animation
		passedTime = 0.f;
		xPosBackground1 = 0;
		xPosBackground1 = -menusBackground.getWidth();

		// Custom cursor
		cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("ui/cursor.png")), 6, 0);
		cursorHover = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("ui/cursorhover.png")), 6, 0);
		Gdx.graphics.setCursor(cursor);

		// Music
		Sounds.MUSIC.setVolume(VOLUME_LOW);
		Sounds.MUSIC.play();
		Sounds.MUSIC.setLooping(true);

		// Screens
		splashScreen = new SplashScreen(this);
		pauseScreen = new PauseScreen(this);
		saveScreen = new SaveScreen(this);
		difficultyScreen = new DifficultyScreen(this);
		worldNameScreen = new WorldNameScreen(this);

		String systemName = System.getProperty("os.name").toLowerCase();

		// two options : either windows or unix (might not work on macOS, we don't have it to try)
		if (systemName.contains("windows"))
			saveDirBasePath = Gdx.files.external("AppData/Roaming/almost-a-tinkerer/").file().getAbsolutePath();
		else
			saveDirBasePath = Gdx.files.external(".config/almost-a-tinkerer/").file().getAbsolutePath();

		// Create directory if doesn't exists
		if (!Gdx.files.absolute(saveDirBasePath).exists())
			Gdx.files.absolute(saveDirBasePath).mkdirs();

		setScreen(splashScreen);
	}

	public String saveDirBasePath()
	{
		return this.saveDirBasePath;
	}

	public void addCursorHoverEffect(Actor actor)
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

	public void toWorldNameScreen(String worldName, String savePath)
	{
		setDefaultCursor();
		changeVolume(VOLUME_LOW);
		setScreen(worldNameScreen);
		worldNameScreen.setName(worldName, savePath);
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
		toGameScreen();
	}

	public void toNewGameScreen(String name)
	{
		gameScreen = new GameScreen(this, name);
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

	public void toSplashScreen()
	{
		setDefaultCursor();
		changeVolume(VOLUME_LOW);
		setScreen(splashScreen);
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

	public void drawAnimatedBackground(float width, float height)
	{
		float widthRatio = width / (float) menusBackground.getWidth();
		float heightRatio = height / (float) menusBackground.getHeight();
		float bestRatio = Math.max(widthRatio, heightRatio);

		float newHeight = menusBackground.getHeight() * bestRatio;

		batch.draw(menusBackground, xPosBackground1, (height - newHeight) / 2, menusBackground.getWidth(), menusBackground.getHeight());
		batch.draw(menusBackground, xPosBackground2, (height - newHeight) / 2, menusBackground.getWidth(), menusBackground.getHeight());
	}

	@Override
	public void render()
	{
		super.render();

		if (passedTime >= ANIMATION_TIME)
		{
			while (passedTime >= ANIMATION_TIME)
			{
				passedTime -= ANIMATION_TIME;

				xPosBackground1++;
				if (xPosBackground1 == menusBackground.getWidth())
					xPosBackground1 = -menusBackground.getWidth();

				xPosBackground2++;
				if (xPosBackground2 == menusBackground.getWidth())
					xPosBackground2 = -menusBackground.getWidth();
			}
		}
		else
			passedTime++;
	}

	@Override
	public void dispose()
	{
		if (gameScreen != null)
			gameScreen.dispose();

		batch.dispose();
	}
}
