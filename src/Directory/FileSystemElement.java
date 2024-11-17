package Directory;

public interface FileSystemElement {
    String getName();
    int getSize();
    String getType();
    void setName(String name); // Check if the name is valid/unique before setting.
    void printInfo();
    Directory getParent();
    void setParent(Directory parent);
}
