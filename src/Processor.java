import hk.edu.polyu.comp.comp2021.cvfs.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main processor the program
 */
public class Processor {
    private static final Scanner input = new Scanner(System.in);

    /**
     * @throws IOException If write or read file failed
     */
    public static void runNextStep() throws IOException {
        String command = input.nextLine();
        Operator_Base curOp = Operator_Base.getOperator(command);
        if (!(curOp instanceof RedoOperator)) {
            if(curOp instanceof CriteriaOperator) {
                curOp.runCommand();
                ArrayList<Operator_Base> Op = new ArrayList<>();
                Op.add(curOp);
                Disk.getDisk().pushUndoStack(Op, true);
            }
            else {
                curOp.runCommand();
            }
        } else {
            ArrayList<Operator_Base> revOp = ((RedoOperator) curOp).getReverse();
            curOp.runCommand();
            Disk.getDisk().pushUndoStack(revOp, true);
        }
    }

    /**
     * The mainloop the program
     */
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

    /**
     * @param args The passing argument to the main function
     */
    public static void main(String[] args) {
        mainLoop();
    }
}
