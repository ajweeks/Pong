package ca.liqwidice.pong;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	public static Sound boop = new Sound("res/sounds/boop.wav");
	public static Sound boop2 = new Sound("res/sounds/boop2.wav");
	public static Sound click = new Sound("res/sounds/click.wav");
	public static Sound lose = new Sound("res/sounds/lose.wav");
	public static Sound win = new Sound("res/sounds/win.wav");

	private static float volume = 1.0f;

	private String path;

	public Sound(String path) {
		this.path = path;
	}

	public synchronized void play() {
		if (volume == 0) return;
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
			clip.open(inputStream);
			((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume); //TODO actually implement volume control
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void increaseVolume() {
		if (++volume > 6.0) volume = 6.0f;
	}

	public static void decreaseVolume() {
		if (--volume < 0) volume = 0;
	}

	public static float getVolume() {
		return volume;
	}

}
