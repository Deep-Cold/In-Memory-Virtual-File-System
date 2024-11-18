package Document;

import Directory.*;

public class Document implements FileSystemElement {
    private String docName;
    private final String docContent;
    private Directory parent;

    public enum docTypes {
        txt("txt"), java("java"), html("html");
        private final String type;
        docTypes(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }
        public static docTypes fromString(String type) {
            for (docTypes d : docTypes.values()) {
                if (d.getType().equals(type)) {
                    return d;
                }
            }
            return null;
        }
        public String toString() {
            return type;
        }
    }
    private final docTypes docType;
    public Document(String name, docTypes docType, String docContent, Directory parent) {
        this.docType = docType;
        this.docContent = docContent;
        this.parent = parent;
        setName(name);
    }

    @Override
    public String getName(){
        return docName;
    }

    @Override
    public void setName(String name) {
        if (this.parent != null)
            if (this.parent.getFileByName(name) != null) {
                throw new IllegalArgumentException("File already exists.");
            }
        if (!validateName(name)) {
            throw new IllegalArgumentException("The name is invalid.");
        }
        this.docName = name;
    }

    private boolean validateName(String name) {
        if (name.isEmpty() || name.length() > 10) return false;
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) return false;
        }
        return true;
    }


    @Override
    public int getSize(){
        return 40 + docContent.length() * 2;
    }

    @Override
    public Directory getParent() {
        return parent;
    }

    @Override
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    @Override
    public String getType(){
        return docType.toString();
    }

    public docTypes getDocType() {
        return docType;
    }

    public String getContent(){
        return docContent;
    }

    @Override
    public void printInfo() {
        System.out.println(docName + " " + getType() + " " + getContent());
    }

}