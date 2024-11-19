package hk.edu.polyu.comp.comp2021.cvfs.model.Criteria;

/**
 * The Int Value of the CriteriaValue
 */
public class CriteriaIntValue implements CriteriaValue {
    private final int value;

    /**
     * @param value The value of the CriteriaValue
     */
    public CriteriaIntValue(int value){
        this.value = value;
    }

    /**
     * @return The value of the CriteriaValue
     */
    public int getValue(){
        return value;
    }
    
    public String toString(){return String.valueOf(value);}

    /**
     * @param value The value of the CriteriaValue
     * @return The boxed CriteriaValue
     */
    public static CriteriaIntValue parse(String value){
        return new CriteriaIntValue(Integer.parseInt(value));
    }

    /**
     * @param value The value of the CriteriaValue
     * @return The boxed CriteriaValue
     */
    public static CriteriaIntValue parse(int value){
        return new CriteriaIntValue(value);
    }

}
