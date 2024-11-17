package Criteria;

public class CriteriaStringValue extends CriteriaValue{
    private String value;

    public CriteriaStringValue(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }

    public boolean contains(String value){
        return this.value.contains(value);
    }

    public int getValue(){
        return 0;
    }
}
