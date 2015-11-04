package entities;

import typedefs.*;

public class ZipFile extends Entity{
	
	public ZipFile(Name name, Path path) {
		super(name, path);
	}

	@Override
	public EntityType getType() {
		return EntityType.ZipFile;
	}
	
}
