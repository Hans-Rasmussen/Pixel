package pixel.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioClip {
	
	private Clip clip;

	public AudioClip(String path) {
		try {
			AudioInputStream aiStream = AudioSystem.getAudioInputStream(getClass().getResource(path));
			clip = AudioSystem.getClip();
			clip.open(aiStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if (clip.isRunning()) {
			clip.setFramePosition(0);
			clip.stop();
		}
		clip.start();
	}

	public void play(int times) {
		if (clip.isRunning()) {
			clip.setFramePosition(0);
			clip.stop();
		}
		clip.loop(times);
	}

	public void setPosition(int frame) {
		clip.setFramePosition(frame);
	}

	public void stop() {
		if (clip.isRunning()) {
			clip.stop();
		}
	}

	public void reset() {
		clip.stop();
		clip.setFramePosition(0);
	}

	public void resume() {
		if (!clip.isRunning() && clip.getFramePosition() != 0) {
			clip.start();
		}
	}
}
