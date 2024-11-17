package Criteria;

public class CriteriaIntValue extends CriteriaValue{
    private int value;

    public CriteriaIntValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    
    public String toString(){
        return String.valueOf(value);
    }

    public boolean contains(String value){
        return false;
    }
}
