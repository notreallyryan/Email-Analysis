package cpen221.mp2;

/**
 * UndirectedSenderReceiverPair represents a pair of users
 * Helper class to be used as a key for the UDWInteractionGraph HashMap
 */
public class UndirectedSenderReceiverPair {

    private int user1;
    private int user2;

    //  Representation Invariant
    //      user1 >= 0
    //      user2 >= 0

    //  Abstraction Function
    //      user1 and user2 are a pair of users (represented by integer identifications) within the same email server
    //      a user can email themselves: in that case user1 == user2



    /**
     * Checks that the representation invariant holds for the UndirectedSenderReceiverPair object
     * This operation does nothing unless there is a bug, in which case it sometimes throws an exception
     */
    private void checkRep() {
        assert this.user1 >= 0;
        assert this.user2 >= 0;
    }

    /**
     * Constructs a new UndirectedSenderReceiverPair given a sender and receiver ID
     *
     * @param user1 >= 0; the ID of the first user
     * @param user2 >= 0; the ID of the second user
     */
    public UndirectedSenderReceiverPair(int user1, int user2) {
        setUser1(user1);
        setUser2(user2);

        checkRep();
    }

    /**
     * Default constructor for UndirectedSenderReceiverPair
     * Sets user1 = 0, user2 = 0
     */
    public UndirectedSenderReceiverPair() {
        setUser1(0);
        setUser2(0);

        checkRep();
    }

    /**
     * Sets the user1 of the pair to a specified ID
     *
     * @param user1 >= 0; the ID to set as user1
     */
    private void setUser1(int user1) {
        if (user1 >= 0) {
            this.user1 = user1;
        }
    }

    /**
     * Sets the user2 of the pair to a specified ID
     *
     * @param user2 >= 0; the ID to set as user2
     */
    private void setUser2(int user2) {
        if (user2 >= 0) {
            this.user2 = user2;
        }
    }

    /**
     * Gets the ID of user1
     *
     * @return user1 >= 0; the ID of user1
     */
    public int getUser1() {
        checkRep();
        final int returnUser = this.user1;
        checkRep();
        return returnUser;
    }

    /**
     * Gets the ID of user2
     *
     * @return user2 >= 0; the ID of user2
     */
    public int getUser2() {
        checkRep();
        final int returnUser = this.user2;
        checkRep();
        return returnUser;
    }

    @Override
    public boolean equals(Object obj) {
        checkRep();

        if (!(obj instanceof UndirectedSenderReceiverPair)) {
            checkRep();
            return false;
        }

        final UndirectedSenderReceiverPair otherPair = (UndirectedSenderReceiverPair) obj;

        if (this.getUser1() == otherPair.getUser1() || this.getUser1() == otherPair.getUser2()) {
            if (this.getUser2() == otherPair.getUser1() || this.getUser2() == otherPair.getUser2()) {
                checkRep();
                return true;
            }
        }

        checkRep();
        return false;
    }

    @Override
    public int hashCode() {
        checkRep();
        int result = (int) ((Math.pow(getUser1(), 1.32) + Math.pow(getUser2(), 1.32)) * 97.0);
        checkRep();
        return result;
    }
}
