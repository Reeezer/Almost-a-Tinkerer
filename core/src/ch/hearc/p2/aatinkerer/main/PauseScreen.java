package ch.hearc.p2.aatinkerer.main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ch.hearc.p2.aatinkerer.data.Scale;
import ch.hearc.p2.aatinkerer.util.Sounds;

public class PauseScreen extends MenuScreen
{
	private Label scaleLabel;
	private List<TextButton> scaleButtons;
	private TextButtonStyle scaleButtonStyle;

	public PauseScreen(final AATinkererGame game)
	{
		super(game);

		Table mainTable = new Table();
		Table buttonTable = new Table();
		mainTable.setFillParent(true);

		stage.addActor(mainTable);

		int pad = 75;

		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.toPausedGameScreen();
			};
		});

		// Title
		Label title = new Label("Pause", AATinkererGame.titleLabelStyle);

		// Resume
		ImageButtonStyle resumeButtonStyle = new ImageButtonStyle();
		resumeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/play.png"))));
		resumeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/playhover.png"))));

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
		muteButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/soundon.png"))));
		muteButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/soundonhover.png"))));
		muteButtonStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/soundoff.png"))));
		muteButtonStyle.imageCheckedOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/soundoffhover.png"))));

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
		saveButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/save.png"))));
		saveButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/savehover.png"))));

		ImageButton saveButton = new ImageButton(saveButtonStyle);
		saveButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				Sounds.CLICK.play();
				game.save();
			};
		});
		game.addCursorHoverEffect(saveButton);
		buttonTable.add(saveButton).pad(pad);

		// Back home
		ImageButtonStyle homeButtonStyle = new ImageButtonStyle();
		homeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/home.png"))));
		homeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("ui/buttons/homehover.png"))));

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
		scaleButtonStyle = new TextButtonStyle();
		scaleButtonStyle.font = AATinkererGame.font.generateFont(AATinkererGame.normalFontParam);
		scaleButtonStyle.fontColor = AATinkererGame.WHITE;
		scaleButtonStyle.up = AATinkererGame.normalPatch;
		scaleButtonStyle.over = AATinkererGame.hoverPatch;
		scaleButtonStyle.checked = AATinkererGame.hoverPatch;

		scaleLabel = new Label("Scale", AATinkererGame.normalLabelStyle);
		stage.addActor(scaleLabel);

		scaleButtons = new ArrayList<TextButton>();

		createScaleButtons("Auto", Scale.AUTO);
		createScaleButtons("x1", Scale.X1);
		createScaleButtons("x2", Scale.X2);
		createScaleButtons("x4", Scale.X4);

		ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();
		for (TextButton button : scaleButtons)
			buttonGroup.add(button);
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(1);
		scaleButtons.get(0).toggle();
	}

	private void createScaleButtons(String text, final Scale scale)
	{
		TextButton button = new TextButton(text, scaleButtonStyle);
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
	public void render(float delta)
	{
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.toPausedGameScreen();
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);

		float buttonWidth = scaleButtons.get(0).getWidth();

		scaleLabel.setPosition(20, 20);
		for (int i = 0; i < scaleButtons.size(); i++)
			scaleButtons.get(i).setPosition(100 + (buttonWidth + 20) * i, 20);
	}
}
