package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Conveyor extends Building
{
	public Conveyor(int direction)
	{
		this.texture = new Texture(Gdx.files.internal("Conveyor.png"));
		this.direction = direction;
	}
}