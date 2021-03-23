package ch.hearc.p2.aatinkerer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AATinkererGame extends ApplicationAdapter {
	public static final String NAME = "Almost a Tinkerer";
	
	public static final int TILESIZE = 32;
	public static final int TILES_X = 10;
	public static final int TILES_Y = 5;
	
	private SpriteBatch batch;
	private Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("grid.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		for (int i = 1; i < AATinkererGame.TILES_X; i++) {
			for (int j = 1; j < AATinkererGame.TILES_Y; j++) {
				batch.draw(img, i * AATinkererGame.TILESIZE, j * AATinkererGame.TILESIZE);
			}
		}
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
