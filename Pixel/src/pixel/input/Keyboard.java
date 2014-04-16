package pixel.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import pixel.Pixel;

public class Keyboard extends KeyAdapter {
	private static volatile Keyboard keyboard;
	private static boolean enabled;
	private static final int KEY_COUNT = 256;
	private static Key[] keys;

	static {
		keyboard = new Keyboard();
		keys = new Key[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; i++) {
			keys[i] = new Key();
		}
	}
	
	public static void enable(){
		if(!enabled){
			enabled = true;
			Pixel.getInstance().addKeyListener(keyboard);
		}
	}
	
	public static void disable(){
		if(enabled){
			enabled = false;
			Pixel.getInstance().removeKeyListener(keyboard);
		}
	}
	
	public static boolean isEnabled(){
		return enabled;
	}
	
	
	public static void clear(int... sequence) {
		if (sequence.length == 0 || sequence == null) {
			for (int i = 0; i < KEY_COUNT; i++) {
				keys[i].clear();
			}
		} else {
			for(int i = 0; i<sequence.length;i++){
				keys[sequence[i]].clear();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		press(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		release(e.getKeyCode());
	}

	private void press(int keyCode) {
		if (KEY_COUNT > keyCode) {
			keys[keyCode].press();
		}
	}

	private void release(int keyCode) {
		if (KEY_COUNT > keyCode) {
			keys[keyCode].release();
		}
	}

	public static boolean isKeyDown(int keyCode) {
		return keys[keyCode].down;
	}

	public static boolean isKeyReleased(int keyCode) {
		if (keys[keyCode].released) {
			keys[keyCode].released = false;
			return true;
		}
		return false;
	}

	public static boolean isKeyPressed(int keyCode) {
		if (keys[keyCode].pressed) {
			keys[keyCode].pressed = false;
			return true;
		}
		return false;
	}

	private static class Key {
		private boolean pressed, released, down;

		private void press() {
			if (!down) {
				pressed = true;
			}
			down = true;
		}

		private void release() {
			released = true;
			down = false;

		}

		private void clear() {
			pressed = false;
			released = false;
			down = false;
		}
	}
}
