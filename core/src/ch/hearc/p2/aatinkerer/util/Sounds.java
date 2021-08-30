package ch.hearc.p2.aatinkerer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	public static final Music MUSIC = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
	public static final Sound MAXIMISE = Gdx.audio.newSound(Gdx.files.internal("sounds/maximise.mp3"));
	public static final Sound MINIMISE = Gdx.audio.newSound(Gdx.files.internal("sounds/minimise.mp3"));
	public static final Sound CLICK = Gdx.audio.newSound(Gdx.files.internal("sounds/button_click.mp3"));
	public static final Sound ACHIEVE = Gdx.audio.newSound(Gdx.files.internal("sounds/achieve.wav"));
	public static final Sound PLACING = Gdx.audio.newSound(Gdx.files.internal("sounds/placing.mp3"));
	public static final Sound DESTROYING = Gdx.audio.newSound(Gdx.files.internal("sounds/destroying.mp3"));
}
