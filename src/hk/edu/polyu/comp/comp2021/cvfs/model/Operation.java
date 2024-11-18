package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * Basic Operation
 */
public enum Operation {

    /**
     * newDisk Operator
     */
    newDisk("newDisk"),
    /**
     * newDoc Operator
     */
    newDoc("newDoc"),
    /**
     * newDir Operator
     */
    newDir("newDir"),
    /**
     * delete Operator
     */
    delete("delete"),
    /**
     * rename Operator
     */
    rename("rename"),
    /**
     * changeDir Operator
     */
    changeDir("changeDir"),
    /**
     * list Operator
     */
    list("list"),
    /**
     * rList Operator
     */
    rList("rList"),
    /**
     * newSimpleCri Operator
     */
    newSimpleCri("newSimpleCri"),
    /**
     * newNegation Operator
     */
    newNegation("newNegation"),
    /**
     * newBinaryCri Operator
     */
    newBinaryCri("newBinaryCri"),
    /**
     * printAllCriteria Operator
     */
    printAllCriteria("printAllCriteria"),
    /**
     * search Operator
     */
    search("search"),
    /**
     * rSearch Operator
     */
    rSearch("rSearch"),
    /**
     * save Operator
     */
    save("save"),
    /**
     * load Operator
     */
    load("load"),
    /**
     * quit Operator
     */
    quit("quit"),
    /**
     * undo Operator
     */
    undo("undo"),
    /**
     * redo Operator
     */
    redo("redo");
    private final String content;

    Operation(String content)
    {
        this.content = content;
    }

    public String toString()
    {
        return content;
    }

    /**
     * @param str The String to be converted
     * @return The corresponding Operator
     */
    public static Operation fromString(String str)
    {
        for(Operation op : Operation.values())
            if(op.toString().equals(str))
                return op;
        return null;
    }

}