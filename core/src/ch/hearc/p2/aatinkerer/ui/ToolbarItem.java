package ch.hearc.p2.aatinkerer.ui;

import com.badlogic.gdx.graphics.Texture;

public interface ToolbarItem
{
	public Texture getItemTexture();

	public void setEnabled(boolean enabled);

	public boolean enabled();
}
