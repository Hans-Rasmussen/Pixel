package pixel.image;

public class Font {

	private String characters;
	private Sprite[] sprites;
	private int width, height;
	
	public Font(String characters, SpriteSheet sheet){
		int length = characters.length();
		this.characters = characters;
		this.width = sheet.getWidth()/length;
		this.height = sheet.getHeight();
		sprites = new Sprite[length];
		for(int i = 0; i<length;i++){
			sprites[i] = sheet.createSprite("" + characters.charAt(i), i*width, 0, width, height, 0xFF00FF);
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public String getCharacters(){
		return characters;
	}
	
	public Sprite getSprite(int character){
		int index = characters.indexOf(character);
		if(index >0){
			return sprites[index];
		} else {
			return sprites[0];	
		}
		
	}
}
