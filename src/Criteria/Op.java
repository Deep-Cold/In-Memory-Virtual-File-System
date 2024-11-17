package Criteria;
import java.util.HashSet;
public enum Op {
    Contain("contains"), NotContain("doesn't contain"), Equal("equals"), NotEqual("doesn't equal"), Greater(">"), LessEqual("<="), Less("<"), GreaterEqual(">="), sizeEqual("=="), sizeNotEqual("!=");
    private final String value;
    private final static HashSet<Op> ariOps = new HashSet<Op>();
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
    public static Op fromString(String str) {
        for(Op op : Op.values()){
            if(op.toString().equals(str)){
                return op;
            }
        }
        return null;
    }
    public static boolean ariOp(Op op) {
        return ariOps.contains(op);
    }
}
