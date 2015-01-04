package ca.liqwidice.pong.sound;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class Sound {

	private final Runnable play = new Runnable() {
		public void run() {
			try {
				if (sound.getName().toLowerCase().contains(".wav") == false) {
					System.err.println(sound.getName() + " is not a wav file!");
					return;
				}

				if (volume == 0) return;

				AudioInputStream stream = AudioSystem.getAudioInputStream(sound);

				AudioFormat format = stream.getFormat();

				if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
					format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
							format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
							format.getFrameRate(), true);

					stream = AudioSystem.getAudioInputStream(format, stream);
				}

				SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(),
						(int) (stream.getFrameLength() * format.getFrameSize()));

				SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(stream.getFormat());
				line.start();

				// Set Volume
				FloatControl volume_control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
				volume_control.setValue((float) (Math.log(volume / 100.0f) / Math.log(10.0f) * 20.0f));

				int num_read = 0;
				byte[] buf = new byte[line.getBufferSize()];

				while ((num_read = stream.read(buf, 0, buf.length)) >= 0) {
					int offset = 0;

					while (offset < num_read) {
						offset += line.write(buf, offset, num_read - offset);
					}
				}

				line.drain();
				line.stop();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	public static Sound boop = new Sound("res/sounds/boop.wav");
	public static Sound boop2 = new Sound("res/sounds/boop2.wav");
	public static Sound click = new Sound("res/sounds/click.wav");
	public static Sound lose = new Sound("res/sounds/lose.wav");
	public static Sound win = new Sound("res/sounds/win.wav");

	private File sound;
	private static float volume = 100.0f; // range 0 - 100 inclusive

	public Sound(String path) {
		sound = new File(path);
	}

	public synchronized void play() {
		new Thread(play).start();
	}

	public static void increaseVolume() {
		if ((volume += 10) > 100.0) volume = 100.0f;
	}

	public static void decreaseVolume() {
		if ((volume -= 10) < 0) volume = 0;
	}

	public static int getVolume() {
		return (int) volume;
	}
}
