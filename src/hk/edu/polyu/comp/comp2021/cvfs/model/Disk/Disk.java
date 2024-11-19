package hk.edu.polyu.comp.comp2021.cvfs.model.Disk;

import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.Criteria;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.CriteriaOperator;
import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.CriteriaStringValue;
import hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent.Directory;
import hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent.Document;
import hk.edu.polyu.comp.comp2021.cvfs.model.FileSystemComponent.FileSystemElement;
import hk.edu.polyu.comp.comp2021.cvfs.model.Criteria.Op;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.OperatorBase;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.RedoOperator;

import java.io.*;
import java.util.*;

/**
 * Disk
 */
public final class Disk {

    private final int diskSize;
    private final Directory rootDir;
    private Directory curDir;
    private final TreeSet<Criteria> criteriaList;
    private static Disk root;
    private final Stack<ArrayList<OperatorBase>> redoStack, undoStack;
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

    /**
     * @param criteria The criteria to be added
     */
    public void addCriteria(Criteria criteria){
        if(this.searchCriteria(criteria.getName()) != null) throw new IllegalArgumentException("Criteria exists.");
        criteriaList.add(criteria);
    }

    /**
     * @param s The name of the Criteria
     * @return The Criteria with name s
     */
    public Criteria searchCriteria(String s){
        Criteria ret = criteriaList.ceiling(new Criteria(s));
        return ret != null && ret.getName().equals(s) ? ret : null;
    }

    /**
     * @param s The name of the Criteria
     */
    public void deleteCriteria(String s){
        Criteria cur = searchCriteria(s);
        if(cur != null){
            criteriaList.remove(cur);
        }
    }

    /**
     * Print all the criteria
     */
    public void printAllCriteria() {
        for(Criteria criteria : criteriaList) {
            if(Objects.equals(criteria.getName(), "IsDocument")) System.out.println("IsDocument");
            else System.out.println(criteria.getName() + " " + criteria.getCriteria());
        }
    }

    /**
     * @param siz The size of the disk
     */
    public static void newDisk(int siz) {
        if(siz <= 0) throw new IllegalArgumentException("Size must be greater than zero.");
        root = new Disk(siz);
    }

    /**
     * @return The current disk
     */
    public static Disk getDisk() {
        if(root == null) throw new IllegalArgumentException("No current disk.");
        return root;
    }

    /**
     * @return The current working directory
     */
    public Directory getCurDir() {
        if(curDir == null)
            throw new IllegalArgumentException("No working directory found.");
        return curDir;
    }

    /**
     * @param curDir The new working directory
     */
    public void setCurDir(Directory curDir) {
        this.curDir = curDir;
    }

    /**
     * @param operator The sequence of operator to be pushed
     * @param needClear Do we need to clear the redoStack
     */
    public void pushUndoStack(ArrayList<OperatorBase> operator, boolean needClear) {
        undoStack.push(operator);
        if(needClear) {
            if(operator.getFirst() instanceof CriteriaOperator) {
                getCriList().add(((CriteriaOperator) operator.getFirst()));
            }
            redoStack.clear();
        }
    }

    /**
     * @param operator The sequence of operator to be pushed
     */
    public void pushRedoStack(ArrayList<OperatorBase> operator) {
        undoStack.push(operator);
    }

    /**
     * @throws IOException If opening the file cause error.
     */
    public void undo() throws IOException {
        if(undoStack.isEmpty()) throw new IllegalArgumentException("No operation can be performed");
        ArrayList<OperatorBase> operator = undoStack.pop();
        ArrayList<OperatorBase> RedoOperator = new ArrayList<>();
        for(OperatorBase op : operator) {
            if(op instanceof CriteriaOperator) {
                deleteCriteria(((CriteriaOperator) op).getName());
                ((CriteriaOperator) op).setDelete(true);
            }
            else {
                RedoOperator.addAll(((hk.edu.polyu.comp.comp2021.cvfs.model.Operator.RedoOperator) op).getReverse());
                op.runCommand();
            }
        }
        pushRedoStack(RedoOperator);
    }

    /**
     * @throws IOException If opening the file cause error.
     */
    public void redo() throws IOException {
        if(redoStack.isEmpty()) throw new IllegalArgumentException("No operation can be performed.");
        ArrayList<OperatorBase> operator = redoStack.pop();
        ArrayList<OperatorBase> UndoOperator = new ArrayList<>();
        for(OperatorBase op : operator) {
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

    /**
     * @param path The path of the file to be saved
     * @throws IOException If opening the file cause error.
     */
    public void save(String path) throws IOException {
        File file = new File(path);
        // if(file.exists()) throw new IllegalArgumentException("File exists.");
        if(!file.exists()) file.createNewFile();
        if(!file.canWrite()) throw new IOException("Can't write to file.");
        FileOutputStream fileStream = new FileOutputStream(file);
        OutputStreamWriter output = new OutputStreamWriter(fileStream);
        output.write("newDisk" + " " + diskSize + "\n");
        saveFile(rootDir, output);
        for(CriteriaOperator op : getCriList()) {
            if(op.isDelete()) continue;
            output.write(op.toString());
        }
        output.close();
        fileStream.close();
    }

    /**
     * @param cur The current Directory
     * @param out The output Stream
     * @throws IOException If opening the file cause error.
     */
    public void saveFile(Directory cur, OutputStreamWriter out) throws IOException {
        for(FileSystemElement element : cur.getFiles()) {
            if(element instanceof Directory) {
                out.write("newDir" + " " + element.getName() + "\n");
                out.write("changeDir" + " " + element.getName() + "\n");
                saveFile((Directory) element, out);
            }
            else {
                out.write("newDoc" + " " + element.getName() + " " + element.getType() + " " + ((Document) element).getContent() + "\n");
            }
            if(cur != rootDir) out.write("changeDir ..\n");
        }
    }

    /**
     * @param path The path of the file to be loaded
     * @throws IOException If opening the file cause error.
     */
    public static void load(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()) throw new IllegalArgumentException("File does not exists.");
        if(!file.canRead()) throw new IOException("Can't read file.");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        String command;
        while((command = reader.readLine()) != null) {
            OperatorBase curOp = OperatorBase.getOperator(command);
            curOp.runCommand();
            if(curOp instanceof CriteriaOperator) {
                getDisk().getCriList().add(((CriteriaOperator) curOp));
            }
        }
        reader.close();
        fileStream.close();
    }

    /**
     * @return The remaining size of the disk
     */
    public int getRemSiz() {
        return diskSize - rootDir.getSize();
    }

    /**
     * @return The criteria list of the disk
     */
    public ArrayList<CriteriaOperator> getCriList() {
        return criList;
    }
}
