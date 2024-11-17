package Operator;
import Directory.*;
import Disk.*;
import Criteria.*;
import Document.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Operator_Base {
    private Operation op;
    Operator_Base() {
        this.op = null;
    }
    Operator_Base(Operation op) {
        this.op = op;
    }
    public Operation getOp() {
        return op;
    }
    public void setOp(Operation op) {
        this.op = op;
    }

    public abstract void runCommand();

    public static void errInput() {
        throw new IllegalArgumentException("The input format is incorrect.");
    }

    public static void memExceed() {
        throw new IllegalArgumentException("The memory is not enough.");
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
        if(Operation.fromString(elem[0]) != Operation.newDisk) errInput();
        int siz = Integer.parseInt(elem[1]);
        if(siz < 0) errInput();
        return new OpNewDisk(Operation.newDisk, siz);
    }
}

class OpNewDoc extends RedoOperator {
    private final String fileName, fileType, fileContent;
    OpNewDoc(Operation op, String fileName, String fileType, String fileContent) {
        super(op);
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileContent = fileContent;
    }

    public void runCommand() {
        Directory dir = Disk.getDisk().getCurDir();
        Document nDoc = new Document(fileName, fileType, fileContent, dir);
        if(nDoc.getSize() > Disk.getDisk().getRemSiz()) memExceed();
        dir.addFile(nDoc);
    }

    public OpNewDoc fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 3) errInput();
        if(Operation.fromString(elem[0]) != Operation.newDoc) errInput();
        return new OpNewDoc(Operation.fromString(elem[0]), elem[1], elem[2], fileContent);
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
        if(Operation.fromString(elem[0]) != Operation.newDir) errInput();
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
    public OpDelete fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        if(Operation.fromString(elem[0]) != Operation.delete) errInput();
        return new OpDelete(Operation.delete, elem[1]);
    }
    public ArrayList<Operator_Base> getReverse() {
        Directory dir = Disk.getDisk().getCurDir();
        FileSystemElement cur = dir.getFileByName(fileName);
        ArrayList<Operator_Base> ret = new ArrayList<Operator_Base>();
        if(cur == null) errInput();
        if(cur instanceof Directory) {
            List<FileSystemElement> elems = ((Directory) cur).getAllFiles();
            for(FileSystemElement elem : elems) {
                if(elem instanceof Directory) {
                    ret.add(new OpNewDir(Operation.newDir, elem.getName()));
                }
                else if(elem instanceof Document) {
                    ret.add(new OpNewDoc(Operation.newDoc, elem.getName(), ((Document) elem).getType(), ((Document) elem).getContent()));
                }
            }
        }
        else if(cur instanceof Document) {
            ret.add(new OpNewDoc(Operation.newDoc, cur.getName(), ((Document) cur).getType(), ((Document) cur).getContent()));
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
        if(Operation.fromString(elem[0]) != Operation.rename) errInput();
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
        if(Objects.equals(dirName, "..")) Disk.getDisk().setCurDir(dir.getParent());
        else Disk.getDisk().setCurDir((Directory) dir.getFileByName(dirName));
    }
    public static OpChangeDir fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        if(Operation.fromString(elem[0]) != Operation.changeDir) errInput();
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
    public OpList fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        if(Operation.fromString(elem[0]) != Operation.list) errInput();
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
        if(Operation.fromString(elem[0]) != Operation.rList) errInput();
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
        if(Operation.fromString(elem[0]) != Operation.newSimpleCri) errInput();
        Criteria.AttrName attrName = Criteria.AttrName.fromString(elem[2]);
        Op op = Op.fromString(elem[3]);
        if(!Criteria.checkName(elem[1])) errInput();
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
}

class OpNewNegation extends CriteriaOperator {
    String criName1, criName2;
    OpNewNegation(Operation op, String attrName1, String attrName2) {
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
        disk.addCriteria(new Criteria(criName1, null, ori));
    }
    public static OpNewNegation fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 3) errInput();
        if(Operation.fromString(elem[0]) != Operation.newNegation) errInput();
        if(!Criteria.checkName(elem[1])) errInput();
        return new OpNewNegation(Operation.newNegation, elem[1], elem[2]);
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
        if(Operation.fromString(elem[0]) != Operation.newBinary) errInput();
        LogicOp logicOp = LogicOp.fromString(elem[3]);
        if(logicOp == null) errInput();
        if(!Criteria.checkName(elem[1])) errInput();
        return new OpNewBinary(Operation.newBinary, elem[1], elem[2], LogicOp.fromString(elem[3]), elem[4]);
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
        if(Operation.fromString(elem[0]) != Operation.printAllCriteria) errInput();
        return new OpPrintAllCriteria(Operation.printAllCriteria);
    }
}

class OpSearch extends Operator_Base {
    private final Criteria.AttrName attrName;
    OpSearch(Operation op, Criteria.AttrName attrName) {
        super(op);
        this.attrName = attrName;
    }
    public void runCommand() {}
    public static OpSearch fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        if(Operation.fromString(elem[0]) != Operation.search) errInput();
        return new OpSearch(Operation.search, Criteria.AttrName.fromString(elem[1]));
    }
}

class OpRSearch extends Operator_Base {
    Criteria.AttrName attrName;
    OpRSearch(Operation op, Criteria.AttrName attrName) {
        super(op);
        this.attrName = attrName;
    }
    public void runCommand() {}
    public static OpRSearch fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        if(Operation.fromString(elem[0]) != Operation.rSearch) errInput();
        return new OpRSearch(Operation.rSearch, Criteria.AttrName.fromString(elem[1]));
    }
}

class OpSave extends Operator_Base {
    private final String path;
    OpSave(Operation op, String path) {
        super(op);
        this.path = path;
    }
    public void runCommand() {}
    public static OpSave fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        if(Operation.fromString(elem[0]) != Operation.save) errInput();
        return new OpSave(Operation.save, elem[1]);
    }
}

class OpLoad extends Operator_Base {
    private final String path;
    OpLoad(Operation op, String path) {
        super(op);
        this.path = path;
    }
    public void runCommand() {}
    public static OpLoad fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 2) errInput();
        if(Operation.fromString(elem[0]) != Operation.load) errInput();
        return new OpLoad(Operation.load, elem[1]);
    }
}

class OpUndo extends Operator_Base {
    OpUndo(Operation op) {
        super(op);
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        disk.undo();
    }
    public static OpUndo fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        if(Operation.fromString(elem[0]) != Operation.undo) errInput();
        return new OpUndo(Operation.undo);
    }
}

class OpRedo extends Operator_Base {
    OpRedo(Operation op) {
        super(op);
    }
    public void runCommand() {
        Disk disk = Disk.getDisk();
        disk.redo();
    }
    public static OpRedo fromString(String str) {
        String[] elem = str.split(" ");
        if(elem.length != 1) errInput();
        if(Operation.fromString(elem[0]) != Operation.redo) errInput();
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
        if(Operation.fromString(elem[0]) != Operation.quit) errInput();
        return new OpQuit(Operation.quit);
    }
}