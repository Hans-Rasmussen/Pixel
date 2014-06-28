package pixel.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import pixel.Pixel;

public class Keyboard extends KeyAdapter {
	private static volatile Keyboard keyboard;
	private static int KEY_PRESSED = KeyEvent.KEY_PRESSED;

	private static final int KEY_COUNT = 256;
	private static boolean[] keyDown = new boolean[KEY_COUNT];
	private static boolean[] keyPressed = new boolean[KEY_COUNT];
	private static boolean[] keyReleased = new boolean[KEY_COUNT];

	private static Queue<KeyEvent> keysNew = new ArrayBlockingQueue<>(32);
	private static Queue<KeyEvent> keysOld = new ArrayBlockingQueue<>(32);

	private Keyboard() {
		Pixel.getInstance().getDisplay().addKeyListener(this);
	}

	public static Keyboard getInstance() {
		if (keyboard == null) {
			synchronized (Keyboard.class) {
				if (keyboard == null) {
					keyboard = new Keyboard();

				}
			}
		}
		return keyboard;
	}

	public static boolean isKeyDown(int keyCode) {
		return keyDown[keyCode];
	}

	public static boolean isKeyReleased(int keyCode) {
		return keyReleased[keyCode];
	}

	public static boolean isKeyPressed(int keyCode) {
		return keyPressed[keyCode];
	}

	public void update() {
		while (!keysOld.isEmpty()) {
			KeyEvent ke = keysOld.remove();
			if (ke.getID() == KEY_PRESSED) {
				updateOldPress(ke.getKeyCode());
			} else {
				updateOldRelease(ke.getKeyCode());
			}
		}

		while (!keysNew.isEmpty()) {
			KeyEvent ke = keysNew.remove();
			if (ke.getID() == KEY_PRESSED) {
				updateNewPress(ke.getKeyCode());
			} else {
				updateNewRelease(ke.getKeyCode());
			}
			keysOld.add(ke);
		}
	}

	public void updateOldPress(int key) {
		keyPressed[key] = false;
	}

	public void updateOldRelease(int key) {
		keyReleased[key] = false;
	}

	public void updateNewPress(int key) {
		if (!keyDown[key]) {
			keyPressed[key] = true;
		}
		keyDown[key] = true;
	}

	public void updateNewRelease(int key) {
		if (keyDown[key]) {
			keyReleased[key] = true;
		}
		keyDown[key] = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keysNew.add(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysNew.add(e);
	}
}
