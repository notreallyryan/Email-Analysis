package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Task3DWTests {

    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwigblank;
    private static DWInteractionGraph dwigOnlyOne;
    private static DWInteractionGraph dwigsametime;

    @BeforeAll
    public static void setupTests() {
        dwig1 = new DWInteractionGraph("resources/Task3Transactions1.txt");
        dwig2 = new DWInteractionGraph("resources/Task3Transactions2.txt");
        dwigblank = new DWInteractionGraph("resources/blank.txt");
        dwigOnlyOne = new DWInteractionGraph("resources/sent_to_self.txt");
        dwigsametime = new DWInteractionGraph("resources/same_times.txt");
    }
    //BFS TESTS
    @Test
    public void testBFSGraph1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6);
        Assertions.assertEquals(expected, dwig1.BFS(1, 6));
    }

    @Test
    public void testBFSGraph2() {
        List<Integer> expected = Arrays.asList(1, 3, 5, 6, 4, 8, 7, 2, 9, 10);
        Assertions.assertEquals(expected, dwig2.BFS(1, 10));
    }

    @Test
    public void testSenderOrReceiverDoesntExistBFS(){
        Assertions.assertEquals(null, dwig1.BFS(100,2));
        Assertions.assertEquals(null, dwig2.BFS(1,20));
    }
    @Test
    public void testSenderisreceiverBFS(){
        List<Integer> expected1 = Arrays.asList(1);
        List<Integer> expected2 = Arrays.asList(3);
        Assertions.assertEquals(expected1, dwig1.BFS(1,1));
        Assertions.assertEquals(expected2, dwig2.BFS(3,3));
    }
    @Test
    public void blankBFS(){
        Assertions.assertEquals(null, dwigblank.BFS(1,3));
        Assertions.assertEquals(null, dwigblank.BFS(3,4));
    }
    @Test
    public void noConnectionBFS(){
        Assertions.assertEquals(null, dwigOnlyOne.BFS(1,3));
    }

    //DFS TESTS
    @Test
    public void testDFSGraph1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6);
        Assertions.assertEquals(expected, dwig1.DFS(1, 6));
    }
    @Test
    public void testDFSGraph2() {
        List<Integer> expected = Arrays.asList(1, 3, 4, 8, 5, 7, 2, 9, 10);
        Assertions.assertEquals(expected, dwig2.DFS(1, 10));
    }
    @Test
    public void testSenderOrReceiverDoesNotExistDFS(){
        Assertions.assertEquals(null, dwig1.DFS(100,2));
        Assertions.assertEquals(null, dwig2.DFS(1,20));
    }
    @Test
    public void testSenderIsReceiverDFS(){
        List<Integer> expected1 = Arrays.asList(1);
        List<Integer> expected2 = Arrays.asList(3);
        Assertions.assertEquals(expected1, dwig1.DFS(1,1));
        Assertions.assertEquals(expected2, dwig2.DFS(3,3));
    }
    @Test
    public void blankDFS(){
        Assertions.assertEquals(null, dwigblank.DFS(1,3));
        Assertions.assertEquals(null, dwigblank.DFS(3,4));
    }
    @Test
    public void noConnectionDFS(){
        Assertions.assertEquals(null, dwigOnlyOne.DFS(1,3));
    }
}
