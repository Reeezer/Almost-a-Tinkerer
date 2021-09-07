package ch.hearc.p2.aatinkerer.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class WorldNameScreen extends MenuScreen
{
	private TextField nameField;
	private Table textFieldTable;

	private String savePath;

	public WorldNameScreen(final AATinkererGame game)
	{
		super(game);

		Table mainTable = new Table();
		mainTable.setFillParent(true);

		textFieldTable = new Table();

		stage.addActor(mainTable);

		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toSaveScreen();
			};
		});

		// Title
		Label title = new Label("World name", AATinkererGame.titleLabelStyle);

		// Rename
		TextButton renameButton = new TextButton("Rename", AATinkererGame.textButtonStyle);
		renameButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();

				renameWorld();
				game.toSaveScreen();
			};
		});
		game.addCursorHoverEffect(renameButton);

		// Positioning
		mainTable.add(title).padBottom(50);
		mainTable.row();
		mainTable.add(textFieldTable).width(300).padBottom(20);
		mainTable.row();
		mainTable.add(renameButton);
	}

	private void renameWorld()
	{
		String path = game.saveDirBasePath() + "/" + savePath + "/gamedata.json";

		JsonReader jsonReader = new JsonReader();
		JsonValue jsonV = null;
		try
		{
			jsonV = jsonReader.parse(Gdx.files.absolute(path));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		final String difficultyString = jsonV.getString("difficulty").toLowerCase();
		final Difficulty difficulty = Difficulty.valueOf(difficultyString.toUpperCase());
		final String date = jsonV.getString("date");

		Json json = new Json();
		json.setWriter(new JsonWriter(new StringWriter()));
		json.writeObjectStart();
		json.writeValue("name", nameField.getText());
		json.writeValue("date", date);
		json.writeValue("difficulty", difficulty);
		json.writeObjectEnd();

		try
		{
			FileWriter jsonFileWriter = new FileWriter(path);
			jsonFileWriter.write(json.getWriter().getWriter().toString());
			jsonFileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setName(String worldName, String savePath)
	{
		nameField.setText(worldName);
		this.savePath = savePath;
	}

	@Override
	public void show()
	{
		super.show();

		// World name
		nameField = new TextField("", AATinkererGame.textFieldStyle);
		nameField.setMaxLength(20);
		game.addCursorHoverEffect(nameField);

		textFieldTable.clear();
		textFieldTable.add(nameField).grow();
	}

	@Override
	public void render(float delta)
	{
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toSaveScreen();
	}
}
