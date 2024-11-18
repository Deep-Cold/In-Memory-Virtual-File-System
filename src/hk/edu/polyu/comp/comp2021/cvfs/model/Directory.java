package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Directory
 */
public class Directory implements FileSystemElement {
    /**
     * The Basic Size of Directory
     */
    public static final int TOTAL_SIZE = 40;
    /*
     * hk.edu.polyu.comp.comp2021.cvfs.model.Directory(String name, hk.edu.polyu.comp.comp2021.cvfs.model.Directory parent): void
     * addFile(hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemElement file): void
     * delFile(String name): void
     * getFileByName(String name): hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemElement
     * renameFile(String oldName, String newName): void
     * listFiles(): void
     * rListFiles(): void
     * getFiles(): List<hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemElement>
     * getName(): String
     * getSize(): int
     * setName(String name): void
     * validateName(String name): boolean
     * printInfo(): void
     * getParent(): hk.edu.polyu.comp.comp2021.cvfs.model.Directory
     * setParent(hk.edu.polyu.comp.comp2021.cvfs.model.Directory parent): void
     */
    private String name;
    private final List<FileSystemElement> files;
    private Directory parent;

    /**
     * @param name Directory Name
     * @param parent Directory Parent Directory
     */
    public Directory(String name, Directory parent) {
        this.setName(name);
        this.files = new ArrayList<>();
        this.parent = parent;
    }
    @Override
    public String getType() {
        return "Directory";
    }

    /**
     * @param file The file to be added in the Directory
     */
    // Add a file(Directory.Document or hk.edu.polyu.comp.comp2021.cvfs.model.Directory) to the directory.
    public void addFile(FileSystemElement file) {
        if (files.contains(file)) {
            throw new IllegalArgumentException("File already exists."); // Please catch exception in the caller. So does the following.
        }
        files.add(file); // Assume the file is valid
        file.setParent(this);
    }

    /**
     * @return All the files in the Directory
     */
    // Get all file in the directory.
    public List<FileSystemElement> getAllFiles() {
        List<FileSystemElement> cur = new ArrayList<FileSystemElement>();
        cur.add(this);
        for(FileSystemElement file : files) {
            if(file instanceof Directory) {
                cur.addAll(((Directory) file).getAllFiles());
            }
        }
        return cur;
    }

    /**
     * @param name The name of the file to be deleted
     */
    // Remove the file with the given name from the directory.
    public void delFile(String name) {
        FileSystemElement file = getFileByName(name);
        if (file == null) {
            throw new IllegalArgumentException("File does not exist.");
        }
        files.remove(file);
    }

    /**
     * @param name The name of the file to be got
     * @return The corresponding file
     */
    // Get the file with the given name from the directory.
    public FileSystemElement getFileByName(String name) {
        for (FileSystemElement file : files) {
            if (file.getName().equals(name)) { // Instead of ==, use equals() to compare strings
                return file;
            }
        }
        return null;
    }

    /**
     * @param oldName The original name of the file
     * @param newName The new name of the file
     */
    // Rename the file with the old name to the new name in the directory.
    public void renameFile(String oldName, String newName) {
        FileSystemElement file = getFileByName(oldName);
        // old name not exist
        if (file == null) { 
            throw new IllegalArgumentException("File does not exist."); 
        }
        file.setName(newName);
    }

    /**
     * Print all the Files in the directory
     */
    public void listFiles() {
        int cnt = 0, size = 0;
        for (FileSystemElement file : files) {
            file.printInfo();
            cnt++;
            size += file.getSize();
        }
        System.out.println("Total files: " + cnt + ", Total size: " + size);
    }

    /**
     * @param cri The Criteria required to check the files
     */
    public void searchFiles(Criteria cri) {
        int cnt = 0, size = 0;
        for (FileSystemElement file : files) {
            if (!checkFile(cri, file)) continue;
            file.printInfo();
            cnt++;
            size += file.getSize();
        }
        System.out.println("Total files: " + cnt + ", Total size: " + size);
    }

    /**
     * Recursive Printing all the files
     */
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
                int[] childRes = dir.rListSol(indent + 1);
                res[0] += childRes[0]; res[1] += childRes[1];
            }
            else file.printInfo();  
            res[0]++; res[1] += file.getSize();
        }
        return res;
    }

    /**
     * @param cri The Criteria required to check the files
     */
    public void rSearchFiles(Criteria cri) {
        int[] res = rSearchSol(cri);
        System.out.println("Total files: " + res[0] + ", Total size: " + res[1]);
    }

    private int[] rSearchSol(Criteria cri) {
        int[] res = new int[]{0, 0};
        for (FileSystemElement file : files) {
            if (file instanceof Directory dir) {
                if (checkFile(cri, dir)) {
                    dir.printInfo();
                    res[0] ++;
                    res[1] += dir.getSize();
                }
                int[] childRes = dir.rSearchSol(cri);
                res[0] += childRes[0]; res[1] += childRes[1];
            }
            else if(checkFile(cri, file)) {
                file.printInfo();
                res[0]++;
                res[1] += file.getSize();
            }
        }
        return res;
    }

    private boolean checkFile(Criteria cri, FileSystemElement file) {
        return cri.check(file);
    }

    /**
     * @return All the files in the directory.
     */
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
        int totalSize = TOTAL_SIZE;
        for (FileSystemElement file : files) {
            totalSize += file.getSize();  
        }
        return totalSize;
    }

    private boolean validateName(String name) {
        if (name.isEmpty() || name.length() > 10) return false;
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
    public void setParent(FileSystemElement parent) {
        if(parent instanceof Directory) {
            this.parent = (Directory) parent;
        }
        else {
            throw new IllegalArgumentException("Parent is not a directory.");
        }
    }
}
