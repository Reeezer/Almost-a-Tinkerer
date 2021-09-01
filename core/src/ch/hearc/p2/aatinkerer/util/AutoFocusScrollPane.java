package ch.hearc.p2.aatinkerer.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

// https://stackoverflow.com/questions/63432420/how-to-make-a-libgdx-scrollpane-scroll-without-clicking-on-it-first
public class AutoFocusScrollPane extends ScrollPane
{
	public AutoFocusScrollPane(Actor actor, ScrollPaneStyle style)
	{
		super(actor, style);

		addListener(new InputListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
			{
				getStage().setScrollFocus(AutoFocusScrollPane.this);
			}

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
			{
				getStage().setScrollFocus(null);
			}
		});
	}
}
