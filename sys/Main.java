package sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import typedefs.*;

public class Main {

	public static void main(String[] args) throws IOException {
		
		FileSystem sys = new FileSystem();
		
		Name name = new Name("CDrive");
		Path path = new Path("");
		sys.create(EntityType.Drive, name, path);
		
		name = new Name("Documents");
		path = new Path("CDrive\\");
		sys.create(EntityType.Folder, name, path);
		
		name = new Name("testtext");
		path = new Path("CDrive\\");
		sys.create(EntityType.TextFile, name, path);

		name = new Name("testzip");
		path = new Path("CDrive\\");
		sys.create(EntityType.ZipFile, name, path);
		
		name = new Name("ReallyAwesomeDocument");
		path = new Path("CDrive\\Documents\\");
		sys.create(EntityType.TextFile, name, path);
		
		name = new Name("Books");
		path = new Path("CDrive\\Documents\\");
		sys.create(EntityType.Folder, name, path);
		
		name = new Name("GreatExpectations");
		path = new Path("CDrive\\Documents\\Books\\");
		sys.create(EntityType.TextFile, name, path);
		
		name = new Name("AnotherAwesomeDocument");
		path = new Path("CDrive\\Documents\\");
		sys.create(EntityType.TextFile, name, path);
		
		sys.printFileSystem();
		
		while(true) {
			
			//Valid Commands
			//create type(drive, folder, text, zip) name parentPath
			//delete path
			//move sourcePath destinationPath
			//write path content
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        System.out.println("Enter Command:");
	        String s = br.readLine();
			
	        String[] arguments = s.split("\\s+");
	        
	        if(arguments[0].toLowerCase().equals("create")) {
	        	if(arguments.length >= 3) {
		        	EntityType type = getType(arguments[1]);
		        	name = new Name(arguments[2]);
		        	if(arguments.length == 4)
		        		path = new Path(arguments[3]);
		        	else
		        		path = new Path("");
		        	sys.create(type, name, path);
	        	}
	        	else
	        		System.out.println("Invalid Number of Arguments");
	        }
	        if(arguments[0].toLowerCase().equals("delete")) {
	        	if(arguments.length == 2) {
		        	path = new Path(arguments[1]);
		        	sys.delete(path);
	        	}
	        	else
	        		System.out.println("Invalid Number of Arguments");
	        }
	        if(arguments[0].toLowerCase().equals("move")) {
	        	if(arguments.length == 3) {
		        	Path source = new Path(arguments[1]);
		        	Path destination = new Path(arguments[2]);
		        	sys.move(source, destination);
	        	}
	        	else
	        		System.out.println("Invalid Number of Arguments");
	        }
	        if(arguments[0].toLowerCase().equals("write")) {
	        	if(arguments.length == 3) {
		        	path = new Path(arguments[1]);
		        	String content = arguments[2];
		        	sys.writeToFile(path, content);
	        	}
	        	else
	        		System.out.println("Invalid Number of Arguments");
	        }
	        
	        sys.printFileSystem();
		}
		
	}

	private static EntityType getType(String type) {
		if(type.equals("drive"))
			return EntityType.Drive;
		if(type.equals("folder"))
			return EntityType.Folder;
		if(type.equals("text"))
			return EntityType.TextFile;
		if(type.equals("zip"))
			return EntityType.ZipFile;
		else
			return null;
	}
	
}
