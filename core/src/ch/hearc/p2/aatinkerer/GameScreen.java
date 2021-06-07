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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.TimeUtils;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.FactoryType;
import ch.hearc.p2.aatinkerer.ui.BuildingRecipeDisplay;
import ch.hearc.p2.aatinkerer.ui.UIElement;
import ch.hearc.p2.aatinkerer.ui.ContractDisplay;
import ch.hearc.p2.aatinkerer.ui.ItemDropdownMenu;
import ch.hearc.p2.aatinkerer.ui.NotificationManager;
import ch.hearc.p2.aatinkerer.ui.Notification;
import ch.hearc.p2.aatinkerer.ui.Toolbar;

public class GameScreen implements Screen
{
	final private AATinkererGame game;

	private OrthographicCamera mapCamera;
	private OrthographicCamera uiCamera;
	private OrthographicCamera hoverCamera;

	private List<UIElement> uiElements;

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

	private NotificationManager notificationManager;
	private ContractDisplay contractDisplay;

	private MilestoneListener milestoneListener;
	private ContractListener contractListener;
	private BuildingRecipeDisplay buildingRecipeDisplay;
	private ItemDropdownMenu itemDropdownMenu;

	public GameScreen(AATinkererGame game)
	{
		this.game = game;

		mapCamera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		hoverCamera = new OrthographicCamera();

		uiElements = new ArrayList<UIElement>();

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

		notificationManager = new NotificationManager();
		uiElements.add(notificationManager);

		contractDisplay = new ContractDisplay();
		uiElements.add(contractDisplay);

		itemDropdownMenu = new ItemDropdownMenu();
		buildingRecipeDisplay = new BuildingRecipeDisplay(itemDropdownMenu);
		uiElements.add(buildingRecipeDisplay);
		uiElements.add(itemDropdownMenu);

		milestoneListener = new MilestoneListener() {
			@Override
			public void unlockMilestone(Milestone milestone)
			{
				for (FactoryType factoryType : milestone.getUnlockedFactoryTypes())
					factoryToolbar.setItemEnabled(factoryType, true);

				if (milestone != Milestone.START)
				{
					Notification popup = new Notification("Milestone Unlocked", milestone.description(), 8.f);
					notificationManager.displayPopup(popup);
				}
			}
		};

		contractListener = new ContractListener() {
			@Override
			public void contractAdded(Contract contract, boolean isStoryContract)
			{
				notificationManager.displayPopup(new Notification("New contract", contract.description(), 10.f));
				contractDisplay.setContract(contract);
			}
		};

		// popupManager.displayPopup(new Popup("Bleh", "Hello everybody, today we are
		// going to write a huge text so we can try notifications. Hello everybody,
		// today we are going to write a huge text so we can try notifications. Hello
		// everybody, today we are going to write a huge text so we can try
		// notifications.", 3.f));
		// popupManager.displayPopup(new Popup("Salut", "Wesh la famille", 3.f));
		// popupManager.displayPopup(new Popup("Ouais ben ouais voilà quoi", "Salut yo
		// yo ouais yo yo yo ouais ouais yo", 10.f));

		GameManager.init().addMilestoneListener(milestoneListener);
		GameManager.getInstance().addContractListener(contractListener);
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
		if (Gdx.input.isKeyJustPressed(Keys.A))
		{
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
		if (Gdx.input.isKeyJustPressed(Keys.R))
		{
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
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_0))
			factoryToolbar.setActiveItem(10);
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			factoryToolbar.setActiveItem(-1);
			buildingRecipeDisplay.setBuilding(null); // FIXME implement close method for clickable
		}
		FactoryType factoryType = (FactoryType) factoryToolbar.getActiveItem();

		// FIXME ça marche pas pour le contract display
		// handle left mouse click
		if (Gdx.input.isButtonPressed(Buttons.LEFT))
		{

			boolean mouseCaptured = false;

			// check if we need to capture mouse input or let it through to the rest of the
			// UI to place buildings
			for (UIElement clickable : uiElements)
			{
				// si l'élement est pas visible on ne le considère pas
				if (!clickable.visible())
					continue;

				int mx = Gdx.input.getX();
				int my = height - Gdx.input.getY();
				
				Rectangle bounds = clickable.getBounds();
				
				System.out.format("checking bounds for '%s' = %s, with mouse coords = (%d,%d)%n", clickable.getClass().getSimpleName(), clickable.getBounds(), mx, my);
				
				if (bounds.contains(new Vector2(mx, my)))
				{
					System.out.format("click captured at (%d,%d) by %s%n", mx, my, clickable.getClass().getSimpleName());
					
					mouseCaptured = true;

					// inner positions
					int ix = (int) (mx - bounds.x);
					int iy = (int) (my - bounds.y);

					clickable.passRelativeClick((int) ix, (int) iy);

					break; // stop checking for clicks on clickables, only one should capture the click
				}
			}

			if (!mouseCaptured)
			{
				int tileX = screenToTileX(Gdx.input.getX());
				int tileY = screenToTileY(Gdx.input.getY());
				// System.out.format("Button left at (%d, %d), converted to (%d, %d)\n", Gdx.input.getX(), Gdx.input.getY(), tileX, tileY);

				// place a building if one is selected, else interact with the map
				if (factoryType != null)
				{
					map.placeBuilding(tileX, tileY, direction, factoryType, mirrored);
				} else
				{
					Building attemptContextualMenuBuilding = map.factoryAt(tileX, tileY);

					if (attemptContextualMenuBuilding != null && attemptContextualMenuBuilding.recipes() != null && attemptContextualMenuBuilding.canSelectRecipe())
						buildingRecipeDisplay.setBuilding(attemptContextualMenuBuilding);
					else
						buildingRecipeDisplay.setBuilding(null);
				}
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
			GameManager.getInstance().tick();
		}

		if (fpsDisplayTicks++ > 60)
		{
			fpsDisplayTicks = 0;
			// System.out.println(Gdx.graphics.getFramesPerSecond());
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
		if (factoryType != null)
		{
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

			game.batch.draw(textureRegion, (x + xoffset) * hoverCamera.zoom, (y + yoffset) * hoverCamera.zoom, 0, 0,
					(float) hoverTexture.getWidth(), (float) hoverTexture.getHeight(), 1.f, 1.f,
					(float) direction * 90.f);
		}

		game.batch.setProjectionMatrix(uiCamera.combined);
		
		for (UIElement uiElement : this.uiElements)
			uiElement.render(game.batch, delta);

		// FIXME
		// ItemDropdownMenu itemMenu = new ItemDropdownMenu();
		// itemMenu.render(game.batch, Gdx.input.getX() / 2, (this.height -
		// Gdx.input.getY()) / 2);

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
		
		// changer l'écran pour les élements de l'interface afin qu'ils puissent se repositionner
		for (UIElement clickable : this.uiElements)
			clickable.setScreenSize(width, height);
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
