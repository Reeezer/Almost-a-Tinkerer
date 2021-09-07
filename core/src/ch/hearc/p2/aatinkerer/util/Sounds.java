package ch.hearc.p2.aatinkerer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds
{
	public static boolean muted = false;
	public static final Music MUSIC = Gdx.audio.newMusic(Gdx.files.internal("Sounds/music.mp3"));
	public static final Sound MAXIMISE = Gdx.audio.newSound(Gdx.files.internal("Sounds/maximise.mp3"));
	public static final Sound MINIMISE = Gdx.audio.newSound(Gdx.files.internal("Sounds/minimise.mp3"));
	public static final Sound CLICK = Gdx.audio.newSound(Gdx.files.internal("Sounds/button_click.mp3"));
	public static final Sound ACHIEVE = Gdx.audio.newSound(Gdx.files.internal("Sounds/achieve.wav"));
	public static final Sound PLACING = Gdx.audio.newSound(Gdx.files.internal("Sounds/placing.mp3"));
	public static final Sound DESTROYING = Gdx.audio.newSound(Gdx.files.internal("Sounds/destroying.mp3"));
}
