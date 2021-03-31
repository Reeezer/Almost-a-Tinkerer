package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen
{
	
	final private AATinkererGame game;
	
	private OrthographicCamera camera;
	
	private int x, y;
	private int zoomLevel;
	
	private TileMap map;
	
	public GameScreen(AATinkererGame game)
	{
		this.game = game;
		
		camera = new OrthographicCamera();

		map = new TileMap(250, 250);
		
		x = 0;
		y = 0;
		zoomLevel = 0;
	}

	@Override
	public void show()
	{
				
	}

	@Override
	public void render(float delta)
	{
		/* input */

		// zoom

		// with the numpad
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_ADD))
			zoomLevel -= 1;
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_SUBTRACT))
			zoomLevel += 1;

		// with the mouse wheel
		zoomLevel += game.input.getScrollY();

		zoomLevel = (zoomLevel < -1) ? -1 : zoomLevel;
		zoomLevel = (zoomLevel > 4) ? 4 : zoomLevel;

		float zoom = (float) Math.pow(2.f, (float) zoomLevel);

		// move the map

		// with the keys
		int dd = (int) (5.f * zoom);
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			dd *= 5;
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			y -= 1 * dd;
		if (Gdx.input.isKeyPressed(Keys.UP))
			y += 1 * dd;
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			x += 1 * dd;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			x -= 1 * dd;

		// with the mouse (drag and drop)
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			x -= (int) (Gdx.input.getDeltaX() * zoom);
			y += (int) (Gdx.input.getDeltaY() * zoom);
		}

		// with the mouse (border) FIXME fix border scrolling (which is temporarily disabled)
		final int border = -1;
		if (Gdx.input.getY() >= Gdx.graphics.getHeight() - border)
			y -= 1 * dd;
		if (Gdx.input.getY() <= border)
			y += 1 * dd;
		if (Gdx.input.getX() >= Gdx.graphics.getWidth() - border)
			x += 1 * dd;
		if (Gdx.input.getX() <= border)
			x -= 1 * dd;

		game.input.reset();

		/* render */

		Gdx.gl.glClearColor(0, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.x = x;
		camera.position.y = y;
		camera.zoom = zoom;
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		map.render(game.batch);

		game.batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
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
		map.dispose();
	}

}
