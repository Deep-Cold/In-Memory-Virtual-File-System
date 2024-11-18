package hk.edu.polyu.comp.comp2021.cvfs.model;
import org.junit.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class OperatorBaseTest {
    private static Scanner sc;

    @Before
    public void init() throws Exception {
        sc = new Scanner(new FileReader("init.in"));
        Operator_Base initOp;
        while(sc.hasNextLine()) {
            initOp = Operator_Base.getOperator(sc.nextLine());
            initOp.runCommand();
        }
    }

    @Test
    public void testCriteria() throws Exception {
        sc = new Scanner(new FileReader("testCriteria.in"));
        Operator_Base testOp;
        while(sc.hasNextLine()) {
            testOp = Operator_Base.getOperator(sc.nextLine());
            testOp.runCommand();
        }
    }

}