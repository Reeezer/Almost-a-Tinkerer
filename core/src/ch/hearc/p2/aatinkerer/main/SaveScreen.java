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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.data.Difficulty;
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

	private String selectedSaveDirName;
	private String selectedWorldName;

	private TextButton loadButton;
	private TextButton deleteButton;
	private TextButton editButton;
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

		// Styles
		labelStyle = new LabelStyle();
		labelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		labelStyle.fontColor = AATinkererGame.WHITE;

		paneStyle = new ScrollPaneStyle();
		paneStyle.vScroll = game.getButtonPatch();
		paneStyle.vScrollKnob = game.getButtonHoverPatch();

		LabelStyle titleLabelStyle = new LabelStyle();
		titleLabelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		titleLabelStyle.fontColor = AATinkererGame.WHITE;

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		textButtonStyle.fontColor = AATinkererGame.WHITE;
		textButtonStyle.up = game.getButtonPatch();
		textButtonStyle.over = game.getButtonHoverPatch();
		textButtonStyle.disabled = game.getButtonDisabledPatch();

		// Title
		Label title = new Label("Worlds", titleLabelStyle);

		int buttonPad = 111;
		// Load button
		loadButton = new TextButton("Load world", textButtonStyle);
		loadButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				System.out.format("launching save file at: %s%n", selectedSaveDirName);

				if (!selectedSaveDirName.isEmpty() && Gdx.files.absolute(game.saveDirBasePath() + "/" + selectedSaveDirName).exists())
					game.toNewGameScreenFromSave(selectedWorldName, selectedSaveDirName);
			};
		});
		game.addCursorHoverEffect(loadButton);
		buttonsTable.add(loadButton).padRight(buttonPad).padTop(50).padBottom(50);

		// Edit button
		editButton = new TextButton("Edit", textButtonStyle);
		editButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();

				System.out.println("EDIIIIIIIIIIIIIIIIIITTTTTTT");
			};
		});
		game.addCursorHoverEffect(editButton);
		buttonsTable.add(editButton).padRight(buttonPad);

		// Delete button
		deleteButton = new TextButton("Delete", textButtonStyle);
		deleteButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();

				if (Gdx.files.absolute(game.saveDirBasePath() + "/" + selectedSaveDirName).exists())
					Gdx.files.absolute(game.saveDirBasePath() + "/" + selectedSaveDirName).deleteDirectory();
				displaySaves();
			};
		});
		game.addCursorHoverEffect(deleteButton);
		buttonsTable.add(deleteButton).padRight(buttonPad);

		// New button
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
		selectedSaveDirName = "";

		loadButton.setTouchable(Touchable.disabled);
		loadButton.setDisabled(true);
		deleteButton.setTouchable(Touchable.disabled);
		deleteButton.setDisabled(true);
		editButton.setTouchable(Touchable.disabled);
		editButton.setDisabled(true);

		savesList = new LinkedList<Table>();

		Table table = new Table();

		// Scroll pane which takes directly the mouse scroll when entering the mouse into its bounds
		AutoFocusScrollPane pane = new AutoFocusScrollPane(table, paneStyle);
		pane.setScrollbarsVisible(true);
		pane.setFadeScrollBars(false);

		// Read all files 1 by 1
		JsonReader jsonReader = new JsonReader();

		FileHandle[] files = Gdx.files.absolute(game.saveDirBasePath()).list();

		for (FileHandle file : files)
		{
			final String currentSaveDirName = file.name();
			final String currentSaveDirPath = game.saveDirBasePath() + "/" + currentSaveDirName;

			JsonValue json;

			System.out.format("found new save directory at %s%n", currentSaveDirPath);
			try
			{
				json = jsonReader.parse(Gdx.files.absolute(currentSaveDirPath + "/gamedata.json"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}

			System.out.println(json);

			final String name = json.getString("name");
			final String difficultyString = json.getString("difficulty").toLowerCase();
			final Difficulty difficulty = Difficulty.valueOf(difficultyString.toUpperCase());

			Label nameLabel = new Label(json.getString("name"), labelStyle);
			Label dateLabel = new Label(json.getString("date"), labelStyle);
			Image difficultyImage = new Image(new Texture("Ui/Buttons/" + difficultyString + "hover.png"));

			// Create a table for each row
			table.row().padBottom(10).padTop(10);
			final Table line = new Table();
			savesList.add(line);
			line.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y)
				{
					selectedSaveDirName = currentSaveDirName;
					selectedWorldName = name;

					AATinkererGame.difficulty = difficulty;

					if (loadButton.isDisabled())
					{
						loadButton.setDisabled(false);
						loadButton.setTouchable(Touchable.enabled);
						deleteButton.setDisabled(false);
						deleteButton.setTouchable(Touchable.enabled);
						editButton.setDisabled(false);
						editButton.setTouchable(Touchable.enabled);
					}

					for (Table otherLine : savesList)
						if (otherLine != line)
							otherLine.setBackground(game.getButtonPatch());
					line.setBackground(game.getButtonHoverPatch());
				}
			});
			game.addCursorHoverEffect(line);

			line.setBackground(game.getButtonPatch());
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
