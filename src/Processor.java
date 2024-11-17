import Operator.*;
import Disk.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Processor {
    private static final Scanner input = new Scanner(System.in);

    public static void runNextStep() throws IOException {
        String command = input.nextLine();
        Operator_Base curOp = Operator_Base.getOperator(command);
        if(curOp instanceof RedoOperator) {
            ArrayList<Operator_Base> revOp = ((RedoOperator) curOp).getReverse();
            curOp.runCommand();
            Disk.getDisk().pushUndoStack(revOp, true);
        }
        else if(curOp instanceof CriteriaOperator) {
            curOp.runCommand();
            ArrayList<Operator_Base> Op = new ArrayList<>();
            Op.add(curOp);
            Disk.getDisk().pushUndoStack(Op, true);
        }
        else {
            curOp.runCommand();
        }
    }
    public static void mainLoop() {
        while(true)
        {
            try {
                runNextStep();
                System.out.println("Run successful.");
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
    }
    public static void main(String[] args) {
        mainLoop();
    }
}
