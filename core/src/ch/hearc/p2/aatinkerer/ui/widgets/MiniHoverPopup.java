package ch.hearc.p2.aatinkerer.ui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import ch.hearc.p2.aatinkerer.main.AATinkererGame;

public class MiniHoverPopup
{

	private Texture spritesheet;

	private TextureRegion leftBorderRegion;
	private TextureRegion rightBorderRegion;
	private TextureRegion middleRegion;

	BitmapFont font;

	public MiniHoverPopup()
	{
		this.spritesheet = new Texture(Gdx.files.internal("ui/mini_popup.png"));

		this.leftBorderRegion = new TextureRegion(this.spritesheet, 0, 0, 7, 32);
		this.rightBorderRegion = new TextureRegion(this.spritesheet, 10, 0, 7, 32);
		this.middleRegion = new TextureRegion(this.spritesheet, 8, 0, 1, 32);

		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 32;
		fontParameter.color = Color.WHITE;
		font = AATinkererGame.font.generateFont(fontParameter);
	}

	public void render(SpriteBatch batch, int x, int y, String text)
	{
		batch.draw(this.leftBorderRegion, x, y);

		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, text);

		for (int i = 0; i < layout.width; i++)
			batch.draw(this.middleRegion, x + 5 + (i * this.middleRegion.getRegionWidth()), y);
		font.draw(batch, text, x + 5, y + (this.middleRegion.getRegionHeight() + layout.height) / 2.f);

		batch.draw(this.rightBorderRegion, x + 5 + layout.width * this.middleRegion.getRegionWidth(), y);
	}
}
