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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DifficultyScreen implements Screen
{
	private AATinkererGame game;

	private OrthographicCamera camera;
	private FitViewport viewport;

	private Stage stage;
	private Table diffTable;
	private TextButton exitButton;

	public DifficultyScreen(final AATinkererGame game)
	{
		this.game = game;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(0, 0, camera);

		diffTable = new Table();
		Table mainTable = new Table();
		mainTable.setFillParent(true);

		stage = new Stage();
		stage.setViewport(viewport);
		stage.addActor(mainTable);

		Gdx.input.setInputProcessor(stage);

		Color blueColor = new Color(31.f / 255, 33.f / 255, 59.f / 255, 1);
		Color whiteColor = new Color(246.f / 255, 246.f / 255, 246.f / 255, 1);
		Color transparentColor = new Color(1, 1, 1, 0);

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

		createButton(regularPatch, regularHoverPatch, "Regular", fontParam, transparentColor, blueColor);
		createButton(abundantPatch, abundantHoverPatch, "Abundant", fontParam, transparentColor, blueColor);
		createButton(rarePatch, rareHoverPatch, "Rare", fontParam, transparentColor, blueColor);
		diffTable.row();
		createButton(bigSparsePatch, bigSparseHoverPatch, "Big sparse", fontParam, transparentColor, blueColor);
		createButton(everywherePatch, everywhereHoverPatch, "Everywhere", fontParam, transparentColor, blueColor);
		createButton(goodLuckPatch, goodLuckHoverPatch, "Good luck", fontParam, transparentColor, blueColor);

		mainTable.add(title).padBottom(25);
		mainTable.row();
		mainTable.add(diffTable);

		// Exit
		NinePatch textButtonPatch = new NinePatch(new Texture("Ui/Buttons/textbutton.png"), 2, 2, 2, 2);

		TextButtonStyle regularButtonStyle = new TextButtonStyle();
		regularButtonStyle.font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(fontParam);
		regularButtonStyle.overFontColor = blueColor;
		regularButtonStyle.fontColor = whiteColor;
		regularButtonStyle.up = new NinePatchDrawable(textButtonPatch);

		exitButton = new TextButton("Exit", regularButtonStyle);
		exitButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				// FIXME return to previous menu
			};
		});
		stage.addActor(exitButton);
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

		exitButton.setPosition(20, height - 20 - exitButton.getHeight());
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
