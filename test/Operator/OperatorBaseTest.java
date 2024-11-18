package Operator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class OperatorBaseTest {
    @Before
    public void init() throws Exception {
        Operator_Base initOp = Operator_Base.getOperator("newDisk 500");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("newDir dir1");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("newDir dir2");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("newDoc txtfile txt \"txt content\"");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("changeDir dir1");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("newDoc htmlfile html \"html content\"");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("changeDir ..");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("changeDir dir2");
        initOp.runCommand();
        initOp = Operator_Base.getOperator("newDoc javafile java \"java content\"");
        initOp.runCommand();
    }
    @Test
    public void testCriteria() throws IOException {
        Operator_Base testOp = Operator_Base.getOperator("newSimpleCri AA name contains \"txt\"");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newSimpleCri AB type equals \"java\"");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newSimpleCri AC type equals \"html\"");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newSimpleCri AD type equals \"txt\"");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation NA AA");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation NB AB");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation NC AC");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation ND AD");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newBinaryCri BA AA || AB");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newBinaryCri BB BA && AC");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newBinaryCri BC AA && AC");
        assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
    }

}