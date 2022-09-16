package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TestSuite {
    private static DWInteractionGraph dwig;
    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwig4;
    private static ArrayList<Integer> senders;
    private static ArrayList<Integer> receivers;
    private static ArrayList<String[]> EmailData;


    @BeforeAll
    public static void setupTests() throws IOException {
        dwig = new DWInteractionGraph("resources/email-Eu-core-temporal.txt");
        dwig1 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept1.txt");
        dwig2 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept2.txt");
        dwig3 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept3.txt");
        dwig4 = new DWInteractionGraph("resources/email-Eu-core-temporal-Dept4.txt");

        BufferedReader reader = new BufferedReader(new FileReader("resources/email-Eu-core-temporal-Dept1.txt"));

        ArrayList<Integer> sender = new ArrayList<>();
        ArrayList<Integer> receiver = new ArrayList<>();
        ArrayList<String[]> emailData = new ArrayList<>();

        for (String fileLine = reader.readLine();
             fileLine != null;
             fileLine = reader.readLine()) {
            String[] file;
            file = fileLine.split(" ");
            String[] users = Arrays.copyOf(file, file.length - 1);
            emailData.add(file);
            sender.add(Integer.valueOf(users[0]));
            receiver.add(Integer.valueOf(users[1]));

        }
        senders = sender;
        receivers = receiver;
        EmailData = emailData;

    }

    @Test
    public void testGetUserIds() {
        HashSet<Integer> expected = new HashSet<>();

        expected.addAll(senders);
        expected.addAll(receivers);

        Assertions.assertEquals(expected, dwig1.getUserIDs());
    }

    @Test
    public void test1GetUserIds() {
        int expected = 0;
        for (int i = 0; i < senders.size(); i++) {
            if (senders.get(i) == 26 && receivers.get(i) == 280) {
                expected++;
            }
        }
        Assertions.assertEquals(expected, dwig1.getEmailCount(26, 280));
    }

    @Test
    public void testFirewallRead() {
        System.out.println(dwig.MaxBreachedUserCount(3));
        System.out.println(dwig1.MaxBreachedUserCount(2000));
        System.out.println(dwig2.MaxBreachedUserCount(0));
        System.out.println(dwig3.MaxBreachedUserCount(24));
        System.out.println(dwig4.MaxBreachedUserCount(980000000));
    }

    @Test
    public void testNthmostActiveUser() {
        System.out.println(dwig.NthMostActiveUser(1, SendOrReceive.SEND));
        System.out.println(dwig1.NthMostActiveUser(3, SendOrReceive.RECEIVE));
        Assertions.assertEquals(-1, dwig3.NthMostActiveUser(100000000, SendOrReceive.RECEIVE));
        Assertions.assertEquals(-1, dwig3.NthMostActiveUser(104, SendOrReceive.SEND));
        System.out.println(dwig4.NthMostActiveUser(30, SendOrReceive.RECEIVE));
    }

    @Test
    public void UserEmailsSent() {
        int expected = 0;
        for (int i = 0; i < senders.size(); i++) {
            if (senders.get(i) == 26) {
                expected++;
            }
        }
        Assertions.assertEquals(expected, dwig1.ReportOnUser(26)[0]);
    }

    @Test
    public void UserEmailsReceived() {
        int expected = 0;
        for (int i = 0; i < receivers.size(); i++) {
            if (receivers.get(i) == 26) {
                expected++;
            }
        }
        Assertions.assertEquals(expected, dwig1.ReportOnUser(26)[1]);
    }

    @Test
    public void UserInteracted() {
        int expected = 0;
        HashSet<Integer> interactedUser = new HashSet<>();
        for (int i = 0; i < receivers.size(); i++) {
            if (receivers.get(i) == 161 || senders.get(i) == 161) {
                interactedUser.add(receivers.get(i));
                interactedUser.add(senders.get(i));
            }
        }
        interactedUser.remove(161);
        expected = interactedUser.size();

        Assertions.assertEquals(expected, dwig1.ReportOnUser(161)[2]);
    }

    @Test
    public void testActivityinTimeWindow() {
        ArrayList<String[]> emails = EmailData;
        HashSet<Integer> expected1 = new HashSet<>();
        HashSet<Integer> expected2 = new HashSet<>();
        int expected = 0;
        int timeframe = 0;

        for (int i = 0; i < emails.size(); i++) {
            timeframe = Integer.parseInt(emails.get(i)[2]);
            if (timeframe == 45283) {
                expected++;
                expected1.add(Integer.parseInt(emails.get(i)[0]));
                expected2.add(Integer.parseInt(emails.get(i)[1]));
            }
        }
        Assertions.assertEquals(expected, dwig1.ReportActivityInTimeWindow(new int[]{45283, 45283})[2]);
        Assertions.assertEquals(expected1.size(), dwig1.ReportActivityInTimeWindow(new int[]{45283, 45283})[0]);
        Assertions.assertEquals(expected2.size(), dwig1.ReportActivityInTimeWindow(new int[]{45283, 45283})[1]);
    }

    @Test
    public void testBFS() {
        System.out.println(dwig.BFS(24,102));
        System.out.println(dwig1.BFS(3, 284));
        System.out.println(dwig2.BFS(2100, 20));
        System.out.println(dwig3.BFS(40, 79));
        System.out.println(dwig4.BFS(64,120));
    }
    @Test
    public void testDFS() {
        System.out.println(dwig.DFS(24,102));
        System.out.println(dwig1.DFS(3, 284));
        System.out.println(dwig2.DFS(2100, 20));
        System.out.println(dwig3.DFS(40, 79));
        System.out.println(dwig4.DFS(64,120));
    }
}
