package entities;

import typedefs.*;

public class Folder extends Entity{
	
	public Folder(Name name, Path path) {
		super(name, path);
	}

	@Override
	public EntityType getType() {
		return EntityType.Folder;
	}

}
