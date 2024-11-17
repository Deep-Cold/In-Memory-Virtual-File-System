package Directory;

public interface FileSystemElement {
    String getName();
    int getSize();
    void setName(String name); // Check if the name is valid/unique before setting.
    void printInfo();
    Directory getParent();
    void setParent(Directory parent);
}
