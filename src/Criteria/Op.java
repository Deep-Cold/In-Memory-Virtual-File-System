package Criteria;
public enum Op {
    Contain("Contains"), NotContain("Doesn't Contain"), Equal("Equals"), NotEqual("Doesn't Equal"), Greater(">"), LessEqual("<="), Less("<"), GreaterEqual(">="), sizeEqual("=="), sizeNotEqual("!=");
    private final String value;
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
}
