package Operator;

public abstract class CriteriaOperator extends Operator_Base {
    private boolean isDelete;
    public CriteriaOperator(Operation op) {
        super(op);
        isDelete = false;
    }
    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
    public boolean isDelete() {
        return isDelete;
    }
    public abstract String getName();
    public abstract String toString();
}
