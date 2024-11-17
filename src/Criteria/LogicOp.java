package Criteria;

public enum LogicOp {
    AND("&&"), OR("||"), NOT("~");
    private final String value;
    LogicOp(String value){
        this.value = value;
    }
    public String toString(){
        return value;
    }
}
