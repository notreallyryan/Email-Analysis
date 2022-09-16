package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class Task3UDWTests {

    //for base tests
    private static UDWInteractionGraph testGraph0;
    private static UDWInteractionGraph testGraph1;
    private static UDWInteractionGraph testGraph2;
    private static UDWInteractionGraph testGraph3;
    private static UDWInteractionGraph blankgraph;

    //for further tests
    private static UDWInteractionGraph testGraph4;

    @BeforeAll
    public static void setupTests() {
        testGraph0 = new UDWInteractionGraph("resources/Task3-components-test.txt");
        testGraph1 = new UDWInteractionGraph("resources/Task3-components-test1.txt");
        testGraph2 = new UDWInteractionGraph("resources/Task3-components-test2.txt");
        testGraph3 = new UDWInteractionGraph(testGraph2, List.of(2));
        testGraph4 = new UDWInteractionGraph("resources/task3-test1.txt");
        blankgraph = new UDWInteractionGraph(testGraph0, new int[]{6,10});
    }



    /* ------- Base Tests ------- */

    @Test
    public void testNumComponent() {
        Assertions.assertEquals(1, testGraph0.NumberOfComponents());
    }

    @Test
    public void testNumComponent1() {
        Assertions.assertEquals(2, testGraph1.NumberOfComponents());
    }

    @Test
    public void testPathExists() {
        Assertions.assertTrue(testGraph0.PathExists(1, 2));
        Assertions.assertTrue(testGraph0.PathExists(1, 3));
        Assertions.assertTrue(testGraph0.PathExists(1, 4));
        Assertions.assertTrue(testGraph0.PathExists(2, 3));
        Assertions.assertTrue(testGraph0.PathExists(2, 4));
        Assertions.assertTrue(testGraph0.PathExists(3, 4));
    }

    @Test
    public void testPathExists1() {
        Assertions.assertTrue(testGraph1.PathExists(1, 2));
        Assertions.assertTrue(testGraph1.PathExists(3, 4));
        Assertions.assertFalse(testGraph1.PathExists(1, 4));
        Assertions.assertFalse(testGraph1.PathExists(2, 3));
    }

    @Test
    public void testSingleUser() {
        Assertions.assertEquals(2, testGraph2.getEmailCount(1, 1));
        Assertions.assertEquals(1, testGraph2.NumberOfComponents());
        Assertions.assertTrue(testGraph2.PathExists(1, 1));
    }

    @Test
    public void testNoUser() {
        Assertions.assertEquals(0, testGraph3.NumberOfComponents());
    }



    /* ------- Further Tests ------- */

    @Test
    public void testNumComponent2() {
        Assertions.assertEquals(3, testGraph4.NumberOfComponents());
    }

    @Test
    public void testNumComponents_blank() {
        Assertions.assertEquals(0, blankgraph.NumberOfComponents());
    }

    @Test
    public void testPathExists2() {
        Assertions.assertTrue(testGraph4.PathExists(21, 5));
        Assertions.assertTrue(testGraph4.PathExists(5, 21));
        Assertions.assertTrue(testGraph4.PathExists(5, 5));
        Assertions.assertTrue(testGraph4.PathExists(38, 5));
        Assertions.assertTrue(testGraph4.PathExists(11, 5));
        Assertions.assertTrue(testGraph4.PathExists(21, 38));
    }

    @Test
    public void testPathExists3() {
        Assertions.assertTrue(testGraph4.PathExists(69, 69));
        Assertions.assertTrue(testGraph4.PathExists(2, 77));
        Assertions.assertFalse(testGraph4.PathExists(5, 2));
        Assertions.assertFalse(testGraph4.PathExists(11, 77));
        Assertions.assertFalse(testGraph4.PathExists(77, 69));
        Assertions.assertFalse(testGraph4.PathExists(38, 69));
    }

    @Test
    public void testPathExists_noSuchUser() {
        Assertions.assertFalse(blankgraph.PathExists(69,69));
    }

}
