package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class DifficultyScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;

	private Table difficultiesTable;
	private TextButton exitButton;

	private int width;
	private int height;

	public final static String WORLD_NAME = "World name";
	private TextField nameTextField;
	private TextFieldStyle textFieldStyle;
	private Table textFieldTable;
	private final static String NEW_WOLRD = "New world";

	public DifficultyScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		difficultiesTable = new Table();
		Table mainTable = new Table();
		mainTable.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		// Return
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		textButtonStyle.fontColor = AATinkererGame.WHITE;
		textButtonStyle.up = game.getButtonHoverPatch();
		textButtonStyle.over = game.getButtonHoverPatch();

		exitButton = new TextButton("Return", textButtonStyle);
		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toSaveScreen();
			};
		});
		game.addCursorHoverEffect(exitButton);
		stage.addActor(exitButton);

		// Title
		LabelStyle titleLabelStyle = new LabelStyle();
		titleLabelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		titleLabelStyle.fontColor = AATinkererGame.WHITE;

		Label title = new Label("Choose a difficulty", titleLabelStyle);

		// World name
		textFieldTable = new Table();

		textFieldStyle = new TextFieldStyle();
		textFieldStyle.background = game.getButtonPatch();
		textFieldStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		textFieldStyle.fontColor = AATinkererGame.WHITE;
		textFieldStyle.cursor = new TextureRegionDrawable(new Texture("Ui/Buttons/cursor.png"));

		// Difficulties
		Texture regularTexture = new Texture("Ui/Buttons/regular.png");
		Texture abundantTexture = new Texture("Ui/Buttons/abundant.png");
		Texture rareTexture = new Texture("Ui/Buttons/rare.png");
		Texture bigSparseTexture = new Texture("Ui/Buttons/bigsparse.png");
		Texture everywhereTexture = new Texture("Ui/Buttons/everywhere.png");
		Texture goodLuckTexture = new Texture("Ui/Buttons/goodluck.png");

		Texture regularHoverTexture = new Texture("Ui/Buttons/regularhover.png");
		Texture abundantHoverTexture = new Texture("Ui/Buttons/abundanthover.png");
		Texture rareHoverTexture = new Texture("Ui/Buttons/rarehover.png");
		Texture bigSparseHoverTexture = new Texture("Ui/Buttons/bigsparsehover.png");
		Texture everywhereHoverTexture = new Texture("Ui/Buttons/everywherehover.png");
		Texture goodLuckHoverTexture = new Texture("Ui/Buttons/goodluckhover.png");

		createButton(regularTexture, regularHoverTexture, "Regular", Difficulty.REGULAR);
		createButton(abundantTexture, abundantHoverTexture, "Abundant", Difficulty.ABUNDANT);
		createButton(rareTexture, rareHoverTexture, "Rare", Difficulty.RARE);
		difficultiesTable.row();
		createButton(bigSparseTexture, bigSparseHoverTexture, "Big sparse", Difficulty.BIGSPARSE);
		createButton(everywhereTexture, everywhereHoverTexture, "Everywhere", Difficulty.EVERYWHERE);
		createButton(goodLuckTexture, goodLuckHoverTexture, "Good luck", Difficulty.GOODLUCK);

		// Positioning
		mainTable.add(title).padBottom(50);
		mainTable.row();
		mainTable.add(textFieldTable).padBottom(20).width(680);
		mainTable.row();
		mainTable.add(difficultiesTable);
	}

	private void createButton(Texture texture, Texture textureHover, String title, final Difficulty difficulty)
	{
		// Create a new style for each button (background and foreground image)
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		buttonStyle.overFontColor = AATinkererGame.TRANSPARENT;
		buttonStyle.fontColor = AATinkererGame.WHITE;
		buttonStyle.up = new NinePatchDrawable(new NinePatch(texture, 2, 2, 2, 2));
		buttonStyle.over = new NinePatchDrawable(new NinePatch(textureHover, 2, 2, 2, 2));

		TextButton button = new TextButton(title, buttonStyle);
		difficultiesTable.add(button).pad(50);

		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				AATinkererGame.difficulty = difficulty;
				String name = nameTextField.getText().isEmpty() ? NEW_WOLRD : nameTextField.getText();
				game.toNewGameScreen(name);
			};
		});
		game.addCursorHoverEffect(button);
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);

		// Didn't find another way to remove the focus of the textfield when moving between the screens
		nameTextField = new TextField("", textFieldStyle);
		nameTextField.setMessageText(WORLD_NAME);
		game.addCursorHoverEffect(nameTextField);

		textFieldTable.clear();
		textFieldTable.add(nameTextField).grow();
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

		// Draw background
		game.drawAnimatedBackground(width, height);

		game.batch.end();

		stage.act(delta);
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toSaveScreen();
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
