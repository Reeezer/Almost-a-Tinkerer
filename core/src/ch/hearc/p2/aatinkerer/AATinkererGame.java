package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AATinkererGame extends Game
{
	public SpriteBatch batch;
	
	public Input input;

	@Override
	public void create()
	{
		batch = new SpriteBatch();

		input = new Input();
		Gdx.input.setInputProcessor(input);
		
		setScreen(new GameScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}
}
