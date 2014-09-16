package ca.liqwidice.pong;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	public static Sound boop = new Sound("res/sounds/boop.wav");
	public static Sound boop2 = new Sound("res/sounds/boop2.wav");
	public static Sound lose = new Sound("res/sounds/lose.wav");
	public static Sound win = new Sound("res/sounds/win.wav");

	private String path;
	private float volume = 1.0f;

	public Sound(String path) {
		this.path = path;
	}

	public synchronized void play() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(0.1f);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public float getVolume() {
		return volume;
	}

}
