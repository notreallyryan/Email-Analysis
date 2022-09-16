package cpen221.mp2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UDWInteractionGraph represents an undirected weighted interaction graph capturing emails in a closed email server.
 * No directionality: emails sent by user1 to user2, and emails sent by user2 to user1, are treated as part of the same
 *      group of user interactions (put another way, as part of the same weighted edge in the graph)
 *
 * References:
 *      [NthMostActiveUser] https://stackoverflow.com/questions/2776176/get-minvalue-of-a-mapkey-double
 *      [NthMostActiveUser] https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
 */
public class UDWInteractionGraph {

    private HashMap<UndirectedSenderReceiverPair, EmailData> UDWGraph;

    //  Representation Invariant
    //      UDWGraph != null
    //      for any key (UndirectedSenderReceiverPair) that exists in UDWGraph:
    //          key.getUser1 >= 0
    //          key.getUser2 >= 0
    //          UDWGraph.get(key) != null

    //  Abstraction Function
    //      UDWGraph is a HashMap that maps an UndirectedSenderReceiverPair (two users, not necessarily unique)
    //          to an EmailData object (holds weight and email times of the interactions)



    /* ------- Constructors and Task 1 ------- */

    /**
     * Checks that the representation invariant holds for the UDWInteractionGraph object
     * This operation does nothing unless there is a bug, in which case it sometimes throws an exception
     */
    private void checkRep() {
        assert this.UDWGraph != null;

        for (UndirectedSenderReceiverPair key : this.UDWGraph.keySet()) {
            assert key.getUser1() >= 0;
            assert key.getUser2() >= 0;
            assert this.UDWGraph.get(key) != null;
        }
    }

    /**
     * Creates a new UDWInteractionGraph using an email interaction file
     *
     * @param fileName the name of the file in the resources directory containing email interactions, must be in the
     *              resources directory.
     */
    public UDWInteractionGraph(String fileName) {
        try {
            this.UDWGraph = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String fileLine = reader.readLine();
            int[] separatedInfo;

            while (fileLine != null) {
                separatedInfo = Arrays.stream(fileLine.split("\\s"))
                        .mapToInt(Integer::parseInt).toArray();
                UndirectedSenderReceiverPair USRP = new UndirectedSenderReceiverPair(separatedInfo[0], separatedInfo[1]);

                if (this.UDWGraph.containsKey(USRP)) {
                    UDWGraph.get(USRP).addEmail(separatedInfo[2]);
                }
                else {
                    ArrayList<Integer> emailTimeArray = new ArrayList<>();
                    emailTimeArray.add(separatedInfo[2]);
                    EmailData newEmailInfo = new EmailData(separatedInfo[1], emailTimeArray);

                    this.UDWGraph.put(USRP, newEmailInfo);
                }

                fileLine = reader.readLine();
            }
        }
        catch (IOException ioe) {
            System.out.println("Error reading file.");
        }
        finally {
            checkRep();
        }
    }

    /**
     * Creates a new UDWInteractionGraph from an email interaction file and considering a time window filter.
     *
     * @param fileName the email interaction file to construct from, must be in the resources directory
     * @param timeFilter an integer array of length 2: [t0, t1] where t0 <= t1. The created UDWInteractionGraph
     *               should only include those emails in the input UDWInteractionGraph with send time t in the
     *               t0 <= t <= t1 range.
     */
    public UDWInteractionGraph(String fileName, int[] timeFilter) {
        try {
            this.UDWGraph = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String fileLine = reader.readLine();
            int[] separatedInfo;
            int minTime = timeFilter[0];
            int maxTime = timeFilter[1];

            while (fileLine != null) {
                separatedInfo = Arrays.stream(fileLine.split("\\s"))
                        .mapToInt(Integer::parseInt).toArray();
                UndirectedSenderReceiverPair USRP = new UndirectedSenderReceiverPair(separatedInfo[0], separatedInfo[1]);

                if (this.UDWGraph.containsKey(USRP)) {
                    if (separatedInfo[2] >= minTime && separatedInfo[2] <= maxTime) {
                        UDWGraph.get(USRP).addEmail(separatedInfo[2]);
                    }
                }
                else {
                    if (separatedInfo[2] >= minTime && separatedInfo[2] <= maxTime) {
                        ArrayList<Integer> emailTimeArray = new ArrayList<>();
                        emailTimeArray.add(separatedInfo[2]);
                        EmailData newEmailInfo = new EmailData(separatedInfo[1], emailTimeArray);

                        this.UDWGraph.put(USRP, newEmailInfo);
                    }
                }

                fileLine = reader.readLine();
            }
        }
        catch (IOException ioe) {
            System.out.println("Error reading file.");
        }
        finally {
            checkRep();
        }
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object and considering a time window filter.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1] where t0 <= t1. The created UDWInteractionGraph
     *               should only include those emails in the input UDWInteractionGraph with send time t in the
     *               t0 <= t <= t1 range.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, int[] timeFilter) {
        this.UDWGraph = new HashMap<>();
        EmailData emailInfo = new EmailData();
        int minTime = timeFilter[0];
        int maxTime = timeFilter[1];

        for (UndirectedSenderReceiverPair key : inputUDWIG.UDWGraph.keySet()) {
            for (int sendTime : inputUDWIG.UDWGraph.get(key).getEmailTimes()) {
                if (sendTime >= minTime && sendTime <= maxTime) {
                    emailInfo.addEmail(sendTime);
                }
            }

            if (emailInfo.getNumEmails() != 0) {
                this.UDWGraph.put(key, emailInfo);
            }

            emailInfo = new EmailData();
        }

        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object and considering a list of User IDs.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param userFilter a List of User IDs. The created UDWInteractionGraph should exclude those emails in the input
     *               UDWInteractionGraph for which neither the sender nor the receiver exist in userFilter.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, List<Integer> userFilter) {
        this.UDWGraph = new HashMap<>();

        for (UndirectedSenderReceiverPair key : inputUDWIG.UDWGraph.keySet()) {
            if (userFilter.contains(key.getUser1()) || userFilter.contains(key.getUser2())) {
                this.UDWGraph.put(key, inputUDWIG.UDWGraph.get(key));
            }
        }

        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph from a DWInteractionGraph object.
     *
     * @param inputDWIG the DWGraph to create the new UDW from
     */
    public UDWInteractionGraph(DWInteractionGraph inputDWIG) {
        this.UDWGraph = new HashMap<>();
        Set<Integer> userIDs = inputDWIG.getUserIDs();
        Set<Integer> userIDsModified = inputDWIG.getUserIDs();
        List<Integer> listInteractions;

        for (int user1 : userIDs) {
            userIDsModified.remove(user1);

            for (int user2 : userIDsModified) {
                listInteractions = returnAllInteractions(user1, user2, inputDWIG);

                if (!listInteractions.isEmpty()) {
                    UndirectedSenderReceiverPair key = new UndirectedSenderReceiverPair(user1, user2);
                    EmailData emailList = new EmailData(user2, listInteractions);
                    this.UDWGraph.put(key, emailList);
                }
            }

            userIDsModified.add(user1);
        }

        checkRep();
    }

    /**
     * Gets all user IDs present in the UDW graph
     *
     * @return a Set of Integers, where every element in the set is a User ID in this UDWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        checkRep();
        Set<Integer> userIDs = new HashSet<>();

        userIDs.addAll(this.UDWGraph.keySet().stream()
                .map(UndirectedSenderReceiverPair::getUser1)
                .toList());
        userIDs.addAll(this.UDWGraph.keySet().stream()
                .map(UndirectedSenderReceiverPair::getUser2)
                .toList());

        checkRep();
        return userIDs;
    }

    /**
     * Gets the number of emails sent between two users
     *
     * @param user1 >= 0, the User ID of the first user.
     * @param user2 >= 0, the User ID of the second user.
     * @return the number of email interactions (send/receive) between user1 and user2; emailCount >= 0
     */
    public int getEmailCount(int user1, int user2) {
        checkRep();
        int emailCount = 0;
        int encounters = 0;
        UndirectedSenderReceiverPair searchPair = new UndirectedSenderReceiverPair(user1, user2);

        for (UndirectedSenderReceiverPair key : this.UDWGraph.keySet()) {
            if (key.equals(searchPair)) {
                emailCount = this.UDWGraph.get(key).getNumEmails();
                encounters++;
            }

            if (encounters >= 2) {
                break;
            }
        }

        checkRep();
        return emailCount;
    }

    /**
     * Finds and returns all the interactions in a given DW graph
     *
     * @param user1 the user ID for the first user
     * @param user2 the user ID for the second user.
     * @param DWGraph the original DW graph object
     * @return Arraylist A such that for 0 <= i < A.length(), every A[i] contains the time of an email interaction
     *              between user 1 and 2, disregarding direction.
     */
    private ArrayList<Integer> returnAllInteractions(int user1, int user2, DWInteractionGraph DWGraph){
        ArrayList<Integer> allInteractions = new ArrayList<>();

        if (DWGraph.getEmailCount(user1, user2) != 0 || DWGraph.getEmailCount(user2, user1) != 0) {
            Iterator<EmailData> iteratorUser1 = DWGraph.returnList(user1).listIterator();
            Iterator<EmailData> iteratorUser2 = DWGraph.returnList(user2).listIterator();

            while (iteratorUser1.hasNext()) {
                EmailData findUser2 = iteratorUser1.next();

                if (findUser2.getReceiver() == user2) {
                    allInteractions.addAll(findUser2.getEmailTimes());
                }
            }

            while (iteratorUser2.hasNext()) {
                EmailData findUser1 = iteratorUser2.next();

                if (findUser1.getReceiver() == user1) {
                    allInteractions.addAll(findUser1.getEmailTimes());
                }
            }
        }

        return allInteractions;
    }



    /* ------- Task 2 ------- */

    /**
     * Finds the number of users and number of emails sent/received in a specified time window in the UDWGraph
     *
     * @param timeWindow is an int array of size 2 [t0, t1], where t0 <= t1
     * @return an int array of length 2, with the following structure:
     *              [NumberOfUsers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        checkRep();
        int numUsers;
        int numEmails = 0;
        Set<Integer> usersRecorded = new HashSet<>();
        boolean emailsAdded;
        int minTime = timeWindow[0];
        int maxTime = timeWindow[1];

        for (UndirectedSenderReceiverPair key : this.UDWGraph.keySet()) {
            emailsAdded = false;

            for (int sendTime : this.UDWGraph.get(key).getEmailTimes()) {
                if (sendTime >= minTime && sendTime <= maxTime) {
                    numEmails++;
                    emailsAdded = true;
                }
            }

            if (emailsAdded) {
                usersRecorded.add(key.getUser1());
                usersRecorded.add(key.getUser2());
            }
        }

        numUsers = usersRecorded.size();
        int[] activityReport = {numUsers, numEmails};
        checkRep();
        return activityReport;
    }

    /**
     * For a specified user. finds the number of emails sent/received by that user, and the number of unique users
     *              that the user has interacted with
     *
     * @param userID the User ID of the user for which the report will be created
     * @return an int array of length 2 with the following structure:
     *              [NumberOfEmails, UniqueUsersInteractedWith]
     *              If the specified User ID does not exist in this instance of a graph,
     *              returns [0, 0].
     */
    public int[] ReportOnUser(int userID) {
        checkRep();
        int numEmails = 0;
        int uniqueUsersInteractedWith;
        Set<Integer> uniqueUsers = new HashSet<>();

        for (UndirectedSenderReceiverPair key : this.UDWGraph.keySet()) {
            if (key.getUser1() == userID || key.getUser2() == userID) {
                numEmails += this.UDWGraph.get(key).getNumEmails();
                uniqueUsers.add(key.getUser1());
                uniqueUsers.add(key.getUser2());
            }
        }

        uniqueUsers.remove(userID);
        uniqueUsersInteractedWith = uniqueUsers.size();
        int[] activityReport = {numEmails, uniqueUsersInteractedWith};
        checkRep();
        return activityReport;
    }

    /**
     * Gets the Nth most active user in the interaction graph
     * References:  https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
     *              https://stackoverflow.com/questions/2776176/get-minvalue-of-a-mapkey-double
     *
     * @param N a positive number representing rank. N = 1 means the most active.
     * @return the User ID for the Nth most active user. If the Nth user does not exist in the graph, returns -1.
     *              If there are two or more users with the same level of activity in the graph, returns the ID of
     *              the user with the lower ID.
     */
    public int NthMostActiveUser(int N) {
        checkRep();
        Map<Integer, Integer> rankList = new HashMap<>();
        Map<Integer, Integer> topN;
        int emailsToAdd;
        int nthMostActiveUser = -1;

        if (N <= this.getUserIDs().size()) {
            for (UndirectedSenderReceiverPair key : this.UDWGraph.keySet()) {
                emailsToAdd = this.UDWGraph.get(key).getNumEmails();

                if (rankList.get(key.getUser1()) == null) {
                    rankList.put(key.getUser1(), emailsToAdd);
                } else {
                    rankList.put(key.getUser1(), rankList.get(key.getUser1()) + emailsToAdd);
                }

                if (key.getUser1() != key.getUser2()) {
                    if (rankList.get(key.getUser2()) == null) {
                        rankList.put(key.getUser2(), emailsToAdd);
                    } else {
                        rankList.put(key.getUser2(), rankList.get(key.getUser2()) + emailsToAdd);
                    }
                }
            }

            topN = rankList.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(N)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            nthMostActiveUser = (int) topN.keySet().toArray()[topN.size() - 1];
        }

        checkRep();
        return nthMostActiveUser;
    }



    /* ------- Task 3 ------- */

    /**
     * Determines the number of disjoint components within the UDWInteractionGraph object
     *
     * @return the number of completely disjoint graph components
     */
    public int NumberOfComponents() {
        checkRep();
        int numDisjointComponents = 0;
        int searchID;
        Set<Integer> allUserIDs = this.getUserIDs();
        Set<Integer> nodesInComponent = new HashSet<>();

        while (!allUserIDs.isEmpty()) {
            searchID = allUserIDs.stream().findFirst().get();
            allUserIDs.removeAll(this.getNodesInComponent(searchID, nodesInComponent));
            numDisjointComponents++;
        }

        checkRep();
        return numDisjointComponents;
    }

    /**
     * Determines whether a path exists between two users.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return whether a path exists between the two users
     */
    public boolean PathExists(int userID1, int userID2) {
        checkRep();

        boolean pathExists = false;
        Set<Integer> nodesInComponent = new HashSet<>();
        Set<Integer> interactionsUser1 = getNodesInComponent(userID1, nodesInComponent);

        if (interactionsUser1.contains(userID2)) {
            pathExists = true;
        }

        checkRep();
        return pathExists;
    }

    /**
     * Helper method for NumberOfComponents and PathExists
     * Finds all nodes that exist within a component, given a node from that component
     *
     * @param node >= 0; the node to check (does not matter as long as it is within the component that should be
     *             checked, which also shouldn't matter)
     * @param nodesCounted the set that holds the nodes that have already been searched by the function,
     *             the set is mutated with every recursive call
     * @return a set of the nodes that exist in the component, is not null
     */
    private Set<Integer> getNodesInComponent(int node, Set<Integer> nodesCounted) {
        Set<Integer> immediateNeighbours = new HashSet<>();

        if (!getUserIDs().contains(node)) {
            return new HashSet<>();
        }

        for (int otherNode : getUserIDs()) {
            UndirectedSenderReceiverPair key = new UndirectedSenderReceiverPair(node, otherNode);

            if (this.UDWGraph.get(key) != null) {
                immediateNeighbours.add(otherNode);
            }
        }

        immediateNeighbours.remove(node);
        immediateNeighbours.removeAll(nodesCounted);
        nodesCounted.addAll(immediateNeighbours);

        if (!immediateNeighbours.isEmpty()) {
            for (int neighbour : immediateNeighbours) {
                getNodesInComponent(neighbour, nodesCounted);
            }
        }

        Set<Integer> nodesInComponent = new HashSet<>(nodesCounted);
        nodesInComponent.add(node);
        return nodesInComponent;
    }
}


