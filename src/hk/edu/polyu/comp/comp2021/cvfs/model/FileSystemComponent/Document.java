package hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent;

/**
 * Document
 */
public class Document implements FileSystemElement {
    /**
     * The basic size of document
     */
    public static final int BASIC_SIZE = 40;
    private String docName;
    private final String docContent;
    private Directory parent;

    /**
     * The type of the document
     */
    public enum docTypes {
        /**
         * txt type
         */
        txt("txt"),
        /**
         * java type
         */
        java("java"),
        /**
         * html type
         */
        html("html"),

        /**
         * html type
         */
        css("css");
        private final String type;
        docTypes(String type) {
            this.type = type;
        }

        /**
         * @return get the type of the document
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The string to be converted
         * @return the corresponding docType
         */
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

    /**
     * @param name Document Name
     * @param docType Document Type
     * @param docContent Document Content
     * @param parent The Parent Directory
     */
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
        return BASIC_SIZE + docContent.length() * 2;
    }

    @Override
    public Directory getParent() {
        return parent;
    }

    @Override
    public void setParent(FileSystemElement parent) {
        if(parent instanceof Directory)
            this.parent = (Directory) parent;
        else throw new IllegalArgumentException("Parent is not a Directory.");
    }

    @Override
    public String getType(){
        return docType.toString();
    }

    /**
     * @return The type of the document
     */
    public docTypes getDocType() {
        return docType;
    }

    /**
     * @return The content of the document
     */
    public String getContent(){
        return docContent;
    }

    @Override
    public void printInfo() {
        System.out.println(docName + " " + getType() + " " + getContent());
    }

}