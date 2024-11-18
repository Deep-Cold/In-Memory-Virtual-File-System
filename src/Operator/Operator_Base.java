package Operator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Criteria.Criteria;
import Criteria.CriteriaIntValue;
import Criteria.CriteriaStringValue;
import Criteria.LogicOp;
import Criteria.Op;
import Directory.Directory;
import Directory.FileSystemElement;
import Disk.Disk;
import Document.Document;

public abstract class Operator_Base {
    private final Operation op;
    Operator_Base(Operation op) {
        this.op = op;
    }

    public abstract void runCommand() throws IOException;

    public static void errInput() {
        throw new IllegalArgumentException("The input format is incorrect.");
    }

    public static void memExceed() {
        throw new IllegalArgumentException("The memory is not enough.");
    }

    public static Operator_Base getOperator(String str) {
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
            case newBinary -> OpNewBinary.fromString(str);
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

class OpNewDisk extends Operator_Base {
    private final int siz;
    OpNewDisk(Operation op, int siz) {
        super(op);
        this.siz = siz;
    }

    public void runCommand() {
        Disk.newDisk(siz);
    }

    public static OpNewDisk fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        int siz = Integer.parseInt(elem[1]);
        if(siz < 0) errInput();
        return new OpNewDisk(Operation.newDisk, siz);
    }
}

class OpNewDoc extends RedoOperator {
    private final String fileName, fileContent;
    private final Document.docTypes docType;
    OpNewDoc(Operation op, String fileName, Document.docTypes docType, String fileContent) {
        super(op);
        this.fileName = fileName;
        this.docType = docType;
        this.fileContent = fileContent;
    }

    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        Document nDoc = new Document(fileName, docType, fileContent, dir);
        if(nDoc.getSize() > Disk.getDisk().getRemSiz()) memExceed();
        dir.addFile(nDoc);
    }

    public static OpNewDoc fromString(String str) {
        String[] elem = str.split(" ", 4);
        if (elem.length != 4) errInput();
        Document.docTypes docType = Document.docTypes.fromString(elem[2]);
        if (docType == null) errInput();
        String docContent = elem[3];
        if (docContent.contains(" ") && !docContent.startsWith("\"")) {
            errInput();
        }
        return new OpNewDoc(Operation.newDoc, elem[1], docType, docContent);
    }

    public ArrayList<Operator_Base> getReverse() {
        ArrayList<Operator_Base> cur = new ArrayList<>();
        cur.add(new OpDelete(Operation.delete, fileName));
        return cur;
    }
}

class OpNewDir extends RedoOperator {
    private final String dirName;
    OpNewDir(Operation op, String dirName) {
        super(op);
        this.dirName = dirName;
    }

    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        Directory nDir = new Directory(dirName, dir);
        if(nDir.getSize() > Disk.getDisk().getRemSiz()) memExceed();
        dir.addFile(nDir);
    }

    public static OpNewDir fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpNewDir(Operation.newDir, elem[1]);
    }

    public ArrayList<Operator_Base> getReverse() {
        ArrayList<Operator_Base> cur = new ArrayList<>();
        cur.add(new OpDelete(Operation.delete, dirName));
        return cur;
    }
}

class OpDelete extends RedoOperator {
    private final String fileName;
    OpDelete(Operation op, String fileName) {
        super(op);
        this.fileName = fileName;
    }
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.delFile(fileName);
    }
    public static OpDelete fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpDelete(Operation.delete, elem[1]);
    }
    public ArrayList<Operator_Base> getReverse() {
        Directory dir = Disk.getDisk().getCurDir();
        FileSystemElement cur = dir.getFileByName(fileName);
        ArrayList<Operator_Base> ret = new ArrayList<>();
        if(cur == null) errInput();
        if(cur instanceof Directory) {
            List<FileSystemElement> elems = ((Directory) cur).getAllFiles();
            for(FileSystemElement elem : elems) {
                if(elem instanceof Directory) {
                    ret.add(new OpNewDir(Operation.newDir, elem.getName()));
                }
                else if(elem instanceof Document) {
                    ret.add(new OpNewDoc(Operation.newDoc, elem.getName(), ((Document) elem).getDocType(), ((Document) elem).getContent()));
                }
            }
        }
        else if(cur instanceof Document) {
            ret.add(new OpNewDoc(Operation.newDoc, cur.getName(), ((Document) cur).getDocType(), ((Document) cur).getContent()));
        }
        return ret;
    }
}

class OpRename extends RedoOperator {
    private final String oldName, newName;
    OpRename(Operation op, String oldName, String newName) {
        super(op);
        this.oldName = oldName;
        this.newName = newName;
    }
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.renameFile(oldName, newName);
    }
    public static OpRename fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 3) errInput();
        return new OpRename(Operation.rename, elem[1], elem[2]);
    }
    public ArrayList<Operator_Base> getReverse() {
        ArrayList<Operator_Base> cur = new ArrayList<>();
        cur.add(new OpRename(Operation.rename, newName, oldName));
        return cur;
    }
}

class OpChangeDir extends RedoOperator {
    private final String dirName;
    OpChangeDir(Operation op, String dirName) {
        super(op);
        this.dirName = dirName;
    }
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        FileSystemElement nDir;
        if(Objects.equals(dirName, "..")) nDir = dir.getParent();
        else nDir = dir.getFileByName(dirName);
        if(nDir == null || nDir instanceof Document) errInput();
        Disk.getDisk().setCurDir((Directory) nDir);
    }
    public static OpChangeDir fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpChangeDir(Operation.changeDir, elem[1]);
    }
    public ArrayList<Operator_Base> getReverse() {
        Directory dir = Disk.getDisk().getCurDir();
        ArrayList<Operator_Base> cur = new ArrayList<>();
        cur.add(new OpChangeDir(Operation.changeDir, Objects.equals(dirName, "..") ? dir.getName() : ".."));
        return cur;
    }
}

class OpList extends Operator_Base {
    OpList(Operation op) {
        super(op);
    }
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.listFiles();
    }
    public static OpList fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        return new OpList(Operation.list);
    }
}

class OpRList extends Operator_Base {
    OpRList(Operation op) {
        super(op);
    }
    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        dir.rListFiles();
    }
    public static OpRList fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        return new OpRList(Operation.rList);
    }
}

class OpNewSimpleCri extends CriteriaOperator {
    private final String criName, val;
    private final Op criOp;
    private final Criteria.AttrName attrName;
    OpNewSimpleCri(Operation op, String criName, Criteria.AttrName attr, Op criOp, String val) {
        super(op);
        this.criName = criName;
        this.criOp = criOp;
        this.attrName = attr;
        this.val = val;
    }
    public String getName() {
        return criName;
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        if(attrName != Criteria.AttrName.Size)
            disk.addCriteria(new Criteria(criName, attrName, criOp, CriteriaStringValue.parse(val)));
        else disk.addCriteria(new Criteria(criName, attrName, criOp, CriteriaIntValue.parse(val)));
    }
    public static OpNewSimpleCri fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 5) errInput();
        Criteria.AttrName attrName = Criteria.AttrName.fromString(elem[2]);
        Op op = Op.fromString(elem[3]);
        if(Criteria.checkName(elem[1])) errInput();
        switch(attrName) {
            case null:
                errInput();
                break;
            case Name:
                if(op != Op.Contain) errInput();
                if(elem[4].length() <= 2 || (elem[4].charAt(0) != '\"') || (elem[4].charAt(elem[4].length() - 1) != '\"')) errInput();
                elem[4] = elem[4].substring(1, elem[4].length() - 1);
                break;
            case Type:
                if(op != Op.Equal) errInput();
                if(elem[4].length() <= 2 || (elem[4].charAt(0) != '\"') || (elem[4].charAt(elem[4].length() - 1) != '\"')) errInput();
                elem[4] = elem[4].substring(1, elem[4].length() - 1);
                break;
            case Size:
                if(!Op.ariOp(op)) errInput();
                for(Character x : elem[4].toCharArray()) {
                   if(!Character.isDigit(x)) errInput();
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

class OpNewNegation extends CriteriaOperator {
    String criName1, criName2;
    OpNewNegation(Operation op, String criName1, String criName2) {
        super(op);
        this.criName1 = criName1;
        this.criName2 = criName2;
    }
    public String getName() {
        return criName1;
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        if(disk.searchCriteria(criName1) != null) errInput();
        Criteria ori = disk.searchCriteria(criName2);
        if(ori == null) errInput();
        disk.addCriteria(new Criteria(criName1, LogicOp.NOT, ori));
    }
    public static OpNewNegation fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 3) errInput();
        if(Criteria.checkName(elem[1])) errInput();
        return new OpNewNegation(Operation.newNegation, elem[1], elem[2]);
    }
    public String toString() {
        return "newNegation " + criName1 + " " + criName2 + "\n";
    }
}

class OpNewBinary extends CriteriaOperator {
    private final String criName1, criName3, criName4;
    private final LogicOp logicOp;
    OpNewBinary(Operation op, String attr1, String attr3, LogicOp logicOp, String attr4) {
        super(op);
        this.criName1 = attr1;
        this.criName3 = attr3;
        this.logicOp = logicOp;
        this.criName4 = attr4;
    }
    public String getName() {
        return criName1;
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        if(disk.searchCriteria(criName1) != null) errInput();
        Criteria ori1 = disk.searchCriteria(criName3), ori2 = disk.searchCriteria(criName4);
        if(ori1 == null || ori2 == null) errInput();
        disk.addCriteria(new Criteria(criName1, ori1, logicOp, ori2));
    }
    public static OpNewBinary fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 5) errInput();
        LogicOp logicOp = LogicOp.fromString(elem[3]);
        if(logicOp == null) errInput();
        if(Criteria.checkName(elem[1])) errInput();
        return new OpNewBinary(Operation.newBinary, elem[1], elem[2], LogicOp.fromString(elem[3]), elem[4]);
    }
    public String toString() {
        return "newBinaryCri " + criName1 + " " + criName3 + " " + logicOp + " " + criName4 + "\n";
    }
}

class OpPrintAllCriteria extends Operator_Base {
    OpPrintAllCriteria(Operation op) {
        super(op);
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        disk.printAllCriteria();
    }
    public static OpPrintAllCriteria fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        return new OpPrintAllCriteria(Operation.printAllCriteria);
    }
}

class OpSearch extends Operator_Base {
    private final String criName;
    OpSearch(Operation op, String criName) {
        super(op);
        this.criName = criName;
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        Criteria cri = disk.searchCriteria(criName);
        if(cri == null) errInput();
        disk.getCurDir().searchFiles(cri);
    }
    public static OpSearch fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpSearch(Operation.search, elem[1]);
    }
}

class OpRSearch extends Operator_Base {
    String criName;
    OpRSearch(Operation op, String criName) {
        super(op);
        this.criName = criName;
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        Criteria cri = disk.searchCriteria(criName);
        if(cri == null) errInput();
        disk.getCurDir().rSearchFiles(cri);
    }
    public static OpRSearch fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpRSearch(Operation.rSearch, elem[1]);
    }
}

class OpSave extends Operator_Base {
    private final String path;
    OpSave(Operation op, String path) {
        super(op);
        this.path = path;
    }
    public void runCommand() throws IOException {
        Disk disk = Disk.getDisk();
        disk.save(path);
    }
    public static OpSave fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpSave(Operation.save, elem[1]);
    }
}

class OpLoad extends Operator_Base {
    private final String path;
    OpLoad(Operation op, String path) {
        super(op);
        this.path = path;
    }
    public void runCommand() throws IOException {
        Disk.load(path);
    }
    public static OpLoad fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        return new OpLoad(Operation.load, elem[1]);
    }
}

class OpUndo extends Operator_Base {
    OpUndo(Operation op) {
        super(op);
    }
    public void runCommand() throws IOException {
        Disk disk = Disk.getDisk();
        disk.undo();
    }
    public static OpUndo fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        return new OpUndo(Operation.undo);
    }
}

class OpRedo extends Operator_Base {
    OpRedo(Operation op) {
        super(op);
    }
    public void runCommand() throws IOException {
        Disk disk = Disk.getDisk();
        disk.redo();
    }
    public static OpRedo fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        return new OpRedo(Operation.redo);
    }
}

class OpQuit extends Operator_Base {
    OpQuit(Operation op) {
        super(op);
    }
    public void runCommand() {
        System.exit(0);
    }
    public static OpQuit fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        return new OpQuit(Operation.quit);
    }
}