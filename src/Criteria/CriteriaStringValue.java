package Criteria;

public class CriteriaStringValue extends CriteriaValue{
    private final String value;

    public CriteriaStringValue(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }

    public boolean contains(String value){
        return this.value.contains(value);
    }

    public boolean equals(Object value){
        if(value instanceof CriteriaStringValue){
            return this.value.equals(((CriteriaStringValue)value).value);
        }
        return false;
    }

    public static CriteriaStringValue parse(String value){
        return new CriteriaStringValue(value);
    }

    public int getValue(){
        return 0;
    }
}
