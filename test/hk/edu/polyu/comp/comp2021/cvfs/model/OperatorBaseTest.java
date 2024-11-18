package hk.edu.polyu.comp.comp2021.cvfs.model;
import org.junit.*;

import java.io.IOException;

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
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newSimpleCri AB type equals \"java\"");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newSimpleCri AC type equals \"html\"");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newSimpleCri AD type equals \"txt\"");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation NA AA");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation NB AB");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation NC AC");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newNegation ND AD");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newBinaryCri BA AA || AB");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newBinaryCri BB BA && AC");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
        testOp = Operator_Base.getOperator("newBinaryCri BC AA && AC");
        Assert.assertTrue(testOp instanceof CriteriaOperator);
        testOp.runCommand();
    }

}