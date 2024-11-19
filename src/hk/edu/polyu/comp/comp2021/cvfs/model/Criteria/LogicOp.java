package hk.edu.polyu.comp.comp2021.cvfs.model.Criteria;

/**
 * The LogicOp of the composite Criteria
 */
public enum LogicOp {
    /**
     * Logic And
     */
    AND("&&"),
    /**
     * Logic Or
     */
    OR("||"),
    /**
     * Logic Not
     */
    NOT("~");
    private final String value;
    LogicOp(String value){
        this.value = value;
    }

    /**
     * @param value The String to be converted
     * @return The corresponding LogicOp
     */
    public static LogicOp fromString(String value){
        LogicOp ret = null;
        for(LogicOp op : LogicOp.values()){
            if(op.getValue().equals(value)){
                ret = op;
                break;
            }
        }
        return ret;
    }
    public String toString(){
        return getValue();
    }

    /**
     * @return The String representation of the LogicOp
     */
    public String getValue() {
        return value;
    }
}
