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
	
	void create(EntityType type, Name name, Path path, String content) {

		if(type == EntityType.TextFile)
			createTextFile(name, path, content);
		else
			System.out.println("Error : Cannot specify content for non text file");
		
	}
	
	void delete(Path path) {
		
		String[] segments = path.getPathSegments();
		
		if(segments.length == 1)
			deleteDrive(segments[0]);
		else {
			Entity parent = getParentByPath(path, segments);
			
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
				System.out.println("Error : Could not delete at " + path.toString());
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
		
		Entity entry = getByPath(source);
		delete(source);
		Entity parent = getByPath(destination);
		parent.getDirectoriesAndFiles().add(entry);
	}

	void writeToFile(Path path, String content) {
		
		Entity entry = getByPath(path);
		
		if(entry instanceof TextFile)
			entry.setContent(content);
		else
			System.out.println("Cannot write to " + entry.getName().toString()
					+ " : Not a text file");
	}
	
	private void createDrive(Name name, Path path) {
		if(pathIsEmpty(path)) {
			Drive drive = new Drive(name, path);
			Drives.add(drive);
		}
	}

	private void createZipFile(Name name, Path path) {
		Entity parent = getByPath(path);
		Entity entry = new ZipFile(name, path);
		if(parent != null)
			parent.getDirectoriesAndFiles().add(entry);
		else
			System.out.println("No such path : " + path.toString());
	}

	private void createTextFile(Name name, Path path, String content) {
		Entity parent = getByPath(path);
		Entity entry = new TextFile(name, path, content);
		if(!(parent instanceof TextFile)) {
			if(parent != null)
				parent.getDirectoriesAndFiles().add(entry);
			else
				System.out.println("No such path : " + path.toString());
		}
		else
			System.out.println("Error : Text files have no subdirectories");
	}

	private void createFolder(Name name, Path path) {
		Entity parent = getByPath(path);
		Entity entry = new Folder(name, path);
		if(parent != null)
			parent.getDirectoriesAndFiles().add(entry);
		else
			System.out.println("No such path : " + path.toString());
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
					System.out.println("No such directory or file: " + pathSegments[i]);
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
		System.out.println("Error : No such drive");
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
			System.out.print(entry.getName().toString());
			printType(entry);
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
			System.out.println("(Drive)");
		if(entry instanceof Folder)
			System.out.println("(Folder)");
		if(entry instanceof TextFile)
			System.out.println(".txt");
		if(entry instanceof ZipFile)
			System.out.println(".zip");
		
	}

	private boolean pathIsEmpty(Path path) {
		if(path.toString().trim().length() > 0) {
			System.out.println("Error : Drives cannot have a parent directory");
			return false;
		}
		else
			return true;
	}
	
}
