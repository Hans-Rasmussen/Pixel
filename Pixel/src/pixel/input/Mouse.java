package pixel.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import pixel.Pixel;

public class Mouse extends MouseAdapter {
	private static volatile Mouse mouse;

	private static int MOUSE_PRESSED = MouseEvent.MOUSE_PRESSED;

	private static final int BUTTON_COUNT = 32;
	private static boolean[] buttonDown = new boolean[BUTTON_COUNT];
	private static boolean[] buttonPressed = new boolean[BUTTON_COUNT];
	private static boolean[] buttonReleased = new boolean[BUTTON_COUNT];

	private static float xNew, xOld, xDelta, yNew, yOld, yDelta;
	private static int wheelNew, wheelOld;

	private static Queue<MouseEvent> buttonsNew = new ArrayBlockingQueue<>(32);
	private static Queue<MouseEvent> buttonsOld = new ArrayBlockingQueue<>(32);

	private Mouse() {
		Pixel.getInstance().getDisplay().addMouseListener(this);
		Pixel.getInstance().getDisplay().addMouseWheelListener(this);
		Pixel.getInstance().getDisplay().addMouseMotionListener(this);
	}

	public static Mouse getInstance() {
		if (mouse == null) {
			synchronized (Mouse.class) {
				if (mouse == null) {
					mouse = new Mouse();
				}
			}
		}
		return mouse;
	}

	public void update() {
		xDelta = xNew - xOld;
		xOld = xNew;
		yDelta = yNew - yOld;
		yOld = yNew;
		wheelOld = wheelNew;
		wheelNew = 0;

		while (!buttonsOld.isEmpty()) {
			MouseEvent me = buttonsOld.remove();
			if (me.getID() == MOUSE_PRESSED) {
				updateOldPress(me.getButton());
			} else {
				updateOldRelease(me.getButton());
			}
		}

		while (!buttonsNew.isEmpty()) {
			MouseEvent me = buttonsNew.remove();
			if (me.getID() == MOUSE_PRESSED) {
				updateNewPress(me.getButton());
			} else {
				updateNewRelease(me.getButton());
			}
			buttonsOld.add(me);
		}
	}

	public static boolean isButtonDown(int button) {
		return buttonDown[button];
	}

	public static boolean isButtonReleased(int button) {
		return buttonReleased[button];
	}

	public static boolean isButtonPressed(int button) {
		return buttonPressed[button];
	}

	public static float getX() {
		return xOld;
	}

	public static float getY() {
		return yOld;
	}

	public static float getXDelta() {
		return xDelta;
	}

	public static float getYDelta() {
		return yDelta;
	}

	public static int getWheelRotations() {
		return wheelOld;
	}

	public void updateOldPress(int key) {
		buttonPressed[key] = false;
	}

	public void updateOldRelease(int key) {
		buttonReleased[key] = false;
	}

	public void updateNewPress(int key) {
		if (!buttonDown[key]) {
			buttonPressed[key] = true;
		}
		buttonDown[key] = true;
	}

	public void updateNewRelease(int key) {
		if (buttonDown[key]) {
			buttonReleased[key] = true;
		}
		buttonDown[key] = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		buttonsNew.add(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttonsNew.add(e);

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheelNew += e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateMouseCoord(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateMouseCoord(e.getX(), e.getY());
	}

	private void updateMouseCoord(float x, float y) {
		xNew = (Pixel.getInstance().getDisplay().getScaleWidth() * x);
		yNew = (Pixel.getInstance().getDisplay().getScaleHeight() * y);
	}
}
