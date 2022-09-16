package cpen221.mp2;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * DWInteractionGraph represents a weighted directional interaction adjacency graph capturing emails in a closed email
 *      server.
 * Captures directionality: emails sent by user1 to user2 are not treated the same as emails sent by user2 to user1
 */
public class DWInteractionGraph {

    private final HashMap<Integer, LinkedList<EmailData>> senders = new HashMap<>();
    private final ArrayList<Integer> MOST_SENT = new ArrayList<>();
    private final ArrayList<Integer> MOST_RECEIVED = new ArrayList<>();
    private final int SENDER_INDEX = 0;
    private final int RECEIVER_INDEX = 1;
    private final int TIME_INDEX = 2;

    //  Representation Invariant
    //      no key in senders exists that has a null or empty LinkedList value
    //      every key in senders is not null and greater or equal to zero.
    //      MOST_SENT and MOST_RECEIVED are not null, and have no negative values or duplicates.

    //  Abstraction Function
    //      senders represents a record of emails exchanged between various users.
    //      for each userID key, there exists a linkedlist of EmailData objects representing
    //      email data of every userID that has received emails from the key. If the userID did not
    //      send any emails, no key or value exists for that userID.
    //
    //      MOST_SENT and MOST_RECEIVED represent a record of userIDS sorted by number of sent/received emails.
    //      the first userID in each list is the user who sent/received the most, and the last is the
    //      userID who sent/received the least. In the event of two users having sent/received the same number
    //      of emails, the one with the smaller userID is listed first.
    //
    //      SENDER_INDEX, RECEIVER_INDEX, and TIME_INDEX represent the position of the respective data points
    //      in a line in the temporary arraylist that is created while reading a line from an input file.
    //      These are just magic number stated for convenience and do not represent anything meaningful.



    /* ------- Constructors and Task 1 ------- */

    /**
     * Checks that the representation invariant holds for the DWInteractionGraph object
     * This operation does nothing unless there is a bug, in which case it sometimes throws an exception
     */
    private void checkRep() {
        for (int key: senders.keySet()){
            assert (!senders.get(key).isEmpty());
            assert (key >= 0);
        }
        HashSet<Integer> alreadythere = new HashSet<>();
        for (int user: MOST_RECEIVED){
            assert (user >= 0 && !alreadythere.contains(user));
            alreadythere.add(user);
        }
        alreadythere.clear();
        for (int user: MOST_SENT){
            assert (user >= 0 && !alreadythere.contains(user));
            alreadythere.add(user);
        }
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file.
     *              The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *              directory containing email interactions
     */
    public DWInteractionGraph(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine();
                 fileLine != null;
                 fileLine = reader.readLine()) {
                   String[] separatedLine;
                   int[] data = new int[3];
                   separatedLine = fileLine.split(" ");
                   for (int number = 0; number < 3; number++) {
                       data[number] = Integer.parseInt(separatedLine[number]);
                   }
                   if (!senders.containsKey(data[SENDER_INDEX])){
                       LinkedList<EmailData> receiverList = new LinkedList<>();
                       receiverList.add(new EmailData(data[RECEIVER_INDEX], data[TIME_INDEX]));
                       senders.put(data[SENDER_INDEX], receiverList);
                   }
                   else{
                       boolean isThere = false;
                       for (EmailData receiver : senders.get(data[SENDER_INDEX])){
                           if (receiver.getReceiver() == data[RECEIVER_INDEX]){
                               isThere = true;
                               receiver.addEmail(data[TIME_INDEX]);
                               break;
                           }
                       }
                        if (!isThere){
                            senders.get(data[SENDER_INDEX]).add(new EmailData(data[RECEIVER_INDEX], data[TIME_INDEX]));
                        }
                   }
            }
            reader.close();
        }
        catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }
        finally {
            checkRep();
        }
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file and a time constraint
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     * @param timeframe an integer array of length 2: [t0, t1]
     *                 where t0 <= t1. The created DWInteractionGraph
     *                 should only include those emails in the file
     *                 with send time t in the t0 <= t <= t1 range.
     **/
    public DWInteractionGraph(String fileName, int[] timeframe) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int startTime = timeframe[0];
            int endTime = timeframe[1];
            for (String fileLine = reader.readLine();
                 fileLine != null;
                 fileLine = reader.readLine()) {
                String[] separatedLine;
                int[] data = new int[3];
                separatedLine = fileLine.split(" ");
                for (int number = 0; number < 3; number++) {
                    data[number] = Integer.parseInt(separatedLine[number]);
                }
                if (data[TIME_INDEX] >= startTime && data[TIME_INDEX] <= endTime) {
                    if (!senders.containsKey(data[SENDER_INDEX])) {
                        LinkedList<EmailData> receiverList = new LinkedList<>();
                        receiverList.add(new EmailData(data[RECEIVER_INDEX], data[TIME_INDEX]));
                        senders.put(data[SENDER_INDEX], receiverList);
                    } else {
                        boolean isthere = false;
                        for (EmailData receiver : senders.get(data[SENDER_INDEX])) {
                            if (receiver.getReceiver() == data[RECEIVER_INDEX]) {
                                isthere = true;
                                receiver.addEmail(data[TIME_INDEX]);
                                break;
                            }
                        }
                        if (!isthere) {
                            senders.get(data[SENDER_INDEX]).add(new EmailData(data[RECEIVER_INDEX], data[TIME_INDEX]));
                        }
                    }
                }
            }
            reader.close();
        }
        catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }
        finally {
            checkRep();
        }
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     *              and considering a time window filter.
     *
     * @param inputDWIG a DWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *              where t0 <= t1. The created DWInteractionGraph
     *              should only include those emails in the input
     *              DWInteractionGraph with send time t in the
     *              t0 <= t <= t1 range.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, int[] timeFilter) {
        for (Integer sender: inputDWIG.senders.keySet()){
            LinkedList<EmailData> revisedList = new LinkedList<>();
            Iterator<EmailData> iterator = inputDWIG.senders.get(sender).iterator();

            while (iterator.hasNext()) {
                EmailData oldEmail = iterator.next();
                ArrayList<Integer> relevantEmails = oldEmail.constrainTimes(timeFilter);

                if (!relevantEmails.isEmpty()) {
                    EmailData truncatedEmails = new EmailData(oldEmail.getReceiver());
                    truncatedEmails.addEmails(relevantEmails);
                    revisedList.add(truncatedEmails);
                }
            }

            if (!revisedList.isEmpty()) {
                senders.put(sender, revisedList);
            }
        }

        checkRep();
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     *              and considering a list of User IDs.
     *
     * @param inputDWIG a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *              should exclude those emails in the input
     *              DWInteractionGraph for which neither the sender
     *              nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        for (Integer sender: inputDWIG.senders.keySet()){
            LinkedList<EmailData> revisedList = new LinkedList<>();
            Iterator<EmailData> iterator = inputDWIG.senders.get(sender).iterator();

            if (userFilter.contains(sender)){
                while (iterator.hasNext()) {
                    EmailData oldEmail = iterator.next();
                    EmailData newEmail = new EmailData(oldEmail.getReceiver());
                    newEmail.addEmails(oldEmail.getEmailTimes());
                    revisedList.add(newEmail);
                }

                senders.put(sender, revisedList);
            }
            else {
                while (iterator.hasNext()) {
                    EmailData oldEmail = iterator.next();

                    if (userFilter.contains(oldEmail.getReceiver())){
                        EmailData newEmail = new EmailData(oldEmail.getReceiver());
                        newEmail.addEmails(oldEmail.getEmailTimes());
                        revisedList.add(newEmail);
                    }
                }

                if (!revisedList.isEmpty()){
                    senders.put(sender, revisedList);
                }
            }
        }

        checkRep();
    }

    /**
     * Finds every unique active userID in the DWInteractionGraph. UserIDs that have not sent or received emails are not considered.
     *
     * @return a Set of Integers, where every element in the set is an active User ID
     *              in this DWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        checkRep();
        HashSet<Integer> userIDs = new HashSet<>();

        for (Integer sender: senders.keySet()){
            userIDs.add(sender);

            for (EmailData receiver: senders.get(sender)){
                userIDs.add(receiver.getReceiver());
            }
        }

        checkRep();
        return userIDs;
    }

    /**
     * Finds the number of emails sent from a specified sender ID to a specified receiver ID in the DWInteractionGraph.
     *
     * @param sender >= 0; the User ID of the sender in the email transaction.
     * @param receiver >= 0; the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     *              receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        checkRep();
        int numOfEmails = 0;

        if (!senders.containsKey(sender)){
            checkRep();
            return numOfEmails;
        }

        for (EmailData recipient : senders.get(sender)) {
            if (recipient.getReceiver() == receiver){
                numOfEmails = recipient.getNumEmails();
                break;
            }
        }

        checkRep();
        return numOfEmails;
    }



    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     *              Suppose an email in this graph is sent at time t, then all emails
     *              sent where t0 <= t <= t1 are included in this report.
     *
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return an int array of length 3, with the following structure:
     *              [NumberOfSenders, NumberOfReceivers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        checkRep();

        HashSet<Integer> numSenders = new HashSet<>();
        HashSet<Integer> numReceivers =  new HashSet<>();
        int numEmails = 0;
        int[] returnValues = new int[3];

        for (Integer sender: senders.keySet()){
            int numEmailsToAdd = 0;

            for (EmailData receiver: senders.get(sender)){
                ArrayList<Integer> EmailsInTime =receiver.constrainTimes(timeWindow);

                if (!EmailsInTime.isEmpty()){
                    numEmailsToAdd += EmailsInTime.size();
                    numReceivers.add(receiver.getReceiver());
                }
            }

            if (numEmailsToAdd > 0) {
                numEmails += numEmailsToAdd;
                numSenders.add(sender);
            }
        }

        returnValues[0] = numSenders.size();
        returnValues[1] = numReceivers.size();
        returnValues[2] = numEmails;

        checkRep();
        return returnValues;
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which the report will be created.
     * @return an int array of length 3 with the following structure:
     *              [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     *              If the specified User ID does not exist in this instance of a graph,
     *              returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {
        checkRep();

        int sentEmails = 0;
        int receivedEmails = 0;
        HashSet<Integer> interactedWith = new HashSet<>();
        int[] returnValues = new int[3];

        for (Integer sender: senders.keySet()) {
            for (EmailData receiver : senders.get(sender)) {
                if (sender == userID){
                    sentEmails += receiver.getNumEmails();
                    interactedWith.add(receiver.getReceiver());
                }

                if (receiver.getReceiver() == userID){
                    receivedEmails += receiver.getNumEmails();
                    interactedWith.add(sender);
                }
            }
        }

        returnValues[0] = sentEmails;
        returnValues[1] = receivedEmails;
        returnValues[2] = interactedWith.size();

        checkRep();
        return returnValues;
    }

    /**
     * Finds the Nth most active user in terms of sending emails, or receiving emails in this DWInteractionGraph.
     *
     * @param N a positive number greater than zero representing rank. N=1 means the most active.
     * @param interactionType Represent the type of interaction to calculate the rank for
     *              Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     *              Sorts User IDs by their number of sent or received emails first. In the case of a
     *              tie, secondarily sorts the tied User IDs in ascending order. If N is outside the bounds,
     *              returns -1
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {
        checkRep();

        if (interactionType.equals(SendOrReceive.SEND)) {
            if (MOST_SENT.isEmpty()){
                makeMostSent();
            }
            if (N <= MOST_SENT.size()) {
                checkRep();
                return MOST_SENT.get(N - 1);
            }
            else{
                checkRep();
                return -1;
            }
        }
        else {
            if (MOST_RECEIVED.isEmpty()){
                makeMostReceived();
            }
            if (N <= MOST_RECEIVED.size()) {
                checkRep();
                return MOST_RECEIVED.get(N - 1);
            }
            else{
                checkRep();
                return -1;
            }
        }
    }

    /**
     * Creates a cached record of user IDs, where the first userID corresponds to
     *              the user that sent the most emails, and the last one corresponds to the one who sent
     *              the least. In the even of a tie, the smaller user ID is listed first.
     */
    private void makeMostSent(){
        HashMap<Integer, Integer> SendData = new HashMap<>();

        for (int sender: senders.keySet()){
            int sentEmails = 0;
            Iterator<EmailData> iterator = senders.get(sender).listIterator();

            while (iterator.hasNext()){
                EmailData currentEmail = iterator.next();
                sentEmails += currentEmail.getNumEmails();
            }
            SendData.put(sender,sentEmails);
        }

        int numLoops = SendData.size();
        while (MOST_SENT.size() < numLoops) {
            int nextHighest = 0;
            int senderToAdd = 0;

            for (int sender : SendData.keySet()) {
                if (SendData.get(sender) > nextHighest) {
                    senderToAdd = sender;
                    nextHighest = SendData.get(sender);
                } else if (SendData.get(sender) == nextHighest && sender < senderToAdd) {
                    senderToAdd = sender;
                }
            }
            MOST_SENT.add(senderToAdd);
            SendData.remove(senderToAdd);
        }
        checkRep();
    }

    /**
     * Creates a cached record of user IDs, where the first userID corresponds to
     *              the user that received the most emails, and the last one corresponds to the one who received
     *              the least. In the even of a tie, the smaller user ID is listed first.
     */
    private void makeMostReceived(){
        HashMap<Integer, Integer> ReceiveData = new HashMap<>();
        for (int sender: senders.keySet()){
            Iterator<EmailData> iterator = senders.get(sender).listIterator();

            while (iterator.hasNext()){
                EmailData currentEmail = iterator.next();
                int receiverID = currentEmail.getReceiver();

                if (!ReceiveData.containsKey(receiverID)){
                    ReceiveData.put(receiverID, currentEmail.getNumEmails());
                }
                else {
                    int updatedCount = currentEmail.getNumEmails() + ReceiveData.get(receiverID);
                    ReceiveData.put(receiverID,updatedCount);
                }
            }
        }

        int numLoops = ReceiveData.size();
        while (MOST_RECEIVED.size() < numLoops) {
            int nextHighest = 0;
            int receiverToAdd = 0;

            for (int receiver : ReceiveData.keySet()) {
                if (ReceiveData.get(receiver) > nextHighest) {
                    receiverToAdd = receiver;
                    nextHighest = ReceiveData.get(receiver);
                } else if (ReceiveData.get(receiver) == nextHighest && receiver < receiverToAdd) {
                    receiverToAdd = receiver;
                }
            }
            MOST_RECEIVED.add(receiverToAdd);
            ReceiveData.remove(receiverToAdd);
        }
        checkRep();
    }



    /* ------- Task 3 ------- */

    /**
     * Performs breadth first search on the DWInteractionGraph object
     *              to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     *              in the order encountered in the search.
     *              if no path exists, should return null.
     */
    public List<Integer> BFS(int userID1, int userID2) {
        checkRep();

        if (senders.containsKey(userID1)) {
            int sender = userID1;

            boolean[] visited = new boolean[Collections.max(this.getUserIDs())+1];

            LinkedList<Integer> queue = new LinkedList<>();
            List<Integer> visitedNodes = new ArrayList<>();

            visited[sender] = true;
            queue.add(sender);
            visitedNodes.add(sender);

            if(userID1 == userID2){
                return visitedNodes;
            }

            while (queue.size() != 0) {
                List<Integer> childNodes = new ArrayList<>(queue);
                Collections.sort(childNodes);
                sender = queue.poll();

                for (Integer childNode : childNodes) {
                    if (!visitedNodes.contains(childNode)) {
                        visitedNodes.add(childNode);
                    }
                }

                if (senders.containsKey(sender)) {
                    Iterator<EmailData> iterator = (senders.get(sender).listIterator());

                    while (iterator.hasNext()) {
                        EmailData currentEmail = iterator.next();
                        int receiverId = currentEmail.getReceiver();

                        if (!visited[receiverId]) {
                            visited[receiverId] = true;
                            queue.add(receiverId);
                        }
                        if (receiverId == userID2){
                            break;
                        }
                    }
                }
            }
            if (!visitedNodes.contains(userID2)){
                checkRep();
                return null;
            }
            checkRep();
            return visitedNodes;
        } else {
            checkRep();
            return null;
        }
    }

    /**
     * Performs depth first search on the DWInteractionGraph object
     *              to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     *              in the order encountered in the search.
     *              if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {
        checkRep();

        List<Integer> usersEncountered = new ArrayList<>();
        usersEncountered = DFS_helper(userID1, userID2, usersEncountered);

        if (usersEncountered.contains(userID2)){
            checkRep();
            return usersEncountered;
        }
        else {
            checkRep();
            return null;
        }
    }

    /**
     * Continuously updates a List with the order of userIDs searched in the DFS pathfinding process
     *              from a specified sender to a specified receiver. When choosing which node to explore next, the
     *              node with the smallest UserID is selected.
     * @param userID1 an integer value j such that j >= 0. Represents the userID of the current node.
     * @param userID2 an integer value i such that i >= 0. Represents the targeted userID.
     * @param visited a List L of integers i where every i is a userID that has already been visited.
     * @return an updated List L that includes all userIDs searched from the current node in correct DFS order.
     */
    private List<Integer> DFS_helper(int userID1, int userID2, List<Integer> visited){
        visited.add(userID1);

        if (senders.containsKey(userID1)) {
            ArrayList<Integer> order = new ArrayList<>();
            Iterator<EmailData> iterator = senders.get(userID1).listIterator();

            while (iterator.hasNext() && !visited.contains(userID2)) {
                EmailData nextID = iterator.next();
                order.add(nextID.getReceiver());
            }

            Collections.sort(order);

            Iterator<Integer> iterator2 = order.listIterator();
            while (iterator2.hasNext() && !visited.contains(userID2)) {
                int next_sender = iterator2.next();

                if (next_sender == userID2) {
                    visited.add(next_sender);
                    return visited;
                } else if (!visited.contains(next_sender)) {
                    DFS_helper(next_sender, userID2, visited);
                }
            }
        }

        return visited;
    }



    /* ------- Task 4 ------- */

    /**
     * Finds the maximum number of users that could be infected by a single infected user in the database, given the
     *              constraint that a firewall activates N hours after the first infected sender sends an email.
     *              if t is the time at which the first infected email was sent, then any emails from an infected user
     *              within the time interval [t, t+N*3600) is considered an infectious email.
     *
     * @param hours a non-negative integer N that represents how many hours will pass until the firewall starts
     * @return the maximum number of users that can be polluted in N hours
     */
    public int MaxBreachedUserCount(int hours) {
        checkRep();

        HashMap<Integer, Integer> infected = new HashMap<>();

        for (Integer origin: senders.keySet()){
            int startTime = -1;
            Iterator<EmailData> iterator = senders.get(origin).listIterator();

            while (iterator.hasNext()){
                EmailData nextReceiver = iterator.next();

                if (nextReceiver.earliestEmail() < startTime || startTime == -1){
                    startTime = nextReceiver.earliestEmail();
                }
            }

            int endTime = startTime + (hours* 3600);
            HashMap<Integer, Integer> lastStart = new HashMap<>();
            infected.put(origin, returnInfected(origin, startTime, endTime, lastStart).size());
        }

        int maxInfected = 0;
        for (int origin: infected.keySet()){
            if (infected.get(origin) > maxInfected){
                maxInfected = infected.get(origin);
            }
        }

        checkRep();
        return maxInfected;
    }

    /**
     * Finds which users have been infected, and the earliest time each user could have been infected.
     *
     * For the current node specified by userID, do the following:
     *              if lastStart already contains the userID, and the last considered start time of infection is less
     *                  than or equal to the currently considered time, return an unmodified list.
     *              if the userID has no record of sending emails, return an unmodified list
     *              else insert the userID of the current node, and the current start time considered into
     *                  lastStart, then for every receiving node connected to the current node, determine the earliest
     *                  possible infected email sent between the start and end times (if any), and call the
     *                  returnInfected function for the new node with the receiver id, the earliest time of receiving
     *                  infected email from current node, the end time, and an updated lastStart HashMap.
     *
     * @param userID a non-negative integer i that is the userID of the current node.
     * @param startTime a non-negative integer t that is the time at which the userID received got polluted from the
     *                  previous node.
     * @param endTime a non-negative integer e that is the time at which the firewall will activate, prevent any more
     *                pollution.
     * @param lastStart a Hashmap of integer keys and values, where the key is the userID, and earliest time of
     *              infection considered is the value. Keys only exist if that UserID has already been explored.
     * @return an updated lastStart Hashmap, with the userIDs and respective start times explored from the current
     *              node, as well as an updated start time for the current node.
     */
    private HashMap<Integer, Integer> returnInfected(int userID, int startTime, int endTime, HashMap<Integer, Integer> lastStart){
        if (lastStart.containsKey(userID) && startTime >= lastStart.get(userID)) {
            return lastStart;
        }
        else if (!senders.containsKey(userID)) {
            lastStart.put(userID,startTime);
            return lastStart;
        }
        else {
            lastStart.put(userID, startTime);
            Iterator<EmailData> iterator2 = senders.get(userID).listIterator();

            while (iterator2.hasNext()) {
                EmailData infectionCandidate = iterator2.next();
                List<Integer> receiverEmailTimes = infectionCandidate.getEmailTimes();
                int nextStartTime = -1;
                int nextStart = 0;
                for (int i = 0; i < receiverEmailTimes.size(); i++) {
                    if (receiverEmailTimes.get(i) >= startTime && receiverEmailTimes.get(i) < endTime) {
                        nextStartTime = receiverEmailTimes.get(i);
                        nextStart = i;
                        break;
                    }
                }

                if (nextStartTime != -1) {
                    for (int j = nextStart; j < receiverEmailTimes.size(); j++) {
                        if (receiverEmailTimes.get(j) >= startTime && receiverEmailTimes.get(j) < endTime && receiverEmailTimes.get(j) < nextStartTime) {
                            nextStartTime = receiverEmailTimes.get(j);
                        }
                    }
                    returnInfected(infectionCandidate.getReceiver(), nextStartTime, endTime, lastStart);
                }
            }
            return lastStart;
        }
    }

    /**
     * Method that returns a linkedlist of receivers from a specified sender.
     *
     * @param user the userID of the sender.
     * @return a LinkedList of EmailData objects that the user has sent emails to.
     */
    public LinkedList<EmailData> returnList(int user) {
        checkRep();

        LinkedList<EmailData> toReturn = new LinkedList<>();

        if (this.senders.get(user) != null) {
            Iterator<EmailData> listIterator = this.senders.get(user).listIterator();

            while (listIterator.hasNext()){
                EmailData email = listIterator.next();
                int receiver = email.getReceiver();
                List<Integer> times = email.getEmailTimes();
                EmailData nextEmail = new EmailData(receiver, times);
                toReturn.add(nextEmail);
            }
        }

        checkRep();
        return toReturn;
    }

}
