package pixel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Display extends Canvas implements ComponentListener {

	private JFrame frame;
	private int displayWidth, displayHeight;
	private float scaleWidth, scaleHeight;
	private BufferStrategy buffer;
	private BufferedImage image;
	private int[] pixels;

	protected Display(String title, int width, int height) {
		this.displayWidth = width;
		this.displayHeight = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) (image.getRaster().getDataBuffer()))
				.getData();

		createJFrame(title);
		initBuffer();
	}

	private void createJFrame(String title) {
		setPreferredSize(new Dimension(displayWidth, displayHeight));
		setFocusTraversalKeysEnabled(false);

		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addComponentListener(this);
	}

	private void initBuffer() {
		createBufferStrategy(3);
		buffer = getBufferStrategy();
	}

	public Screen createScreen() {
		return new Screen(0, 0, displayWidth, displayHeight);
	}

	public Screen createScreen(int x, int y, int width, int height) {
		return new Screen(x, y, width, height);
	}

	public void clear(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}

	public void refresh() {
		Graphics g = buffer.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		buffer.show();
	}

	public JFrame getJFrame() {
		return frame;
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public String getTitle() {
		return frame.getTitle();
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
		if (visible) {
			requestFocus();
		}
	}

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}

	public float getScaleWidth() {
		return scaleWidth;
	}

	public float getScaleHeight() {
		return scaleHeight;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int[] getPixels() {
		return pixels;
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		scaleWidth = getDisplayWidth() / (getWidth() * 1.0F);
		scaleHeight = getDisplayHeight() / (getHeight() * 1.0F);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

}
