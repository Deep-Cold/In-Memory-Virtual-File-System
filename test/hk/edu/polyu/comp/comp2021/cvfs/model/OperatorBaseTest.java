package hk.edu.polyu.comp.comp2021.cvfs.model;
import hk.edu.polyu.comp.comp2021.cvfs.model.Operator.OperatorBase;
import org.junit.*;

import java.io.FileReader;
import java.util.Scanner;

public class OperatorBaseTest {
    private static Scanner sc;

    @Before
    public void init() throws Exception {
        sc = new Scanner(new FileReader("init.in"));
        OperatorBase initOp;
        while(sc.hasNextLine()) {
            initOp = OperatorBase.getOperator(sc.nextLine());
            initOp.runCommand();
        }
    }

    @Test
    public void testCriteria() throws Exception {
        sc = new Scanner(new FileReader("testCriteria.in"));
        OperatorBase testOp;
        while(sc.hasNextLine()) {
            testOp = OperatorBase.getOperator(sc.nextLine());
            testOp.runCommand();
        }
    }

    @Test
    public void testErrors() throws Exception {
        sc = new Scanner(new FileReader("testErrors.in"));
        OperatorBase testOp;
        while(sc.hasNextLine()) {
            try {
                testOp = OperatorBase.getOperator(sc.nextLine());
                testOp.runCommand();
            }catch(Exception e) {}
        }
    }

}