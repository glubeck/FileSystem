package typedefs;

public class Name {

	private String name;
	
	public Name(String name) {
		if(name.length() > 0) {
			if(isAlphanumeric(name)) {
				this.name = name;
			}
		}else{
			System.out.println("Name of Entity must be at least "
					+ "one character long");
		}
	}

	private boolean isAlphanumeric(String name) {
		for(int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if(!Character.isLetterOrDigit(c)) {
				System.out.println("Name of Entity must consist of only"
						+ "alphabetical or numerical characters");
				return false;	
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
