package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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

import ch.hearc.p2.aatinkerer.util.Sounds;

public class SaveScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private int width;
	private int height;

	private float passedTime;
	private final static float TIME = 0.005f;

	private Stage stage;

	private int frame1;
	private int frame2;

	private Texture background;

	public SaveScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		Table mainTable = new Table();
		Table buttonsTable = new Table();
		mainTable.setFillParent(true);

		background = new Texture("Menus/menu_background.png");

		passedTime = 0.f;
		frame1 = 0;
		frame2 = -background.getWidth();

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		// Title
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		labelStyle.fontColor = AATinkererGame.WHITE;

		Label title = new Label("Worlds", labelStyle);

		// Buttons
		NinePatch textButtonPatch = new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2);

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.buttonFontParam);
		textButtonStyle.overFontColor = AATinkererGame.BLUE;
		textButtonStyle.fontColor = AATinkererGame.WHITE;
		textButtonStyle.up = new NinePatchDrawable(textButtonPatch);

		TextButton loadButton = new TextButton("Load world", textButtonStyle);
		loadButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toNewGameScreen();
				// FIXME disable le bouton si rien n'est sélectionné
				// FIXME lancer la partie en fonction de ce qui est sélectionné
			};
		});
		buttonsTable.add(loadButton).pad(100);

		TextButton newButton = new TextButton("New world", textButtonStyle);
		newButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toDifficultyScreen();
			};
		});
		buttonsTable.add(newButton).pad(100);

		mainTable.add(title);
		mainTable.row();
		mainTable.add(buttonsTable);
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
