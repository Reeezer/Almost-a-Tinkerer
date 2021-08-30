package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;

	public PauseScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		Table table = new Table();
		table.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(table);

		// Resume
		ImageButtonStyle resumeButtonStyle = new ImageButtonStyle();
		resumeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/play.png"))));
		resumeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/playhover.png"))));

		ImageButton resumeButton = new ImageButton(resumeButtonStyle);
		resumeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				game.toGameScreen();
			};
		});
		table.add(resumeButton).pad(115);

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
				// FIXME couper musique
			};
		});
		table.add(muteButton).pad(115);

		// Back home
		ImageButtonStyle homeButtonStyle = new ImageButtonStyle();
		homeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/home.png"))));
		homeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/homehover.png"))));

		ImageButton homeButton = new ImageButton(homeButtonStyle);
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				game.toSaveScreen();
			};
		});
		table.add(homeButton).pad(115);
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
	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
		viewport.setWorldSize(width, height);
		stage.getViewport().update(width, height, true);
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
