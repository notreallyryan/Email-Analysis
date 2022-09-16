package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Task4DWTests {

    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwig4;
    private static DWInteractionGraph dwig5;

    @BeforeAll
    public static void setupTests() {
        dwig1 = new DWInteractionGraph("resources/Task4Transactions1.txt");
        dwig2 = new DWInteractionGraph("resources/Task4Transactions2.txt");
        dwig3 = new DWInteractionGraph("resources/Task4Transactions3.txt");
        dwig4 = new DWInteractionGraph("resources/email-Eu-core-temporal.txt");
        dwig5 = new DWInteractionGraph("resources/DW-task4-test1.txt");
    }

    @Test
    public void testMaxedBreachedUserCount1() {
        // Attacking user 7 any time in the window [0,120] will pollute 8 users in a 2 hour window.
        Assertions.assertEquals(8, dwig1.MaxBreachedUserCount(2));
    }

    @Test
    public void testMaxedBreachedUserCount2() {
        // Attacking user 3 at t=0, or attacking user 5 any time in the window [0,60] will pollute
        // 10 users in a 4 hour window.
        Assertions.assertEquals(10, dwig2.MaxBreachedUserCount(4));
    }

    @Test
    public void testMaxedBreachedUserCount3() {
        // Attacking user 4 at t=3600 will lead to users 4, 5, 6, 3, and 1 (5 users) to be polluted
        // in a 6-hour-long window after the attack starts.
        Assertions.assertEquals(5, dwig3.MaxBreachedUserCount(6));
    }

    @Test
    public void testTiming() {
        int x = dwig4.MaxBreachedUserCount(100);
        System.out.println(x); //503
    }

    @Test
    public void testInstantFirewall() {
        Assertions.assertEquals(1, dwig1.MaxBreachedUserCount(0));
        Assertions.assertEquals(1, dwig2.MaxBreachedUserCount(0));
        Assertions.assertEquals(1, dwig3.MaxBreachedUserCount(0));
        Assertions.assertEquals(1, dwig4.MaxBreachedUserCount(0));
        Assertions.assertEquals(1, dwig5.MaxBreachedUserCount(0));
    }

    @Test
    public void testEveryUserInfected() {
        Assertions.assertEquals(11, dwig5.MaxBreachedUserCount(6));
    }

    @Test
    public void testSomeUsersInfected() {
        Assertions.assertEquals(10, dwig5.MaxBreachedUserCount(4));
    }

    @Test
    public void testFirewallUpAtSendTime() {
        Assertions.assertEquals(3, dwig5.MaxBreachedUserCount(1));
        Assertions.assertEquals(6, dwig5.MaxBreachedUserCount(2));
        Assertions.assertEquals(9, dwig5.MaxBreachedUserCount(3));
    }
}
