package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AATinkererGame extends ApplicationAdapter
{
	private SpriteBatch batch;	
	private OrthographicCamera camera;
	
	private TileMap map;
	
	private int x, y;
	private int zoomLevel;

	@Override
	public void create()
	{
		batch = new SpriteBatch();	
		camera = new OrthographicCamera();
		
		map = new TileMap(250, 250);
		
		
		x = 0;
		y = 0;
		zoomLevel = 0;
	}
	

	@Override
	public void render()
	{
		/* input */
		
		// zoom
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_ADD))
			zoomLevel -= 1;
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_SUBTRACT))
			zoomLevel += 1;
		
		zoomLevel = (zoomLevel < -1) ? -1 : zoomLevel;
		zoomLevel = (zoomLevel > 4) ? 4 : zoomLevel;
		
		float zoom = (float) Math.pow(2.f, (float)zoomLevel);
		
		// move the map
		
		// with the keys
		int dd = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) dd = 50;
		if (Gdx.input.isKeyPressed(Keys.DOWN)) y -= 1 * dd;
		if (Gdx.input.isKeyPressed(Keys.UP)) y += 1 * dd;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) x += 1 * dd;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) x -= 1 * dd;
		
		// with the mouse (drag and drop)
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			x -= (int)(Gdx.input.getDeltaX() * zoom);
			y += (int)(Gdx.input.getDeltaY() * zoom);
		}
		
		// with the mouse (border) FIXME fix border scrolling
		final int border = 50;
		if (Gdx.input.getY() >= Gdx.graphics.getHeight() - border) y -= 1 * dd;
		if (Gdx.input.getY() <= border) y += 1 * dd;
		if (Gdx.input.getX() >= Gdx.graphics.getWidth() - border) x += 1 * dd;
		if (Gdx.input.getX() <= border) x -= 1 * dd;
		
		
		/* render */
		
		Gdx.gl.glClearColor(0, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = x;
		camera.position.y = y;
		camera.zoom = zoom;
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		map.render(batch);

		batch.end();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		map.dispose();
	}
}
