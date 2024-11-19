package hk.edu.polyu.comp.comp2021.cvfs.model;
import hk.edu.polyu.comp.comp2021.cvfs.model.Disk.Disk;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.CriteriaOperator;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.OperatorBase;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.RedoOperator;
import org.junit.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class OperatorBaseTest {
    private static Scanner sc;

    @Before
    public void init() throws Exception {
        sc = new Scanner(new FileReader("./test/init.in"));
        OperatorBase curOp;
        while(sc.hasNextLine()) {
            curOp = OperatorBase.getOperator(sc.nextLine());
            if (!(curOp instanceof RedoOperator)) {
                if(curOp instanceof CriteriaOperator) {
                    curOp.runCommand();
                    ArrayList<OperatorBase> Op = new ArrayList<>();
                    Op.add(curOp);
                    Disk.getDisk().pushUndoStack(Op, true);
                }
                else {
                    curOp.runCommand();
                }
            } else {
                ArrayList<OperatorBase> revOp = ((RedoOperator) curOp).getReverse();
                curOp.runCommand();
                Disk.getDisk().pushUndoStack(revOp, true);
            }
        }
    }

    @Test
    public void testCriteria() throws Exception {
        sc = new Scanner(new FileReader("./test/testCriteria.in"));
        OperatorBase testOp;
        while(sc.hasNextLine()) {
            testOp = OperatorBase.getOperator(sc.nextLine());
            if (!(testOp instanceof RedoOperator)) {
                if(testOp instanceof CriteriaOperator) {
                    testOp.runCommand();
                    ArrayList<OperatorBase> Op = new ArrayList<>();
                    Op.add(testOp);
                    Disk.getDisk().pushUndoStack(Op, true);
                }
                else {
                    testOp.runCommand();
                }
            } else {
                ArrayList<OperatorBase> revOp = ((RedoOperator) testOp).getReverse();
                testOp.runCommand();
                Disk.getDisk().pushUndoStack(revOp, true);
            }
        }
    }

    @Test
    public void testErrors() throws Exception {
        sc = new Scanner(new FileReader("./test/testErrors.in"));
        OperatorBase testOp;
        while(sc.hasNextLine()) {
            try {
                testOp = OperatorBase.getOperator(sc.nextLine());
                if (!(testOp instanceof RedoOperator)) {
                    if(testOp instanceof CriteriaOperator) {
                        testOp.runCommand();
                        ArrayList<OperatorBase> Op = new ArrayList<>();
                        Op.add(testOp);
                        Disk.getDisk().pushUndoStack(Op, true);
                    }
                    else {
                        testOp.runCommand();
                    }
                } else {
                    ArrayList<OperatorBase> revOp = ((RedoOperator) testOp).getReverse();
                    testOp.runCommand();
                    Disk.getDisk().pushUndoStack(revOp, true);
                }
            }catch(Exception e) {}
        }
    }
    @Test
    public void testUtils() throws Exception {
        sc = new Scanner(new FileReader("./test/testUtils.in"));
        OperatorBase testOp;
        while(sc.hasNextLine()) {
            testOp = OperatorBase.getOperator(sc.nextLine());
            if (!(testOp instanceof RedoOperator)) {
                if(testOp instanceof CriteriaOperator) {
                    testOp.runCommand();
                    ArrayList<OperatorBase> Op = new ArrayList<>();
                    Op.add(testOp);
                    Disk.getDisk().pushUndoStack(Op, true);
                }
                else {
                    testOp.runCommand();
                }
            } else {
                ArrayList<OperatorBase> revOp = ((RedoOperator) testOp).getReverse();
                testOp.runCommand();
                Disk.getDisk().pushUndoStack(revOp, true);
            }
        }
    }
    @AfterClass
    public static void testQuit() throws Exception {
        sc = new Scanner(new FileReader("./test/testQuit.in"));
        OperatorBase testOp;
        while(sc.hasNextLine()) {
            testOp = OperatorBase.getOperator(sc.nextLine());
            while(true) {
                testOp.runCommand();
            }
        }
    }
}