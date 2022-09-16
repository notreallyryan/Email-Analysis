package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Task2UDWTests {

    //for base tests
    private static UDWInteractionGraph testGraphBase;

    //for further tests
    private static UDWInteractionGraph testGraph0;
    private static UDWInteractionGraph testGraph1;
    private static UDWInteractionGraph testGraph2;
    private static UDWInteractionGraph blankgraph;
    private static UDWInteractionGraph sendtoself;
    private static UDWInteractionGraph testGraphBig1;
    private static UDWInteractionGraph testGraphBig2;
    private static UDWInteractionGraph testGraphBig3;
    private static UDWInteractionGraph testGraphBig4;
    private static UDWInteractionGraph testGraph3;

    @BeforeAll
    public static void setupTests() {
        testGraphBase = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        testGraph0 = new UDWInteractionGraph("resources/email-Eu-core-temporal.txt");
        testGraph1 = new UDWInteractionGraph(testGraph0, new int[]{40746039, 40746250});
        testGraph2 = new UDWInteractionGraph(testGraph1, new int[]{40746206, 40746207});
        blankgraph = new UDWInteractionGraph("resources/blank.txt");
        sendtoself = new UDWInteractionGraph("resources/sent_to_self.txt");
        testGraphBig1 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept1.txt");
        testGraphBig2 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept2.txt");
        testGraphBig3 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept3.txt");
        testGraphBig4 = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept4.txt");
        testGraph3 = new UDWInteractionGraph("resources/same_times.txt");
    }



    /* ------- Base Tests ------- */

    @Test
    public void testReportActivityInTimeWindow() {
        int[] result = testGraphBase.ReportActivityInTimeWindow(new int[]{0, 1});
        Assertions.assertEquals(3, result[0]);
        Assertions.assertEquals(2, result[1]);
    }

    @Test
    public void testReportOnUser() {
        int[] result = testGraphBase.ReportOnUser(0);
        Assertions.assertEquals(6, result[0]);
        Assertions.assertEquals(3, result[1]);
    }

    @Test
    public void testReportOnUser1() {
        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, userFilter);
        int[] result = t.ReportOnUser(0);
        Assertions.assertEquals(6, result[0]);
        Assertions.assertEquals(3, result[1]);
    }

    @Test
    public void testReportOnUser2() {
        int[] result = testGraphBase.ReportOnUser(4);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testNthActiveUser() {
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, new int[]{0, 2});
        Assertions.assertEquals(0, t.NthMostActiveUser(1));
    }

    @Test
    public void testNthActiveUser1() {
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, new int[]{0, 2});
        Assertions.assertEquals(1, t.NthMostActiveUser(2));
    }



    /* ------- Further Tests ------- */

    @Test
    public void testReportActivityInTimeWindow1() {
        int[] result = testGraph0.ReportActivityInTimeWindow(new int[]{45396764, 45405138});
        Assertions.assertEquals(13, result[0]);
        Assertions.assertEquals(10, result[1]);
    }

    @Test
    public void testReportActivityInTimeWindow2() {
        int[] result = testGraph0.ReportActivityInTimeWindow(new int[]{45405139, 45405140});
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testReportActivityInTimeWindow_blank(){
        int[] results = blankgraph.ReportActivityInTimeWindow(new int[]{0,10000});
        Assertions.assertEquals(0,results[0]);
        Assertions.assertEquals(0,results[1]);
    }

    @Test
    public void testReportActivityInTimeWindow_toselfonly(){
        int[] results = sendtoself.ReportActivityInTimeWindow(new int[]{0,10});
        Assertions.assertEquals(3, results[0]);
        Assertions.assertEquals(3, results[1]);
    }

    @Test
    public void testReportActivityInTimeWindow_instantonly(){
        int[] results = sendtoself.ReportActivityInTimeWindow(new int[]{1,1});
        Assertions.assertEquals(1,results[0]);
        Assertions.assertEquals(1,results[1]);
    }

    @Test
    public void testReportActivityInTimeWindow_LargeGraphs() {
        int[] results0 = testGraph0.ReportActivityInTimeWindow(new int[]{0,100000000});
        int[] results1 = testGraphBig1.ReportActivityInTimeWindow(new int[]{0,100000000});
        int[] results2 = testGraphBig2.ReportActivityInTimeWindow(new int[]{0,100000000});
        int[] results3 = testGraphBig3.ReportActivityInTimeWindow(new int[]{0,100000000});
        int[] results4 = testGraphBig4.ReportActivityInTimeWindow(new int[]{0,100000000});

        Assertions.assertEquals(986, results0[0]);
        Assertions.assertEquals(309, results1[0]);
        Assertions.assertEquals(162, results2[0]);
        Assertions.assertEquals(89, results3[0]);
        Assertions.assertEquals(142, results4[0]);
        Assertions.assertEquals(332334, results0[1]);
        Assertions.assertEquals(61046, results1[1]);
        Assertions.assertEquals(46772, results2[1]);
        Assertions.assertEquals(12216, results3[1]);
        Assertions.assertEquals(48141, results4[1]);
    }

    @Test
    public void testSameSenderReceiverTime() {
        int[] result = testGraph3.ReportActivityInTimeWindow(new int[]{0,1});
        int[] result1 = testGraph3.ReportOnUser(1);

        Assertions.assertEquals(6, result[1]);
        Assertions.assertEquals(4, result1[0]);
    }

    @Test
    public void testReportOnUser3() {
        int[] result = testGraph1.ReportOnUser(415);
        Assertions.assertEquals(12, result[0]);
        Assertions.assertEquals(5, result[1]);
    }

    @Test
    public void testReportOnUser4() {
        int[] result = testGraph1.ReportOnUser(558);
        Assertions.assertEquals(1, result[0]);
        Assertions.assertEquals(1, result[1]);
    }

    @Test
    public void testReportOnUser5() {
        int[] result = testGraph1.ReportOnUser(416);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testReportOnNonexistantUser(){
        int[] result = blankgraph.ReportOnUser(10000);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testNthActiveUser2() {
        Assertions.assertEquals(415, testGraph1.NthMostActiveUser(1));
    }

    @Test
    public void testNthActiveUser3() {
        Assertions.assertEquals(207, testGraph2.NthMostActiveUser(1));
        Assertions.assertEquals(558, testGraph2.NthMostActiveUser(2));  //buggy
        Assertions.assertEquals(-1, testGraph2.NthMostActiveUser(3));
    }

    @Test
    public void testNthActiveUser_tie(){
        Assertions.assertEquals(1, sendtoself.NthMostActiveUser(1));
        Assertions.assertEquals(2, sendtoself.NthMostActiveUser(2));
        Assertions.assertEquals(3, sendtoself.NthMostActiveUser(3));
    }
}
