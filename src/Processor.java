import Operator.*;

import java.util.Scanner;
import java.util.Stack;

public class Processor {
    private static Scanner input = new Scanner(System.in);

    Stack<Operator_Base> stk = new Stack<Operator_Base>();

    public static void runNextStep() {
        String command = input.nextLine();
        Operation op = Operation.fromString(command.split(" ")[0]);
        if(op == null) throw new RuntimeException("Invalid operation");
        switch(op) {
            case
        }

    }
    public static void mainLoop() {

        while(true)
        {
            try {
                runNextStep();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
