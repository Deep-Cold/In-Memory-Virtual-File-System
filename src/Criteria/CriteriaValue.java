package Criteria;

public interface CriteriaValue {
    public abstract String toString();
    public abstract int getValue();
    public abstract boolean contains(String value);
}
