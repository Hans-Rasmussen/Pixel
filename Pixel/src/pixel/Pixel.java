package pixel;

import pixel.input.Keyboard;
import pixel.input.Mouse;

public abstract class Pixel implements Runnable {
	private static volatile Pixel pixel;

	private Thread mainThread;

	private Display display;
	private Keyboard keyboard;
	private Mouse mouse;

	private boolean running;
	private int ticks, update;
	private int targetUpdates;

	/**
	 * Pixel constructor should only be used for creating the frame, use
	 * initialize() for anything else.
	 * 
	 * @see Pixel#initialize()
	 * 
	 * @param title
	 *            of the JFrame
	 * @param width
	 *            of the canvas the game is drawn to
	 * @param height
	 *            of the canvas the game is drawn to
	 * @param targetUpdates
	 *            is the updates/sec
	 */
	public Pixel(String title, int width, int height, int targetUpdates) {
		if (width <= 0 || height <= 0 || targetUpdates <= 0) {
			throw new RuntimeException("Invalid params");
		}
		this.targetUpdates = targetUpdates;
		display = new Display(title, width, height);
	}

	/**
	 * 
	 * @return The current Pixel Instance.
	 */
	public static Pixel getInstance() {
		if (pixel == null) {
			throw new RuntimeException("PixelEngine not initialized");
		}
		return pixel;
	}

	public Display getDisplay(){
		return display;
	}
	
	/**
	 * 
	 * @return The total amount of ticks since Pixel main thread started.
	 */
	public int getTotalTicks() {
		return ticks;
	}

	/**
	 * 
	 * @return The tick of the current update cycle.
	 */
	public int getCurrentTick() {
		return update;
	}

	/**
	 * Initialize the game, and starts the main thread.
	 */
	public void start() {
		if (!running) {
			initPixel();
			mainThread = new Thread(this);
			mainThread.start();
		}
	}

	/**
	 * Stops the main thread.
	 */
	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		running = true;
		long now = 0L;
		long then = System.nanoTime();
		double delta = 0.0D;
		double tickSec = 1E9D / targetUpdates;
		while (running) {
			now = System.nanoTime();
			delta += (now - then) / tickSec;
			then = now;
			if (delta >= 1.0D) {
				updateInput();
				updatePixel();
				renderPixel();
				delta -= 1.0D;
			}
		}
		display.getJFrame().dispose();
	}

	/**
	 * Called once when the method start() is used.
	 */
	private void initPixel() {
		pixel = this;
		initialize();
		display.setVisible(true);
		keyboard = Keyboard.getInstance();
		mouse = Mouse.getInstance();
	}

	/**
	 * Updates both the state of the keyboard and mouse input.
	 */
	private void updateInput() {
		keyboard.update();
		mouse.update();
	}

	private void updatePixel() {
		{
			update();
		}
		ticks++;
		update = (update + 1) % targetUpdates;
	}

	private void renderPixel() {
		{
			render();
		}
		display.refresh();
	}

	/**
	 * One time Initialize once method: '<code>start()</code>' is called.
	 * 
	 * @see Pixel#start()
	 */
	public abstract void initialize();

	public abstract void update();

	public abstract void render();
}
