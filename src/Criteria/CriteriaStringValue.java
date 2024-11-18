package Criteria;

public class CriteriaStringValue implements CriteriaValue{
    private final String value;

    public CriteriaStringValue(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }

    @Override
    public boolean contains(String value){
        return this.value.contains(value);
    }

    public boolean equals(Object value){
        if(value instanceof CriteriaStringValue){
            return this.value.equals(((CriteriaStringValue)value).toString());
        }
        return false;
    }

    public static CriteriaStringValue parse(String value){
        return new CriteriaStringValue(value);
    }

    @Override
    public int getValue(){
        return 0;
    }
}
