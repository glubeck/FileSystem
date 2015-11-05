package entities;

import java.util.List;

import typedefs.*;

public class TextFile extends Entity{

	private String content;
	
	public TextFile(Name name, Path path, String content) {
		super(name, path, content.length());
		this.content = content;
		
	}

	@Override
	public void add(Entity entry) {
		System.out.println("Error : Cannot add entity to text file");
	}
	
	@Override
	public List<Entity> getDirectoriesAndFiles() {
		System.out.println("Error : Nothing contained in text file");
		return null;
	}

	@Override
	public boolean containsItem(String name) {
		System.out.println("Error : Nothing can be contained in text file");
		return false;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.TextFile;
	}
	
	@Override
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
}
