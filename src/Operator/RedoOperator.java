package Operator;
import java.util.ArrayList;

public abstract class RedoOperator extends Operator_Base{
    RedoOperator(Operation op){
        super(op);
    }
    public abstract ArrayList<Operator_Base> getReverse();
}
