package Disk;
import Directory.*;

import java.io.*;
import java.util.*;

import Criteria.*;
import Document.Document;
import Operator.*;

public class Disk {

    private final int diskSize;
    private final Directory rootDir;
    private Directory curDir;
    private final TreeSet<Criteria> criteriaList;
    private static Disk root;
    private final Stack<ArrayList<Operator_Base>> redoStack, undoStack;
    private final ArrayList<CriteriaOperator> criList;

    private Disk(int siz) {
        this.diskSize = siz;
        curDir = rootDir = new Directory("root", null);
        criteriaList = new TreeSet<Criteria>(Comparator.comparing(Criteria::getName));
        redoStack = new Stack<>();
        undoStack = new Stack<>();
        criList = new ArrayList<>();
        Criteria cri = new Criteria("IsDocument", Criteria.AttrName.Type, Op.NotEqual, CriteriaStringValue.parse("Directory"));
        cri.setSimple(false);
        this.addCriteria(cri);
    }

    public void addCriteria(Criteria criteria){
        if(this.searchCriteria(criteria.getName()) != null) throw new IllegalArgumentException("Criteria exists.");
        criteriaList.add(criteria);
    }
    public Criteria searchCriteria(String s){
        Criteria ret = criteriaList.ceiling(new Criteria(s));
        return ret != null && ret.getName().equals(s) ? ret : null;
    }

    public void deleteCriteria(String s){
        Criteria cur = searchCriteria(s);
        if(cur != null){
            criteriaList.remove(cur);
        }
    }

    public void printAllCriteria() {
        for(Criteria criteria : criteriaList) {
            if(Objects.equals(criteria.getName(), "IsDocument")) System.out.println("IsDocument");
            else System.out.println(criteria.getName() + " " + criteria.getCriteria());
        }
    }

    public static void newDisk(int siz) {
        root = new Disk(siz);
    }

    public static Disk getDisk() {
        if(root == null) throw new IllegalArgumentException("No current disk.");
        return root;
    }

    public Directory getCurDir() {
        if(curDir == null)
            throw new IllegalArgumentException("No working directory found.");
        return curDir;
    }

    public void setCurDir(Directory curDir) {
        this.curDir = curDir;
    }

    public void pushUndoStack(ArrayList<Operator_Base> operator, boolean needClear) {
        undoStack.push(operator);
        if(needClear) {
            if(operator.getFirst() instanceof CriteriaOperator) {
                criList.add(((CriteriaOperator) operator.getFirst()));
            }
            redoStack.clear();
        }
    }

    public void pushRedoStack(ArrayList<Operator_Base> operator) {
        undoStack.push(operator);
    }

    public void undo() throws IOException {
        if(undoStack.isEmpty()) throw new IllegalArgumentException("No operation can be performed");
        ArrayList<Operator_Base> operator = undoStack.pop();
        ArrayList<Operator_Base> RedoOperator = new ArrayList<>();
        for(Operator_Base op : operator) {
            if(op instanceof CriteriaOperator) {
                deleteCriteria(((CriteriaOperator) op).getName());
                ((CriteriaOperator) op).setDelete(true);
            }
            else {
                RedoOperator.addAll(((RedoOperator) op).getReverse());
                op.runCommand();
            }
        }
        pushRedoStack(RedoOperator);
    }

    public void redo() throws IOException {
        if(redoStack.isEmpty()) throw new IllegalArgumentException("No operation can be performed.");
        ArrayList<Operator_Base> operator = redoStack.pop();
        ArrayList<Operator_Base> UndoOperator = new ArrayList<>();
        for(Operator_Base op : operator) {
            if(op instanceof CriteriaOperator) {
                op.runCommand();
                ((CriteriaOperator) op).setDelete(false);
            }
            else {
                UndoOperator.addAll(((RedoOperator) op).getReverse());
                op.runCommand();
            }
        }
        pushUndoStack(UndoOperator, false);
    }

    public void save(String path) throws IOException {
        File file = new File(path);
        if(file.exists()) throw new IllegalArgumentException("File exists.");
        if(!file.exists()) file.createNewFile();
        FileOutputStream fileStream = new FileOutputStream(file);
        OutputStreamWriter output = new OutputStreamWriter(fileStream);
        output.write("newDisk" + " " + diskSize + "\n");
        saveFile(rootDir, output);
        for(CriteriaOperator op : criList) {
            if(op.isDelete()) continue;
            output.write(op.toString());
        }
        output.close();
        fileStream.close();
    }

    public void saveFile(Directory cur, OutputStreamWriter out) throws IOException {
        for(FileSystemElement element : cur.getFiles()) {
            if(element instanceof Directory) {
                out.write("newDir" + " " + element.getName() + "\n");
                out.write("changeDir" + " " + element.getName() + "\n");
                saveFile((Directory) element, out);
            }
            else if (element instanceof Document) {
                out.write("newDoc" + " " + element.getName() + " " + element.getType() + " " + ((Document) element).getContent() + "\n");
            }
            if(cur != rootDir) out.write("changeDir ..\n");
        }
    }

    public static void load(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()) throw new IllegalArgumentException("File does not exists.");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        String command;
        while((command = reader.readLine()) != null) {
            Operator_Base curOp = Operator_Base.getOperator(command);
            curOp.runCommand();
            if(curOp instanceof CriteriaOperator) {
                getDisk().criList.add(((CriteriaOperator) curOp));
            }
        }
        reader.close();
        fileStream.close();
    }

    public int getRemSiz() {
        return diskSize - rootDir.getSize();
    }
}
