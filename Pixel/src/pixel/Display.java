package pixel;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display {

	private int width, height;
	private BufferedImage image;
	private int[] pixels;

	protected Display(int width, int height) {
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) (image.getRaster().getDataBuffer())).getData();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int[] getPixels() {
		return pixels;
	}

}
