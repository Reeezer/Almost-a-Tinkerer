package ch.hearc.p2.aatinkerer.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ch.hearc.p2.aatinkerer.main.GameManager;
import ch.hearc.p2.aatinkerer.world.Chunk;
import ch.hearc.p2.aatinkerer.world.TileMap;

public class Hub extends Building
{
	private transient Texture[] textures;

	public Hub(TileMap tilemap, int x, int y)
	{
		super(tilemap, x, y, 0, Integer.MAX_VALUE, 9, null);
		this.inputPositions = new int[][] { { x + 1, y + 1, 0 }, { x + 1, y, 0 }, { x + 1, y - 1, 0 }, { x - 1, y + 1, 1 }, { x, y + 1, 1 }, { x + 1, y + 1, 1 }, { x - 1, y + 1, 2 }, { x - 1, y, 2 }, { x - 1, y - 1, 2 }, { x - 1, y - 1, 3 }, { x, y - 1, 3 }, { x + 1, y - 1, 3 } };
		this.outputPosition = null;

		textures = new Texture[tilecount];
		for (int i = 0; i < tilecount; i++)
			textures[i] = new Texture(String.format("tile/hub/%02d/00.png", i));
	}

	@Override
	public void render(SpriteBatch batch, int dx, int dy)
	{
		int z = 0;
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				int tx = x + i;
				int ty = y + j;

				TextureRegion textureRegion = new TextureRegion(textures[z]);
				batch.draw(textureRegion, tx * Chunk.TILESIZE, ty * Chunk.TILESIZE, (float) Chunk.TILESIZE / 2.f, (float) Chunk.TILESIZE / 2.f, (float) textures[z].getWidth(), (float) textures[z].getHeight(), 1.f, 1.f, (float) direction * 90.f);

				z++;
			}
		}
	}

	@Override
	public void addItem(Item item)
	{
		GameManager.getInstance().itemDelivered(item.type);
	}
}
