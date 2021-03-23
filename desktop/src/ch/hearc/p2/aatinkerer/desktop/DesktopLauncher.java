package ch.hearc.p2.aatinkerer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.hearc.p2.aatinkerer.AATinkererGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = AATinkererGame.TILESIZE * AATinkererGame.TILES_X;
		config.height = AATinkererGame.TILESIZE * AATinkererGame.TILES_Y;
		config.resizable = false;
		config.title = AATinkererGame.NAME;
		
		new LwjglApplication(new AATinkererGame(), config);
	}
}
