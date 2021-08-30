package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class DifficultyScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;

	private Table diffTable;
	private TextButton exitButton;

	private int width;
	private int height;

	private float passedTime;
	private final static float TIME = 0.005f;

	private int frame1;
	private int frame2;

	private Texture background;

	public DifficultyScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		diffTable = new Table();
		Table mainTable = new Table();
		mainTable.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		background = new Texture("Menus/menu_background.png");

		passedTime = 0.f;
		frame1 = 0;
		frame2 = -background.getWidth();

		// Title
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		labelStyle.fontColor = AATinkererGame.WHITE;

		Label title = new Label("Choose a difficulty", labelStyle);

		// Difficulties
		NinePatch regularPatch = new NinePatch(new Texture("Ui/Buttons/difficulty1.png"), 2, 2, 2, 2);
		NinePatch abundantPatch = new NinePatch(new Texture("Ui/Buttons/difficulty2.png"), 2, 2, 2, 2);
		NinePatch rarePatch = new NinePatch(new Texture("Ui/Buttons/difficulty3.png"), 2, 2, 2, 2);
		NinePatch bigSparsePatch = new NinePatch(new Texture("Ui/Buttons/difficulty4.png"), 2, 2, 2, 2);
		NinePatch everywherePatch = new NinePatch(new Texture("Ui/Buttons/difficulty5.png"), 2, 2, 2, 2);
		NinePatch goodLuckPatch = new NinePatch(new Texture("Ui/Buttons/difficulty6.png"), 2, 2, 2, 2);

		NinePatch regularHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty1hover.png"), 2, 2, 2, 2);
		NinePatch abundantHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty2hover.png"), 2, 2, 2, 2);
		NinePatch rareHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty3hover.png"), 2, 2, 2, 2);
		NinePatch bigSparseHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty4hover.png"), 2, 2, 2, 2);
		NinePatch everywhereHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty5hover.png"), 2, 2, 2, 2);
		NinePatch goodLuckHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty6hover.png"), 2, 2, 2, 2);

		createButton(regularPatch, regularHoverPatch, "Regular", Difficulty.REGULAR);
		createButton(abundantPatch, abundantHoverPatch, "Abundant", Difficulty.ABUNDANT);
		createButton(rarePatch, rareHoverPatch, "Rare", Difficulty.RARE);
		diffTable.row();
		createButton(bigSparsePatch, bigSparseHoverPatch, "Big sparse", Difficulty.BIGSPARSE);
		createButton(everywherePatch, everywhereHoverPatch, "Everywhere", Difficulty.EVERYWHERE);
		createButton(goodLuckPatch, goodLuckHoverPatch, "Good luck", Difficulty.GOODLUCK);

		// Positioning
		mainTable.add(title).padBottom(25);
		mainTable.row();
		mainTable.add(diffTable);

		// Exit
		NinePatch textButtonPatch = new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2);

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.buttonFontParam);
		textButtonStyle.overFontColor = AATinkererGame.BLUE;
		textButtonStyle.fontColor = AATinkererGame.WHITE;
		textButtonStyle.up = new NinePatchDrawable(textButtonPatch);

		exitButton = new TextButton("Exit", textButtonStyle);
		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toSaveScreen();
			};
		});
		stage.addActor(exitButton);
	}

	private void createButton(NinePatch ninePatch, NinePatch ninePatchHover, String title, final Difficulty difficulty)
	{
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.buttonFontParam);
		buttonStyle.overFontColor = AATinkererGame.TRANSPARENT;
		buttonStyle.fontColor = AATinkererGame.BLUE;
		buttonStyle.up = new NinePatchDrawable(ninePatch);
		buttonStyle.over = new NinePatchDrawable(ninePatchHover);

		TextButton button = new TextButton(title, buttonStyle);
		diffTable.add(button).pad(50);

		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				AATinkererGame.difficulty = difficulty;
				game.toNewGameScreen();
			};
		});
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(AATinkererGame.BLUE.r, AATinkererGame.BLUE.g, AATinkererGame.BLUE.b, AATinkererGame.BLUE.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		game.batch.begin();

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		// Background
		float widthRatio = this.width / (float) background.getWidth();
		float heightRatio = this.height / (float) background.getHeight();
		float bestRatio = Math.max(widthRatio, heightRatio);

		float newHeight = background.getHeight() * bestRatio;

		game.batch.draw(background, frame1, (height - newHeight) / 2, background.getWidth(), background.getHeight());
		game.batch.draw(background, frame2, (height - newHeight) / 2, background.getWidth(), background.getHeight());

		game.batch.end();

		stage.act(delta);
		stage.draw();

		if (passedTime >= TIME) {
			while (passedTime >= TIME) {
				passedTime -= TIME;

				frame1++;
				if (frame1 == background.getWidth())
					frame1 = -background.getWidth();

				frame2++;
				if (frame2 == background.getWidth())
					frame2 = -background.getWidth();
			}
		}
		else
			passedTime += delta;
	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
		viewport.setWorldSize(width, height);
		stage.getViewport().update(width, height, true);

		exitButton.setPosition(20, height - 20 - exitButton.getHeight());

		this.width = width;
		this.height = height;
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}
