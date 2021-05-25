package ch.hearc.p2.aatinkerer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import ch.hearc.p2.aatinkerer.buildings.FactoryType;
import ch.hearc.p2.aatinkerer.ui.Clickable;
import ch.hearc.p2.aatinkerer.ui.Toolbar;

public class GameScreen implements Screen
{

	final private AATinkererGame game;

	private OrthographicCamera mapCamera;
	private OrthographicCamera uiCamera;
	private OrthographicCamera hoverCamera;

	private List<Clickable> uiElements;

	private int x, y;
	private int width, height;
	private int zoomLevel;
	private int direction;
	private float zoom;
	private int fpsDisplayTicks;
	private boolean mirrored;

	private long lastTime;
	private long unprocessedTime;
	private final long processingTimeCap = 10L; // 100TPS

	private Toolbar factoryToolbar;

	private TileMap map;

	private MilestoneListener milestoneListener;

	public GameScreen(AATinkererGame game)
	{
		this.game = game;

		mapCamera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		hoverCamera = new OrthographicCamera();

		uiElements = new ArrayList<Clickable>();

		uiCamera.zoom = 0.5f;

		map = new TileMap(250, 250);

		x = 0;
		y = 0;
		width = 0;
		height = 0;
		zoomLevel = 0;
		direction = 0;
		mirrored = false;
		fpsDisplayTicks = 0;

		lastTime = TimeUtils.millis();
		unprocessedTime = 0;

		factoryToolbar = new Toolbar(FactoryType.values());

		uiElements.add(factoryToolbar);

		milestoneListener = new MilestoneListener() {
			@Override
			public void unlockMilestone(Milestone milestone)
			{
				// TODO on récupère les factories unlock par la milestone et on les active dans la toolbar et dans les raccourcis
				for (FactoryType factoryType : milestone.getUnlockedFactoryTypes())
				{
					factoryToolbar.setItemEnabled(factoryType, true);
				}
			}
		};

		ContractManager.init().addMilestoneListener(milestoneListener);
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
		if (Gdx.input.isKeyJustPressed(Keys.A)) {
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
		if (Gdx.input.isButtonPressed(Buttons.RIGHT))
		{
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
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				direction = (direction + 3) % 4;
			else
				direction = (direction + 1) % 4;
		}
		if (Gdx.input.isKeyJustPressed(Keys.T))
			mirrored = !mirrored;

		// choose building
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1))
			factoryToolbar.setActiveItem(0);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2))
			factoryToolbar.setActiveItem(1);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_3))
			factoryToolbar.setActiveItem(2);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_4))
			factoryToolbar.setActiveItem(3);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_5))
			factoryToolbar.setActiveItem(4);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_6))
			factoryToolbar.setActiveItem(5);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_7))
			factoryToolbar.setActiveItem(6);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_8))
			factoryToolbar.setActiveItem(7);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_9))
			factoryToolbar.setActiveItem(8);
		if (Gdx.input.isKeyJustPressed(Keys.NUM_0))
			factoryToolbar.setActiveItem(9);
		FactoryType factoryType = (FactoryType) factoryToolbar.getActiveItem();

		// handle left mouse click
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {

			boolean mouseCaptured = false;

			// check if we need to capture mouse input or let it through to the rest of the UI to place buildings
			for (Clickable clickable : uiElements) {
				int mx = Gdx.input.getX();
				int my = height - Gdx.input.getY();

				Rectangle bounds = factoryToolbar.getBounds();

				if (bounds.contains(new Vector2(mx, my))) {
					mouseCaptured = true;

					// inner positions
					int ix = (int) (mx - bounds.x);
					int iy = (int) (my - bounds.y);

					// the item doesn't care about zoom so it needs to be taken into account
					clickable.passRelativeClick((int) (ix * uiCamera.zoom), (int) (iy * uiCamera.zoom));
				}
			}

			if (!mouseCaptured) {
				// place building
				int tileX = screenToTileX(Gdx.input.getX());
				int tileY = screenToTileY(Gdx.input.getY());
				System.out.format("Button left at (%d, %d), converted to (%d, %d)\n", Gdx.input.getX(), Gdx.input.getY(), tileX, tileY);

				if (factoryType != null)
					map.placeBuilding(tileX, tileY, direction, factoryType, mirrored);
			}
		}

		// delete building
		if (Gdx.input.isKeyPressed(Keys.DEL))
			map.deleteBuilding(screenToTileX(Gdx.input.getX()), screenToTileY(Gdx.input.getY()));

		game.input.reset();

		/* update */

		// cap on fixed TPS
		while (unprocessedTime >= processingTimeCap)
		{
			unprocessedTime -= processingTimeCap;
			map.update();

			// FIXME do all logic updates here
			ContractManager.getInstance().tick();
		}

		if (fpsDisplayTicks++ > 60) {
			fpsDisplayTicks = 0;
			System.out.println(Gdx.graphics.getFramesPerSecond());
		}
		/* render */

		Gdx.gl.glClearColor(0, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mapCamera.position.x = x;
		mapCamera.position.y = y;
		mapCamera.zoom = zoom;

		if (mapCamera.zoom < 1.f)
			hoverCamera.zoom = mapCamera.zoom;
		else
			hoverCamera.zoom = 1.f;

		// without this the hovered item is at the wrong position once zoomed-in
		hoverCamera.position.x = width / 2 * hoverCamera.zoom;
		hoverCamera.position.y = height / 2 * hoverCamera.zoom;

		mapCamera.update();
		uiCamera.update();
		hoverCamera.update();

		game.batch.begin();

		game.batch.setProjectionMatrix(mapCamera.combined);
		map.render(game.batch);

		game.batch.setProjectionMatrix(hoverCamera.combined);
		// item to be placed
		if (factoryType != null) {
			Texture hoverTexture = factoryType.getHoverTexture();
			int x = Gdx.input.getX();
			int y = height - Gdx.input.getY();

			int xoffset = 0;
			if (direction == 1 || direction == 2)
				xoffset = 32;

			int yoffset = 0;
			if (direction == 2 || direction == 3)
				yoffset = 32;
			TextureRegion textureRegion = new TextureRegion(hoverTexture);

			game.batch.draw(textureRegion, (x + xoffset) * hoverCamera.zoom, (y + yoffset) * hoverCamera.zoom, 0, 0, (float) hoverTexture.getWidth(), (float) hoverTexture.getHeight(), 1.f, 1.f, (float) direction * 90.f);
		}

		game.batch.setProjectionMatrix(uiCamera.combined);

		factoryToolbar.setBounds((int) ((width - (FactoryType.values().length * Toolbar.TEXSIZE / uiCamera.zoom)) / 2), 0, (int) (FactoryType.values().length * Toolbar.TEXSIZE / uiCamera.zoom), (int) (Toolbar.TEXSIZE / uiCamera.zoom));
		factoryToolbar.render(game.batch, (int) (factoryToolbar.getBounds().x * uiCamera.zoom), (int) factoryToolbar.getBounds().y);

		game.batch.end();
	}

	public int screenToTileX(int screenX)
	{
		return (int) (((screenX - (width / 2.f)) * zoom + mapCamera.position.x) / TileMap.TILESIZE);
	}

	public int screenToTileY(int screenY)
	{
		return (int) ((((height - screenY) - (height / 2.f)) * zoom + mapCamera.position.y) / TileMap.TILESIZE);
	}

	@Override
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;

		mapCamera.setToOrtho(false, width, height);
		uiCamera.setToOrtho(false, width, height);
		hoverCamera.setToOrtho(false, width, height);
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
