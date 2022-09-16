package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Task1UDWTests {

    //for base tests
    private static UDWInteractionGraph testGraphBase;
    private static UDWInteractionGraph testGraph1;
    private static UDWInteractionGraph testGraph2;

    //for further tests
    private static UDWInteractionGraph testGraph3;
    private static UDWInteractionGraph testGraph4;
    private static UDWInteractionGraph testGraph5;
    private static UDWInteractionGraph testGraph6;
    private static UDWInteractionGraph testGraph7;
    private static UDWInteractionGraph blankGraph;
    private static UDWInteractionGraph onlyone;

    @BeforeAll
    public static void setupTests() {
        onlyone = new UDWInteractionGraph("resources/sent_to_self.txt", new int[]{0,1});
        testGraphBase = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        testGraph1 = new UDWInteractionGraph(testGraphBase, new int[]{0, 9});
        testGraph2 = new UDWInteractionGraph(testGraphBase, new int[]{10, 11});
        testGraph3 = new UDWInteractionGraph(testGraphBase, new int[]{12, 13});
        testGraph4 = new UDWInteractionGraph("resources/email-Eu-core-temporal.txt");
        testGraph5 = new UDWInteractionGraph(testGraph4, new int[]{32379511, 32381166});
        testGraph6 = new UDWInteractionGraph(testGraph4, new int[]{0, 3000});
        testGraph7 = new UDWInteractionGraph(testGraph5, List.of(915));
        blankGraph = new UDWInteractionGraph("resources/blank.txt");

    }



    /* ------- Base Tests ------- */

    @Test
    public void testGetUserIds() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), testGraphBase.getUserIDs());
    }

    @Test
    public void testGetUserIds1() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), testGraph2.getUserIDs());
    }

    @Test
    public void testGetEmailCount() {
        Assertions.assertEquals(2, testGraphBase.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraphBase.getEmailCount(0, 1));
    }

    @Test
    public void testGetEmailCount1() {
        Assertions.assertEquals(2, testGraph1.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraph1.getEmailCount(0, 3));
    }

    @Test
    public void testGetEmailCount2() {
        Assertions.assertEquals(0, testGraph2.getEmailCount(1, 0));
        Assertions.assertEquals(1, testGraph2.getEmailCount(1, 3));
    }

    @Test
    public void testUserConstructor() {
        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, userFilter);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), t.getUserIDs());
        Assertions.assertEquals(2, t.getEmailCount(0, 1));
        Assertions.assertEquals(2, t.getEmailCount(0, 3));
    }

    @Test
    public void testConstructionFromDW() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testConstructionFromDW1() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }



    /* ------- Further Tests ------- */

    @Test
    public void testGetUserIDs2() {
        Assertions.assertEquals(new HashSet<>(List.of()), testGraph3.getUserIDs());
        Assertions.assertEquals(new HashSet<>(List.of(582, 364, 168, 472)), testGraph6.getUserIDs());
    }

    @Test
    public void testGetUserIDs3() {
        HashSet<Integer> graph5Users = new HashSet<>(List.of(
                61, 718, 696, 607, 126, 782, 790, 168, 915, 61, 502, 178, 949, 200, 923, 857, 883, 160, 530, 250, 646,
                551, 854, 542, 823, 369, 770, 722, 113, 535, 513, 485, 444
        ));

        Assertions.assertEquals(graph5Users, testGraph5.getUserIDs());
    }

    @Test
    public void testGetUserIDs4() {
        Assertions.assertEquals(new HashSet<>(
                List.of(915, 61, 502, 178, 200, 923, 857, 883, 160, 530)),
                testGraph7.getUserIDs());
    }

    @Test
    public void testGetUserIDsBlank(){
        Assertions.assertEquals(new HashSet<>(), blankGraph.getUserIDs());
    }

    @Test
    public void testOnlyOneUserID(){
        Assertions.assertEquals(new HashSet<>(List.of(1)), onlyone.getUserIDs());
    }

    //tests for emailcount
    @Test
    public void testGetEmailCount3() {
        Assertions.assertEquals(2, testGraph5.getEmailCount(61, 718));
        Assertions.assertEquals(1, testGraph5.getEmailCount(250, 444));
    }

    @Test
    public void testGetEmailCount4() {
        Assertions.assertEquals(1, testGraph6.getEmailCount(168, 472));
        Assertions.assertEquals(0, testGraph6.getEmailCount(0, 9));
        Assertions.assertEquals(0, testGraph6.getEmailCount(749104583, 73711017));
    }

    @Test
    public void testGetEmailCount5() {
        Assertions.assertEquals(1, testGraph7.getEmailCount(915, 883));
        Assertions.assertEquals(0, testGraph7.getEmailCount(915, 62));
    }

    @Test
    public void testgetEmailsblank(){
        Assertions.assertEquals(0, blankGraph.getEmailCount(100,200));
    }
    @Test
    public void testgetEmailCount_onlytoself(){
        Assertions.assertEquals(1, onlyone.getEmailCount(1,1));
    }
    @Test
    public void testgetEmailCount_noEmailsSent(){
        Assertions.assertEquals(0,testGraph5.getEmailCount(915,100000));
    }

    @Test
    public void testUndirectedSenderReceiverPairDefaultConstructor() {
        UndirectedSenderReceiverPair pair = new UndirectedSenderReceiverPair();

        Assertions.assertEquals(0, pair.getUser1());
        Assertions.assertEquals(0, pair.getUser2());
    }

}
