package entities;

import java.util.ArrayList;
import java.util.List;

import typedefs.*;

public abstract class Entity {

	private Name name;
	private Path path;
	private Integer size;
	List<Entity> directoriesAndFiles;
	
	public Entity(Name name, Path path) {
		this.name = name;
		this.path = path;
		directoriesAndFiles = new ArrayList<>();
	}
	
	public Name getName() {
		return name;
	}
	
	public Path getPath() {
		return path;
	}
	
	public int getSize() {
		return size;
	}

	public void add(Entity entry) {
		directoriesAndFiles.add(entry);
	}

	public List<Entity> getDirectoriesAndFiles() {
		return directoriesAndFiles;
	}

	public boolean containsItem(String name) {
		for(Entity entry : directoriesAndFiles) {
			if(name.equals(entry.getName().toString()))
				return true;
		}
		return false;
	}

	public Entity getEntityByName(String name) {
		for(Entity entry : directoriesAndFiles) {
			if(name.equals(entry.getName().toString()))
				return entry;
		}
		return null;
	}
	
	public abstract EntityType getType();

	public void setContent(String content) {}

}
