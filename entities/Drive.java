package entities;

import typedefs.*;

public class Drive extends Entity{
	
	public Drive(Name name, Path path) {
		super(name, path);
	}

	@Override
	public EntityType getType() {
		return EntityType.Drive;
	}
	

	
	

}
