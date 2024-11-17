package Operator;

public enum Operation {

    newDisk("newDisk"), newDoc("newDoc"), newDir("newDir"), delete("delete"), rename("rename"),
    changeDir("changeDir"), list("list"), rList("rList"), newSimpleCri("newSimpleCri"), newNegation("newNegation"), newBinary("newBinary"), printAllCriteria("printAllCriteria"),
    search("search"), rSearch("rSearch"), save("save"), load("load"), quit("quit"), undo("undo"), redo("redo");
    private final String content;

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

}