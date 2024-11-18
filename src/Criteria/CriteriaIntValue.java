package Criteria;

public class CriteriaIntValue implements CriteriaValue{
    private final int value;

    public CriteriaIntValue(int value){
        this.value = value;
    }

    @Override
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

    @Override
    public boolean contains(String value){
        return false;
    }
}
