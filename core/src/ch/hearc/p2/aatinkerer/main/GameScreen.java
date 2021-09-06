package ch.hearc.p2.aatinkerer.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.TimeUtils;

import ch.hearc.p2.aatinkerer.buildings.Building;
import ch.hearc.p2.aatinkerer.buildings.Splitter;
import ch.hearc.p2.aatinkerer.data.Contract;
import ch.hearc.p2.aatinkerer.data.FactoryType;
import ch.hearc.p2.aatinkerer.data.ItemType;
import ch.hearc.p2.aatinkerer.data.Milestone;
import ch.hearc.p2.aatinkerer.data.Ressource;
import ch.hearc.p2.aatinkerer.data.Tile;
import ch.hearc.p2.aatinkerer.data.TileType;
import ch.hearc.p2.aatinkerer.listeners.ContractListener;
import ch.hearc.p2.aatinkerer.listeners.MilestoneListener;
import ch.hearc.p2.aatinkerer.ui.Notification;
import ch.hearc.p2.aatinkerer.ui.UIElement;
import ch.hearc.p2.aatinkerer.ui.widgets.BuildingRecipeDisplay;
import ch.hearc.p2.aatinkerer.ui.widgets.ItemDropdownMenu;
import ch.hearc.p2.aatinkerer.ui.widgets.MiniHoverPopup;
import ch.hearc.p2.aatinkerer.ui.widgets.NotificationManager;
import ch.hearc.p2.aatinkerer.ui.widgets.SplitterMenu;
import ch.hearc.p2.aatinkerer.ui.widgets.StoryContractDisplay;
import ch.hearc.p2.aatinkerer.ui.widgets.Toolbar;
import ch.hearc.p2.aatinkerer.util.Sounds;
import ch.hearc.p2.aatinkerer.world.Chunk;
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

	private Texture arrowTexture;
	private TextureRegion arrowTextureRegion;
	
	private String saveDirName;
	private String name;

	public GameScreen(AATinkererGame game, String name)
	{
		this.game = game;
		this.name = name;
		
		mapCamera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		uiCamera.zoom = 1.f;
		hoverCamera = new OrthographicCamera();

		uiElements = new ArrayList<UIElement>();

		x = 0;
		y = 0;
		width = 0;
		height = 0;
		zoomLevel = 0;
		direction = 0;
		mirrored = false;
		isInputTunnel = false;

		justClicked = true;
		initialx = -1;
		initialy = -1;

		arrowTexture = new Texture("Ui/Arrow.png");
		arrowTextureRegion = new TextureRegion(arrowTexture);

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
		font = AATinkererGame.font.generateFont(titleFontParameter);

		milestoneListener = new MilestoneListener() {
			@Override
			public void unlockMilestone(Milestone milestone, boolean notify)
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

				if (notify)
					notificationManager.displayPopup(new Notification(title, milestone.description(), 10.f));
			}
		};

		contractListener = new ContractListener() {
			@Override
			public void contractAdded(Contract contract, boolean isStoryContract, boolean notify)
			{
				contractDisplay.setContract(contract);
			}
		};

		factoryToolbar.reset();
		GameManager.getInstance().reset();

		GameManager.getInstance().addMilestoneListener(milestoneListener);
		GameManager.getInstance().addContractListener(contractListener);
	}

	public GameScreen(AATinkererGame game, String name, String saveDirName)
	{
		this(game, name);
		
		this.saveDirName = saveDirName;
		
		String saveDirPath = game.saveDirBasePath() + "/" + this.saveDirName;
		String datFilePath = saveDirPath + "/save.dat";
		
		System.out.format("gamescreen: loading game from directory '%s'%n", saveDirPath);
		System.out.format("gamescreen: loading save data from file '%s'%n", datFilePath);

		File savefile = new File(datFilePath);
		try
		{
			FileInputStream fileInput = new FileInputStream(savefile);
			BufferedInputStream bis = new BufferedInputStream(fileInput);
			ObjectInputStream objectInputStream = new ObjectInputStream(bis);

			this.map = (TileMap) objectInputStream.readObject();
			GameManager.getInstance().setProgress((Integer) objectInputStream.readObject(), (HashMap<ItemType, Integer>) objectInputStream.readObject());

			objectInputStream.close();
		}
		catch (IOException | ClassNotFoundException | ClassCastException e)
		{
			e.printStackTrace();
		}

	}

	public void saveGame()
	{
		// if this is a fresh save, then saveDirName will be null, generate one that doesn't exist
		if (this.saveDirName == null)
		{
			if (!this.name.isEmpty())
				this.saveDirName = this.name;
			else
				this.saveDirName = "_"; // if the player never chooses a name, all saves directories will simply be underscores which kind of looks like an empty string
			
			// hopefully that won't last forever
			while (Gdx.files.absolute(game.saveDirBasePath() + "/" + this.saveDirName).exists())
				this.saveDirName = this.saveDirName + "_";
			
			Gdx.files.absolute(game.saveDirBasePath() + "/" + this.saveDirName).mkdirs();
		}
		
		String saveDirPath = game.saveDirBasePath() + "/" + this.saveDirName;
		
		String datFilePath = saveDirPath + "/save.dat";
		String jsonFilePath = saveDirPath + "/gamedata.json";
		
		System.out.format("saving game to '%s'%n", datFilePath);
		
		File savefile = new File(datFilePath);
		try
		{
			FileOutputStream fileOutput = new FileOutputStream(savefile);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutput);
			ObjectOutputStream objectOutput = new ObjectOutputStream(bos);

			objectOutput.writeObject(this.map);
			objectOutput.writeObject(GameManager.getInstance().getContractMilestoneIndex());
			objectOutput.writeObject(GameManager.getInstance().getProducedItems());

			objectOutput.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		Json json = new Json();
		StringWriter stringWriter = new StringWriter();
		JsonWriter writer = new JsonWriter(stringWriter);
		json.setWriter(writer);
		
		json.writeObjectStart();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();
		
		json.writeValue("date", dtf.format(now));
		json.writeValue("difficulty", game.difficulty.toString().toLowerCase());
		json.writeValue("name", this.name);
		
		json.writeObjectEnd();
		

		try
		{
			FileWriter jsonFileWriter = new FileWriter(jsonFilePath);
			jsonFileWriter.write(json.getWriter().getWriter().toString());
			jsonFileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(game.input);

		// happens if the game was not loaded from a save
		if (this.map == null)
			map = new TileMap();
	}

	@Override
	public void render(float delta)
	{
		// wait for a map to be generated from show()
		if (this.map == null)
			return;

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
		zoomLevel = (zoomLevel > 2) ? 2 : zoomLevel;

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

		if (Gdx.input.isKeyPressed(Keys.SPACE))
		{
			x = 0;
			y = 0;
		}

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
			if (factoryToolbar.getActiveItem() == null)
				game.toPauseScreen();

			factoryToolbar.setActiveItem(-1);
			buildingRecipeDisplay.setBuilding(null);
			itemDropdownMenu.setItems(null);
			splitterMenu.setSplitter(null);
		}
		FactoryType factoryType = (FactoryType) factoryToolbar.getActiveItem();

		// hover effect
		int mx = (int) (Gdx.input.getX() * uiCamera.zoom);
		int my = (int) ((height - Gdx.input.getY()) * uiCamera.zoom);
		boolean isHover = false;
		for (UIElement clickable : uiElements)
		{
			if (!clickable.visible())
				continue;

			Rectangle bounds = clickable.getBounds();

			if (bounds.contains(new Vector2(mx, my)))
				isHover = true;
		}
		int tX = screenToTileX(Gdx.input.getX());
		int tY = screenToTileY(Gdx.input.getY());
		Building attContextualMenuBuilding = (Building) map.tileAt(TileType.FACTORY, tX, tY);
		if (attContextualMenuBuilding != null && attContextualMenuBuilding instanceof Splitter)
			isHover = true;
		if (attContextualMenuBuilding != null && attContextualMenuBuilding.recipes() != null && attContextualMenuBuilding.canSelectRecipe())
			isHover = true;

		if (!isHover)
			game.setDefaultCursor();
		else
			game.setHoverCursor();

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
					{
						clickable.passRelativeClick((int) ix, (int) iy);
						Sounds.CLICK.play();
					}

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
				else if (factoryType == FactoryType.CONVEYOR) // make it so conveyors can be place more easily in a
																// line
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
					Building attemptContextualMenuBuilding = (Building) map.tileAt(TileType.FACTORY, tileX, tileY);

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

			Tile itemTile = map.tileAt(TileType.RESSOURCE, x, y);
			if (itemTile != null)
			{
				ItemType item = ((Ressource) map.tileAt(TileType.RESSOURCE, x, y)).getExtractedItem();

				// FIXME check for null too
				Building building = (Building) map.tileAt(TileType.FACTORY, x, y);
				Building conveyor = (Building) map.tileAt(TileType.CONVEYOR, x, y);

				// FIXME maybe still display the ressources if the hovered building is an extractor?
				// only display on ressources tiles that don't have a building on top
				if ((building == null || (building != null && building.getType() == FactoryType.EXTRACTOR)) && conveyor == null && item != null && item != ItemType.NONE)
				{
					tooltipText = item.fullname();
					renderTooltip = true;
				}

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

		map.cameraMovedToPosition(mapCamera.position, width, height);

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
		map.render(game.batch, mapCamera.position, width, height, zoom);

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

		// Display the different controls
		font.draw(game.batch, "Press [Ctrl]\nto see controls", 30, 50);
		if (ctrlPressed)
		{
			int toolbarHeight = 50;
			float pos = (width * uiCamera.zoom - (FactoryType.values().length * Toolbar.TEXSIZE)) / 2;
			float deltaa = Toolbar.TEXSIZE;
			for (int i = 1; i <= 9; i++)
				font.draw(game.batch, String.format("[%d]", i), pos + (i - 1) * deltaa + Toolbar.TEXSIZE / 3, toolbarHeight);
			font.draw(game.batch, "[Num 0]", pos + 9 * deltaa - 6, toolbarHeight);
			font.draw(game.batch, "[Num 1]", pos + 10 * deltaa + 15, toolbarHeight);

			font.draw(game.batch, "[Left click]\nPlace", width * uiCamera.zoom / 3, 150);
			font.draw(game.batch, "[BACKSPACE]\nDelete", width * uiCamera.zoom / 3, 100);

			font.draw(game.batch, "[R]\nRotate left", width * uiCamera.zoom * 5 / 12, 250);
			font.draw(game.batch, "[Shift + R]\nRotate right", width * uiCamera.zoom * 5 / 12, 200);
			font.draw(game.batch, "[T]\nMirror rotation", width * uiCamera.zoom * 5 / 12, 150);
			font.draw(game.batch, "[Escape]\nUnselect", width * uiCamera.zoom * 5 / 12, 100);

			font.draw(game.batch, "[Space]\nTo hub", width * uiCamera.zoom / 2, 400);
			font.draw(game.batch, "[Right click + drag]\nMove", width * uiCamera.zoom / 2, 350);
			font.draw(game.batch, "[Up]\nMove up", width * uiCamera.zoom / 2, 300);
			font.draw(game.batch, "[Down]\nMove down", width * uiCamera.zoom / 2, 250);
			font.draw(game.batch, "[Left]\nMove left", width * uiCamera.zoom / 2, 200);
			font.draw(game.batch, "[Right]\nMove right", width * uiCamera.zoom / 2, 150);
			font.draw(game.batch, "[Hold Shift]\nMove faster", width * uiCamera.zoom / 2, 100);

			font.draw(game.batch, "[Scroll]\nZoom", width * uiCamera.zoom * 7 / 12, 200);
			font.draw(game.batch, "[Num +]\nZoom in", width * uiCamera.zoom * 7 / 12, 150);
			font.draw(game.batch, "[Num -]\nZoom out", width * uiCamera.zoom * 7 / 12, 100);
		}

		font.draw(game.batch, String.format("camera coordinates (%d, %d)", x / Chunk.TILESIZE, y / Chunk.TILESIZE), (width * uiCamera.zoom - 200), 30);
		font.draw(game.batch, String.format("mouse coordinates (%d, %d)", screenToTileX(Gdx.input.getX()), screenToTileY(Gdx.input.getY())), (width * uiCamera.zoom - 200), 40);
		for (UIElement uiElement : this.uiElements)
			uiElement.render(game.batch, delta);

		if (renderTooltip)
			this.miniHoverPopup.render(game.batch, (int) (tooltipx * uiCamera.zoom), (int) (tooltipy * uiCamera.zoom), tooltipText);

		// Drawing an arrow pointing towards the hub
		float arrowPositionDisapear = Math.min(width * uiCamera.zoom, height * uiCamera.zoom) * zoom;
		if (x > arrowPositionDisapear || x < -arrowPositionDisapear || y < -arrowPositionDisapear || y > arrowPositionDisapear)
		{
			float angle = (float) (Math.atan2(mapCamera.position.y, mapCamera.position.x) * 180 / Math.PI) + 180;

			float rad = (float) (angle * (Math.PI / 180));
			float cosX = (float) Math.cos(rad);
			float cosY = (float) Math.sin(rad);

			float posX = width * uiCamera.zoom / 2 + cosX * width * uiCamera.zoom;
			float posY = height * uiCamera.zoom / 2 + cosY * height * uiCamera.zoom;

			posX = (posX < 100) ? 100 : ((posX > width * uiCamera.zoom - 100) ? width * uiCamera.zoom - 100 : posX);
			posY = (posY < 100) ? 100 : ((posY > height * uiCamera.zoom - 100) ? height * uiCamera.zoom - 100 : posY);

			game.batch.draw(arrowTextureRegion, posX, posY, (float) arrowTexture.getWidth() / 2, (float) arrowTexture.getHeight() / 2, (float) arrowTexture.getWidth(), (float) arrowTexture.getHeight(), 1.f, 1.f, angle - 90);
		}

		game.batch.end();
	}

	public int screenToTileX(int screenX)
	{
		// - original (likely buggy) formula
		// int result = (int) (((screenX - (width / 2.f)) * zoom + mapCamera.position.x) / Chunk.TILESIZE);

		float screenCenter = width / 2.f;
		float positionRelativeToCenter = screenX - screenCenter;
		float positionRelativeToCamera = (positionRelativeToCenter * mapCamera.zoom) + mapCamera.position.x;
		float actualTilePosition = positionRelativeToCamera / Chunk.TILESIZE;

		int result = (int) Math.floor(actualTilePosition);

		return result;
	}

	public int screenToTileY(int screenY)
	{
		// - original (likely buggy) formula
		// int result = (int) ((((height - screenY) - (height / 2.f)) * zoom + mapCamera.position.y) / Chunk.TILESIZE);

		float screenCenter = height / 2.f;
		float positionRelativeToCenter = (height - screenY) - screenCenter; // height - y => vertical screen coordinates are flipped compared to camera coordinates
		float positionRelativeToCamera = (positionRelativeToCenter * mapCamera.zoom) + mapCamera.position.y;
		float actualTilePosition = positionRelativeToCamera / Chunk.TILESIZE;

		int result = (int) Math.floor(actualTilePosition);

		return result;
	}

	@Override
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;

		// make it so the UI's scale adapts itself to the screen size
		switch (AATinkererGame.scale)
		{
			case AUTO:
				if (height > 2000 && width > 3000)
					uiCamera.zoom = 0.25f;
				else if (height > 1000 && width > 1500)
					uiCamera.zoom = 0.5f;
				else
					uiCamera.zoom = 1.f;
				break;
			case X1:
				uiCamera.zoom = 1.f;
				break;
			case X2:
				uiCamera.zoom = 0.5f;
				break;
			case X4:
				uiCamera.zoom = 0.25f;
				break;
			default:
				break;
		}

		mapCamera.setToOrtho(false, width, height);
		uiCamera.setToOrtho(false, width, height);
		hoverCamera.setToOrtho(false, width, height);

		// changer l'écran pour les élements de l'interface afin qu'ils puissent se
		// repositionner
		for (UIElement clickable : this.uiElements)
			clickable.setScreenSize((int) (width * uiCamera.zoom), (int) (height * uiCamera.zoom));
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{
		lastTime = TimeUtils.millis();
		unprocessedTime = 0;
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
