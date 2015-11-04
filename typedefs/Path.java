package typedefs;

public class Path {

	private String path;

	public Path(String path) {
		this.path = path;
	}
	
	public String[] getPathSegments() {
		
		return path.split("\\\\");
	}
	
	@Override
	public String toString() {
		return path;
	}
	
}
