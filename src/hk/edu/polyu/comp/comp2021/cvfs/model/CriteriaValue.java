package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * The CriteriaValue of the comparation
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
