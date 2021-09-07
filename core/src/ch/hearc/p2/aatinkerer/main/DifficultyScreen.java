package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class DifficultyScreen extends MenuScreen
{
	private Table difficultiesTable;

	private TextField nameTextField;
	private Table textFieldTable;

	private final static String NEW_WOLRD = "New world";
	private final static String WORLD_NAME = "World name";

	public DifficultyScreen(final AATinkererGame game)
	{
		super(game);

		difficultiesTable = new Table();
		textFieldTable = new Table();
		Table mainTable = new Table();
		mainTable.setFillParent(true);

		stage.addActor(mainTable);

		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toSaveScreen();
			};
		});

		// Title
		Label title = new Label("Choose a difficulty", AATinkererGame.titleLabelStyle);

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
		super.show();

		// World name
		nameTextField = new TextField("", AATinkererGame.textFieldStyle);
		nameTextField.setMessageText(WORLD_NAME);
		game.addCursorHoverEffect(nameTextField);

		textFieldTable.clear();
		textFieldTable.add(nameTextField).grow();
	}

	@Override
	public void render(float delta)
	{
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toSaveScreen();
	}
}
