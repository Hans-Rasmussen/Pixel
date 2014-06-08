package pixel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import pixel.input.Keyboard;
import pixel.input.Mouse;

@SuppressWarnings("serial")
public abstract class Pixel extends Canvas implements Runnable, ComponentListener {
	private static volatile Pixel pixel;

	private String title;
	private Thread mainThread;
	private JFrame frame;

	private Keyboard keyboard;
	private Mouse mouse;
	protected Display display;
	private BufferStrategy buffer;

	private boolean running;
	private int width, height;
	private float scaleWidth, scaleHeight;
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
		this.title = title;
		this.width = width;
		this.height = height;
		this.targetUpdates = targetUpdates;
		display = new Display(width, height);
		
	}

	/**
	 * Create and setup the JFrame.
	 */
	private void createFrame() {
		setPreferredSize(new Dimension(display.getWidth(), display.getHeight()));

		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addComponentListener(this);
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
	 * returns the <code>width</code> of the canvas in which Pixel draws on.
	 */
	public int getCanvasWidth() {
		return width;
	}

	/**
	 * returns the height of the canvas in which Pixel draws on.
	 */
	public int getCanvasHeight() {
		return height;
	}

	/**
	 * Whenever the window size change, the width scale is updated.
	 * 
	 * @return The current scale for width.
	 */
	public float getScaleWidth() {
		return scaleWidth;
	}

	/**
	 * Whenever the window size change, the height scale is updated.
	 * 
	 * @return The current scale for height.
	 */
	public float getScaleHeight() {
		return scaleHeight;
	}

	/**
	 * 
	 * @return The active JFrame in which the game is running.
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Initialize the game, and starts the main thread.
	 */
	public void start() {
		if (!running) {
			createFrame();
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
		frame.dispose();
	}

	/**
	 * Called once when the method start() is used.
	 */
	private void initPixel() {
		pixel = this;
		initialize();

		frame.setVisible(true);
		createBufferStrategy(3);
		buffer = getBufferStrategy();

		setFocusTraversalKeysEnabled(false);
		requestFocus();
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
		Graphics g = buffer.getDrawGraphics();
		g.drawImage(display.getImage(), 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		buffer.show();
	}

	/**
	 * One time Initialize once method: '<code>start()</code>' is called.
	 * 
	 * @see Pixel#start()
	 */
	public abstract void initialize();

	public abstract void update();

	public abstract void render();

	@Override
	public void componentResized(ComponentEvent e) {
		scaleWidth = display.getWidth() / (getWidth() * 1.0F);
		scaleHeight = display.getHeight() / (getHeight() * 1.0F);
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}
}
