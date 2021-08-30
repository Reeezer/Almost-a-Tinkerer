package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DifficultyScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;
	private Table diffTable;
	private Table mainTable;

	public DifficultyScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		diffTable = new Table();
		mainTable = new Table();
		mainTable.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		Gdx.input.setInputProcessor(stage);
		
		// Title
		FreeTypeFontParameter titleFontParam = new FreeTypeFontParameter();
		titleFontParam.size = 80;

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(titleFontParam);
		labelStyle.fontColor = Color.WHITE;

		Label title = new Label("Choose a difficulty", labelStyle);

		// Difficulties
		FreeTypeFontParameter fontParam = new FreeTypeFontParameter();
		fontParam.size = 40;
		fontParam.padLeft = 8;
		fontParam.padRight = 8;

		Color fontColor = new Color(31.f / 255, 33.f / 255, 59.f / 255, 1);
		Color overColor = new Color(1, 1, 1, 0);

		NinePatch regularPatch = new NinePatch(new Texture("Ui/Buttons/difficulty1.png"), 2, 2, 2, 2);
		NinePatch abundantPatch = new NinePatch(new Texture("Ui/Buttons/difficulty2.png"), 2, 2, 2, 2);
		NinePatch rarePatch = new NinePatch(new Texture("Ui/Buttons/difficulty3.png"), 2, 2, 2, 2);
		NinePatch bigSparsePatch = new NinePatch(new Texture("Ui/Buttons/difficulty4.png"), 2, 2, 2, 2);
		NinePatch everywherePatch = new NinePatch(new Texture("Ui/Buttons/difficulty5.png"), 2, 2, 2, 2);
		NinePatch goodLuckPatch = new NinePatch(new Texture("Ui/Buttons/difficulty6.png"), 2, 2, 2, 2);

		NinePatch regularHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty1hover.png"), 2, 2, 2, 2);
		NinePatch abundantHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty2hover.png"), 2, 2, 2, 2);
		NinePatch rareHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty3hover.png"), 2, 2, 2, 2);
		NinePatch bigSparseHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty4hover.png"), 2, 2, 2, 2);
		NinePatch everywhereHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty5hover.png"), 2, 2, 2, 2);
		NinePatch goodLuckHoverPatch = new NinePatch(new Texture("Ui/Buttons/difficulty6hover.png"), 2, 2, 2, 2);

//		TextButtonStyle regularButtonStyle = new TextButtonStyle();
//		regularButtonStyle.font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(fontParam);
//		regularButtonStyle.overFontColor = overColor;
//		regularButtonStyle.fontColor = fontColor;
//		regularButtonStyle.up = new NinePatchDrawable(regularPatch);
//		TextButton regularButton = new TextButton("Regular", regularButtonStyle);
//		diffTable.add(regularButton);

		createButton(regularPatch, regularHoverPatch, "Regular", fontParam, overColor, fontColor);
		createButton(abundantPatch, abundantHoverPatch, "Abundant", fontParam, overColor, fontColor);
		createButton(rarePatch, rareHoverPatch, "Rare", fontParam, overColor, fontColor);
		diffTable.row();
		createButton(bigSparsePatch, bigSparseHoverPatch, "Big sparse", fontParam, overColor, fontColor);
		createButton(everywherePatch, everywhereHoverPatch, "Everywhere", fontParam, overColor, fontColor);
		createButton(goodLuckPatch, goodLuckHoverPatch, "Good luck", fontParam, overColor, fontColor);

		mainTable.add(title).padBottom(25);
		mainTable.row();
		mainTable.add(diffTable);
	}

	private void createButton(NinePatch ninePatch, NinePatch ninePatchHover, String title, FreeTypeFontParameter fontParam, Color overColor, Color fontColor)
	{
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(fontParam);
		buttonStyle.overFontColor = overColor;
		buttonStyle.fontColor = fontColor;
		buttonStyle.up = new NinePatchDrawable(ninePatch);
		buttonStyle.over = new NinePatchDrawable(ninePatchHover);

		TextButton button = new TextButton(title, buttonStyle);
		diffTable.add(button).pad(50);
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
