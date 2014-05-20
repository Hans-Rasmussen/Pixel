package pixel.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private String path;
	private int width, height;
	private int[] pixels;

	public SpriteSheet(String path) {
		try {
			BufferedImage buffImg = ImageIO.read(getClass().getResource(path));
			width = buffImg.getWidth();
			height = buffImg.getHeight();

			BufferedImage conImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			conImg.getGraphics().drawImage(buffImg, 0, 0, null);

			pixels = ((DataBufferInt) (conImg.getRaster().getDataBuffer())).getData();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels() {
		return pixels;
	}

	public Sprite createSprite(String name, int x, int y, int width, int height) {
		return createSprite(name, x, y, width, height, 0xFF00FF);
	}
	
	public Sprite createSprite(String name, int x, int y, int width, int height, int transparentColour) {
		int[] sPixels = new int[width * height];
		int w2 = width + x;
		int h2 = height + y;

		int i = 0;
		for (int h = y; h < h2; h++) {
			for (int w = x; w < w2; w++) {
				sPixels[i++] = pixels[w+h*this.width];
			}
		}

		return new Sprite(name, width, height, sPixels, transparentColour);
	}
}
