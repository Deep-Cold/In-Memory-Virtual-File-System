package Operator;

/**
 * CriteriaOperator
 */
public abstract class CriteriaOperator extends Operator_Base {
    private boolean isDelete;

    /**
     * @param op The corresponding operator
     */
    public CriteriaOperator(Operation op) {
        super(op);
        isDelete = false;
    }

    /**
     * @param isDelete New state if the Criteria is deleted
     */
    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * @return If the Criteria is deleted
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * @return The name of the Criteria
     */
    public abstract String getName();
    public abstract String toString();
}
