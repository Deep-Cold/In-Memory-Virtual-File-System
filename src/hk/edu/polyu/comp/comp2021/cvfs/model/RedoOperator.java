package hk.edu.polyu.comp.comp2021.cvfs.model;
import java.util.ArrayList;

/**
 * The operator support undo
 */
public abstract class RedoOperator extends Operator_Base{
    /**
     * @param op The corresponding operation name
     */
    RedoOperator(Operation op){
        super(op);
    }

    /**
     * @return The list of the reverse operation
     */
    public abstract ArrayList<Operator_Base> getReverse();
}