package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * The String Value of the CriteriaValue
 */
public class CriteriaStringValue implements CriteriaValue {
    private final String value;

    /**
     * @param value The value of the CriteriaValue
     */
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
            return this.value.equals(((CriteriaStringValue)value).getStringValue());
        }
        return false;
    }

    /**
     * @param value The value of the CriteriaValue
     * @return The boxed CriteriaValue
     */
    public static CriteriaStringValue parse(String value){
        return new CriteriaStringValue(value);
    }

    /**
     * @return The boxing value
     */
    public String getStringValue(){
        return value;
    }
    @Override
    public int getValue(){
        return 0;
    }
}
