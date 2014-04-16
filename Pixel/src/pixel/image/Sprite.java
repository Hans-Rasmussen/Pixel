package pixel.image;

public class Sprite {

	private String name;
	private int width, height;
	private int[] pixels;
	private int tColour;
	
	protected Sprite(String name, int width, int height, int[] pixels, int tColour){
		this.name = name;
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		this.tColour = tColour;
	}
	
	public String toString(){
		return "SPRITE[name='" + name + "', width='" + width + "', height='" + height+"']";
	}
	
	public int getColour(int x, int y){
		return pixels[x+y*width];
	}
	
	public String getName(){
		return name;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int[] getPixels(){
		return pixels;
	}
	
	public int getTransparentColour(){
		return tColour;
	}
}
