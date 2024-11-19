package hk.edu.polyu.comp.comp2021.cvfs.model.Criteria;

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

    /**
     * @param value The value to be checked
     * @return If the CriteriaValue contains value
     */
    public boolean contains(String value){
        return this.value.contains(value);
    }

    /**
     * @param value The value to be checked
     * @return If the CriteriaValue equals value
     */
    public boolean equals(CriteriaValue value){
            return this.value.equals(value.toString());
    }

    /**
     * @param value The value of the CriteriaValue
     * @return The boxed CriteriaValue
     */
    public static CriteriaStringValue parse(String value){
        return new CriteriaStringValue(value);
    }

}
