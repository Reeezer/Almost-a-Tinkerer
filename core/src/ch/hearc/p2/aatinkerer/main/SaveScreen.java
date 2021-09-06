package ch.hearc.p2.aatinkerer.main;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.util.AutoFocusScrollPane;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class SaveScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private int width;
	private int height;

	private Stage stage;

	private int saveSelected;

	private TextButton loadButton;
	private Table savesTable;
	private List<Table> savesList;

	private LabelStyle labelStyle;
	private ScrollPaneStyle paneStyle;

	public SaveScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		Table mainTable = new Table();
		Table buttonsTable = new Table();
		savesTable = new Table();
		mainTable.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		// Initializations
		labelStyle = new LabelStyle();
		labelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		labelStyle.fontColor = AATinkererGame.WHITE;

		paneStyle = new ScrollPaneStyle();
		paneStyle.vScroll = new NinePatchDrawable(new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2));
		paneStyle.vScrollKnob = new NinePatchDrawable(new NinePatch(new Texture("Ui/Buttons/textbuttonhover.png"), 2, 2, 2, 2));

		// Title
		LabelStyle titleLabelStyle = new LabelStyle();
		titleLabelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		titleLabelStyle.fontColor = AATinkererGame.WHITE;

		Label title = new Label("Worlds", titleLabelStyle);

		// Buttons
		NinePatch textButtonPatch = new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2);
		NinePatch textButtonHoverPatch = new NinePatch(new Texture("Ui/Buttons/textbuttonhover.png"), 2, 2, 2, 2);
		NinePatch textButtonDisabledPatch = new NinePatch(new Texture("Ui/Buttons/textbuttondisabled.png"), 2, 2, 2, 2);

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		textButtonStyle.fontColor = AATinkererGame.WHITE;
		textButtonStyle.up = new NinePatchDrawable(textButtonPatch);
		textButtonStyle.over = new NinePatchDrawable(textButtonHoverPatch);
		textButtonStyle.disabled = new NinePatchDrawable(textButtonDisabledPatch);

		loadButton = new TextButton("Load world", textButtonStyle);
		loadButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				System.out.format("%s%02d%s", "AppData/Roaming/almost-a-tinkerer/save", saveSelected, "/\n");
				// FIXME lancer la partie en fonction de ce qui est sélectionné
			};
		});
		game.addCursorHoverEffect(loadButton);
		buttonsTable.add(loadButton).padRight(200).padTop(50).padBottom(50);

		TextButton newButton = new TextButton("New world", textButtonStyle);
		newButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toDifficultyScreen();
			};
		});
		game.addCursorHoverEffect(newButton);
		buttonsTable.add(newButton);

		// Adding to the layout
		mainTable.add(title).padTop(50).padBottom(50);
		mainTable.row();
		mainTable.add(savesTable).grow().width(800);
		mainTable.row();
		mainTable.add(buttonsTable);
	}

	public void displaySaves()
	{
		savesTable.clear();
		saveSelected = -1;
		loadButton.setTouchable(Touchable.disabled);
		loadButton.setDisabled(true);

		savesList = new LinkedList<Table>();

		Table table = new Table();

		// Scroll pane which takes directly the mouse scroll when entering the mouse into its bounds
		AutoFocusScrollPane pane = new AutoFocusScrollPane(table, paneStyle);
		pane.setScrollbarsVisible(true);
		pane.setFadeScrollBars(false);

		// Create directory if doesn't exists
		if (!Gdx.files.external("AppData/Roaming/almost-a-tinkerer/").exists())
			Gdx.files.external("AppData/Roaming/almost-a-tinkerer/").mkdirs();

		// Read all files 1 by 1
		JsonReader jsonReader = new JsonReader();
		FileHandle[] files = Gdx.files.external("AppData/Roaming/almost-a-tinkerer/").list();
		for (FileHandle file : files) {
			System.out.println(file.path());
			JsonValue json = jsonReader.parse(Gdx.files.external(file.path() + "/gamedata.json"));
			System.out.println(json);

			Label nameLabel = new Label(json.getString("name"), labelStyle);
			Label dateLabel = new Label(json.getString("date"), labelStyle);
			Image difficultyImage = new Image(new Texture("Ui/Buttons/" + json.getString("difficulty").toLowerCase() + "hover.png"));

			// Create a table for each row
			table.row().padBottom(10).padTop(10);
			final Table line = new Table();
			savesList.add(line);
			line.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y)
				{
					saveSelected = savesList.indexOf(line) + 1;

					if (loadButton.isDisabled()) {
						loadButton.setDisabled(false);
						loadButton.setTouchable(Touchable.enabled);
					}

					for (Table otherLine : savesList)
						if (otherLine != line)
							otherLine.setBackground(new NinePatchDrawable(new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2)));
					line.setBackground(new NinePatchDrawable(new NinePatch(new Texture("Ui/Buttons/textbuttonhover.png"), 2, 2, 2, 2)));
				}
			});
			game.addCursorHoverEffect(line);

			line.setBackground(new NinePatchDrawable(new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2)));
			line.add(nameLabel).width(350).fill();
			line.add(dateLabel).width(250).fill();
			line.add(difficultyImage).width(150).height(150).fill();
			table.add(line).expand();
		}

		savesTable.add(pane).grow();
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);
		displaySaves();
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(AATinkererGame.BLUE.r, AATinkererGame.BLUE.g, AATinkererGame.BLUE.b, AATinkererGame.BLUE.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		// Draw background
		game.drawAnimatedBackground(width, height);

		game.batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
		viewport.setWorldSize(width, height);
		stage.getViewport().update(width, height, true);

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
