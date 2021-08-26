package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;
	private Table table;

	public PauseScreen(AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		table = new Table();
		table.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);

//		FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
//		fontParam.size = 32;
//		fontParam.padLeft = 8;
//		fontParam.padRight = 8;
//
//		NinePatch ninePatch = new NinePatch(new Texture("button.png"), 2, 2, 2, 2);
//
//		TextButtonStyle textButtonStyle = new TextButtonStyle();
//		textButtonStyle.font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(fontParam);
//		textButtonStyle.overFontColor = new Color(31.f / 255, 33.f / 255, 59.f / 255, 1);
//		textButtonStyle.downFontColor = Color.GOLD;
//		textButtonStyle.fontColor = new Color(246.f / 255, 246.f / 255, 246.f / 255, 1);
//		textButtonStyle.up = new NinePatchDrawable(ninePatch);
//
//		TextButton textButton = new TextButton("qsdfqdsfdqsf", textButtonStyle);
//		table.add(textButton);

		ImageButtonStyle resumeButtonStyle = new ImageButtonStyle();
		resumeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/play.png"))));
		resumeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/playhover.png"))));
		ImageButton resumeButton = new ImageButton(resumeButtonStyle);
		table.add(resumeButton);

		ImageButtonStyle muteButtonStyle = new ImageButtonStyle();
		muteButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundon.png"))));
		muteButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundonhover.png"))));
		muteButtonStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundoff.png"))));
		muteButtonStyle.imageCheckedOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/soundoffhover.png"))));
		ImageButton muteButton = new ImageButton(muteButtonStyle);
		table.add(muteButton);

		ImageButtonStyle homeButtonStyle = new ImageButtonStyle();
		homeButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/home.png"))));
		homeButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Ui/Buttons/homehover.png"))));
		ImageButton homeButton = new ImageButton(homeButtonStyle);
		table.add(homeButton);
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(31.f / 255, 33.f / 255, 59.f / 255, 1);
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

	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}
