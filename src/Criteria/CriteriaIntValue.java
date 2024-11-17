package Criteria;

public class CriteriaIntValue extends CriteriaValue{
    private final int value;

    public CriteriaIntValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    
    public String toString(){
        return String.valueOf(value);
    }

    public static CriteriaIntValue parse(String value){
        return new CriteriaIntValue(Integer.parseInt(value));
    }

    public static CriteriaIntValue parse(int value){
        return new CriteriaIntValue(value);
    }

    public boolean contains(String value){
        return false;
    }
}
