package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.TimeUtils;

import ch.hearc.p2.aatinkerer.buildings.FactoryType;

public class GameScreen implements Screen
{

	final private AATinkererGame game;

	private OrthographicCamera camera;

	private int x, y;
	private int width, height;
	private int zoomLevel;
	private int direction;
	private float zoom;
	private int fpsDisplayTicks;
	
	private long lastTime;
	private long unprocessedTime;
	private final long processingTimeCap = 10L; // 100TPS

	private FactoryType factoryType;

	private TileMap map;

	public GameScreen(AATinkererGame game)
	{
		this.game = game;

		camera = new OrthographicCamera();

		map = new TileMap(250, 250);

		x = 0;
		y = 0;
		width = 0;
		height = 0;
		zoomLevel = 0;
		direction = 0;
		fpsDisplayTicks = 0;
		
		lastTime = TimeUtils.millis();
		unprocessedTime = 0;

		factoryType = FactoryType.NONE;
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		long firstTime = TimeUtils.millis();
		long passedTime = firstTime - lastTime;
		lastTime = firstTime;
		unprocessedTime += passedTime;
		
		/* input */

		// regen new map FIXME debug
		if (Gdx.input.isKeyJustPressed(Keys.T)) {
			map.dispose();
			map = new TileMap(250, 250);
		}

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

		zoom = (float) Math.pow(2.f, (float) zoomLevel);

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

		// with the mouse (border) FIXME fix border scrolling (which is temporarily
		// disabled)
		final int border = -1;
		if (Gdx.input.getY() >= Gdx.graphics.getHeight() - border)
			y -= 1 * dd;
		if (Gdx.input.getY() <= border)
			y += 1 * dd;
		if (Gdx.input.getX() >= Gdx.graphics.getWidth() - border)
			x += 1 * dd;
		if (Gdx.input.getX() <= border)
			x -= 1 * dd;

		// buildings

		// rotation of buildings you are about to place
		if (Gdx.input.isKeyJustPressed(Keys.R))
			direction = (direction + 1) % 4;

		// choose building
		if (Gdx.input.isKeyJustPressed(Keys.A))
			this.factoryType = FactoryType.CONVEYOR;

		if (Gdx.input.isKeyJustPressed(Keys.Z))
			this.factoryType = FactoryType.EXTRACTOR;

		// place building
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			int tileX = screenToTileX(Gdx.input.getX());
			int tileY = screenToTileY(Gdx.input.getY());
			System.out.format("Button left at (%d, %d), converted to (%d, %d)\n", Gdx.input.getX(), Gdx.input.getY(),
					tileX, tileY);

			if (factoryType != FactoryType.NONE)
				map.placeBuilding(tileX, tileY, direction, factoryType);
		}

		// delete building
		if (Gdx.input.isKeyPressed(Keys.DEL))
			map.deleteBuilding(screenToTileX(Gdx.input.getX()), screenToTileY(Gdx.input.getY()));

		game.input.reset();

		/* update */
		
		// cap on fixed TPS
		while (unprocessedTime >= processingTimeCap) {
			unprocessedTime -= processingTimeCap;
			map.update();
			
			// FIXME do all logic updates here
		}
		
		if (fpsDisplayTicks++ > 60) {
			fpsDisplayTicks = 0;
			System.out.println(Gdx.graphics.getFramesPerSecond());
		}
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

	public int screenToTileX(int screenX)
	{
		return (int) (((screenX - (width / 2.f)) * zoom + camera.position.x) / TileMap.TILESIZE);
	}

	public int screenToTileY(int screenY)
	{
		return (int) ((((height - screenY) - (height / 2.f)) * zoom + camera.position.y) / TileMap.TILESIZE);
	}

	@Override
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
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
