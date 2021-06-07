package ch.hearc.p2.aatinkerer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

// interface for all GUI items that can be clicked on
public interface UIElement
{
	public void render(SpriteBatch batch, float delta);
	
	public void setScreenSize(int w, int h);
	// allows us to iterate through a list of clickable items to detect if they are under the mouse cursor and as such capture the click
	public Rectangle getBounds();
	public void passRelativeClick(int x, int y);
	
	public boolean visible();
}
