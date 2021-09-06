package ch.hearc.p2.aatinkerer.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.data.Difficulty;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class WorldNameScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;

	private int width;
	private int height;

	private TextButton exitButton;
	private TextField nameField;
	private Table textFieldTable;

	private String savePath;

	public WorldNameScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		Table mainTable = new Table();
		mainTable.setFillParent(true);

		textFieldTable = new Table();

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		// Return
		exitButton = new TextButton("Return", AATinkererGame.textButtonStyle);
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
		mainTable.add(textFieldTable).width(200).padBottom(20);
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
		Gdx.input.setInputProcessor(stage);

		// World name
		nameField = new TextField("", AATinkererGame.textFieldStyle);
		game.addCursorHoverEffect(nameField);

		textFieldTable.clear();
		textFieldTable.add(nameField).grow();
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
