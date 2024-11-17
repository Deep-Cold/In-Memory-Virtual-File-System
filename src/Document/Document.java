package Document;

import Directory.Document;

public class Document extends Directory.Document {
    String docName;
    String docType;
    String docContent;
    int size;

    public Document(String name, int size) {
        super(name, size);
    }

    public String getName(){
        return docName;
    }

    public void setName(String name){
        docName = name;
    }

    public int getSize(){
        return size;
    }

    public String getType(){
        return docType;
    }

    public String getpath(){
        return docName+'.'+ docType;
    }

    public void newDoc(String docName, String docType, String docContent) {
        this.docName = docName;
        this.docType = docType;
        this.docContent = docContent;
        size = 40 + docContent.length() * 2;
    }

    public void printInfo() {

    }
}