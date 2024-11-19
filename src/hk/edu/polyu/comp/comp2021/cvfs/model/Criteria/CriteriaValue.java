package hk.edu.polyu.comp.comp2021.cvfs.model.Criteria;

/**
 * The CriteriaValue of the comparing
 */
public interface CriteriaValue {
    String toString();

    /**
     * @return The integer value of Criteria
     */
    int getValue();

    /**
     * @param value The value to be checked
     * @return If the CriteriaValue contains value
     */
    boolean contains(String value);
}