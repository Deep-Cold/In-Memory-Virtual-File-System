package Directory;

/**
 * Fundamental file system element
 */
public interface FileSystemElement {
    /**
     * @return The name of the file
     */
    String getName();

    /**
     * @return The size of the file
     */
    int getSize();

    /**
     * @param parent The Parent Directory
     */
    void setParent(FileSystemElement parent);

    /**
     * @return The type of the file
     */
    String getType();

    /**
     * @param name The name of the file
     */
    void setName(String name); // Check if the name is valid/unique before setting.

    /**
     * Print the information of the file
     */
    void printInfo();

    /**
     * @return The Parent Directory
     */
    FileSystemElement getParent();
    
}
