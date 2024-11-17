package Operator;
import java.util.HashSet;

public enum Operation {

    newDisk("newDisk"), newDoc("newDoc"), newDir("newDir"), delete("delete"), rename("rename"),
    changeDir("changeDir"), list("list"), rList("rList"), newSimpleCri("newSimpleCri"), newNegation("newNegation"), newBinary("newBinary"), printAllCriteria("printAllCriteria"),
    search("search"), rSearch("rSearch"), save("save"), load("load"), quit("quit"), undo("undo"), redo("redo");
    private final String content;

    private HashSet<String> pushOperations = new HashSet<String>();

    {
        pushOperations.add("newDoc");
        pushOperations.add("newDir");
        pushOperations.add("delete");
        pushOperations.add("rename");
        pushOperations.add("changeDir");
        pushOperations.add("newSimpleCri");
        pushOperations.add("newNegation");
    }

    Operation(String content)
    {
        this.content = content;
    }

    public String toString()
    {
        return content;
    }

    public static Operation fromString(String str)
    {
        for(Operation op : Operation.values())
            if(op.toString().equals(str))
                return op;
        return null;
    }

};