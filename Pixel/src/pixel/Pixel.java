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
	private static volatile Pixel pixelEngine;

	private String title;
	private Thread mainThread;
	private JFrame frame;

	private Keyboard keyboard;
	private Mouse mouse;
	protected Display display;
	private BufferStrategy buffer;

	private boolean running;
	private float scaleWidth, scaleHeight;
	private int ticks, update;

	/**
	 * PixelEngine constructor should only be used for creating the frame, use
	 * initialize() anything else.
	 * 
	 * @see Pixel#initialize()
	 * 
	 * @param title
	 *            of the JFrame
	 * @param width
	 *            of the canvas the game is drawn to
	 * @param height
	 *            of the canvas the game is drawn to
	 */
	public Pixel(String title, int width, int height) {
		this.title = title;
		display = new Display(width, height);
		createFrame();
	}

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

	public static Pixel getInstance() {
		if (pixelEngine == null) {
			throw new RuntimeException("PixelEngine not initialized");
		}
		return pixelEngine;
	}

	public int getTotalTicks() {
		return ticks;
	}

	public int getCurrentTick() {
		return update;
	}
	
	public float getScaleWidth(){
		return scaleWidth;
	}
	
	public float getScaleHeight(){
		return scaleHeight;
	}
	
	public JFrame getFrame(){
		return frame;
	}

	/**
	 * Initialize the game, and starts the main thread.
	 */
	public void start() {
		if (!running) {
			initEngine();
			mainThread = new Thread(this);
			mainThread.start();
		}
	}

	/**
	 * Tells the engine to close upon ending it's cycle.
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
		double tickSec = 1E9D/60D;
		while (running) {
			now = System.nanoTime();
			delta += (now - then) / tickSec;
			then = now;
			if (delta >= 1.0D) {
				keyboard.update();
				mouse.update();
				updateEngine();
				renderEngine();
				delta -= 1.0D;
			}
		}
	}

	private void initEngine() {
		pixelEngine = this;
		initialize();

		frame.setVisible(true);
		createBufferStrategy(3);
		buffer = getBufferStrategy();

		setFocusTraversalKeysEnabled(false);
		requestFocus();
		keyboard = Keyboard.getInstance();
		mouse = Mouse.getInstance();
	}

	private void updateEngine() {
		{
			update();
		}
		ticks++;
		update = (update + 1) % 60;
	}

	private void renderEngine() {
	
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
