package Directory;

import Document.Document;
import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystemElement {
    /*
     * Directory.Directory(String name, Directory.Directory parent): void
     * addFile(Directory.FileSystemElement file): void
     * delFile(String name): void
     * getFileByName(String name): Directory.FileSystemElement
     * renameFile(String oldName, String newName): void
     * listFiles(): void
     * rListFiles(): void
     * getFiles(): List<Directory.FileSystemElement>
     * getName(): String
     * getSize(): int
     * setName(String name): void
     * validateName(String name): boolean
     * printInfo(): void
     * getParent(): Directory.Directory
     * setParent(Directory.Directory parent): void
     */
    private String name;
    private List<FileSystemElement> files;
    private Directory parent;

    public Directory(String name, Directory parent) {
        this.setName(name);
        this.files = new ArrayList<>();
        this.parent = parent;
    }

    // Add a file(Directory.Document or Directory.Directory) to the directory.
    public void addFile(FileSystemElement file) {
        if (files.contains(file)) {
            throw new IllegalArgumentException("File already exists."); // Please catch exception in the caller. So does the following.
        }
        files.add(file); // Assume the file is valid
        file.setParent(this);
    }

    // Remove the file with the given name from the directory.
    public void delFile(String name) {
        FileSystemElement file = getFileByName(name);
        if (file == null) {
            throw new IllegalArgumentException("File does not exist."); 
        }
        files.remove(file);
    }

    // Get the file with the given name from the directory.
    public FileSystemElement getFileByName(String name) {
        for (FileSystemElement file : files) {
            if (file.getName().equals(name)) { // Instead of ==, use equals() to compare strings
                return file;
            }
        }
        return null;
    }

    // Rename the file with the old name to the new name in the directory.
    public void renameFile(String oldName, String newName) {
        FileSystemElement file = getFileByName(oldName);
        // old name not exist
        if (file == null) { 
            throw new IllegalArgumentException("File does not exist."); 
        }
        file.setName(newName);
    }

    public void listFiles() {
        int cnt = 0, size = 0;
        for (FileSystemElement file : files) {
            file.printInfo();
            cnt++;
            size += file.getSize();
        }
        System.out.println("Total files: " + cnt + ", Total size: " + size);
    }

    public void rListFiles() {
        int[] res = rListSol(0);
        System.out.println("Total files: " + res[0] + ", Total size: " + res[1]);
    }

    private int[] rListSol(int indent) {
        int[] res = new int[]{0, 0};
        for (FileSystemElement file : files) {
            for (int i = 0; i < indent; i++) {
                System.out.print("    "); // Indentation
            }
            if (file instanceof Directory dir) {
                dir.printInfo();
                int[] childRes = ((Directory) file).rListSol(indent + 1);
                res[0] += childRes[0]; res[1] += childRes[1];
            }
            else file.printInfo();  
            res[0]++; res[1] += file.getSize();
        }
        return res;
    }

    // If the getter is not needed in other classes, please remove it.
    public List<FileSystemElement> getFiles() {
        return files;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        int totalSize = 40;  
        for (FileSystemElement file : files) {
            totalSize += file.getSize();  
        }
        return totalSize;
    }

    private boolean validateName(String name) {
        if (name.length() == 0 || name.length() > 10) return false;
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) return false;
        }
        return true;
    }

    @Override
    public void setName(String name) {
        if (this.parent != null)
            if (getFileByName(name) != null) {
                throw new IllegalArgumentException("File already exists."); 
            }

        if (!validateName(name)) {
            throw new IllegalArgumentException("The name is invalid."); 
        }
        this.name = name;
    }

    @Override
    public void printInfo() {
        System.out.println(name + " " + getSize());
    }

    @Override
    public Directory getParent() {
        return parent;
    }

    @Override
    public void setParent(Directory parent) {
        this.parent = parent;
    }
}