package Disk;
import Directory.Directory;
import Directory.Document;

import java.util.*;

import Criteria.*;
import Operator.Operator_Base;

public class Disk {

    private int diskSize, curSize;
    private List<Directory> dirs;
    private List<Document> docs;
    private Directory curDir;
    private TreeSet<Criteria> criteriaList;

    private Stack<Operator_Base> redoStack, undoStack;

    private Disk(int siz) {
        this.diskSize = siz;
        dirs = new ArrayList<Directory>();
        docs = new ArrayList<Document>();
        curDir = null;
        criteriaList = new TreeSet<Criteria>(Comparator.comparing(Criteria::getName))
    }

    public void addCriteria(Criteria criteria){
        criteriaList.add(criteria);
    }
    public Criteria searchCriteria(String s){
        Criteria ret = criteriaList.ceiling(new Criteria(s));
        return ret != null && ret.getName().equals(s) ? ret : null;
    }
    private static Disk root;

    public void updateCurSize(int dSiz)
    {
        if(curSize + dSiz > diskSize)
            throw new IllegalArgumentException("Disk memory exceed.");
        curSize += dSiz;
    }

    public static void newDisk(int siz) {
        root = new Disk(siz);
    }

    public static Disk getDisk() {
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

    public void pushRedoStack(Operator_Base operator) {
        if (operator.getOp() != )
    }
}
