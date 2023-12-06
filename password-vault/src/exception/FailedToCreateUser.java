package exception;

public class FailedToCreateUser extends Exception {
    public FailedToCreateUser(String message) {
        super(message);
    }
}
