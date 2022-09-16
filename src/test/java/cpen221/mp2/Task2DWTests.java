package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Task2DWTests {

    private static DWInteractionGraph dwig;
    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwigblank;
    private static DWInteractionGraph dwigOnlyOne;
    private static DWInteractionGraph dwigsametime;
    private static DWInteractionGraph dwigonly3;

    @BeforeAll
    public static void setupTests() {
        dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        dwig1 = new DWInteractionGraph(dwig, new int[]{3, 9});
        dwig2 = new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4));
        dwig3 = new DWInteractionGraph("resources/task3-test1.txt");
        dwigblank = new DWInteractionGraph("resources/blank.txt");
        dwigOnlyOne = new DWInteractionGraph("resources/sent_to_self.txt", new int[]{3,3});
        dwigsametime = new DWInteractionGraph("resources/same_times.txt");
        dwigonly3 = new DWInteractionGraph(dwig, Arrays.asList(3));
    }

    // TESTS FOR TEST_REPORT_ACTIVITY_IN_TIME_WINDOW_BASE
    @Test
    public void testReportActivityInTimeWindowBase() {
        int[] expected1 = new int[]{5, 4, 7};
        Assertions.assertArrayEquals(expected1, dwig.ReportActivityInTimeWindow(new int[]{1, 15}));
        int[] expected2 = new int[]{1, 2, 2};
        Assertions.assertArrayEquals(expected2, dwig.ReportActivityInTimeWindow(new int[]{9, 12}));
    }

    @Test
    public void testReportActivityInTimeWindowGraph1() {
        int[] expected1 = new int[]{2, 2, 2};
        Assertions.assertArrayEquals(expected1, dwig1.ReportActivityInTimeWindow(new int[]{2, 7}));
        int[] expected2 = new int[]{4, 2, 4};
        Assertions.assertArrayEquals(expected2, dwig1.ReportActivityInTimeWindow(new int[]{3, 9}));
    }

    @Test
    public void testReportActivityInTimeWindowGraph2() {
        int[] expected1 = new int[]{0, 0, 0};
        Assertions.assertArrayEquals(expected1, dwig2.ReportActivityInTimeWindow(new int[]{3, 6}));
        int[] expected2 = new int[]{1, 1, 1};
        Assertions.assertArrayEquals(expected2, dwig2.ReportActivityInTimeWindow(new int[]{7, 7}));
    }

    @Test
    public void testReportActivityInTimeWindowGraphBlank(){
        int[] expected = new int[]{0,0,0};
        Assertions.assertArrayEquals(expected, dwigblank.ReportActivityInTimeWindow(new int[]{0,100}));
    }

    @Test
    public void testSmallTimeWindow(){
        int[] expected = new int[]{1,1,1};
        Assertions.assertArrayEquals(expected, dwig.ReportActivityInTimeWindow(new int[]{12,12}));
        Assertions.assertArrayEquals(expected, dwig1.ReportActivityInTimeWindow(new int[]{8,8}));
        Assertions.assertArrayEquals(new int[]{3,3,6}, dwigsametime.ReportActivityInTimeWindow(new int[]{0,0}));
    }

    @Test
    public void EncapsulatesAllOrNone(){
        int[] expected1 = new int[]{5,4,8};
        int[] expected2 = new int[]{0,0,0};
        Assertions.assertArrayEquals(expected1, dwig.ReportActivityInTimeWindow(new int[]{0,100}));
        Assertions.assertArrayEquals(expected2, dwig.ReportActivityInTimeWindow(new int[]{99,100}));
    }

    @Test
    public void only3(){
        int[] expected = new int[]{1,1,2};
        Assertions.assertArrayEquals(expected,dwigonly3.ReportActivityInTimeWindow(new int[]{0,14}));
    }



    //TEST FOR REPORT_ON_USER_BASE
    @Test
    public void testReportOnUserBase() {
        Assertions.assertArrayEquals(new int[]{2, 3, 3}, dwig.ReportOnUser(0));
        Assertions.assertArrayEquals(new int[]{2, 1, 3}, dwig.ReportOnUser(8));
    }

    @Test
    public void testReportOnUserGraph1() {
        Assertions.assertArrayEquals(new int[]{1, 3, 3}, dwig1.ReportOnUser(0));
        Assertions.assertArrayEquals(new int[]{1, 0, 1}, dwig1.ReportOnUser(1));
    }

    @Test
    public void testReportOnUserGraph2() {
        Assertions.assertArrayEquals(new int[]{0, 2, 1}, dwig2.ReportOnUser(3));
        Assertions.assertArrayEquals(new int[]{0, 0, 0}, dwig2.ReportOnUser(6));
    }

    @Test
    public void testNoSuchUser(){
        int[] expected = new int[]{0,0,0};
        Assertions.assertArrayEquals(expected, dwig1.ReportOnUser(100));
        Assertions.assertArrayEquals(expected, dwigblank.ReportOnUser(1));
    }

    @Test
    public void OneOfEach(){
        int[] expected = new int[]{1,1,1};
        Assertions.assertArrayEquals(expected, dwigOnlyOne.ReportOnUser(3));
    }

    @Test
    public void InteractedWithAll(){
        int[] expected = new int[]{2,2,2};
        Assertions.assertArrayEquals(expected, dwigsametime.ReportOnUser(1));
    }



    //TEST FOR Nth MOST ACTIVE USER
    @Test
    public void testNthMostActiveUserBase() {
        Assertions.assertEquals(0, dwig.NthMostActiveUser(1, SendOrReceive.SEND));
        Assertions.assertEquals(0, dwig.NthMostActiveUser(1, SendOrReceive.RECEIVE));
    }

    @Test
    public void testNthMostActiveUserGraph1() {
        Assertions.assertEquals(1, dwig1.NthMostActiveUser(2, SendOrReceive.SEND));
        Assertions.assertEquals(8, dwig1.NthMostActiveUser(2, SendOrReceive.RECEIVE));
    }

    @Test
    public void testNthMostActiveUserGraph2() {
        Assertions.assertEquals(4, dwig2.NthMostActiveUser(2, SendOrReceive.SEND));
        Assertions.assertEquals(-1, dwig2.NthMostActiveUser(3, SendOrReceive.RECEIVE));
    }

    @Test
    public void testNthMostActiveUserGraph3() {
        Assertions.assertEquals(2, dwig3.NthMostActiveUser(1, SendOrReceive.SEND));
        Assertions.assertEquals(77, dwig3.NthMostActiveUser(1, SendOrReceive.RECEIVE));
    }

    @Test
    public void testNthmostactive_outofbounds(){
        Assertions.assertEquals(-1,dwig3.NthMostActiveUser(100,SendOrReceive.SEND));
    }
}
