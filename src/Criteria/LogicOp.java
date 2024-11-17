package Criteria;

public enum LogicOp {
    AND("&&"), OR("||"), NOT("~");
    private final String value;
    LogicOp(String value){
        this.value = value;
    }
    public static LogicOp fromString(String value){
        for(LogicOp op : LogicOp.values()){
            if(op.value.equals(value)){
                return op;
            }
        }
        return null;
    }
    public String toString(){
        return value;
    }
}
