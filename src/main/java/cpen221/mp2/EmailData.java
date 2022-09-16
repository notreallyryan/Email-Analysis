package cpen221.mp2;
import java.util.ArrayList;
import java.util.List;

/**
 * EmailData holds the data for a specified interaction between a sender and a receiver
 * DWInteractionGraph relies on the receiver field; UDWInteractionGraph does not rely on the receiver field
 */
public class EmailData {

    private int receiver;
    private int numEmails;
    private ArrayList<Integer> emailTimes;

    //  Representation Invariant
    //      receiver >= 0
    //      numEmails >= 0
    //      emailTimes != null
    //      for any int i such that 0 <= i < emailTimes.size():
    //          emailTimes.get(i) >= 0

    //  Abstraction Function
    //      receiver is the user ID of the receiver of the emails stored in EmailData
    //      numEmails is the number of emails stored in the EmailData
    //      emailTimes is a list that stores all the email send times (in seconds) of the interaction represented by
    //          EmailData
    //      the sender ID is stored within DWInteractionGraph as the key to a specific instance of EmailData


    /**
     * Checks that the representation invariant holds for the EmailData object
     * This operation does nothing unless there is a bug, in which case it sometimes throws an exception
     */
    private void checkRep() {
        assert this.receiver >= 0;
        assert this.numEmails >= 0;
        assert this.emailTimes != null;

        if (!this.emailTimes.isEmpty()) {
            for (int emailTime : this.emailTimes) {
                assert emailTime >= 0;
            }
        }
    }

    /**
     * Constructor for EmailData that takes a specified receiver and a list of email times
     *
     * @param receiver >= 0; an integer that is the ID of the receiver
     * @param emailTimes the list that holds the integer email times in the interaction, is not null.
     *              time is measured in seconds.
     */
    public EmailData(int receiver, List<Integer> emailTimes) {
        setReceiver(receiver);
        setNumEmails(emailTimes.size());
        setEmailTimes(emailTimes);

        checkRep();
    }

    /**
     * Constructor for EmailData that takes a specified receiver and a single email time
     *
     * @param receiver >= 0; the ID of the receiver
     * @param singleSendTime the send time of the email to be immediately added to the list of emails in the data,
     *              is not null, and is an integer value of how many seconds have passed since the start of email data collection
     */
    public EmailData(int receiver, int singleSendTime) {
        setReceiver(receiver);
        setNumEmails(1);
        setEmailTimes(new ArrayList<>());
        addEmail(singleSendTime);

        checkRep();
    }

    /**
     * Constructor for EmailData that only sets a specified receiver
     * numEmails is set to 0, and an empty list is created for emailTimes
     *
     * @param receiver >= 0; the ID of the receiver
     */
    public EmailData(int receiver) {
        setReceiver(receiver);
        setNumEmails(0);
        setEmailTimes(new ArrayList<>());

        checkRep();
    }

    /**
     * Default constructor for EmailData
     * Sets receiver = 0, numEmails = 0, and assigns emailTimes to a new, empty ArrayList
     */
    public EmailData() {
        setReceiver(0);
        setNumEmails(0);
        setEmailTimes(new ArrayList<>());

        checkRep();
    }

    /**
     * Sets the receiver ID to the specified integer
     *
     * @param receiver >= 0; the ID of the receiver
     */
    private void setReceiver(int receiver) {
        if (receiver >= 0) {
            this.receiver = receiver;
        }
    }

    /**
     * Sets the numEmails field to the specified integer
     *
     * @param numEmails >= 0; the number of emails within a specific interaction
     */
    private void setNumEmails(int numEmails) {
        if (numEmails >= 0) {
            this.numEmails = numEmails;
        }
    }

    /**
     * Sets the list of email times of the EmailData object
     *
     * @param emailTimes the list of email times, is not null
     */
    private void setEmailTimes(List<Integer> emailTimes) {
        if (emailTimes != null) {
            this.emailTimes = new ArrayList<>(emailTimes);
            setNumEmails(emailTimes.size());
        }
    }

    /**
     * Adds an email to the emailTimes list, given the send time of the email in seconds
     *
     * @param timeInSeconds >= 0; the send time of the email to add to the list in seconds
     *              mutates emailTimes
     */
    public void addEmail(int timeInSeconds) {
        checkRep();
        this.emailTimes.add(timeInSeconds);
        this.numEmails++;
        checkRep();
    }

    /**
     * Gets the receiver ID of the EmailData
     *
     * @return receiver >= 0; the ID of the receiver
     */
    public int getReceiver() {
        checkRep();
        int returnReceiver = this.receiver;
        checkRep();
        return returnReceiver;
    }

    /**
     * Gets the number of emails in the EmailData
     *
     * @return numEmails >= 0; the number of emails in EmailData
     */
    public int getNumEmails() {
        checkRep();
        int returnNumEmails = this.numEmails;
        checkRep();
        return returnNumEmails;
    }

    /**
     * Gets the (immutable) list holding the email times of the EmailData
     *
     * @return a list of email times, no element in the list is empty and all elements are >= 0
     */
    public List<Integer> getEmailTimes() {
        checkRep();
        List<Integer> returnEmailTimes = this.emailTimes;
        checkRep();
        return returnEmailTimes;
    }

    /**
     * Returns a list of email times within and including the endpoints of a given time interval
     *
     * @param timeFilter an integer array of length 2 where timeFilter[0] is the start of the wanted time interval
     *              and timeFilter[1] is the end. Endpoints are inclusive, and timeFilter[0] < timeFilter[1]
     * @return an Arraylist j of integers where each integer corresponds to the times of emails received by the receiver.
     *              For every integer i such that 0<= i < j.size(), timeFilter[0] <= j[i] <= timeFilter[1].
     */
    public ArrayList<Integer> constrainTimes(int[] timeFilter){
        ArrayList<Integer> newTimes = new ArrayList<>();
        for (Integer time: this.emailTimes){
            if (time <= timeFilter[1] && time >= timeFilter[0]){
                newTimes.add(time);
            }
        }
        return newTimes;
    }

    /**
     * Adds multiple email times from a list to a pre-existing EmailData object.
     * @param emails A list of integer email times x, such that for 0<= int i < emails.size(),
     *               x[i] >= 0.
     */
    public void addEmails(List<Integer> emails){
        emailTimes.addAll(emails);
        numEmails = emails.size();
    }

    /**
     * Returns the earliest recorded time at which the EmailData object received an email.
     *
     * @return an integer t such that t >= 0.
     */
    public int earliestEmail(){
        int time = emailTimes.get(0);
        for (int i =1 ; i < emailTimes.size(); i++){
            if (emailTimes.get(i) < time){
                time = emailTimes.get(i);
            }
        }
        return time;
    }

}
