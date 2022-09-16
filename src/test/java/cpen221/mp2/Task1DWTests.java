package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Task1DWTests {

    private static DWInteractionGraph dwig;
    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwigblank;
    private static DWInteractionGraph dwigOnlyOne;
    private static DWInteractionGraph dwig4;

    @BeforeAll
    public static void setupTests() {
        dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        dwig1 = new DWInteractionGraph(dwig, new int[]{3, 9});
        dwig2 = new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4));
        dwigblank = new DWInteractionGraph("resources/blank.txt");
        dwigOnlyOne = new DWInteractionGraph("resources/sent_to_self.txt", new int[]{0,1});
        dwig4 = new DWInteractionGraph("resources/Task1-2Transactions.txt", new int[]{2,13});
    }
    // TESTS FOR GETUSERID
    @Test
    public void test1GetUserIDsBase() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph1() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 4, 8));
        Assertions.assertEquals(expected, dwig1.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph2() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig2.getUserIDs());
    }

    @Test
    public void blanktestGetUserIDs(){
        Set<Integer> expected = new HashSet<>();
        Assertions.assertEquals(expected, dwigblank.getUserIDs());
    }

    @Test
    public void OnlyoneUserGetUserIDs(){
        Set<Integer> expected = new HashSet<>(Arrays.asList(1));
        Assertions.assertEquals(expected, dwigOnlyOne.getUserIDs());
    }

    //TESTS FOR GETEMAILCOUNT
    @Test
    public void test1GetEmailCountBase() {
        Assertions.assertEquals(2, dwig.getEmailCount(2, 3));
        Assertions.assertEquals(0, dwig.getEmailCount(8, 4));
    }

    @Test
    public void test1GetEmailCountGraph1() {
        Assertions.assertEquals(1, dwig1.getEmailCount(1, 0));
        Assertions.assertEquals(1, dwig1.getEmailCount(8, 0));

    }

    @Test
    public void test1GetEmailCountGraph2() {
        Assertions.assertEquals(1, dwig2.getEmailCount(4, 8));
        Assertions.assertEquals(2, dwig2.getEmailCount(2, 3));
    }

    @Test
    public void noSuchUser(){
        Assertions.assertEquals(0,dwig.getEmailCount(100,2));
        Assertions.assertEquals(0,dwig.getEmailCount(2,100));
    }

    @Test
    public void SentOnlyToSelf(){
        Assertions.assertEquals(0,dwigOnlyOne.getEmailCount(1,2));
        Assertions.assertEquals(1,dwigOnlyOne.getEmailCount(1,1));
    }

    @Test
    public void forcoveragepurposes(){
        Assertions.assertEquals(0,dwig4.getEmailCount(0,1));
    }

}
