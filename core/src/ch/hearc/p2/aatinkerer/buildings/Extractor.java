package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Extractor extends Building
{
	public Extractor(int direction)
	{
		this.texture = new Texture(Gdx.files.internal("Extractor.png"));
		this.direction = direction;
	}
}
