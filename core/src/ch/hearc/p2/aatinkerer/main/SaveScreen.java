package ch.hearc.p2.aatinkerer.main;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class SaveScreen extends MenuScreen
{
	private String selectedSaveDirName;
	private String selectedWorldName;

	private TextButton loadButton;
	private TextButton deleteButton;
	private TextButton editButton;
	private Table savesTable;
	private List<Table> savesList;

	private ScrollPaneStyle paneStyle;
	private ScrollPane pane;

	public SaveScreen(final AATinkererGame game)
	{
		super(game);

		Table mainTable = new Table();
		Table buttonsTable = new Table();
		savesTable = new Table();
		mainTable.setFillParent(true);

		stage.addActor(mainTable);

		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toSplashScreen();
			};
		});

		// Style
		paneStyle = new ScrollPaneStyle();
		paneStyle.vScroll = AATinkererGame.normalPatch;
		paneStyle.vScrollKnob = AATinkererGame.hoverPatch;

		// Title
		Label title = new Label("Worlds", AATinkererGame.titleLabelStyle);

		int buttonPad = 107;
		// Load button
		loadButton = new TextButton("Load world", AATinkererGame.textButtonStyle);
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
		editButton = new TextButton("Rename", AATinkererGame.textButtonStyle);
		editButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();

				game.toWorldNameScreen(selectedWorldName, selectedSaveDirName);
			};
		});
		game.addCursorHoverEffect(editButton);
		buttonsTable.add(editButton).padRight(buttonPad);

		// Delete button
		deleteButton = new TextButton("Delete", AATinkererGame.textButtonStyle);
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
		TextButton newButton = new TextButton("New world", AATinkererGame.textButtonStyle);
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
		pane = new ScrollPane(table, paneStyle);
		pane.addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
			{
				stage.setScrollFocus(pane);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
			{
				stage.setScrollFocus(null);
			}
		});
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

			Label nameLabel = new Label(json.getString("name"), AATinkererGame.normalLabelStyle);
			Label dateLabel = new Label(json.getString("date"), AATinkererGame.normalLabelStyle);
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
							otherLine.setBackground(AATinkererGame.normalPatch);
					line.setBackground(AATinkererGame.hoverPatch);
				}
			});
			game.addCursorHoverEffect(line);

			line.setBackground(AATinkererGame.normalPatch);
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
		super.show();
		displaySaves();
	}

	@Override
	public void render(float delta)
	{
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toSplashScreen();
	}
}
