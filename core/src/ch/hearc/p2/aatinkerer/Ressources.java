package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum Ressources
{
	NONE("grid.png"),
	WOOD("wood_01a.png"),
	IRON("gem_01e.png"),
	COPPER("gem_01g.png"),
	OIL("potion_02g.png"),
	WATER("pearl_01b.png"),
	STONE("stoneblock_01a.png"),
	COTTON("scroll_01h.png");

	private Texture texture;

	private Ressources(String texturePath)
	{
		texture = new Texture(Gdx.files.internal(texturePath));
	}

	public Texture getTexture()
	{
		return texture;
	}

	// FIXME how to implement dispose() for an enum?
}
