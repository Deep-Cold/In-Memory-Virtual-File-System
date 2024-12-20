package hk.edu.polyu.comp.comp2021.cvfs.model.Criteria;
import java.util.HashSet;

/**
 * Criteria Operator
 */
public enum Op {
    /**
     * String contain another String
     */
    Contain("contains"),
    /**
     * Not contain another String
     */
    NotContain("\"doesn't contain\""),
    /**
     * Type Equal
     */
    Equal("equals"),
    /**
     * Type Not Equal
     */
    NotEqual("\"doesn't equal\""),
    /**
     * Size Greater
     */
    Greater(">"),
    /**
     * Size LessEqual
     */
    LessEqual("<="),
    /**
     * Size Less
     */
    Less("<"),
    /**
     * Size GreaterEqual
     */
    GreaterEqual(">="),
    /**
     * Size Equal
     */
    sizeEqual("=="),
    /**
     * Size Not Equal
     */
    sizeNotEqual("!=");
    private final String value;
    private final static HashSet<Op> ariOps = new HashSet<>();
    static {
        ariOps.add(Greater);
        ariOps.add(LessEqual);
        ariOps.add(Less);
        ariOps.add(GreaterEqual);
        ariOps.add(sizeEqual);
        ariOps.add(sizeNotEqual);
    }
    Op(String value){
        this.value = value;
    }
    public String toString(){
        return value;
    }

    /**
     * @param str The String to be converted
     * @return The corresponding Operator
     */
    public static Op fromString(String str) {
        Op ret = null;
        for(Op op : Op.values()){
            if(op.toString().equals(str)){
                ret = op;
                break;
            }
        }
        return ret;
    }

    /**
     * @param op Operator to be checked
     * @return Whether the operator is arithmetic operator
     */
    public static boolean ariOp(Op op) {
        return ariOps.contains(op);
    }
}
