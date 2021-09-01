package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.util.Sounds;

public class PauseScreen implements Screen
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

	public PauseScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		Table mainTable = new Table();
		Table buttonTable = new Table();
		mainTable.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		background = new Texture("Menus/menu_background.png");

		passedTime = 0.f;
		frame1 = 0;
		frame2 = -background.getWidth();

		int pad = 75;

		// Title
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		labelStyle.fontColor = AATinkererGame.WHITE;

		Label title = new Label("Pause", labelStyle);

		// Resume
		ImageButtonStyle resumeButtonStyle = new ImageButtonStyle();
		resumeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/play.png"))));
		resumeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/playhover.png"))));

		ImageButton resumeButton = new ImageButton(resumeButtonStyle);
		resumeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toPausedGameScreen();
			};
		});
		buttonTable.add(resumeButton).pad(pad);

		// Sound
		ImageButtonStyle muteButtonStyle = new ImageButtonStyle();
		muteButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundon.png"))));
		muteButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundonhover.png"))));
		muteButtonStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundoff.png"))));
		muteButtonStyle.imageCheckedOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundoffhover.png"))));

		ImageButton muteButton = new ImageButton(muteButtonStyle);
		muteButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();

				Sounds.muted = !Sounds.muted;
				if (Sounds.muted)
					Sounds.MUSIC.setVolume(0.f);
				else
					Sounds.MUSIC.setVolume(AATinkererGame.VOLUME_LOW);
			};
		});
		buttonTable.add(muteButton).pad(pad);

		// Save
		ImageButtonStyle saveButtonStyle = new ImageButtonStyle();
		saveButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/save.png"))));
		saveButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/savehover.png"))));

		ImageButton saveButton = new ImageButton(saveButtonStyle);
		saveButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				// FIXME save the game
			};
		});
		buttonTable.add(saveButton).pad(pad);

		// Back home
		ImageButtonStyle homeButtonStyle = new ImageButtonStyle();
		homeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/home.png"))));
		homeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/homehover.png"))));

		ImageButton homeButton = new ImageButton(homeButtonStyle);
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toSaveScreen();
			};
		});
		buttonTable.add(homeButton).pad(pad);

		mainTable.add(title).padTop(50).padBottom(50);
		mainTable.row();
		mainTable.add(buttonTable);
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

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toPausedGameScreen();
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
