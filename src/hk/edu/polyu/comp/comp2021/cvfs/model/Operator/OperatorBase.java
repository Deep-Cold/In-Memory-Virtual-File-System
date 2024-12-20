package hk.edu.polyu.comp.comp2021.cvfs.model.Operator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.Criteria;
import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.CriteriaIntValue;
import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.CriteriaStringValue;
import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.LogicOp;
import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.Op;
import hk.edu.polyu.comp.comp2021.cvfs.model.Disk.Disk;
import hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent.Directory;
import hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent.Document;
import hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent.FileSystemElement;

/**

 * The Base of Operator
 */
public abstract class OperatorBase {
    private final Operation op;

    /**

     * @param op The corresponding operation name
     */
    OperatorBase(Operation op) {
        this.op = op;
    }

    /**

     * @throws IOException If reading or writing the file is failed
     */
    public abstract void runCommand() throws IOException;

    /**

     * error input
     */
    public static void errInput() {
        throw new IllegalArgumentException("The input format is incorrect.");
    }
    public static void errInput(String message) {
        throw new IllegalArgumentException(message);
    }

    /**

     * memory exceed
     */
    public static void memExceed() {
        throw new IllegalArgumentException("The memory is not enough.");
    }

    /**

     * @param str The String to be converted
     * @return The corresponding Operator
     */
    public static OperatorBase getOperator(String str) {
        Operation op = Operation.fromString(str.split(" ")[0]);
        return switch (op) {
            case newDisk -> OpNewDisk.fromString(str);
            case newDoc -> OpNewDoc.fromString(str);
            case newDir -> OpNewDir.fromString(str);
            case delete -> OpDelete.fromString(str);
            case rename -> OpRename.fromString(str);
            case changeDir -> OpChangeDir.fromString(str);
            case list -> OpList.fromString(str);
            case rList -> OpRList.fromString(str);
            case newSimpleCri -> OpNewSimpleCri.fromString(str);
            case newBinaryCri -> OpNewBinaryCri.fromString(str);
            case newNegation -> OpNewNegation.fromString(str);
            case printAllCriteria -> OpPrintAllCriteria.fromString(str);
            case search -> OpSearch.fromString(str);
            case rSearch -> OpRSearch.fromString(str);
            case save -> OpSave.fromString(str);
            case load -> OpLoad.fromString(str);
            case undo -> OpUndo.fromString(str);
            case redo -> OpRedo.fromString(str);
            case quit -> OpQuit.fromString(str);
            case null -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}

/**

 * Operation newDisk
 */
class OpNewDisk extends OperatorBase {
    private final int siz;

    /**

     * @param op The corresponding operation name
     * @param siz The size of the disk
     */
    OpNewDisk(Operation op, int siz) {
        super(op);
        this.siz = siz;
    }

    @Override
    public void runCommand() {
        Disk.newDisk(siz);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpNewDisk fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for newDisk command. Usage: newDisk diskSize.");
        int siz = 0;
        try{
            siz = Integer.parseInt(elem[1]);
        } catch (NumberFormatException e) {
            errInput("Invalid disk size. Disk size must be a positive integer.");
        }
        if(siz <= 0) errInput("Invalid disk size. Disk size must be a positive integer.");
        return new OpNewDisk(Operation.newDisk, siz);
    }
}

/**

 * Operation newDoc
 */
class OpNewDoc extends RedoOperator {
    private final String fileName, fileContent;
    private final Document.docTypes docType;

    /**

     * @param op The corresponding operation name
     * @param fileName The name of the document
     * @param docType The type of the document
     * @param fileContent The content of the document
     */
    OpNewDoc(Operation op, String fileName, Document.docTypes docType, String fileContent) {
        super(op);
        this.fileName = fileName;
        this.docType = docType;
        this.fileContent = fileContent;
    }

    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        Document nDoc = new Document(fileName, docType, fileContent, dir);
        if(nDoc.getSize() > Disk.getDisk().getRemSiz()) memExceed();
        dir.addFile(nDoc);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpNewDoc fromString(String str) {
        String[] elem = str.split(" ", 4);
        if (elem.length != 4) errInput("Invalid number of arguments for newDoc command. Usage: newDoc docName docType docContent.");
        Document.docTypes docType = Document.docTypes.fromString(elem[2]);
        if (docType == null) errInput("Invalid document type. Please provide a valid document type.");
        String docContent = elem[3];
        if (docContent.contains(" ") && !docContent.startsWith("\"")) {
            errInput("Invalid document content format. If the content contains spaces, please enclose it in double quotes.");
        }
        return new OpNewDoc(Operation.newDoc, elem[1], docType, docContent);
    }

    @Override
    public ArrayList<OperatorBase> getReverse() {
        ArrayList<OperatorBase> cur = new ArrayList<>();
        cur.add(new OpDelete(Operation.delete, fileName));
        return cur;
    }
}

/**

 * Operation newDir
 */
class OpNewDir extends RedoOperator {
    private final String dirName;

    /**

     * @param op The corresponding operation name
     * @param dirName The name of the directory
     */
    OpNewDir(Operation op, String dirName) {
        super(op);
        this.dirName = dirName;
    }

    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        Directory nDir = new Directory(dirName, dir);
        if(nDir.getSize() > Disk.getDisk().getRemSiz()) memExceed();
        dir.addFile(nDir);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpNewDir fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for newDir command. Usage: newDir dirName.");
        return new OpNewDir(Operation.newDir, elem[1]);
    }

    @Override
    public ArrayList<OperatorBase> getReverse() {
        ArrayList<OperatorBase> cur = new ArrayList<>();
        cur.add(new OpDelete(Operation.delete, dirName));
        return cur;
    }
}

/**

 * Operation delete
 */
class OpDelete extends RedoOperator {
    private final String fileName;

    /**

     * @param op The corresponding operation name
     * @param fileName The name of the file to be deleted
     */
    OpDelete(Operation op, String fileName) {
        super(op);
        this.fileName = fileName;
    }
    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.delFile(fileName);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpDelete fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for delete command. Usage: delete fileName.");
        return new OpDelete(Operation.delete, elem[1]);
    }
    @Override
    public ArrayList<OperatorBase> getReverse() {
        Directory dir = Disk.getDisk().getCurDir();
        FileSystemElement cur = dir.getFileByName(fileName);
        ArrayList<OperatorBase> ret = new ArrayList<>();
        if(cur == null) errInput("File '" + fileName + "' does not exist.");
        if(cur instanceof Directory) {
            List<FileSystemElement> elems = ((Directory) cur).getAllFiles();
            for(FileSystemElement elem : elems) {
                if(elem instanceof Directory) {
                    ret.add(new OpNewDir(Operation.newDir, elem.getName()));
                }
                else{
                    ret.add(new OpNewDoc(Operation.newDoc, elem.getName(), ((Document) elem).getDocType(), ((Document) elem).getContent()));
                }
            }
        }
        else {
            ret.add(new OpNewDoc(Operation.newDoc, cur.getName(), ((Document) cur).getDocType(), ((Document) cur).getContent()));
        }
        return ret;
    }
}

/**

 * Operation rename
 */
class OpRename extends RedoOperator {
    private final String oldName, newName;

    /**

     * @param op The corresponding operation name
     * @param oldName The original name of the file
     * @param newName The new name of the file
     */
    OpRename(Operation op, String oldName, String newName) {
        super(op);
        this.oldName = oldName;
        this.newName = newName;
    }
    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.renameFile(oldName, newName);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpRename fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 3) errInput("Invalid number of arguments for rename command. Usage: rename oldFileName newFileName.");
        return new OpRename(Operation.rename, elem[1], elem[2]);
    }
    @Override
    public ArrayList<OperatorBase> getReverse() {
        ArrayList<OperatorBase> cur = new ArrayList<>();
        cur.add(new OpRename(Operation.rename, newName, oldName));
        return cur;
    }
}

/**

 * Operation changeDir
 */
class OpChangeDir extends RedoOperator {
    private final String dirName;

    /**

     * @param op The corresponding operation name
     * @param dirName The new working directory
     */
    OpChangeDir(Operation op, String dirName) {
        super(op);
        this.dirName = dirName;
    }
    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        FileSystemElement nDir;
        if(Objects.equals(dirName, "..")) nDir = dir.getParent();
        else nDir = dir.getFileByName(dirName);
        if(nDir == null || nDir instanceof Document) errInput("Directory '" + dirName + "' does not exist.");
        Disk.getDisk().setCurDir((Directory) nDir);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpChangeDir fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for changeDir command. Usage: changeDir dirName.");
        return new OpChangeDir(Operation.changeDir, elem[1]);
    }
    @Override
    public ArrayList<OperatorBase> getReverse() {
        Directory dir = Disk.getDisk().getCurDir();
        ArrayList<OperatorBase> cur = new ArrayList<>();
        cur.add(new OpChangeDir(Operation.changeDir, Objects.equals(dirName, "..") ? dir.getName() : ".."));
        return cur;
    }
}

/**

 * Operation list
 */
class OpList extends OperatorBase {
    /**

     * @param op The corresponding operation name
     */
    OpList(Operation op) {
        super(op);
    }
    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.listFiles();
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpList fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput("Invalid number of arguments for list command. Usage: list.");
        return new OpList(Operation.list);
    }
}

/**

 * Operation rList
 */
class OpRList extends OperatorBase {
    /**

     * @param op The corresponding operation name
     */
    OpRList(Operation op) {
        super(op);
    }
    @Override
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.rListFiles();
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpRList fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput("Invalid number of arguments for rList command. Usage: rList.");
        return new OpRList(Operation.rList);
    }
}

/**

 * Operation newSimpleCri
 */
class OpNewSimpleCri extends CriteriaOperator {
    private final String criName, val;
    private final Op criOp;
    private final Criteria.AttrName attrName;

    /**

     * @param op The corresponding operation name
     * @param criName The Criteria Name
     * @param attr The Criteria Attribute Name
     * @param criOp The Criteria Operator Name
     * @param val The Criteria Value
     */
    OpNewSimpleCri(Operation op, String criName, Criteria.AttrName attr, Op criOp, String val) {
        super(op);
        this.criName = criName;
        this.criOp = criOp;
        this.attrName = attr;
        this.val = val;
    }
    @Override
    public String getName() {
        return criName;
    }
    @Override
    public void runCommand() {
        Disk disk = Disk.getDisk();
        if(attrName != Criteria.AttrName.Size)
            disk.addCriteria(new Criteria(criName, attrName, criOp, CriteriaStringValue.parse(val)));
        else disk.addCriteria(new Criteria(criName, attrName, criOp, CriteriaIntValue.parse(val)));
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpNewSimpleCri fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 5) errInput("Invalid number of arguments for newSimpleCri command. Usage: newSimpleCri criName attrName op val.");
        Criteria.AttrName attrName = Criteria.AttrName.fromString(elem[2]);
        Op op = Op.fromString(elem[3]);
        if(Criteria.checkName(elem[1])) errInput("Invalid criterion name. Criterion names must contain exactly two English letters.");
        switch(attrName) {
            case null:
                errInput("Invalid attribute name. Expected 'name', 'type', or 'size'.");
                break;
            case Name:
                if(op != Op.Contain) errInput("Invalid operator for 'name' attribute. Expected 'contains' operator.");
                if(elem[4].length() <= 2 || (elem[4].charAt(0) != '\"') || (elem[4].charAt(elem[4].length() - 1) != '\"')) errInput("Invalid value for 'name' attribute. The value must be a string in double quotes.");
                elem[4] = elem[4].substring(1, elem[4].length() - 1);
                break;
            case Type:
                if(op != Op.Equal) errInput("Invalid operator for 'type' attribute. Expected 'equals' operator.");
                if(elem[4].length() <= 2 || (elem[4].charAt(0) != '\"') || (elem[4].charAt(elem[4].length() - 1) != '\"')) errInput("Invalid value for 'type' attribute. The value must be a string in double quotes.");
                elem[4] = elem[4].substring(1, elem[4].length() - 1);
                break;
            case Size:
                if(!Op.ariOp(op)) errInput("Invalid operator for 'size' attribute. Expected one of >, <, >=, <=, ==, or !=.");
                for(Character x : elem[4].toCharArray()) {
                    if(!Character.isDigit(x)) errInput("Invalid value for 'size' attribute. The value must be an integer.");
                }
                break;
        }
        return new OpNewSimpleCri(Operation.newSimpleCri, elem[1], attrName, op, elem[4]);
    }
    public String toString() {
        String quote = ((attrName == Criteria.AttrName.Name || attrName == Criteria.AttrName.Type) ? "\"" : "");
        return "newSimpleCri " + criName + " " + attrName + " " + criOp + ' ' + quote + val + quote + '\n';
    }
}

/**

 * Operation newNegation
 */
class OpNewNegation extends CriteriaOperator {
    private final String criName1, criName2;

    /**

     * @param op The corresponding operation name
     * @param criName1 The new Criteria Name
     * @param criName2 The existing Criteria Name
     */
    OpNewNegation(Operation op, String criName1, String criName2) {
        super(op);
        this.criName1 = criName1;
        this.criName2 = criName2;
    }
    @Override
    public String getName() {
        return criName1;
    }
    @Override
    public void runCommand() {
        Disk disk = Disk.getDisk();
        if(disk.searchCriteria(criName1) != null) errInput("Criterion '" + criName1 + "' already exists.");
        Criteria ori = disk.searchCriteria(criName2);
        if(ori == null) errInput("Criterion '" + criName2 + "' does not exist.");
        disk.addCriteria(new Criteria(criName1, LogicOp.NOT, ori));
    }
    /**
     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpNewNegation fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 3) errInput("Invalid number of arguments for newNegation command. Usage: newNegation criName1 criName2.");
        if(Criteria.checkName(elem[1])) errInput("Invalid criterion name. Criterion names must contain exactly two English letters.");
        return new OpNewNegation(Operation.newNegation, elem[1], elem[2]);
    }
    public String toString() {
        return "newNegation " + criName1 + " " + criName2 + "\n";
    }
}

/**

 * Operation newBinaryCri
 */
class OpNewBinaryCri extends CriteriaOperator {
    private final String criName1, criName3, criName4;
    private final LogicOp logicOp;

    /**

     * @param op The corresponding operation name
     * @param attr1 The new Criteria name
     * @param attr3 The left Criteria name
     * @param logicOp The logicOp applied to the Criteria
     * @param attr4 The right Criteria name
     */
    OpNewBinaryCri(Operation op, String attr1, String attr3, LogicOp logicOp, String attr4) {
        super(op);
        this.criName1 = attr1;
        this.criName3 = attr3;
        this.logicOp = logicOp;
        this.criName4 = attr4;
    }
    @Override
    public String getName() {
        return criName1;
    }
    @Override
    public void runCommand() {
        Disk disk = Disk.getDisk();
        if(disk.searchCriteria(criName1) != null) errInput("Criterion '" + criName1 + "' already exists.");
        Criteria ori1 = disk.searchCriteria(criName3), ori2 = disk.searchCriteria(criName4);
        if(ori1 == null || ori2 == null) errInput("One of the specified criteria does not exist.");
        disk.addCriteria(new Criteria(criName1, ori1, logicOp, ori2));
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpNewBinaryCri fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 5) errInput("Invalid number of arguments for newBinaryCri command. Usage: newBinaryCri criName1 criName3 logicOp criName4.");
        LogicOp logicOp = LogicOp.fromString(elem[3]);
        if(logicOp == null) errInput("Invalid logical operator. Expected '&&' or '||'.");
        if(Criteria.checkName(elem[1])) errInput("Invalid criterion name. Criterion names must contain exactly two English letters.");
        return new OpNewBinaryCri(Operation.newBinaryCri, elem[1], elem[2], LogicOp.fromString(elem[3]), elem[4]);
    }
    public String toString() {
        return "newBinaryCri " + criName1 + " " + criName3 + " " + logicOp + " " + criName4 + "\n";
    }
}

/**

 * Operation printAllCriteria
 */
class OpPrintAllCriteria extends OperatorBase {
    /**

     * @param op The corresponding operation name
     */
    OpPrintAllCriteria(Operation op) {
        super(op);
    }
    @Override
    public void runCommand() {
        Disk disk = Disk.getDisk();
        disk.printAllCriteria();
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpPrintAllCriteria fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput("Invalid number of arguments for printAllCriteria command. Usage: printAllCriteria.");
        return new OpPrintAllCriteria(Operation.printAllCriteria);
    }
}

/**

 * Operation search
 */
class OpSearch extends OperatorBase {
    private final String criName;

    /**

     * @param op The corresponding operation name
     * @param criName The Criteria to be applied
     */
    OpSearch(Operation op, String criName) {
        super(op);
        this.criName = criName;
    }
    @Override
    public void runCommand() {
        Disk disk = Disk.getDisk();
        Criteria cri = disk.searchCriteria(criName);
        if(cri == null) errInput("Criterion '" + criName + "' does not exist.");
        disk.getCurDir().searchFiles(cri);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpSearch fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for search command. Usage: search criName.");
        return new OpSearch(Operation.search, elem[1]);
    }
}

/**

 * Operation rSearch
 */
class OpRSearch extends OperatorBase {
    private final String criName;

    /**

     * @param op The corresponding operation name
     * @param criName The Criteria name to be applied
     */
    OpRSearch(Operation op, String criName) {
        super(op);
        this.criName = criName;
    }
    @Override
    public void runCommand() {
        Disk disk = Disk.getDisk();
        Criteria cri = disk.searchCriteria(criName);
        if(cri == null) errInput("Criterion '" + criName + "' does not exist.");
        disk.getCurDir().rSearchFiles(cri);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpRSearch fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for rSearch command. Usage: rSearch criName.");
        return new OpRSearch(Operation.rSearch, elem[1]);
    }
}

/**

 * Operation save
 */
class OpSave extends OperatorBase {
    private final String path;

    /**

     * @param op The corresponding operation name
     * @param path The path of the file
     */
    OpSave(Operation op, String path) {
        super(op);
        this.path = path;
    }
    @Override
    public void runCommand() throws IOException {
        Disk disk = Disk.getDisk();
        disk.save(path);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpSave fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for save command. Usage: save path.");
        return new OpSave(Operation.save, elem[1]);
    }
}

/**

 * Operation load
 */
class OpLoad extends OperatorBase {
    private final String path;

    /**

     * @param op The corresponding operation name
     * @param path The path of the file
     */
    OpLoad(Operation op, String path) {
        super(op);
        this.path = path;
    }
    @Override
    public void runCommand() throws IOException {
        Disk.load(path);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpLoad fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput("Invalid number of arguments for load command. Usage: load path.");
        return new OpLoad(Operation.load, elem[1]);
    }
}

/**

 * Operation undo
 */
class OpUndo extends OperatorBase {
    /**

     * @param op The corresponding operation name
     */
    OpUndo(Operation op) {
        super(op);
    }
    @Override
    public void runCommand() throws IOException {
        Disk disk = Disk.getDisk();
        disk.undo();
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpUndo fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput("Invalid number of arguments for undo command. Usage: undo.");
        return new OpUndo(Operation.undo);
    }
}

/**

 * Operation redo
 */
class OpRedo extends OperatorBase {
    /**

     * @param op The corresponding operation name
     */
    OpRedo(Operation op) {
        super(op);
    }
    @Override
    public void runCommand() throws IOException {
        Disk disk = Disk.getDisk();
        disk.redo();
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpRedo fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput("Invalid number of arguments for redo command. Usage: redo.");
        return new OpRedo(Operation.redo);
    }
}

/**

 * Operation quit
 */
class OpQuit extends OperatorBase {
    /**

     * @param op The corresponding operation name
     */
    OpQuit(Operation op) {
        super(op);
    }
    @Override
    public void runCommand() {
        System.exit(0);
    }

    /**

     * @param str The String to be converted
     * @return The corresponding operation
     */
    public static OpQuit fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput("Invalid number of arguments for quit command. Usage: quit.");
        return new OpQuit(Operation.quit);
    }
}