package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor
{
	private int scrollY;
	
	public Input()
	{		
		scrollY = 0;
	}
	
	public int getScrollY()
	{
		return scrollY;
	}
	
	public void reset()
	{
		scrollY = 0;
	}
	
	/* callbacks down there */
	
	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY)
	{
		scrollY = (int) amountY;
		return false;
	}

}
