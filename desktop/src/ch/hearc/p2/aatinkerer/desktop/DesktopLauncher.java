package ch.hearc.p2.aatinkerer.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ch.hearc.p2.aatinkerer.main.AATinkererGame;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 900;
		config.height = 700;
		config.resizable = true;
		config.title = "Almost a Tinkerer";
		config.fullscreen = false;
		config.addIcon("ui/appicon.png", Files.FileType.Internal);

		new LwjglApplication(new AATinkererGame(), config);
	}
}
