package sys;

import java.util.ArrayList;
import java.util.List;

import entities.*;
import typedefs.*;

public class FileSystem {

	private List<Drive> Drives;
	
	public FileSystem() {
		Drives = new ArrayList<>();
	}
	
	void create(EntityType type, Name name, Path path) {
		if(name.toString() != null) {
			switch (type) {
				case Drive:
					createDrive(name, path);
					break;
				case Folder:
					createFolder(name, path);
					break;
				case TextFile:
					createTextFile(name, path, "");
					break;
				case ZipFile:
					createZipFile(name, path);
					break;
				default: 
					break;
			}
		}
		else
			System.out.println("Error : name is invalid");
	}
	
	void create(EntityType type, Name name, Path path, String content) {

		if(type == EntityType.TextFile) {
			if(name.toString() != null) {
				createTextFile(name, path, content);
			}
			else {
				System.out.println("Error : Invalid Name");
			}
		}
		else
			System.out.println("Error : Cannot specify content for non text file");
		
	}
	
	void delete(Path path) {
		
		String[] segments = path.getPathSegments();
		
		if(segments.length == 1)
			deleteDrive(segments[0]);
		else {
			Entity parent = getParentByPath(path, segments);
			if(parent != null) {
			
				String entryName = segments[segments.length-1];
				Entity entryToRemove = null;
				
				
				for(Entity entry : parent.getDirectoriesAndFiles()) {
					if(entry.getName().toString().equals(entryName)) {
						entryToRemove = entry;
					}
				}
				
				if(entryToRemove != null)
					parent.getDirectoriesAndFiles().remove(entryToRemove);
				else
					System.out.println("Error : Could not delete at " + path.toString() + ". Does not exist");
			}
			else
				System.out.println("Error : " + path.toString() + " invalid path");
		}
	}
	
	private void deleteDrive(String name) {
		Drive driveToDelete = null;
		for(Drive d : Drives) {
			if(d.getName().toString().equals(name))
				driveToDelete = d;
		}
		
		if(driveToDelete != null)
			Drives.remove(driveToDelete);
		else
			System.out.println("Error : specified drive " + name.toString() + " does not exist");
	}

	private Entity getParentByPath(Path path, String[] segments) {
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < segments.length-1; i++) {
			sb.append(segments[i]);
			sb.append("\\");
		}
		Path parentPath = new Path(sb.toString());
		return getByPath(parentPath);
	}

	void move(Path source, Path destination) {
		
		Entity parent = getByPath(destination);
		if(parent != null) {
			if(!(parent instanceof TextFile)) {
				Entity entry = getByPath(source);
				if(entry != null) {
					if(!(entry instanceof Drive)) {
						delete(source);
						parent.getDirectoriesAndFiles().add(entry);
					}
					else
						System.out.println("Error : Cannot move entities of type Drive");
				}
				else
					System.out.println("Error : path of object to be moved invalid");
			}
			else
				System.out.println("Error : cannot move to text file.  Invalid operation");
		}
		else
			System.out.println("Error : path " + destination.toString() + " invalid");
	}

	void writeToFile(Path path, String content) {
		
		Entity entry = getByPath(path);
		if(entry != null) {
			if(entry instanceof TextFile) {
				entry.setContent(content);
				entry.setSize(content.length());
			}
			else
				System.out.println("Cannot write to " + entry.getName().toString()
						+ " : Not a text file");
		}
		else
			System.out.println("Error : Path " + path.toString() + " Invalid");
	}
	
	private void createDrive(Name name, Path path) {
		if(pathIsEmpty(path)) {
			Drive drive = new Drive(name, path);
			if(!driveExists(name))
				Drives.add(drive);
			else
				System.out.println("Drive : " + drive.getName().toString() + " already exists");
		}
	}

	private boolean driveExists(Name name) {
		for(Drive d : Drives) {
			if(d.getName().toString().equals(name.toString()))
				return true;
		}
		return false;
	}

	private void createZipFile(Name name, Path path) {
		Entity parent = getByPath(path);
		Entity entry = new ZipFile(name, path);
		if(parent != null)
			addToParent(name, parent, entry);
		else
			System.out.println("Error : Must specify valid path ");
	}

	private void createTextFile(Name name, Path path, String content) {
		Entity parent = getByPath(path);
		Entity entry = new TextFile(name, path, content);
		if(!(parent instanceof TextFile)) {
			if(parent != null)
				addToParent(name, parent, entry);
			else
				System.out.println("Error : Must specify valid path ");
		}
		else
			System.out.println("Error : Text files have no subdirectories");
	}

	private void createFolder(Name name, Path path) {
		Entity parent = getByPath(path);
		Entity entry = new Folder(name, path);
		if(parent != null){
			addToParent(name, parent, entry);
		}
		else
			System.out.println("Error : Must specify valid path ");
	}

	private void addToParent(Name name, Entity parent, Entity entry) {
		if(!parent.containsItem(name.toString()))
			parent.getDirectoriesAndFiles().add(entry);
		else
			System.out.println("Error : " +  name.toString() + " already exists in specified directory");
	}
	

	private Entity getByPath(Path path) {
		Entity parent = null;
		String[] pathSegments = path.getPathSegments();
		parent = getDriveByName(pathSegments[0]);
		if(parent != null) {
			for(int i = 1; i < pathSegments.length; i++) {
				if(parent.containsItem(pathSegments[i])) {
					parent = parent.getEntityByName(pathSegments[i]);
				}
				else {
					//System.out.println("No such directory or file: " + pathSegments[i]);
					return null;
				}
			}
		}
		return parent;
	}

	private Entity getDriveByName(String name) {
		for(Drive d : Drives) {
			if(name.equals(d.getName().toString()))
				return d;
		}
		//System.out.println("Error : No such drive");
		return null;
	}
	
	void printDrives() {
		for(Entity e : Drives) 
			System.out.println(e.getName().toString());
	}

	public void printFileSystem() {
		int level = 0;
		for(Drive d : Drives) {
			System.out.print(d.getName().toString());
			printType(d);
			d.setSize(calculateSize(d));
			System.out.println(" : Size - " + Integer.toString(d.getSize()));
			if(d.getDirectoriesAndFiles().size() > 0) {
				level++;
				printRecursively(d.getDirectoriesAndFiles(), level);
				level--;
			}
		}
	}

	private void printRecursively(List<Entity> directoriesAndFiles, int level) {
		for(Entity entry : directoriesAndFiles) {
			for(int i = 0; i < level; i++)
				System.out.print("-");
			entry.setSize(calculateSize(entry));
			System.out.print(entry.getName().toString());
			printType(entry);
			System.out.println(" : Size - " + Integer.toString(entry.getSize()));
			if(!(entry instanceof TextFile)) {
				if(entry.getDirectoriesAndFiles().size() > 0) {
					level++;
					printRecursively(entry.getDirectoriesAndFiles(), level);
					level--;
				}
			}
		}
	}
	
	private void printType(Entity entry) {

		if(entry instanceof Drive)
			System.out.print("(Drive)");
		if(entry instanceof Folder)
			System.out.print("(Folder)");
		if(entry instanceof TextFile)
			System.out.print(".txt");
		if(entry instanceof ZipFile)
			System.out.print(".zip");
		
	}

	private boolean pathIsEmpty(Path path) {
		if(path.toString().trim().length() > 0) {
			System.out.println("Error : Drives cannot have a parent directory");
			return false;
		}
		else
			return true;
	}
	
	private int calculateSize(Entity entry) {
		
		int size = 0;
		
		if(entry instanceof TextFile)
			return entry.getSize();
		
		else 
			for(Entity e : entry.getDirectoriesAndFiles())  {
				if(e instanceof ZipFile)
					size += (getSizeRecursively(e,0)/2);
				else
					size += getSizeRecursively(e, 0);
			}
		
		return size;
	}

	private int getSizeRecursively(Entity entry, int size) {
		
		if(entry instanceof TextFile)
			size += entry.getSize();
		else 
			for(Entity e : entry.getDirectoriesAndFiles()) {
				if(e instanceof ZipFile)
					size += (getSizeRecursively(e,0)/2);
				else
					size += getSizeRecursively(e, 0);
			}
		
		return size;
	}
	
}
