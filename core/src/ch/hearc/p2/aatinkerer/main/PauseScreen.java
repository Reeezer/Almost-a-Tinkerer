package ch.hearc.p2.aatinkerer.main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ch.hearc.p2.aatinkerer.data.Scale;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class PauseScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private int width;
	private int height;

	private Stage stage;

	private Label scaleLabel;
	private List<TextButton> scaleButtons;

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

		int pad = 75;

		// Title
		LabelStyle titleLabelStyle = new LabelStyle();
		titleLabelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.titleFontParam);
		titleLabelStyle.fontColor = AATinkererGame.WHITE;

		Label title = new Label("Pause", titleLabelStyle);

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
		game.addCursorHoverEffect(resumeButton);
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
		game.addCursorHoverEffect(muteButton);
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
		game.addCursorHoverEffect(saveButton);
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
		game.addCursorHoverEffect(homeButton);
		buttonTable.add(homeButton).pad(pad);

		mainTable.add(title).padTop(50).padBottom(50);
		mainTable.row();
		mainTable.add(buttonTable);

		// Scales
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		labelStyle.fontColor = AATinkererGame.WHITE;

		scaleLabel = new Label("Scale", labelStyle);
		stage.addActor(scaleLabel);

		NinePatch textButtonPatch = new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2);
		NinePatch textButtonHoverPatch = new NinePatch(new Texture("Ui/Buttons/textbuttonhover.png"), 2, 2, 2, 2);

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		textButtonStyle.fontColor = AATinkererGame.WHITE;
		textButtonStyle.up = new NinePatchDrawable(textButtonPatch);
		textButtonStyle.over = new NinePatchDrawable(textButtonHoverPatch);
		textButtonStyle.checked = new NinePatchDrawable(textButtonHoverPatch);

		scaleButtons = new ArrayList<TextButton>();

		createScaleButtons(textButtonStyle, "Auto", Scale.AUTO);
		createScaleButtons(textButtonStyle, "x1", Scale.X1);
		createScaleButtons(textButtonStyle, "x2", Scale.X2);
		createScaleButtons(textButtonStyle, "x4", Scale.X4);

		ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();
		for (TextButton button : scaleButtons)
			buttonGroup.add(button);
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(1);
		scaleButtons.get(0).toggle();
	}

	private void createScaleButtons(TextButtonStyle style, String text, final Scale scale)
	{
		TextButton button = new TextButton(text, style);
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				AATinkererGame.scale = scale;
				game.resizeGameScreen();
			};
		});
		game.addCursorHoverEffect(button);

		button.setSize(100, button.getHeight());

		scaleButtons.add(button);
		stage.addActor(button);
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

		// Draw background
		game.drawAnimatedBackground(width, height);

		game.batch.end();

		stage.act(delta);
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toPausedGameScreen();
	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
		viewport.setWorldSize(width, height);
		stage.getViewport().update(width, height, true);

		float buttonWidth = scaleButtons.get(0).getWidth();

		scaleLabel.setPosition(20, 20);
		for (int i = 0; i < scaleButtons.size(); i++)
			scaleButtons.get(i).setPosition(100 + (buttonWidth + 20) * i, 20);

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
