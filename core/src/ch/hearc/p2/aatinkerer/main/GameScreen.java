package ch.hearc.p2.aatinkerer.main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Splitter;
import ch.hearc.p2.aatinkerer.data.Contract;
import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Milestone;
import ch.hearc.p2.aatinkerer.data.Ressource;
import ch.hearc.p2.aatinkerer.listeners.ContractListener;
import ch.hearc.p2.aatinkerer.listeners.MilestoneListener;
import ch.hearc.p2.aatinkerer.ui.Notification;
import ch.hearc.p2.aatinkerer.ui.UIElement;
import ch.hearc.p2.aatinkerer.ui.widgets.BuildingRecipeDisplay;
import ch.hearc.p2.aatinkerer.ui.widgets.StoryContractDisplay;
import ch.hearc.p2.aatinkerer.ui.widgets.ItemDropdownMenu;
import ch.hearc.p2.aatinkerer.ui.widgets.MiniHoverPopup;
import ch.hearc.p2.aatinkerer.ui.widgets.NotificationManager;
import ch.hearc.p2.aatinkerer.ui.widgets.SplitterMenu;
import ch.hearc.p2.aatinkerer.ui.widgets.Toolbar;
import ch.hearc.p2.aatinkerer.world.TileMap;

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
	private boolean mirrored;
	private boolean isInputTunnel;

	private long lastTime;
	private long unprocessedTime;
	private final long processingTimeCap = 10L; // 100TPS

	private Toolbar factoryToolbar;

	private TileMap map;

	private NotificationManager notificationManager;
	private StoryContractDisplay contractDisplay;

	private MilestoneListener milestoneListener;
	private ContractListener contractListener;
	private BuildingRecipeDisplay buildingRecipeDisplay;
	private ItemDropdownMenu itemDropdownMenu;
	private SplitterMenu splitterMenu;

	private MiniHoverPopup miniHoverPopup;

	private BitmapFont font;

	private boolean justClicked;
	private int initialx;
	private int initialy;

	public GameScreen(AATinkererGame game)
	{
		this.game = game;

		mapCamera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		hoverCamera = new OrthographicCamera();

		uiElements = new ArrayList<UIElement>();

		final int MAPWIDTH = 160;
		final int MAPHEIGHT = 160;
		map = new TileMap(MAPWIDTH, MAPHEIGHT);

		x = MAPWIDTH / 2 * TileMap.TILESIZE;
		y = MAPHEIGHT / 2 * TileMap.TILESIZE;
		width = 0;
		height = 0;
		zoomLevel = 0;
		direction = 0;
		mirrored = false;
		isInputTunnel = false;

		justClicked = true;
		initialx = -1;
		initialy = -1;

		lastTime = TimeUtils.millis();
		unprocessedTime = 0;

		factoryToolbar = new Toolbar(FactoryType.values());
		uiElements.add(factoryToolbar);

		notificationManager = new NotificationManager();
		uiElements.add(notificationManager);

		contractDisplay = new StoryContractDisplay();
		uiElements.add(contractDisplay);

		itemDropdownMenu = new ItemDropdownMenu();
		buildingRecipeDisplay = new BuildingRecipeDisplay(itemDropdownMenu);
		uiElements.add(buildingRecipeDisplay);
		uiElements.add(itemDropdownMenu);

		splitterMenu = new SplitterMenu();
		uiElements.add(splitterMenu);

		miniHoverPopup = new MiniHoverPopup();

		FreeTypeFontParameter titleFontParameter = new FreeTypeFontParameter();
		titleFontParameter.size = 16;
		titleFontParameter.color = Color.WHITE;
		titleFontParameter.borderColor = Color.BLACK;
		titleFontParameter.borderWidth = 1;
		font = new FreeTypeFontGenerator(Gdx.files.internal("Font/at01.ttf")).generateFont(titleFontParameter);

		milestoneListener = new MilestoneListener() {
			@Override
			public void unlockMilestone(Milestone milestone)
			{
				for (FactoryType factoryType : milestone.getUnlockedFactoryTypes())
					factoryToolbar.setItemEnabled(factoryType, true);

				String title;

				if (milestone == Milestone.START)
					title = "Welcome";
				else if (milestone == Milestone.END_STORY)
					title = "Good job!";
				else
					title = "Milestone Achieved";

				notificationManager.displayPopup(new Notification(title, milestone.description(), 10.f));
			}
		};

		contractListener = new ContractListener() {
			@Override
			public void contractAdded(Contract contract, boolean isStoryContract)
			{
				contractDisplay.setContract(contract);
			}
		};

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
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_1))
			factoryToolbar.setActiveItem(11);
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			factoryToolbar.setActiveItem(-1);
			buildingRecipeDisplay.setBuilding(null);
			itemDropdownMenu.setItems(null);
			splitterMenu.setSplitter(null);
		}
		FactoryType factoryType = (FactoryType) factoryToolbar.getActiveItem();

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

					if (this.justClicked)
						clickable.passRelativeClick((int) ix, (int) iy);

					this.justClicked = false;
					break; // stop checking for clicks on clickables, only one should capture the click
				}
			}

			if (!mouseCaptured)
			{
				int tileX = screenToTileX(Gdx.input.getX());
				int tileY = screenToTileY(Gdx.input.getY());

				System.out.format("Button left at (%d, %d), converted to (%d, %d)\n", Gdx.input.getX(), Gdx.input.getY(), tileX, tileY);

				if (factoryType != null && factoryType != FactoryType.CONVEYOR)
				{
					int inputTunnel = map.placeBuilding(tileX, tileY, direction, factoryType, mirrored);
					if (inputTunnel == 1 || inputTunnel == 2)
						isInputTunnel = (inputTunnel == 1) ? true : false;
					map.placeBuilding(tileX, tileY, direction, factoryType, mirrored);
				}
				else if (factoryType == FactoryType.CONVEYOR) // make it so conveyors can be place more easily in a line
				{
					if ((direction == 0 || direction == 2) && initialy == -1)
					{
						initialy = tileY;
						initialx = -1;
					}
					if ((direction == 1 || direction == 3) && initialx == -1)
					{
						initialx = tileX;
						initialy = -1;
					}

					if (direction == 0 || direction == 2)
						map.placeBuilding(tileX, initialy, direction, factoryType, mirrored);
					if (direction == 1 || direction == 3)
						map.placeBuilding(initialx, tileY, direction, factoryType, mirrored);
				}
				else
				{
					Building attemptContextualMenuBuilding = map.factoryAt(tileX, tileY);

					if (attemptContextualMenuBuilding != null && attemptContextualMenuBuilding instanceof Splitter)
					{
						this.splitterMenu.setSplitter((Splitter) attemptContextualMenuBuilding);
						this.justClicked = false;
					}
					else
					{
						this.splitterMenu.setSplitter(null);
					}

					if (attemptContextualMenuBuilding != null && attemptContextualMenuBuilding.recipes() != null && attemptContextualMenuBuilding.canSelectRecipe())
						buildingRecipeDisplay.setBuilding(attemptContextualMenuBuilding);
					else
					{
						buildingRecipeDisplay.setBuilding(null);
						itemDropdownMenu.setItems(null);
					}
				}
			}
		}
		else
		{
			justClicked = true;
			initialx = -1;
			initialy = -1;
		}

		// delete building
		if (Gdx.input.isKeyPressed(Keys.DEL))
			map.deleteBuilding(screenToTileX(Gdx.input.getX()), screenToTileY(Gdx.input.getY()));

		// display controls
		boolean ctrlPressed = false;
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
			ctrlPressed = true;

		// hover tooltip if no building is selected to be placed
		boolean renderTooltip = false;
		String tooltipText = "";
		int tooltipx = 0;
		int tooltipy = 0;
		if (factoryType == null)
		{
			int x = screenToTileX(Gdx.input.getX());
			int y = screenToTileY(Gdx.input.getY());

			tooltipx = Gdx.input.getX() + 3;
			tooltipy = (height - Gdx.input.getY()) + 3;

			ItemType item = map.itemAt(x, y);
			Building building = map.factoryAt(x, y);
			Building conveyor = map.conveyorAt(x, y);

			// FIXME maybe still display the ressources if the hovered building is an extractor?
			// only display on ressources tiles that don't have a building on top
			if (building == null && conveyor == null && item != null && item != ItemType.NONE)
			{
				tooltipText = item.fullname();
				renderTooltip = true;
			}
		}

		game.input.reset();

		/* update */

		// cap on fixed TPS
		while (unprocessedTime >= processingTimeCap)
		{
			unprocessedTime -= processingTimeCap;
			map.update();

			GameManager.getInstance().tick();
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
			if (factoryType == FactoryType.TUNNEL)
				factoryType.setMirrored(isInputTunnel);
			else
				factoryType.setMirrored(mirrored);
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

		font.draw(game.batch, "Press [Ctrl]\nto see controls", 30, 50);
		if (ctrlPressed)
		{
			int toolbarHeight = 50;
			float pos = (width - (FactoryType.values().length * Toolbar.TEXSIZE)) / 2;
			float deltaa = Toolbar.TEXSIZE;
			for (int i = 1; i <= 9; i++)
				font.draw(game.batch, String.format("[%d]", i), pos + (i - 1) * deltaa + Toolbar.TEXSIZE / 3, toolbarHeight);
			font.draw(game.batch, "[Num 0]", pos + 9 * deltaa - 6, toolbarHeight);
			font.draw(game.batch, "[Num 1]", pos + 10 * deltaa + 15, toolbarHeight);

			font.draw(game.batch, "[Left click]\nPlace", width / 3, 150);
			font.draw(game.batch, "[DEL]\nDelete", width / 3, 100);

			font.draw(game.batch, "[R]\nRotate left", width * 5 / 12, 250);
			font.draw(game.batch, "[Shift + R]\nRotate right", width * 5 / 12, 200);
			font.draw(game.batch, "[T]\nMirror rotation", width * 5 / 12, 150);
			font.draw(game.batch, "[Escape]\nUnselect", width * 5 / 12, 100);

			font.draw(game.batch, "[Right click + drag]\nMove", width / 2, 350);
			font.draw(game.batch, "[Up]\nMove up", width / 2, 300);
			font.draw(game.batch, "[Down]\nMove down", width / 2, 250);
			font.draw(game.batch, "[Left]\nMove left", width / 2, 200);
			font.draw(game.batch, "[Right]\nMove right", width / 2, 150);
			font.draw(game.batch, "[Hold Shift]\nMove faster", width / 2, 100);

			font.draw(game.batch, "[Scroll]\nZoom", width * 7 / 12, 200);
			font.draw(game.batch, "[Num +]\nZoom in", width * 7 / 12, 150);
			font.draw(game.batch, "[Num -]\nZoom out", width * 7 / 12, 100);
		}

		for (UIElement uiElement : this.uiElements)
			uiElement.render(game.batch, delta);

		if (renderTooltip)
			this.miniHoverPopup.render(game.batch, tooltipx, tooltipy, tooltipText);

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

		// changer l'écran pour les élements de l'interface afin qu'ils puissent se
		// repositionner
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
		// dipose of instance elements
		map.dispose();

		// dispose of static elements
		ItemType.dispose();
		Ressource.dispose();
	}

}
