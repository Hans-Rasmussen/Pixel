package pixel;

import pixel.image.Font;
import pixel.image.Sprite;

public class Screen {
	private static int[] pixels;
	private static int displayWidth, displayHeight;

	static {
		Display d = Pixel.getInstance().display;
		pixels = d.getPixels();
		displayWidth = d.getWidth();
		displayHeight = d.getHeight();
	}

	private int screenX, screenY;
	private int screenW0, screenH0, screenW1, screenH1;

	public Screen() {
		this(0, 0, displayWidth, displayHeight);
	}

	public Screen(int x, int y, int width, int height) {
		this.screenX = x;
		this.screenY = y;
		this.screenW0 = width;
		this.screenH0 = height;
		this.screenW1 = width + x;
		this.screenH1 = height + y;

		if (screenH1 > displayHeight || screenW1 > displayWidth) {
			throw new RuntimeException("Screen-Size can't go beyound your Display-Size");
		}
	}
	
	public int getWidth(){
		return screenW0;
	}
	
	public int getHeight(){
		return screenH0;
	}

	public void drawPixel(int x, int y, int colour) {
		if (x > -1 && x < screenW0 && y > -1 && y < screenH0) {
			x = x + screenX;
			y = y + screenY;
			pixels[x + y * displayWidth] = colour;
		}
	}

	public void drawArea(int x, int y, int width, int height, int colour) {

		// Evaluate x and width
		x += screenX;
		width += x;
		if (x < screenX) {
			x = screenX;
		} else if (width > screenW1) {
			width += (screenW1 - width);
		}

		// Evaluate y and height
		y += screenY;
		height += y;
		if (y < screenY) {
			y = screenY;
		} else if (height > screenH1) {
			height += (screenH1 - height);
		}

		// draw
		for (int yy = y; yy < height; yy++) {
			for (int xx = x; xx < width; xx++) {
				pixels[xx + yy * displayWidth] = colour;
			}
		}
	}

	public void darkenArea(int x, int y, int width, int height, int percentage) {

		// Evaluate x and width
		x += screenX;
		width += x;
		if (x < screenX) {
			x = screenX;
		} else if (width > screenW1) {
			width += (screenW1 - width);
		}

		// Evaluate y and height
		y += screenY;
		height += y;
		if (y < screenY) {
			y = screenY;
		} else if (height > screenH1) {
			height += (screenH1 - height);
		}

		int colour;
		int cord;
		for (int yy = y; yy < height; yy++) {
			for (int xx = x; xx < width; xx++) {
				cord = xx + yy * displayWidth;
				colour = pixels[cord];
				pixels[cord] = darkenColour(colour, percentage);
			}
		}
	}

	private int darkenColour(int colour, int percentage) {
		int red = colour >> 16;
		int green = (colour - (red << 16)) >> 8;
		int blue = (colour - (red << 16) - (green << 8));

		red = (red * (100 - percentage)) / 100;
		green = (green * (100 - percentage)) / 100;
		blue = (blue * (100 - percentage)) / 100;

		if (red > 255) {
			red = 255;
		}
		if (green > 255) {
			green = 255;
		}
		if (blue > 255) {
			blue = 255;
		}

		return (red << 16) + (green << 8) + blue;
	}

	public void drawSprite(int x, int y, Sprite sprite) {

		int sX = 0;
		int sY = 0;
		int sW = sprite.getWidth();
		int sH = sprite.getHeight();

		// Evaluate x and width
		if (x < 0) {
			sX -= x;
			x = 0;
		} else if (x + sW > screenW1) {
			sW += screenW1 - (x + sW);
		}

		// Evaluate y and height
		if (y < 0) {
			sY -= y;
			y = 0;
		} else if (y + sH > screenH1) {
			sH += screenH1 - (y + sH);
		}

		x += screenX;
		y += screenY;
		int tempColour = 0;
		for (int yy = sY; yy < sH; yy++, y++) {
			for (int xx = sX, tX = x; xx < sW; xx++, tX++) {
				tempColour = sprite.getColour(xx, yy);
				if (tempColour != sprite.getTransparentColour()) {
					pixels[tX + y * displayWidth] = tempColour;
				}
			}
		}
	}

	public void drawText(int x, int y, int colour, Font font, String text) {
		int width = font.getWidth();
		for(int i = 0;i<text.length();i++){
			drawSprite(x+width*i, y, colour, font.getSprite(text.charAt(i)));
		}
	}
	
	public void drawSprite(int x, int y, int colour, Sprite sprite) {

		int sX = 0;
		int sY = 0;
		int sW = sprite.getWidth();
		int sH = sprite.getHeight();

		// Evaluate x and width
		if (x < 0) {
			sX -= x;
			x = 0;
		} else if (x + sW > screenW1) {
			sW += screenW1 - (x + sW);
		}

		// Evaluate y and height
		if (y < 0) {
			sY -= y;
			y = 0;
		} else if (y + sH > screenH1) {
			sH += screenH1 - (y + sH);
		}

		x += screenX;
		y += screenY;
		int tempColour = 0;
		for (int yy = sY; yy < sH; yy++, y++) {
			for (int xx = sX, tX = x; xx < sW; xx++, tX++) {
				tempColour = sprite.getColour(xx, yy);
				if (tempColour != sprite.getTransparentColour()) {
					pixels[tX + y * displayWidth] = colour;
				}
			}
		}
	}

	/*
	 * Experimental... top and bottom must be equal in size
	 */
	public void drawDualLayerSprite(int x, int y, Sprite top, Sprite bottom) {

		int sX = 0;
		int sY = 0;
		int sW = top.getWidth();
		int sH = top.getHeight();

		// Evaluate x and width
		if (x < 0) {
			sX -= x;
			x = 0;
		} else if (x + sW > screenW1) {
			sW += screenW1 - (x + sW);
		}

		// Evaluate y and height
		if (y < 0) {
			sY -= y;
			y = 0;
		} else if (y + sH > screenH1) {
			sH += screenH1 - (y + sH);
		}

		x += screenX;
		y += screenY;
		int tempColour = 0;
		for (int yy = sY; yy < sH; yy++, y++) {
			for (int xx = sX, tX = x; xx < sW; xx++, tX++) {
				tempColour = top.getColour(xx, yy);
				if (tempColour != top.getTransparentColour()) {
					pixels[tX + y * displayWidth] = tempColour;
				}else{
					pixels[tX + y * displayWidth] = bottom.getColour(xx, yy);
				}
			}
		}
	}

}
