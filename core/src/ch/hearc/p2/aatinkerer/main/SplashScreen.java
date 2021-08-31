package ch.hearc.p2.aatinkerer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen
{
	private OrthographicCamera camera;

	private AATinkererGame game;
	private Texture background;
	private Texture hearcLogo;

	private int width;
	private int height;

	private float passedTime;
	private final static float MAX_TIME = 2.f;

	private BitmapFont veryLargeFont;
	private BitmapFont largeFont;
	private BitmapFont smallFont;

	public SplashScreen(AATinkererGame game)
	{
		this.game = game;

		camera = new OrthographicCamera();
		camera.position.x = 0;
		camera.position.y = 0;
		camera.zoom = 1;

		background = new Texture("Menus/splash3.png");
		hearcLogo = new Texture("Menus/pixel_arc.png");

		passedTime = 0;

		width = 0;
		height = 0;

		// free to use font https://tinyworlds.itch.io/free-pixel-font-thaleah
		FreeTypeFontParameter veryLargeFontParameter = new FreeTypeFontParameter();
		veryLargeFontParameter.size = 96;
		veryLargeFontParameter.color = AATinkererGame.WHITE;
		veryLargeFont = new FreeTypeFontGenerator(Gdx.files.internal("Font/ThaleahFat.ttf")).generateFont(veryLargeFontParameter);

		// public domain font https://grafxkid.itch.io/at01
		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 64;
		fontParameter.color = AATinkererGame.WHITE;
		largeFont = AATinkererGame.font.generateFont(fontParameter);

		// public domain font https://grafxkid.itch.io/at01
		FreeTypeFontParameter smallFontParameter = new FreeTypeFontParameter();
		smallFontParameter.size = 32;
		smallFontParameter.color = AATinkererGame.WHITE;
		smallFont = AATinkererGame.font.generateFont(smallFontParameter);
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
			game.toSaveScreen();

		Gdx.gl.glClearColor(0, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		// adapted from https://stackoverflow.com/a/50165098
		// keep the image at the center without deforming it and keeping aspect ratio
		float widthRatio = this.width / (float) background.getWidth();
		float heightRatio = this.height / (float) background.getHeight();
		float bestRatio = Math.max(widthRatio, heightRatio);

		float newWidth = background.getWidth() * bestRatio;
		float newHeight = background.getHeight() * bestRatio;

		// background
		game.batch.draw(background, (width - newWidth) / 2, (height - newHeight) / 2, (float) newWidth, (float) newHeight);

		// logo
		GlyphLayout logoTextLayout = new GlyphLayout();
		logoTextLayout.setText(veryLargeFont, "Almost a Tinkerer");
		veryLargeFont.draw(game.batch, "Almost a Tinkerer", (width - logoTextLayout.width) / 2.f, (1.5f * height + logoTextLayout.height) / 2.f);

		// press space to start
		GlyphLayout startTextLayout = new GlyphLayout();
		startTextLayout.setText(largeFont, "Press space to start");
		if (passedTime <= (SplashScreen.MAX_TIME / 2.f))
			largeFont.draw(game.batch, "Press space to start", (width - startTextLayout.width) / 2.f, ((2.f * height / 3.f) + startTextLayout.height) / 2.f);

		// hearc logo
		largeFont.draw(game.batch, "haute ecole", 10, 90);
		smallFont.draw(game.batch, "neuchatel berne jura", 10, 50);
		game.batch.draw(hearcLogo, 210, 0, hearcLogo.getWidth() * 2, hearcLogo.getHeight() * 2);
		largeFont.draw(game.batch, "ingenierie", 470, 90);
		smallFont.draw(game.batch, "www.he-arc.ch", 470, 50);

		// tout droits reservés
		smallFont.draw(game.batch, "Tous droits reserves - Muller Leon, Meyer Luca - HE-Arc Ingenierie", 10, height - 10);

		game.batch.end();

		if (passedTime >= SplashScreen.MAX_TIME)
			passedTime = 0;
		else
			passedTime += delta;

	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);

		this.height = height;
		this.width = width;
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

	}
}
